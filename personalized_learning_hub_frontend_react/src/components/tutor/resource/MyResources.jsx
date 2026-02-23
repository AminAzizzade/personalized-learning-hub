import React, { useEffect, useState } from 'react';
import { getGroupedResourcesByTutor, gradeAssignment , downloadResourceFile} from '../../../services/resourceService';
import { Card, Row, Col, Badge, Button, Form, Accordion } from 'react-bootstrap';

const GroupedByStudent = ({ tutorId, refresh }) => {
  const [groupedBySession, setGroupedBySession] = useState({});
  const [scoreInputs, setScoreInputs] = useState({});

  useEffect(() => {
    if (tutorId) {
      getGroupedResourcesByTutor(tutorId).then((raw) => {
        const grouped = {};
        Object.entries(raw).forEach(([_, resources]) => {
          resources.forEach(r => {
            const sid = r.sessionId || 'Bilinmeyen Oturum';
            if (!grouped[sid]) grouped[sid] = [];
            grouped[sid].push(r);
          });
        });
        setGroupedBySession(grouped);
      });
    }
  }, [tutorId, refresh]);

  const handleScoreChange = (resourceId, value) => {
    setScoreInputs(prev => ({
      ...prev,
      [resourceId]: value
    }));
  };

  const handleScoreSubmit = async (resourceId) => {
    const score = parseInt(scoreInputs[resourceId]);
    if (isNaN(score)) {
      alert("Geçerli bir sayı girin.");
      return;
    }

    try {
      await gradeAssignment(resourceId, score);
      alert("Puan kaydedildi.");
    } catch (err) {
      console.error(err);
      alert("Puanlama başarısız.");
    }
  };

  const getStatusBadge = (status, score) => {
    switch (status) {
      case 'PENDING_SUBMISSION':
        return <Badge bg="danger">⏳ Bekleniyor</Badge>;
      case 'REVIEWED':
        return <Badge bg="warning" text="dark">🔍 İnceleniyor</Badge>;
      case 'COMPLETED':
        return <Badge bg="success">✅ Puan: {score}</Badge>;
      default:
        return null;
    }
  };

  const getSessionStatusSummary = (items) => {
    const reviewed = items.filter(r => r.type === 'ASSIGNMENT' && r.status === 'REVIEWED').length;
    const pending = items.filter(r => r.type === 'ASSIGNMENT' && r.status === 'PENDING_SUBMISSION').length;
    const completed = items.filter(r => r.type === 'ASSIGNMENT' && r.status === 'COMPLETED').length;

    const badges = [];
    if (reviewed > 0) badges.push(<Badge key="rev" bg="warning" text="dark" className="ms-2">🔍 {reviewed} inceleniyor</Badge>);
    if (pending > 0) badges.push(<Badge key="pen" bg="danger" className="ms-2">⏳ {pending} bekliyor</Badge>);
    if (completed > 0) badges.push(<Badge key="com" bg="success" className="ms-2">✅ {completed} tamamlandı</Badge>);

    return <span className="ms-2">{badges}</span>;
  };

  return (
    <div className="mt-4">
      <h5>👨‍🏫 Oturumlara Göre Öğrenci Kaynakları</h5>
      {Object.entries(groupedBySession).length === 0 && (
        <p className="text-muted">Henüz öğrencilere kaynak atanmadı.</p>
      )}
      <Accordion alwaysOpen>
        {Object.entries(groupedBySession).map(([sessionId, items], index) => {
          const assignments = items.filter(r => r.type === 'ASSIGNMENT');
          const materials = items.filter(r => r.type === 'MATERIAL');

          const incompleteAssignments = assignments.filter(r => r.status !== 'COMPLETED');
          const completedAssignments = assignments.filter(r => r.status === 'COMPLETED');

          return (
            <Accordion.Item eventKey={String(index)} key={sessionId} className="mb-3">
              <Accordion.Header>
                📚 Oturum: {items[0]?.sessionId || 'Bilinmeyen'}
                {getSessionStatusSummary(items)}
              </Accordion.Header>
              <Accordion.Body>
                <Row className="g-4">
                  <Col md={6}>
                    <h6>📝 Ödevler</h6>
                    {[...incompleteAssignments, ...completedAssignments].map(r => (
                      <Card
                        key={r.id}
                        className={`position-relative p-2 mb-3 ${r.status === 'COMPLETED'
                            ? 'border-secondary bg-light text-muted'
                            : 'border-warning bg-warning-subtle'
                          }`}
                        style={{ minHeight: 'auto' }}
                      >
                        <div className="position-absolute top-0 end-0 m-2">
                          {getStatusBadge(r.status, r.homeWorkScore)}
                        </div>
                        <Card.Body>
                          <Card.Title>📝 {r.resourceName}</Card.Title>
                          <Card.Subtitle className="mb-3 text-muted">
                            {r.category}
                          </Card.Subtitle>
                          <Card.Text className="mb-3">{r.description}</Card.Text>

                          <div className="mb-3">
                            {r.studentFileName && (
                              <Button
                                className="btn-sm me-2"
                                variant="outline-secondary"
                                onClick={() => downloadResourceFile(r.studentFileName)}
                              >
                                📥 Çözüm
                              </Button>
                            )}
                            <Button
                              className="btn-sm"
                              variant="outline-dark"
                              onClick={() => downloadResourceFile(r.teacherFileName)}
                            >
                              📥 Kaynak
                            </Button>
                          </div>

                          {r.status === 'REVIEWED' && (
                            <div className="d-flex align-items-center gap-2">
                              <Form.Control
                                type="number"
                                min={0}
                                max={100}
                                className="form-control-sm"
                                style={{ maxWidth: '100px' }}
                                value={scoreInputs[r.id] || ''}
                                onChange={(e) => handleScoreChange(r.id, e.target.value)}
                              />
                              <Button
                                className="btn-sm"
                                variant="success"
                                onClick={() => handleScoreSubmit(r.id)}
                              >
                                ✅ Notlandır
                              </Button>
                            </div>
                          )}
                        </Card.Body>
                      </Card>
                    ))}
                  </Col>
                  <Col md={6}>
                    <h6>📘 Materyaller</h6>
                    {materials.map(r => (
                      <Card
                        key={r.id}
                        className="border-primary bg-primary-subtle position-relative p-2 mb-3"
                        style={{ minHeight: 'auto' }}
                      >
                        <Card.Body>
                          <Card.Title>📘 {r.resourceName}</Card.Title>
                          <Card.Subtitle className="mb-3 text-muted">
                            {r.category}
                          </Card.Subtitle>
                          <Card.Text className="mb-3">{r.description}</Card.Text>
                          <a
                            href={`http://localhost:8080/api/students/download?fileName=${encodeURIComponent(r.teacherFileName)}`}
                            className="btn btn-sm btn-outline-dark"
                            target="_blank"
                            rel="noreferrer"
                          >
                            📥 Kaynak
                          </a>
                        </Card.Body>
                      </Card>
                    ))}
                  </Col>
                </Row>
              </Accordion.Body>
            </Accordion.Item>
          );
        })}
      </Accordion>
    </div>
  );
};

export default GroupedByStudent;

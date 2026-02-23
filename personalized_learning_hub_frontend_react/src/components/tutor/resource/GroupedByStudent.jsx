import React, { useEffect, useState } from 'react';
import { getGroupedResourcesByTutor, gradeAssignment } from '../../../services/resourceService';
import { Card, Row, Col, Badge, Button, Form } from 'react-bootstrap';

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

  return (
    <div className="mt-4">
      <h5>👨‍🏫 Oturumlara Göre Öğrenci Kaynakları</h5>
      {Object.entries(groupedBySession).length === 0 && (
        <p className="text-muted">Henüz öğrencilere kaynak atanmadı.</p>
      )}
      {Object.entries(groupedBySession).map(([sessionId, items]) => {
        const assignments = items.filter(r => r.type === 'ASSIGNMENT');
        const materials = items.filter(r => r.type === 'MATERIAL');

        const incompleteAssignments = assignments.filter(r => r.status !== 'COMPLETED');
        const completedAssignments = assignments.filter(r => r.status === 'COMPLETED');

        return (
          <div key={sessionId} className="mb-5">
            <h6 className="mb-4 border-bottom pb-2">📚 Oturum #{sessionId}</h6>
            <Row className="g-4">
              <Col md={6}>
                <h6>📝 Ödevler</h6>
                {[...incompleteAssignments, ...completedAssignments].map(r => (
                  <Card
                    key={r.id}
                    className={`position-relative p-2 mb-3 ${
                      r.status === 'COMPLETED'
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
                          <a
                            href={`http://localhost:8080/api/students/download?fileName=${encodeURIComponent(r.studentFileName)}`}
                            className="btn btn-sm btn-outline-secondary me-2"
                            target="_blank"
                            rel="noreferrer"
                          >
                            📥 Çözüm
                          </a>
                        )}
                        <a
                          href={`http://localhost:8080/api/students/download?fileName=${encodeURIComponent(r.teacherFileName)}`}
                          className="btn btn-sm btn-outline-dark"
                          target="_blank"
                          rel="noreferrer"
                        >
                          📥 Kaynak
                        </a>
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
          </div>
        );
      })}
    </div>
  );
};

export default GroupedByStudent;
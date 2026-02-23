import React, { useEffect, useState } from 'react';
import {
  getResourcesVisibleToStudent,
  uploadHomeworkSolution,
  downloadResourceFile,
} from '../../services/resourceService';

import {
  Badge,
  Card,
  Container,
  Row,
  Col,
  Form,
  Accordion,
} from 'react-bootstrap';

const ResourceLibrary = () => {
  const [resources, setResources] = useState([]);
  const [groupedResources, setGroupedResources] = useState({});

  useEffect(() => {
    const studentId = localStorage.getItem('studentId');
    if (studentId) {
      getResourcesVisibleToStudent(studentId)
        .then((resList) => {
          setResources(resList);
          setGroupedResources(groupBySession(resList));
        })
        .catch(console.error);
    }
  }, []);

  const handleFileChange = async (e, resourceId) => {
    const file = e.target.files[0];
    if (!file) return;

    const formData = new FormData();
    formData.append("file", file);

    try {
      await uploadHomeworkSolution(resourceId, formData);
      alert("✅ Çözüm başarıyla yüklendi.");
    } catch (err) {
      console.error(err);
      alert("❌ Gönderim başarısız.");
    }
  };

  const groupBySession = (resources) => {
    const grouped = {};
    for (const res of resources) {
      const key = res.sessionId || 'Bilinmeyen Oturum';
      if (!grouped[key]) {
        grouped[key] = [];
      }
      grouped[key].push(res);
    }
    return grouped;
  };

  const getStatusBadge = (status, score) => {
    switch (status) {
      case 'PENDING_SUBMISSION':
        return <Badge bg="danger">⏳ Bekleniyor</Badge>;
      case 'REVIEWED':
        return <Badge bg="warning" text="dark">⚠️ İnceleniyor</Badge>;
      case 'COMPLETED':
        return <Badge bg="success">✅ {score} Puan</Badge>;
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
    <Container className="mt-4">
      <h4 className="mb-3">📁 Size Atanan Klasörler</h4>
      <p><strong>Toplam:</strong> {resources.length} kaynak</p>

      {Object.entries(groupedResources).length === 0 ? (
        <p className="text-muted">Henüz size atanmış bir kaynak yok.</p>
      ) : (
        <Accordion defaultActiveKey={Object.keys(groupedResources)[0]}>
          {Object.entries(groupedResources).map(([sessionId, sessionResources], idx) => {
            const assignments = sessionResources.filter(r => r.type === 'ASSIGNMENT');
            const materials = sessionResources.filter(r => r.type === 'MATERIAL');

            return (
              <Accordion.Item eventKey={idx.toString()} key={sessionId}>
                <Accordion.Header>
                  📚 Oturum #{sessionId} ({assignments.length} ödev, {materials.length} materyal)
                  {getSessionStatusSummary(sessionResources)}
                </Accordion.Header>
                <Accordion.Body>
                  <Row className="g-4">
                    {/* ÖDEVLER */}
                    <Col md={6}>
                      <h5>📝 Ödevler</h5>
                      {assignments
                        .sort((a, b) => a.status === 'COMPLETED' ? 1 : -1)
                        .map(resource => (
                          <Card
                            key={resource.id}
                            className={`shadow-sm border-3 position-relative mb-3 ${resource.status === 'COMPLETED'
                              ? 'border-light bg-warning-subtle opacity-55'
                              : 'border-warning bg-warning-subtle'
                              }`}
                          >
                            <div className="position-absolute top-0 end-0 m-2 z-1">
                              {getStatusBadge(resource.status, resource.homeWorkScore)}
                            </div>
                            <Card.Body>
                              <Card.Title>📝 {resource.resourceName}</Card.Title>
                              <Card.Subtitle className="mb-2">{resource.category}</Card.Subtitle>
                              <Card.Text>{resource.description}</Card.Text>


                              <button
                                className="btn btn-sm btn-outline-dark me-2"
                                onClick={() => downloadResourceFile(resource.teacherFileName)}
                              >
                                📥 Ödevi İndir
                              </button>

                              {resource.status === 'PENDING_SUBMISSION' && (
                                <Form.Group className="mt-2">
                                  <Form.Label>📤 Çözüm Yükle:</Form.Label>
                                  <Form.Control
                                    type="file"
                                    onChange={(e) => handleFileChange(e, resource.id)}
                                  />
                                </Form.Group>
                              )}
                            </Card.Body>
                          </Card>
                        ))}
                    </Col>

                    {/* MATERYALLER */}
                    <Col md={6}>
                      <h5>📘 Materyaller</h5>
                      {materials.map(resource => (
                        <Card
                          key={resource.id}
                          className="shadow-sm border-primary border-3 bg-primary-subtle mb-3"
                        >
                          <Card.Body>
                            <Card.Title>📘 {resource.resourceName}</Card.Title>
                            <Card.Subtitle className="mb-2">{resource.category}</Card.Subtitle>
                            <Card.Text>{resource.description}</Card.Text>
 
                            <button
                              className="btn btn-sm btn-outline-dark"
                              onClick={() => downloadResourceFile(resource.teacherFileName)}
                            >
                              📥 Materyali İndir
                            </button>

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
      )}
    </Container>
  );
};

export default ResourceLibrary;

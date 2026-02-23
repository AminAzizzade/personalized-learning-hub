import { useEffect, useState } from "react";
import { Card, Spinner, Alert, Row, Col } from "react-bootstrap";
import {
  fetchAllStudents,
  fetchAllTutors,
  fetchAllSessions,
  fetchAllAlerts,
} from "../../services/adminService";

export default function AdminHome() {
  const [loading, setLoading] = useState(true);
  const [counts, setCounts] = useState({
    students: 0,
    tutors: 0,
    sessions: 0,
    alerts: 0,
  });
  const [error, setError] = useState(null);

  useEffect(() => {
    const loadDashboard = async () => {
      try {
        const [students, tutors, sessions, alerts] = await Promise.all([
          fetchAllStudents(),
          fetchAllTutors(),
          fetchAllSessions(),
          fetchAllAlerts(),
        ]);
        setCounts({
          students: students.length,
          tutors: tutors.length,
          sessions: sessions.length,
          alerts: alerts.length,
        });
      } catch (err) {
        setError("Veriler yüklenemedi.");
      } finally {
        setLoading(false);
      }
    };

    loadDashboard();
  }, []);

  if (loading) return <Spinner animation="border" />;
  if (error) return <Alert variant="danger">{error}</Alert>;

  return (
    <div className="container">
      <h3 className="mb-4">🏠 Admin Dashboard</h3>
      <Row>
        <Col md={3}>
          <Card bg="primary" text="white" className="mb-3">
            <Card.Body>
              <Card.Title>Öğrenciler</Card.Title>
              <Card.Text>{counts.students} kayıtlı öğrenci</Card.Text>
            </Card.Body>
          </Card>
        </Col>
        <Col md={3}>
          <Card bg="success" text="white" className="mb-3">
            <Card.Body>
              <Card.Title>Eğitmenler</Card.Title>
              <Card.Text>{counts.tutors} aktif eğitmen</Card.Text>
            </Card.Body>
          </Card>
        </Col>
        <Col md={3}>
          <Card bg="info" text="white" className="mb-3">
            <Card.Body>
              <Card.Title>Oturumlar</Card.Title>
              <Card.Text>{counts.sessions} oturum oluşturulmuş</Card.Text>
            </Card.Body>
          </Card>
        </Col>
        <Col md={3}>
          <Card bg="danger" text="white" className="mb-3">
            <Card.Body>
              <Card.Title>Uyarılar</Card.Title>
              <Card.Text>{counts.alerts} devamsızlık uyarısı</Card.Text>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </div>
  );
}

import { useEffect, useState } from "react";
import { Table, Spinner, Alert } from "react-bootstrap";
import { fetchAllAlerts } from "../../services/adminService";

export default function AttendanceAlerts() {
  const [alerts, setAlerts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchAllAlerts()
      .then(setAlerts)
      .catch(() => setError("Uyarılar yüklenemedi"))
      .finally(() => setLoading(false));
  }, []);

  return (
    <div className="container">
      <h3 className="mb-4">📅 Devamsızlık Uyarıları</h3>
      {loading ? (
        <Spinner animation="border" />
      ) : error ? (
        <Alert variant="danger">{error}</Alert>
      ) : (
        <Table striped bordered hover responsive>
          <thead>
            <tr>
              <th>#</th>
              <th>Öğrenci</th>
              <th>Tarih</th>
              <th>Detay</th>
            </tr>
          </thead>
          <tbody>
            {alerts.map((alert, index) => (
              <tr key={alert.id}>
                <td>{index + 1}</td>
                <td>{alert.studentName}</td>
                <td>{alert.date}</td>
                <td>{alert.message}</td>
              </tr>
            ))}
          </tbody>
        </Table>
      )}
    </div>
  );
}

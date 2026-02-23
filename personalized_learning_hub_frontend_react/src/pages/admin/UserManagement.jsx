// src/pages/admin/UserManagement.jsx
import { useState, useEffect } from "react";
import { Tabs, Tab, Table, Spinner, Alert } from "react-bootstrap";
import { fetchAllStudents, fetchAllTutors } from "../../services/adminService";


export default function UserManagement() {
  const [key, setKey] = useState("students");
  const [students, setStudents] = useState([]);
  const [tutors, setTutors] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const loadUsers = async () => {
    setLoading(true);
    try {
      const [studentsData, tutorsData] = await Promise.all([
        fetchAllStudents(),
        fetchAllTutors(),
      ]);
      setStudents(studentsData);
      setTutors(tutorsData);
    } catch (err) {
      setError("Kullanıcılar yüklenirken hata oluştu.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadUsers();
  }, []);

  return (
    <div className="container">
      <h3 className="mb-4">👥 Kullanıcı Yönetimi</h3>

      <Tabs activeKey={key} onSelect={(k) => setKey(k)} className="mb-3">
        <Tab eventKey="students" title="Öğrenciler">
          {loading ? (
            <Spinner animation="border" />
          ) : error ? (
            <Alert variant="danger">{error}</Alert>
          ) : (
            <Table striped bordered hover responsive>
              <thead>
                <tr>
                  <th>#</th>
                  <th>Ad Soyad</th>
                  <th>Email</th>
                </tr>
              </thead>
              <tbody>
                {students.map((s, index) => (
                  <tr key={s.id}>
                    <td>{index + 1}</td>
                    <td>{s.fullName}</td>
                    <td>{s.email}</td>
                  </tr>
                ))}
              </tbody>
            </Table>
          )}
        </Tab>

        <Tab eventKey="tutors" title="Tutorlar">
          {loading ? (
            <Spinner animation="border" />
          ) : error ? (
            <Alert variant="danger">{error}</Alert>
          ) : (
            <Table striped bordered hover responsive>
              <thead>
                <tr>
                  <th>#</th>
                  <th>Ad Soyad</th>
                  <th>Email</th>
                </tr>
              </thead>
              <tbody>
                {tutors.map((t, index) => (
                  <tr key={t.id}>
                    <td>{index + 1}</td>
                    <td>{t.fullName}</td>
                    <td>{t.email}</td>
                  </tr>
                ))}
              </tbody>
            </Table>
          )}
        </Tab>
      </Tabs>
    </div>
  );
}

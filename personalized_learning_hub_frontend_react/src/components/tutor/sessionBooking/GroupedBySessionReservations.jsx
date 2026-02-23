import React, { useEffect, useState } from 'react';
import { Accordion, Table, Badge } from 'react-bootstrap';
import { getTutorReservations } from '../../../services/sessionReservationService';

const GroupedBySessionReservations = () => {
  const [groupedBySession, setGroupedBySession] = useState({});
  const tutorId = localStorage.getItem('tutorId');

  useEffect(() => {
    fetchAndGroupReservations();
  }, []);

  const fetchAndGroupReservations = async () => {
    try {
      const data = await getTutorReservations(tutorId);
      const grouped = {};

      data.forEach((r) => {
        const sessionId = r.sessionId || 'Bilinmeyen';
        if (!grouped[sessionId]) grouped[sessionId] = [];
        grouped[sessionId].push(r);
      });

      setGroupedBySession(grouped);
    } catch (err) {
      console.error('Rezervasyonlar alınamadı', err);
    }
  };

  const formatDate = (iso) => new Date(iso).toLocaleString('tr-TR');

  const getStatusBadge = (status) => {
    switch (status) {
      case 'PENDING':
        return <Badge bg="secondary">⏳ Beklemede</Badge>;
      case 'COMPLETED':
        return <Badge bg="success">✅ Tamamlandı</Badge>;
      case 'CANCELED':
        return <Badge bg="danger">❌ İptal</Badge>;
      case 'STUDENT_ABSENT':
        return <Badge bg="warning" text="dark">🚫 Öğrenci Yok</Badge>;
      default:
        return <Badge bg="light" text="dark">{status}</Badge>;
    }
  };

  return (
    <div className="mt-4">
      <h5>📚 Oturumlara Göre Rezervasyonlar</h5>
      {Object.keys(groupedBySession).length === 0 ? (
        <p className="text-muted">Henüz rezervasyon yok.</p>
      ) : (
        <Accordion alwaysOpen>
          {Object.entries(groupedBySession).map(([sessionId, reservations], index) => (
            <Accordion.Item eventKey={String(index)} key={sessionId} className="mb-3">
              <Accordion.Header>
                Oturum #{sessionId}{' '}
                <Badge bg="info" className="ms-2">{reservations.length} rezervasyon</Badge>
              </Accordion.Header>
              <Accordion.Body>
                <Table striped bordered hover responsive>
                  <thead>
                    <tr>
                      <th>#</th>
                      <th>Tarih</th>
                      <th>Durum</th>
                      <th>Skor</th>
                      <th>Öğretmen Puanı</th>
                    </tr>
                  </thead>
                  <tbody>
                    {reservations.map((r, i) => (
                      <tr key={r.id}>
                        <td>{i + 1}</td>
                        <td>{formatDate(r.dateTime)}</td>
                        <td>{getStatusBadge(r.status)}</td>
                        <td>{r.score ?? '-'}</td>
                        <td>
                          {[...Array(5)].map((_, i) => (
                            <i
                              key={i}
                              className={`bi ${i < r.tutorEvaluationRating ? "bi-star-fill text-warning" : "bi-star text-muted"} me-1`}
                            ></i>
                          ))}
                        </td>

                      </tr>
                    ))}
                  </tbody>
                </Table>
              </Accordion.Body>
            </Accordion.Item>
          ))}
        </Accordion>
      )}
    </div>
  );
};

export default GroupedBySessionReservations;

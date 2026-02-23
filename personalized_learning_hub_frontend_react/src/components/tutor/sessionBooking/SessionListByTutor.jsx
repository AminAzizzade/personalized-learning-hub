import { useEffect, useState } from "react";
import { getSessionsByTutor, approveSession, cancelSession } from "../../../services/sessionService";
import { OverlayTrigger, Tooltip } from "react-bootstrap";

const SessionListByTutor = () => {
  const [sessions, setSessions] = useState([]);
  const tutorId = localStorage.getItem("tutorId");

  useEffect(() => {
    fetchSessions();
  }, []);

  const fetchSessions = async () => {
    try {
      const data = await getSessionsByTutor(tutorId);
      const sorted = data.sort((a, b) => new Date(b.startDate) - new Date(a.startDate));

      setSessions(sorted);
    } catch (err) {
      console.error("Oturumlar alınamadı", err);
    }
  };

  const handleApprove = async (id) => {
    try {
      await approveSession(id);
      fetchSessions();
    } catch (err) {
      console.error("Onay başarısız", err);
    }
  };

  const handleCancel = async (id) => {
    try {
      await cancelSession(id);
      fetchSessions();
    } catch (err) {
      console.error("İptal başarısız", err);
    }
  };

  const formatDateTime = (isoString) => {
    const date = new Date(isoString);
    return date.toLocaleDateString("tr-TR", {
      day: "2-digit",
      month: "2-digit",
      year: "numeric",
    });
  };

  const getStatusBadge = (status) => {
    switch (status) {
      case "APPROVED":
        return <span className="badge bg-success px-3 py-2">Onaylandı</span>;
      case "PENDING":
        return <span className="badge bg-warning text-dark px-3 py-2">Beklemede</span>;
      case "CANCELLED":
        return <span className="badge bg-danger px-3 py-2">İptal Edildi</span>;
      default:
        return <span className="badge bg-secondary px-3 py-2">{status}</span>;
    }
  };

  return (
    <div className="card shadow rounded mb-5">
      <div className="card-body">
        <h4 className="card-title mb-4">📅 Oturum Planı</h4>
        {sessions.length === 0 ? (
          <div className="alert alert-info text-center py-4">
            <h5>📭 Oturum bulunamadı</h5>
            <p>Henüz öğrenciye atanmış oturum bulunmuyor.</p>
          </div>
        ) : (
          <div className="table-responsive">
            <table className="table table-bordered align-middle text-center">
              <thead className="table-light">
                <tr>
                  <th>#</th>
                  <th>Konu</th>
                  <th>Öğrenci İsmi</th>
                  <th>Tarih</th>
                  <th>Süre</th>
                  <th>Dil</th>
                  <th>Durum</th>
                  <th>Aksiyon</th>
                </tr>
              </thead>
              <tbody>
                {sessions.map((session, index) => (
                  <tr key={session.id}>
                    <td>{session.id}</td>
                    <td>{session.topic}</td>
                    <td>{session.studentName}</td>
                    <td>{formatDateTime(session.startDate)}</td>
                    <td>{session.totalDuration}</td>
                    <td>{session.language}</td>
                    <td>{getStatusBadge(session.status)}</td>
                    <td>
                      {session.status !== "APPROVED" ? (
                        <div className="d-flex justify-content-center gap-2">
                          <OverlayTrigger placement="top" overlay={<Tooltip>Onayla</Tooltip>}>
                            <button className="btn btn-sm btn-success" onClick={() => handleApprove(session.id)}>
                              ✅
                            </button>
                          </OverlayTrigger>
                          <OverlayTrigger placement="top" overlay={<Tooltip>İptal Et</Tooltip>}>
                            <button className="btn btn-sm btn-outline-danger" onClick={() => handleCancel(session.id)}>
                              ❌
                            </button>
                          </OverlayTrigger>
                        </div>
                      ) : (
                        <span className="text-muted">✔️</span>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
};

export default SessionListByTutor;

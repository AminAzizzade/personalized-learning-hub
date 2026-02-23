import React, { useState } from "react";
import RatingModal from "./RatingModal"; // modal component
import { updateReservation } from "../../services/sessionReservationService";


const renderStatusBadge = (status) => {
  switch (status) {
    case "COMPLETED":
      return <span className="badge bg-success">Onaylandı</span>;
    case "STUDENT_ABSENT":
      return <span className="badge bg-danger">Öğrenci gelmedi</span>;
    case "CANCELLED":
      return <span className="badge bg-danger">İptal edildi</span>;
    case "PENDING":
    default:
      return <span className="badge bg-warning text-dark">Beklemede</span>;
  }
};

const ReservationTable = ({ reservations }) => {
  const [selectedSessionId, setSelectedSessionId] = useState(null);

  
  // state
  const [showModal, setShowModal] = useState(false);
  const [selectedReservationId, setSelectedReservationId] = useState(null);
  const [selectedRating, setSelectedRating] = useState(0);


  if (!reservations || reservations.length === 0) {
    return <p className="text-muted">Henüz rezervasyon yapılmamış.</p>;
  }

  // Oturumlara göre grupla
  const grouped = reservations.reduce((acc, r) => {
    if (!acc[r.sessionId]) acc[r.sessionId] = [];
    acc[r.sessionId].push(r);
    return acc;
  }, {});

  const sessionIds = Object.keys(grouped);

  const selectedReservations = selectedSessionId
    ? [...grouped[selectedSessionId]].sort(
        (a, b) => new Date(a.dateTime) - new Date(b.dateTime)
      )
    : [];

  const unscoredCount = selectedReservations.filter(
    (r) => r.status === "COMPLETED" && r.tutorEvaluationRating === 0
  ).length;

  const handleRatingSubmit = async (rating) => {
    try {
      await updateReservation(selectedReservationId, {
        tutorEvaluationRating: rating,
      });
      alert("🎉 Puan gönderildi!");
      setShowModal(false);
      setSelectedReservationId(null);
      setSelectedRating(0);
    } catch (err) {
      console.error("Puan gönderilemedi", err);
      alert("Hata oluştu.");
    }
  };


  return (
    <div className="row mt-4">
      {/* Sol kenar: Oturum listesi */}
      <div className="col-md-3 border-end">
        <h5 className="mb-3">📚 Oturumlar</h5>
        <div className="list-group">
          {sessionIds.map((id) => {
            const group = grouped[id];
            const topic = group[0]?.sessionTopic || "Başlıksız";
            const unscored = group.filter(
              (r) => r.status === "COMPLETED" && r.tutorEvaluationRating === 0
            ).length;

            return (
              <button
                key={id}
                className={`list-group-item d-flex justify-content-between align-items-center list-group-item-action ${
                  selectedSessionId === id ? "active" : ""
                }`}
                onClick={() => setSelectedSessionId(id)}
              >
                <div className="text-start">
                  <strong>Oturum #{id}</strong>
                  <div className="small text-muted">{topic}</div>
                </div>
                {unscored > 0 && (
                  <span className="badge bg-danger rounded-pill">{unscored}</span>
                )}
              </button>
            );
          })}

        </div>
      </div>

      {/* Sağ taraf: Seçilen oturumun rezervasyonları */}
      <div className="col-md-9">
        {selectedSessionId ? (
          <>
            <div className="d-flex justify-content-between align-items-center mb-3">
              <h5>📋 Oturum #{selectedSessionId} Rezervasyonları</h5>
              {unscoredCount > 0 && (
                <span className="badge bg-danger">
                  🔔 {unscoredCount} öğrenci henüz eğitmeni puanlamadı
                </span>
              )}
            </div>
            <div className="table-responsive">
              <table className="table table-bordered table-striped align-middle">
                <thead className="table-light">
                  <tr>
                    <th>🗓️ Tarih</th>
                    <th>📌 Durum</th>
                    <th>🎯 Skor</th>
                    <th>⭐ Eğitmen Puanı</th>
                  </tr>
                </thead>
                <tbody>
                  {selectedReservations.map((r) => (
                    <tr key={r.id}>
                      <td>{new Date(r.dateTime).toLocaleString("tr-TR")}</td>
                      <td>{renderStatusBadge(r.status)}</td>
                      <td>{r.score ?? "-"}</td>

                      <td>
                        {r.status === "COMPLETED" && r.tutorEvaluationRating === 0 ? (
                          <button
                            className="btn btn-sm btn-outline-primary"
                            onClick={() => {
                              setSelectedReservationId(r.id);
                              setShowModal(true);
                            }}
                          >
                            ⭐ Puanla
                          </button>
                        ) : (
                          [...Array(r.tutorEvaluationRating || 0)].map((_, i) => (
                            <i key={i} className="bi bi-star-fill text-warning me-1"></i>
                          ))
                        )}
                      </td>

                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </>
        ) : (
          <div className="alert alert-info">
            Lütfen sol taraftan bir oturum seçin.
          </div>

          
        )}
      </div>
      
      <RatingModal
        show={showModal}
        onClose={() => {
          setShowModal(false);
          setSelectedReservationId(null);
          setSelectedRating(0);
        }}
        onSubmit={handleRatingSubmit}
        selectedRating={selectedRating}
        setSelectedRating={setSelectedRating}
      />

    </div>
  );
};

export default ReservationTable;

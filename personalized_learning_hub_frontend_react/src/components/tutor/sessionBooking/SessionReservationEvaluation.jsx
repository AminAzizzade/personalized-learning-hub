import { useEffect, useState } from "react";
import { getTutorReservations, updateReservation } from "../../../services/sessionReservationService";

export default function SessionReservationEvaluation() {
  const [reservations, setReservations] = useState([]);
  const [editable, setEditable] = useState([]);
  const tutorId = localStorage.getItem("tutorId");

  useEffect(() => {
    fetchReservations();
  }, []);

  const fetchReservations = async () => {
    try {
      const data = await getTutorReservations(tutorId);
      const filtered = data.filter((r) => {
        const isPast = new Date(r.dateTime) < new Date();
        const isUnscored = r.score === null;
        const isPending = r.status === "PENDING";
        return isPast && (isUnscored || isPending);
      });
      setReservations(filtered);
      setEditable(
        filtered.map((r) => ({
          id: r.id,
          status: r.status,
          score: r.score,
        }))
      );
    } catch (err) {
      console.error("Rezervasyonlar alınamadı", err);
    }
  };

  const handleChange = (index, field, value) => {
    const updated = [...editable];
    updated[index][field] = value;
    setEditable(updated);
  };

  const handleUpdate = async (index) => {
    const { id, status, score } = editable[index];
    try {
      await updateReservation(id, { status, score });
      alert("✅ Rezervasyon güncellendi.");
      fetchReservations();
    } catch (err) {
      console.error("Güncelleme başarısız", err);
    }
  };

  return (
    <div className="container">
      <h4 className="mb-4">📋 Geçmiş Rezervasyonları Değerlendir</h4>

      {reservations.length === 0 ? (
        <div className="alert alert-info text-center py-4">
          <h5>📭 Henüz değerlendirilecek rezervasyon yok</h5>
          <p>Geçmiş oturumlara ait değerlendirme bekleyen kayıt bulunamadı.</p>
        </div>
      ) : (
        <div className="table-responsive">
          <table className="table table-bordered align-middle text-center">
            <thead className="table-light">
              <tr>
                <th>Session #</th>
                <th>Tarih</th>
                <th>Durum</th>
                <th>Skor</th>
                <th>İşlem</th>
              </tr>
            </thead>
            <tbody>
              {reservations.map((r, index) => (
                <tr key={r.id}>
                  <td>{r.sessionId}</td>
                  <td>{new Date(r.dateTime).toLocaleString("tr-TR")}</td>
                  <td>
                    <select
                      className="form-select"
                      value={editable[index]?.status}
                      onChange={(e) => handleChange(index, "status", e.target.value)}
                    >
                      <option value="PENDING">⏳ Beklemede</option>
                      <option value="COMPLETED">✅ Tamamlandı</option>
                      <option value="CANCELED">❌ İptal Edildi</option>
                      <option value="STUDENT_ABSENT">🚫 Öğrenci Gelmedi</option>
                    </select>
                  </td>
                  <td>
                    <input
                      type="number"
                      min={0}
                      max={100}
                      value={editable[index]?.score}
                      onChange={(e) => handleChange(index, "score", Number(e.target.value))}
                      className="form-control text-center"
                      placeholder="0 - 100"
                    />
                  </td>
                  <td>
                    <button className="btn btn-sm btn-success" onClick={() => handleUpdate(index)}>
                      💾 Güncelle
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

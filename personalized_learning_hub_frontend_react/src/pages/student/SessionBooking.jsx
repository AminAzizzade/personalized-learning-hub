import React, { useEffect, useState } from "react";
import {getSessionsByStudent} from "../../services/sessionService";
import {getTutorAvailability} from "../../services/tutorService";
import {
  getStudentReservations,
  createReservation,
} from "../../services/sessionReservationService";

import SessionCard from "../../components/student/SessionCard";
import ReservationTable from "../../components/student/ReservationTable";
import MiniAvailabilityGrid from "../../components/student/MiniAvailabilityGrid";
import { Modal, Button } from "react-bootstrap";

const MiniAvailabilityModal = ({
  show,
  onClose,
  session,
  slots,
  selected,
  onSelect,
  onSubmit,
  weekStartDate,
  setWeekStartDate,
}) => {
  const startDate = new Date(weekStartDate);
  const endDate = new Date(startDate);
  endDate.setDate(startDate.getDate() + 6);

  const formatDate = (date) =>
    date.toLocaleDateString("tr-TR", {
      day: "numeric",
      month: "long",
      year: "numeric",
    });

  const goToPreviousWeek = () => {
    const prev = new Date(weekStartDate);
    prev.setDate(prev.getDate() - 7);
    setWeekStartDate(prev.toISOString().split("T")[0]);
  };

  const goToNextWeek = () => {
    const next = new Date(weekStartDate);
    next.setDate(next.getDate() + 7);
    setWeekStartDate(next.toISOString().split("T")[0]);
  };

  return (
    <Modal show={show} onHide={onClose} centered size="lg">
      <Modal.Header closeButton>
        <Modal.Title>{session?.topic} - Uygun Saat Seçimi</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <div className="d-flex justify-content-between align-items-center mb-2">
          <Button size="sm" variant="outline-secondary" onClick={goToPreviousWeek}>
            ← Önceki Hafta
          </Button>
          <span className="fw-bold text-muted">
            {formatDate(startDate)} - {formatDate(endDate)}
          </span>
          <Button size="sm" variant="outline-secondary" onClick={goToNextWeek}>
            Sonraki Hafta →
          </Button>
        </div>

        <MiniAvailabilityGrid
          slots={slots}
          selected={selected}
          onSelect={onSelect}
        />
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onClose}>
          İptal
        </Button>
        <Button variant="info" onClick={onSubmit}>
          Gönder
        </Button>
      </Modal.Footer>
    </Modal>
  );
};



const renderStatusBadge = (status) => {
  switch (status) {
    case "CONFIRMED":
      return <span className="badge bg-success">Onaylandı</span>;
    case "CANCELLED":
      return <span className="badge bg-danger">İptal Edildi</span>;
    case "COMPLETED":
      return <span className="badge bg-info">Tamamlandı</span>;
    case "PENDING":
    default:
      return <span className="badge bg-warning text-dark">Beklemede</span>;
  }
};

const SessionBooking = () => {
  const studentId = localStorage.getItem("studentId");
  const [sessions, setSessions] = useState([]);
  const [reservations, setReservations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [availabilitySlots, setAvailabilitySlots] = useState([]);
  const [selectedSession, setSelectedSession] = useState(null);
  const [selectedSlot, setSelectedSlot] = useState(null);
  const [showModal, setShowModal] = useState(false);




  // Set default week start date to today
  useEffect(() => {
    if (!studentId) {
      setError("Öğrenci kimliği bulunamadı.");
      setLoading(false);
      return;
    }

    getSessionsByStudent(studentId)
      .then((response) => {
        setSessions(response);
        setLoading(false);
      })
      .catch((err) => {
        console.error("Oturumlar alınırken hata oluştu:", err);
        setError("Oturumlar yüklenemedi.");
        setLoading(false);
      });

    getStudentReservations(studentId)
      .then((response) => {
        setReservations(response);
      })
      .catch((err) => {
        console.error("Rezervasyonlar alınırken hata oluştu:", err);
      });
  }, [studentId]);

  const handleFetchAvailability = (session) => {
    getTutorAvailability(session.id)
      .then((slots) => {
        setSelectedSession(session);
        setAvailabilitySlots(slots);
        setShowModal(true);
      })
      .catch((err) => {
        console.error("Uygun saatler alınamadı:", err);
      });
  };

  const getStartOfWeek = (date = new Date()) => {
    const d = new Date(date);
    const day = d.getDay(); // 0 = Pazar, 1 = Pazartesi, ...
    const diff = day === 0 ? -6 : 1 - day; // Pazartesi'ye gitmek için fark
    d.setDate(d.getDate() + diff);
    return d.toISOString().split("T")[0]; // yyyy-MM-dd formatında döndür
  };

  const [weekStartDate, setWeekStartDate] = useState(getStartOfWeek());




  const handleCreateReservation = () => {
    if (!selectedSlot || !selectedSession || !weekStartDate) return;

    const [day, timeRange] = selectedSlot.split(","); // "cuma", "09:00-10:00"
    const reservationKey = `${weekStartDate},${day},${timeRange}`;

    const requestBody = {
      sessionId: selectedSession.id,
      dateTime: reservationKey, // örn: "2025-06-10,cuma,09:00-10:00"
      status: "PENDING",
      score: 0,
      tutorEvaluationRating: 0
    };

    createReservation(requestBody)
      .then(() => {
        alert("Rezervasyon oluşturuldu.");
        setSelectedSlot(null);
        setSelectedSession(null);
        setShowModal(false);
        getStudentReservations(studentId).then(setReservations);
      })
      .catch((err) => {
        console.error("Rezervasyon oluşturulamadı:", err);
      });
  };




  if (loading) return <p>⏳ Yükleniyor...</p>;
  if (error) return <p className="text-danger">{error}</p>;

  const half = Math.ceil(sessions.length / 2);
  const firstColumn = sessions.slice(0, half);
  const secondColumn = sessions.slice(half);

  return (
    <div className="container">
      <h3 className="mb-4">📆 Oturum Planlama</h3>

      <div className="row">
        <div className="col-bg-8">
          <div className="row">
            <div className="col-md-6">
              {firstColumn.map((session) => (
                <div key={session.id} className="mb-4">
                  <SessionCard
                    session={session}
                    onFetchAvailability={() => handleFetchAvailability(session)}
                    renderStatusBadge={renderStatusBadge}
                  />
                </div>
              ))}
            </div>
            <div className="col-md-6">
              {secondColumn.map((session) => (
                <div key={session.id} className="mb-4">
                  <SessionCard
                    session={session}
                    onFetchAvailability={() => handleFetchAvailability(session)}
                    renderStatusBadge={renderStatusBadge}
                  />
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>


        <MiniAvailabilityModal
          show={showModal}
          onClose={() => setShowModal(false)}
          session={selectedSession}
          slots={availabilitySlots}
          selected={selectedSlot}
          onSelect={setSelectedSlot}
          onSubmit={handleCreateReservation}
          weekStartDate={weekStartDate}
          setWeekStartDate={setWeekStartDate}
        />


      <div className="mt-5">
        <h4>📋 Rezervasyonlarım</h4>
        <ReservationTable
          reservations={reservations}
          renderStatusBadge={renderStatusBadge}
        />
      </div>
    </div>
  );
};

export default SessionBooking;

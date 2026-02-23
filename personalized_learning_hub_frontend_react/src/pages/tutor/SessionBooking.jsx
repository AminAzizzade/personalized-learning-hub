import { useEffect, useState } from "react";
import {
  getSessionsByTutor,
  approveSession,
  cancelSession,
} from "../../services/sessionService";

import {
  getTutorReservations,
  updateReservation,
} from "../../services/sessionReservationService";

import SessionListByTutor from "../../components/tutor/sessionBooking/SessionListByTutor";
import SessionReservationEvaluation from "../../components/tutor/sessionBooking/SessionReservationEvaluation";
import GroupedBySessionReservations from "../../components/tutor/sessionBooking/GroupedBySessionReservations";

export default function SessionBooking() {
  const [sessions, setSessions] = useState([]);
  const tutorId = localStorage.getItem("tutorId");

  useEffect(() => {
    fetchSessions();
  }, []);

  const fetchSessions = async () => {
    try {
      const data = await getSessionsByTutor(tutorId);
      setSessions(data);
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

  return (
    <div className="container mt-5">
      <SessionListByTutor />
      <SessionReservationEvaluation />
      <GroupedBySessionReservations />
    </div>
  );
}

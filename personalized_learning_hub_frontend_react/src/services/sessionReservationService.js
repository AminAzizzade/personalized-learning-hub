import api from "../utils/axiosConfig";



// =======================
// === CRUD İŞLEMLERİ ===
// =======================

/** Yeni rezervasyon oluşturur */
export const createReservation = async (requestBody) => {
  const response = await api.post(`/reservations`, requestBody);
  return response.data;
};

/** Rezervasyonu günceller */
export const updateReservation = async (reservationId, data) => {
  const response = await api.put(`/reservations/${reservationId}`, data);
  return response.data;
};

/** Rezervasyonu siler */
export const deleteReservation = async (reservationId) => {
  await api.delete(`/reservations/${reservationId}`);
};



// ==========================
// === STUDENT İŞLEMLERİ ====
// ==========================

/** Belirli öğrenciye ait tüm rezervasyonları getirir */
export const getStudentReservations = async (studentId) => {
  const response = await api.get(`/reservations/student/${studentId}`);
  return response.data;
};



// ========================
// === TUTOR İŞLEMLERİ ====
// ========================

/** Belirli eğitmene ait tüm rezervasyonları getirir */
export const getTutorReservations = async (tutorId) => {
  const response = await api.get(`/reservations/tutor/${tutorId}`);
  return response.data;
};

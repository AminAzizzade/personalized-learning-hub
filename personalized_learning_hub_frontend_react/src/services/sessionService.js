import api from "../utils/axiosConfig";


// =======================
// === CRUD İŞLEMLERİ ===
// =======================

/** Oturumun durumunu günceller */
export const updateSessionStatus = async (sessionId, data) => {
  const response = await api.put(`/sessions/${sessionId}/status`, data);
  return response.data;
};

/** Oturumu siler */
export const deleteSession = async (sessionId) => {
  await api.delete(`/sessions/${sessionId}`);
};



// =========================
// === STUDENT İŞLEMLERİ ===
// =========================

/** Belirli öğrenciye ait oturumları getirir */
export const getSessionsByStudent = async (studentId) => {
  const response = await api.get(`/sessions/student/${studentId}`);
  return response.data;
};

/** Öğrencinin oturumlara ait ilerlemelerini getirir */
export const getStudentProgress = async (studentId) => {
  const response = await api.get(`/sessions/student/${studentId}/progress`);
  return response.data;
};



// =======================
// === TUTOR İŞLEMLERİ ===
// =======================

/** Belirli eğitmene ait onaylanmış tüm oturumları getirir */
export const getSessionsByTutor = async (tutorId) => {
  const response = await api.get(`/sessions/tutor/${tutorId}`);
  return response.data;
};

/** Belirli eğitmene ait onay bekleyen oturumları getirir */
export const getPendingSessions = async (tutorId) => {
  const response = await api.get(`/sessions/tutor/${tutorId}/pending`);
  return response.data;
};

/** Oturumu onaylar */
export const approveSession = async (sessionId) => {
  await api.put(`/sessions/tutor/${sessionId}/approve`);
};

/** Oturumu iptal eder */
export const cancelSession = async (sessionId) => {
  await api.put(`/sessions/tutor/${sessionId}/cancel`);
};


/** Öğretmenin öğrencileri için oturum bazlı ilerleme verilerini getirir */

export const getTutorProgressTracking = async (tutorId) => {
  const response = await api.get(`/sessions/tutor/${tutorId}/progress`);
  return response.data;
};
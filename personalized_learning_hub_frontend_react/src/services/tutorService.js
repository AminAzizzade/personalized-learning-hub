// import api from "../utils/axiosConfig";

// // Otomatik olarak tutorId'yi localStorage'dan al
// const getTutorId = () => localStorage.getItem("tutorId");

// export const getAvailableTimes = async(tutorId) => {
//   const response = await api.get(`/tutors/${tutorId}/available-times`);
//   return response.data;
// }

// // Öğrenci listesi
// export const getAssignedStudents = async () => {
//   const response = await api.get(`/tutors/assigned-students/${getTutorId()}`);
//   return response.data;
// };

// export const getTutorSessions = async (tutorId) => {
//   const response = await api.get(`/tutors/sessions/${tutorId}`);
//   return response.data;
// }

// // Oturum taleplerini getir
// export const getPendingSessions = async () => {
//   const response = await api.get(`/tutors/pending-sessions/${getTutorId()}`);
//   return response.data;
// };

// // Oturumu onayla
// export const approveSession = async (sessionId) => {
//   await api.put(`/tutors/approve-session/${sessionId}`);
// };

// // Oturumu iptal et
// export const cancelSession = async (sessionId) => {
//   await api.put(`/tutors/cancel-session/${sessionId}`);
// };

// // İlerleme takibini getir
// export const getProgressTracking = async () => {
//   const response = await api.get(`/tutors/progress-tracking/${getTutorId()}`);
//   return response.data;
// };

// // Resource



// // Belirli tutorId’ye ait sessionlara bağlı kaynakları gruplu getir
// export const getMyResources = async () => {
//   const response = await api.get(`/tutors/${getTutorId()}/resources/grouped-by-student`);
//   return response.data;
// };

// export const getGroupedResources = async () => {
//   const response = await api.get(`/tutors/${getTutorId()}/resources/grouped-by-student`);
//   return response.data;
// };

// // Kaynak oluştur
// export const createResource = async (data) => {
//   const response = await api.post(`/resources`, data);
//   return response.data;
// };

// // Kaynağı güncelle
// export const updateResource = async (id, data) => {
//   const response = await api.put(`/resources/${id}`, data);
//   return response.data;
// };

// // Kaynağı sil
// export const deleteResource = async (id) => {
//   await api.delete(`/resources/${id}`);
// };

// // Dosya yükle
// export const uploadFile = async (formData) => {
//   const response = await api.post(`/tutors/resources/`, formData, {
//     headers: { 'Content-Type': 'multipart/form-data' }
//   });
//   return response.data.url;
// };

// // Profile
// export const getTutorProfile = async (id) => {
//   const response = await api.get(`/tutors/${id}`);
//   return response.data;
// };


// export const updateTutorProfile = async (tutorId, profileData) => {
//   return await api.put(`/tutors/${tutorId}`, profileData);
// };

// export const updateTutorCredentials = async (userId, payload) => {
//   return await api.put(`/auth/${userId}/credentials`, payload);
// };

// // export const getUserById = async (userId) => {
// //   return await api.get(`/auth/${userId}`);
// // };

// // Öğrenciye ait oturumları getirir
// export const getStudentSessions = async (studentId) => {
//   const response = await api.get(`/students/${studentId}/sessions`);
//   return response.data;
// };

// // Öğrencinin oluşturduğu tüm rezervasyonları getirir
// export const getStudentReservations = async (studentId) => {
//   const response = await api.get(`/session-reservations/student/${studentId}`);
//   return response.data;
// };

// // Oturuma ait öğretmenin uygun olduğu saatleri getirir
// export const getAvailabilitySlots = async (sessionId) => {
//   const response = await api.get(`/sessions/${sessionId}/availabilities`);
//   return response.data;
// };

// // Rezervasyon 
// export const createReservation = async ({ sessionId, dateTime }) => {
//   const payload = {
//     sessionId,
//     dateTime,
//   };
//   const response = await api.post(`/session-reservations`, payload);
//   return response.data;
// };

// export const gradeAssignment = async (resourceId, score) => {
//   const response = await api.put(`/tutors/${resourceId}/gradeAssignment/${score}`);
//   return response.data;
// }

// export const getTutorReservations = async (tutorId) => {
//   const response = await api.get(`/tutors/${tutorId}/reservations`);
//   return response.data;
// };

// export const updateReservation = async (reservationId, data) => {
//   await api.put(`/reservations/${reservationId}`, data);
// };



import api from "../utils/axiosConfig";


// ========== CRUD ==========

/**
 * Belirli bir öğretmeni ID’ye göre getirir
 */
export const getTutorProfile = async (id) => {
  const response = await api.get(`/tutors/${id}`);
  return response.data;
};

/**
 * Öğretmen profil bilgilerini günceller
 */
export const updateTutorProfile = async (tutorId, profileData) => {
  const response = await api.put(`/tutors/${tutorId}`, profileData);
  return response.data;
};

/**
 * Belirli bir öğretmeni sistemden siler
 */
export const deleteTutor = async (tutorId) => {
  await api.delete(`/tutors/${tutorId}`);
};



// ========== TUTOR İŞLEMLERİ ==========

/**
 * Belirli bir session’a atanmış öğretmenin uygunluk saatlerini getirir
 */
export const getTutorAvailability = async (sessionId) => {
  const response = await api.get(`/tutors/${sessionId}/availabilities`);
  return response.data;
};

/**
 * Öğretmene atanmış öğrenci listesini getirir
 */
export const getAssignedStudents = async (tutorId) => {
  const response = await api.get(`/tutors/assigned-students/${tutorId}`);
  return response.data;
};

/**
 * Öğretmenin herkese açık bilgilerini getirir
 */
export const getPublicTutorProfile = async (id) => {
  const response = await api.get(`/tutors/public/${id}`);
  return response.data;
};

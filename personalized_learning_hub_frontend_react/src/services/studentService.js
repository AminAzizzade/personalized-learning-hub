// import api from "../utils/axiosConfig";

// export const getStudentProgress = async (studentId) => {
//   const response = await api.get(`/students/${studentId}/progress`);
//   return response.data;
// };

// export const getStudentResources = async (studentId) => {
//   const response = await api.get(`/students/resource/${studentId}`);
//   return response.data;
// };



// // Profile

// export const getStudentProfile = async (id) => {
//   const response = await api.get(`/students/${id}`);
//   return response.data;
// };

// export const updateStudentInfo = async (studentId, data) => {
//   return api.put(`/students/${studentId}`, data);
// };

// // Şifre güncelleme gerekiyorsa ayrıca tanımlanabilir:
// export const updateStudentCredentials = async (userId, payload) => {
//   return api.put(`/users/${userId}/credentials`, payload);
// };


// // Skill Assessment

// export const submitSkillAssessment = async (studentId, form) => {
//   return await api.post(`/students/${studentId}/skill-assessments`, form);
// };

// export const getSkillAssessmentsByStudentId = async (studentId) => {
//   return await api.get(`/students/${studentId}/skill-assessments`);
// };

// export const deleteSkillAssessment = async (id) => {
//   return await api.delete(`/students/skill-assessments/${id}`);
// };



// // Session Booking

// export const getSessionBooking = async (studentId) => {
//   const response = await api.get(`/students/${studentId}/sessions`);
//   return response.data;
// };




// export const getResourcesForStudent = async (studentId) => {
//   const response = await api.get(`/resources/${studentId}`);
//   return response.data;
// };



// export const downloadResourceFile = (fileName) => {
//   const url = `http://localhost:8080/api/students/download?fileName=${encodeURIComponent(fileName)}`;
//   // Yeni sekmede indirme başlatmak için
//   const link = document.createElement("a");
//   link.href = url;
//   link.setAttribute("download", fileName);
//   document.body.appendChild(link);
//   link.click();
//   document.body.removeChild(link);
// };


// export const uploadAssignmentSolution = async (resourceId, formData) => {
//   const response = await fetch(`http://localhost:8080/api/students/${resourceId}/solution`, {
//     method: "POST",
//     body: formData,
//   });

//   if (!response.ok) {
//     throw new Error("Failed to upload solution");
//   }
// };



// // Öğrenciye ait oturumları getirir
// export const getStudentSessions = async (studentId) => {
//   const response = await api.get(`/students/${studentId}/sessions`);
//   return response.data;
// };

// // Öğrencinin oluşturduğu tüm rezervasyonları getirir
// export const getStudentReservations = async (studentId) => {
//   const response = await api.get(`/students/${studentId}/reservations`);
//   return response.data;
// };

// // öğretmen Availaibility
// export const getAvailabilitySlots = async (sessionId) => {
//   const response = await api.get(`/tutors/session/${sessionId}/availabilities`);
//   return response.data; // örn: ["Pazartesi 10:00-11:00", "Çarşamba 14:00-15:00"]
// };

// // Rezervasyon oluşturur
// export const createReservation = async (sessionId, dateTime) => {
//   const response = await api.post(`/sessions/${sessionId}/reservations`, {
//     dateTime: dateTime  // sadece bu gönderilecek
//   });
//   return response.data;
// };





import api from "../utils/axiosConfig";

// ========== CRUD İŞLEMLERİ ==========

/**
 * Belirli bir öğrenci profilini ID’ye göre getirir
 */
export const getStudentProfile = async (id) => {
  const response = await api.get(`/students/${id}`);
  return response.data;
};

/**
 * Öğrenci bilgilerini günceller
 */
export const updateStudentInfo = async (studentId, data) => {
  const response = await api.put(`/students/${studentId}`, data);
  return response.data;
};

/**
 * Belirli bir öğrenciyi sistemden siler
 */
export const deleteStudent = async (studentId) => {
  await api.delete(`/students/${studentId}`);
};



// ========== PUBLIC (GENEL) BİLGİLER ==========

/**
 * Belirli bir öğrencinin herkese açık bilgilerini getirir
 */
export const getPublicStudentProfile = async (id) => {
  const response = await api.get(`/students/public/${id}`);
  return response.data;
};

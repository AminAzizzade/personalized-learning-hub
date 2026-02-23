// import api from '../utils/axiosConfig';




// export const registerStudent = (data) => {
//   return api.post('/auth/register/student', data);
// };

// export const registerTutor = (data) => {
//   return api.post('/auth/register/tutor', data);
// };


import api from "../utils/axiosConfig";

/**
 * Kullanıcı giriş yapar (öğrenci veya öğretmen)
 */


export const loginUser = (data) => {
  return api.post('/auth/login', data);
};
// export const login = async (credentials) => {
//   const response = await api.post("/auth/login", credentials);
//   return response.data;
// };


/**
 * Yeni öğrenci kaydı yapar
 */
export const registerStudent = async (studentData) => {
  const response = await api.post("/auth/register/student", studentData);
  return response.data;
};

/**
 * Yeni öğretmen kaydı yapar
 */
export const registerTutor = async (tutorData) => {
  const response = await api.post("/auth/register/tutor", tutorData);
  return response.data;
};


/**
 * Kullanıcının giriş bilgilerini (şifre veya e-posta) günceller
 */
export const updateUserCredentials = async (userId, payload) => {
  const response = await api.put(`/auth/${userId}/credentials`, payload);
  return response.data;
};

// export const updateTutorCredentials = async (userId, payload) => {
//   return await api.put(`/auth/${userId}/credentials`, payload);
// };
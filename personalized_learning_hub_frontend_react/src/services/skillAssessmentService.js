import api from "../utils/axiosConfig";

// ========== STUDENT İŞLEMLERİ ==========

/**
 * Belirli bir öğrenciye ait yetenek değerlendirmelerini getirir
 */
export const getSkillAssessmentsByStudentId = async (studentId) => {
  const response = await api.get(`/skill-assessments/${studentId}`);
  return response.data;
};

/**
 * Yeni bir yetenek değerlendirmesi oluşturur
 */
export const createSkillAssessment = async (studentId, form) => {
  const response = await api.post(`/skill-assessments/${studentId}`, form);
  return response.data;
};

/**
 * Belirli bir yetenek değerlendirmesini siler
 */
export const deleteSkillAssessment = async (assessmentId) => {
  await api.delete(`/skill-assessments/${assessmentId}`);
};

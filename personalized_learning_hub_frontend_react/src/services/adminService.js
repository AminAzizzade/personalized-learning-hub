// src/services/adminService.js
import api from '../utils/axiosConfig';

export const fetchAllStudents = async () => {
  const response = await api.get('/admin/students');
  return response.data;
};

export const fetchAllTutors = async () => {
  const response = await api.get('/admin/tutors');
  return response.data;
};

// İleride:
export const fetchAllSessions = async () => {
  const response = await api.get('/admin/sessions');
  return response.data;
};

export const fetchAllPayments = async () => {
  const response = await api.get('/admin/payments');
  return response.data;
};

export const fetchAllAlerts = async () => {
  const response = await api.get('/admin/alerts');
  return response.data;
};

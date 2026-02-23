import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Login from '../pages/auth/Login';
import Register from '../pages/auth/Register';

import StudentLayout from '../layouts/StudentLayout';
import TutorLayout from '../layouts/TutorLayout';
import AdminLayout from '../layouts/AdminLayout';

import StudentHome from '../pages/student/Home';
import SkillAssessment from '../pages/student/SkillAssessment';
import SessionBooking from '../pages/student/SessionBooking';
import ProgressTracking from '../pages/student/ProgressTracking';
import ResourceLibrary from '../pages/student/ResourceLibrary';
import Alerts from '../pages/student/Alerts';
import StudentPayments from '../pages/student/Payments';
import StudentProfile from '../pages/student/Profile';

import TutorHome from '../pages/tutor/Home';
import AssignedStudents from '../pages/tutor/AssignedStudents';
import TutorSessionBooking from '../pages/tutor/SessionBooking';
import TutorProgressTracking from '../pages/tutor/ProgressTracking';
import TutorResourceLibrary from '../pages/tutor/ResourceLibrary';
import TutorPayments from '../pages/tutor/Payments';
import TutorProfile from '../pages/tutor/Profile';

import AdminHome from '../pages/admin/Home';
import UserManagement from '../pages/admin/UserManagement';
import AttendanceAlerts from '../pages/admin/AttendanceAlerts';
import SystemSettings from '../pages/admin/SystemSettings';

export default function AppRouter() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/login" />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        <Route path="/student" element={<StudentLayout />}>
          <Route index element={<StudentHome />} />
          <Route path="skill-assessment" element={<SkillAssessment />} />
          <Route path="session-booking" element={<SessionBooking />} />
          <Route path="progress-tracking" element={<ProgressTracking />} />
          <Route path="resource-library" element={<ResourceLibrary />} />
          <Route path="alerts" element={<Alerts />} />
          <Route path="payments" element={<StudentPayments />} />
          <Route path="profile" element={<StudentProfile />} />
        </Route>

        <Route path="/tutor" element={<TutorLayout />}>
          <Route index element={<TutorHome />} />
          <Route path="assigned-students" element={<AssignedStudents />} />
          <Route path="session-booking" element={<TutorSessionBooking />} />
          <Route path="progress-tracking" element={<TutorProgressTracking />} />
          <Route path="resource-library" element={<TutorResourceLibrary />} />
          <Route path="payments" element={<TutorPayments />} />
          <Route path="profile" element={<TutorProfile />} />
        </Route>

        <Route path="/admin" element={<AdminLayout />}>
          <Route index element={<AdminHome />} />
          <Route path="users" element={<UserManagement />} />
          <Route path="alerts" element={<AttendanceAlerts />} />
          <Route path="settings" element={<SystemSettings />} />
        </Route>

      </Routes>
    </BrowserRouter>
  );
}

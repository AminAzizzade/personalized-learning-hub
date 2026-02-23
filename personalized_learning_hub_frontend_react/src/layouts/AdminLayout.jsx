import AdminNavbar from '../components/navbar/AdminNavbar';
import { Outlet } from 'react-router-dom';

export default function AdminLayout() {
  return (
    <>
      <AdminNavbar />
      <main className="container mt-4">
        <Outlet />
      </main>
    </>
  );
}



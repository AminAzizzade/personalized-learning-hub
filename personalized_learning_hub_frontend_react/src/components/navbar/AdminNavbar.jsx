// src/components/navbar/AdminNavbar.jsx
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";

export default function AdminNavbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-dark px-4">
      <Link className="navbar-brand fw-bold" to="/admin">
        Admin Paneli
      </Link>

      <div className="collapse navbar-collapse">
        <ul className="navbar-nav me-auto">
          <li className="nav-item">
            <Link className="nav-link" to="/admin/users">Kullanıcı Yönetimi</Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/admin/alerts">Devamsızlık Uyarıları</Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/admin/settings">Sistem Ayarları</Link>
          </li>
        </ul>

        <span className="navbar-text text-white me-3">
          {user?.email}
        </span>
        <button onClick={handleLogout} className="btn btn-outline-light btn-sm">
          Çıkış Yap
        </button>
      </div>
    </nav>
  );
}

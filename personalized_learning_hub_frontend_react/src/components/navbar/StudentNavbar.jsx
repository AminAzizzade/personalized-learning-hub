import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";

export default function StudentNavbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();            // context'ten çıkış
    navigate("/login");  // login sayfasına yönlendir
  };

  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-primary px-4">
      <Link className="navbar-brand fw-bold" to="/student">
        Öğrenci Paneli
      </Link>

      <div className="collapse navbar-collapse">
        <ul className="navbar-nav me-auto">
          <li className="nav-item">
            <Link className="nav-link" to="/student/skill-assessment">Beceri Değerlendirme</Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/student/session-booking">Oturum Planlama</Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/student/progress-tracking">İlerleme Takibi</Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/student/resource-library">Kaynaklar</Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/student/alerts">Bildirimler</Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/student/payments">Ödemelerim</Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/student/profile">Profil</Link>
          </li>
        </ul>

        <span className="navbar-text text-white me-3">
          {user?.fullName}
        </span>
        <button onClick={handleLogout} className="btn btn-outline-light btn-sm">
          Çıkış Yap
        </button>
      </div>
    </nav>
  );
}

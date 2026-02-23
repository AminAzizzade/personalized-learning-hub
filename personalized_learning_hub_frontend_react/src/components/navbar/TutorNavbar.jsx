import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";

export default function TutorNavbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-dark px-4">
      <Link className="navbar-brand fw-bold" to="/tutor">
        Tutor Paneli
      </Link>

      <div className="collapse navbar-collapse">
        <ul className="navbar-nav me-auto">
          <li className="nav-item">
            <Link className="nav-link" to="/tutor/assigned-students">Atanan Öğrenciler</Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/tutor/session-booking">Oturum Planlama</Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/tutor/progress-tracking">İlerleme Takibi</Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/tutor/resource-library">Kaynaklar</Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/tutor/payments">Cüzdanım</Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/tutor/profile">Profil</Link>
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

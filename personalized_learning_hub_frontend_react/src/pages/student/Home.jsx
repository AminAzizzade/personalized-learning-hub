import { useAuth } from "../../context/AuthContext";
import { Link } from "react-router-dom";

export default function StudentHome() {
  const { user } = useAuth();

  return (
    <div className="container mt-4">
      <div className="text-center">
        <h2 className="fw-bold">Hoşgeldiniz, {user?.fullName}</h2>
        <p className="text-muted">Öğrenci paneline giriş yaptınız.</p>
      </div>

      <div className="row mt-5">
        <div className="col-md-6 mb-4">
          <div className="card shadow-sm h-100">
            <div className="card-body">
              <h5 className="card-title">Beceri Değerlendirme</h5>
              <p className="card-text">Yetkinliklerinizi değerlendirin.</p>
              <Link to="/student/skill-assessment" className="btn btn-primary">
                Git
              </Link>
            </div>
          </div>
        </div>

        <div className="col-md-6 mb-4">
          <div className="card shadow-sm h-100">
            <div className="card-body">
              <h5 className="card-title">Oturum Planlama</h5>
              <p className="card-text">Yeni bir eğitim oturumu planlayın.</p>
              <Link to="/student/session-booking" className="btn btn-primary">
                Git
              </Link>
            </div>
          </div>
        </div>

        <div className="col-md-6 mb-4">
          <div className="card shadow-sm h-100">
            <div className="card-body">
              <h5 className="card-title">İlerleme Takibi</h5>
              <p className="card-text">Öğrenme sürecinizi analiz edin.</p>
              <Link to="/student/progress-tracking" className="btn btn-primary">
                Git
              </Link>
            </div>
          </div>
        </div>

        <div className="col-md-6 mb-4">
          <div className="card shadow-sm h-100">
            <div className="card-body">
              <h5 className="card-title">Kaynak Kütüphanesi</h5>
              <p className="card-text">Eğitim materyallerine erişin.</p>
              <Link to="/student/resource-library" className="btn btn-primary">
                Git
              </Link>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
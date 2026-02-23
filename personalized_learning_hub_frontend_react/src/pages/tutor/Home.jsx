import { useAuth } from "../../context/AuthContext";
import { Link } from "react-router-dom";

export default function TutorHome() {
  const { user } = useAuth();


  return (
    <div className="container mt-4">
      <div className="text-center">
        <h2 className="fw-bold">Hoşgeldiniz,  {user?.fullName}</h2>
        <p className="text-muted">Eğitmen paneline eriştiniz. Öğrencilerinizi yönetin ve oturumlarınızı planlayın.</p>
      </div>

      <div className="row mt-5">
        <div className="col-md-6 mb-4">
          <div className="card shadow-sm h-100">
            <div className="card-body">
              <h5 className="card-title">🎯 Atanan Öğrenciler</h5>
              <p className="card-text">Size atanmış öğrencilerin listesine erişin.</p>
              <Link to="/tutor/assigned-students" className="btn btn-primary">
                Git
              </Link>
            </div>
          </div>
        </div>

        <div className="col-md-6 mb-4">
          <div className="card shadow-sm h-100">
            <div className="card-body">
              <h5 className="card-title">📅 Oturum Planlama</h5>
              <p className="card-text">Yeni oturum oluşturabilir ve geçmiş oturumları yönetebilirsiniz.</p>
              <Link to="/tutor/session-booking" className="btn btn-primary">
                Git
              </Link>
            </div>
          </div>
        </div>

        <div className="col-md-6 mb-4">
          <div className="card shadow-sm h-100">
            <div className="card-body">
              <h5 className="card-title">📈 İlerleme Takibi</h5>
              <p className="card-text">Öğrencilerinizin öğrenme süreçlerini analiz edin ve gelişimlerini izleyin.</p>
              <Link to="/tutor/progress-tracking" className="btn btn-primary">
                Git
              </Link>
            </div>
          </div>
        </div>

        <div className="col-md-6 mb-4">
          <div className="card shadow-sm h-100">
            <div className="card-body">
              <h5 className="card-title">📚 Kaynak Kütüphanesi</h5>
              <p className="card-text">Öğrencilerle paylaşmak üzere kaynaklar yükleyin ve yönetin.</p>
              <Link to="/tutor/resource-library" className="btn btn-primary">
                Git
              </Link>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

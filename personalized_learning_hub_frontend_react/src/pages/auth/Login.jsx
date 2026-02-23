import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { loginUser } from '../../services/authService';
import { useAuth } from "../../context/AuthContext";

export default function Login() {
  const [form, setForm] = useState({
    email: "",
    password: "",
    role: "student",
  });


  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  
  const { login } = useAuth();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await loginUser(form);
      console.log("Backend'den dönen kullanıcı bilgisi:", res.data);

      const role = res.data.role.toLowerCase();

      // Genel kullanıcı ID'sini (örneğin veritabanındaki user tablosu ID'si) kaydet
      localStorage.setItem("userId", res.data.id);
      localStorage.setItem("userRole", role);


      // Rol bazlı ekstra ID’leri isteğe bağlı olarak kaydet
      if (role === "student" && res.data.studentId) {
        localStorage.setItem("studentId", res.data.studentId);
      } else {
        localStorage.removeItem("studentId");
      }

      if (role === "tutor" && res.data.tutorId) {
        localStorage.setItem("tutorId", res.data.tutorId);
      } else {
        localStorage.removeItem("tutorId");
      }


      
      login(res.data); // AuthContext'e user'ı kaydet


      // Rol bazlı yönlendirme
      if (role === "student") {
        navigate("/student");
      } else if (role === "tutor") {
        navigate("/tutor");
      } else if (role === "admin") {
        navigate("/admin");
      } else {
        alert("Bilinmeyen rol: " + role);
      }
    } catch (err) {
      alert("Giriş başarısız");
    }
  };

  return (
    <div className="container-fluid vh-100">
      <div className="row h-100">

        {/* Sol taraf: görsel */}
        <div className="col-md-6 d-none d-md-block p-0">
          <img
            src="/image.png"
            alt="Login Illustration"
            style={{
              width: "75%",
              height: "100vh",
              objectFit: "cover",
            }}
          />
        </div>

        {/* Sağ taraf: form */}
        <div className="col-md-4 d-flex align-items-center justify-content-center">
          <div className="w-100" style={{ maxWidth: "400px" }}>
            <div className="text-center mb-4">
              <img
                src="/logo.jpg"
                alt="Logo"
                style={{ width: "40px", height: "40px" }}
              />
              <h3 className="mt-3 fw-bold">Log in</h3>
            </div>

            <form onSubmit={handleSubmit}>
              <div className="mb-3">
                <label className="form-label">Email address</label>
                <div className="input-group">
                  <span className="input-group-text">
                    <i className="bi bi-envelope" />
                  </span>
                  <input
                    type="email"
                    name="email"
                    className="form-control"
                    placeholder="your@mail.com"
                    value={form.email}
                    onChange={handleChange}
                    required
                  />
                </div>
              </div>

              <div className="mb-3">
                <label className="form-label">Password</label>
                <div className="input-group">
                  <span className="input-group-text">
                    <i className="bi bi-lock" />
                  </span>
                  <input
                    type="password"
                    name="password"
                    className="form-control"
                    placeholder="••••••••"
                    value={form.password}
                    onChange={handleChange}
                    required
                  />
                </div>
              </div>

              <div className="mb-4">
                <label className="form-label">Role</label>
                <div className="input-group">
                  <span className="input-group-text">
                    <i className="bi bi-person" />
                  </span>
                  <select
                    name="role"
                    className="form-select"
                    value={form.role}
                    onChange={handleChange}
                  >
                    <option value="student">Student</option>
                    <option value="tutor">Tutor</option>
                    <option value="admin">Admin</option>
                  </select>
                </div>
              </div>

              <button type="submit" className="btn btn-primary w-100 fw-semibold">
                Log in
              </button>

              <p className="text-center mt-3">
                Henüz bir hesabın yok mu? <a href="/register">Kayıt Ol</a>
              </p>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { registerStudent, registerTutor } from "../../services/authService";

export default function Register() {
  const navigate = useNavigate();
  const [form, setForm] = useState({
    fullName: "",
    email: "",
    password: "",
    role: "STUDENT",
  });
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");


  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");
    try {
      if (form.role === "STUDENT") {
        await registerStudent(form);
      } else {
        await registerTutor(form);
      }
      setSuccess("Başarıyla kayıt oldunuz. Giriş ekranına yönlendiriliyorsunuz...");
      setTimeout(() => navigate("/login"), 2500); // 2.5 saniye sonra yönlendir
    } catch (err) {
      setError("Kayıt başarısız oldu.");
    }
  };

  return (
    <div style={{ display: "flex", minHeight: "100vh" }}>
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


      {/* Right Side – Form */}
      <div className="col-md-4 d-flex align-items-center justify-content-center">
        <div style={{ flex: 1, display: "flex", alignItems: "center", justifyContent: "center" }}>
          <form onSubmit={handleSubmit} style={{ width: "80%", maxWidth: "400px" }}>
            <div className="text-center mb-4">
              <img
                src="/logo.jpg"
                alt="Logo"
                style={{ width: "40px", height: "40px" }}
              />
              <h2>Register</h2>
              <p className="text-muted">Create your acoount now</p>
            </div>

            {/* Hata veya Başarı Mesajları */}
            {error && <div className="alert alert-danger">{error}</div>}
            {success && <div className="alert alert-success">{success}</div>}

            <div className="mb-3">
              <label className="form-label">Full Name</label>
              <input type="text" name="fullName" className="form-control" required onChange={handleChange} />
            </div>

            <div className="mb-3">
              <label className="form-label">Email</label>
              <input type="email" name="email" className="form-control" required onChange={handleChange} />
            </div>

            <div className="mb-3">
              <label className="form-label">Password</label>
              <input type="password" name="password" className="form-control" required onChange={handleChange} />
            </div>

            <div className="mb-4">
              <label className="form-label">Role</label>
              <select className="form-select" name="role" onChange={handleChange}>
                <option value="STUDENT">Student</option>
                <option value="TUTOR">Tutor</option>
              </select>
            </div>

            <button type="submit" className="btn btn-primary w-100">
              Register
            </button>

            <p className="text-center mt-3">
              Do you have account? <a href="/login">Log in</a>
            </p>
          </form>
        </div>
      </div>
    </div>
  );
}

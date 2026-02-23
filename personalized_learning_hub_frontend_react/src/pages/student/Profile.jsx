import React, { useEffect, useState } from "react";
import { getStudentProfile, updateStudentInfo } from "../../services/studentService";


import { updateUserCredentials } from "../../services/authService";

const StudentProfile = () => {
  const [activeTab, setActiveTab] = useState("personal");
  const [form, setForm] = useState({
    fullName: "",
    email: "",
    phone: "",
    favoriteTopic: "",
    totalSessionsCompleted: 0,
    totalSessionsScore: 0,
  });

  // const [passwords, setPasswords] = useState({
  //   oldPassword: "",
  //   newPassword: "",
  // });


  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [newEmail, setNewEmail] = useState("");

  
  const [success, setSuccess] = useState("");
  


  const studentId = localStorage.getItem("studentId");
  const userId = localStorage.getItem("userId");

  useEffect(() => {
    if (studentId) {
      getStudentProfile(studentId).then((data) => {
        setForm(data);
      });
    }
  }, [studentId]);

  const handleUpdate = () => {
    updateStudentInfo(studentId, {
      fullName: form.fullName,
      phone: form.phone,
      favoriteTopic: form.favoriteTopic,
    })
      .then(() => alert("Genel bilgiler güncellendi ✅"))
      .catch(() => alert("Güncelleme başarısız ❌"));
  };


  const handleCredentialsUpdate = async () => {
    try {
      await updateUserCredentials(userId, {
        oldPassword,
        newPassword,
        email: newEmail
      });
      setOldPassword("");
      setNewPassword("");
      setSuccess("E-posta ve/veya şifre güncellendi ✅");
    } catch {
      setSuccess("E-posta veya şifre güncellenemedi ❌");
    }
  };

  return (
    <div className="container mt-4">
      <h3>👩‍🎓 Öğrenci Profili</h3>

      <div className="btn-group mb-4">
        <button className={`btn btn-outline-info ${activeTab === "personal" ? "active" : ""}`} onClick={() => setActiveTab("personal")}>
          Kişisel Bilgiler
        </button>
        <button className={`btn btn-outline-info ${activeTab === "general" ? "active" : ""}`} onClick={() => setActiveTab("general")}>
          Genel Bilgiler
        </button>
      </div>

      {activeTab === "personal" && (
        <div>
          <div className="mb-3">
            <label className="form-label">Email</label>
            <input type="email" className="form-control" value={form.email} onChange={(e) => setNewEmail(e.target.value)} />
          </div>

          <div className="mb-3">
            <label className="form-label">Eski Şifre</label>
            <input type="password" className="form-control" value={oldPassword} onChange={(e) => setOldPassword(e.target.value)} />
          </div>

          <div className="mb-3">
            <label className="form-label">Yeni Şifre</label>
            <input type="password" className="form-control" value={newPassword} onChange={(e) => setNewPassword(e.target.value)} />
          </div>

          <button className="btn btn-info" onClick={handleCredentialsUpdate}>Güncelle</button>
        </div>
      )}


      {activeTab === "general" && (
        <>
          <div className="mb-3">
            <label>Ad Soyad</label>
            <input type="text" className="form-control" value={form.fullName} onChange={(e) => setForm({ ...form, fullName: e.target.value })} />
          </div>
          <div className="mb-3">
            <label>Telefon</label>
            <input type="text" className="form-control" value={form.phone} onChange={(e) => setForm({ ...form, phone: e.target.value })} />
          </div>
          <div className="mb-3">
            <label>Favori Konu</label>
            <input type="text" className="form-control" value={form.favoriteTopic} onChange={(e) => setForm({ ...form, favoriteTopic: e.target.value })} />
          </div>

          <div className="mb-3 d-flex gap-3">
            <div className="border rounded p-3 text-center bg-light">
              <div className="small fw-bold text-secondary">📅 Tamamlanan Oturum</div>
              <div className="fs-5">{form.totalSessionsCompleted}</div>
            </div>
            <div className="border rounded p-3 text-center bg-light">
              <div className="small fw-bold text-secondary">🧠 Toplam Puan</div>
              <div className="fs-5">{form.totalSessionsScore}</div>
            </div>
          </div>

          <button className="btn btn-info" onClick={handleUpdate}>Genel Bilgileri Güncelle</button>
        </>
      )}

      {success && <div className="alert alert-info mt-3">{success}</div>}
    </div>
  );
};

export default StudentProfile;

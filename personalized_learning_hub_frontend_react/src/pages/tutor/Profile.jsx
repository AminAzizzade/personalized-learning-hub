import React, { useEffect, useState } from "react";
import {
  getTutorProfile,
  updateTutorProfile,
} from "../../services/tutorService";

import {updateUserCredentials} from "../../services/authService";

import AvailabilityGrid from "../../components/tutor/profile/AvailabilityGrid";
import ExpertiseTopicEditor from "../../components/tutor/profile/ExpertiseTopicEditor";
import LanguageEditor from "../../components/tutor/profile/LanguageEditor";

const TutorProfile = () => {
  const tutorId = localStorage.getItem("tutorId");
  const userId = localStorage.getItem("userId");

  const [profile, setProfile] = useState(null);

  const [form, setForm] = useState({
    name: "",
    expertiseTopics: [],
    preferredStyles: [],
    availability: [],
    language: [],
    pricePerHour: 0
  });
  
  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [newEmail, setNewEmail] = useState("");

  const [success, setSuccess] = useState("");
  const [activeTab, setActiveTab] = useState("personal");

  const [editSection, setEditSection] = useState(null);


  useEffect(() => {
    if (tutorId) {
      getTutorProfile(tutorId).then((res) => {
        if (res) {
          setProfile(res);
          setForm((prev) => ({
            ...prev,
            name: res.name,
            email: res.email,   
            expertiseTopics: res.expertiseTopics || [],
            preferredStyles: res.preferredStyles || [],
            availability: res.availability || [],
            language: res.language || [],
            pricePerHour: res.pricePerHour || 0
          }));
        }
      }).catch((err) => {
        console.error("Profil alınırken hata oluştu:", err);
      });
    }
  }, [tutorId]);


  const updateField = (field, value) => setForm({ ...form, [field]: value });

  const handleCheckbox = (field, value) => {
    const updated = form[field].includes(value)
      ? form[field].filter((v) => v !== value)
      : [...form[field], value];
    updateField(field, updated);
  };



  const handleProfileUpdate = async () => {
    try {
      await updateTutorProfile(tutorId, form);
      setSuccess("Genel bilgiler güncellendi ✅");
    } catch {
      setSuccess("Genel bilgiler güncellenemedi ❌");
    }
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

 const styles = ["Görsel", "İşitsel", "Kinestetik"];

  // if (!profile || !user) return <p>Yükleniyor...</p>;

  return (
    <div className="container mt-4">
      <h3>🧑‍🏫 Eğitmen Profili</h3>
      <div className="btn-group mb-3">
        <button className={`btn btn-outline-info 
          ${activeTab === "personal" && "active"}`} 
          onClick={() => setActiveTab("personal")}
          >Kişisel Bilgiler</button>
        <button className={`btn btn-outline-info ${activeTab === "general" && "active"}`} onClick={() => setActiveTab("general")}>Genel Bilgiler</button>
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
        <div>
          {/* <div className="mb-3">
          <h5 className="mt-4">Ad Soyad</h5>
            <input type="text" className="form-control" value={form.name} onChange={(e) => updateField("name", e.target.value)} />
          </div> */}

          <div className="row align-items-center mb-4">
            {/* Ad Soyad alanı - sol */}
            <div className="col-md-6">
              <h5 className="mt-2">Ad Soyad</h5>
              <input
                type="text"
                className="form-control"
                value={form.name}
                onChange={(e) => updateField("name", e.target.value)}
              />
            </div>

            {/* Sağ: İstatistik kutuları + Saatlik Ücret */}
            <div className="col-md-6 d-flex flex-wrap justify-content-md-end mt-3 mt-md-0 gap-3">
              <div className="border rounded p-3 bg-light text-center shadow-sm" style={{ minWidth: "130px" }}>
                <div className="fw-bold text-secondary small">⭐ Rating</div>
                <div className="fs-5">
                  {profile.rating != null ? Number(profile.rating).toFixed(2) : "-"}
                </div>

              </div>
              <div className="border rounded p-3 bg-light text-center shadow-sm" style={{ minWidth: "130px" }}>
                <div className="fw-bold text-secondary small">📅 Rezervsyon</div>
                <div className="fs-5">{profile.totalMeeting}</div>
              </div>
              <div className="border rounded p-3 bg-light text-center shadow-sm" style={{ minWidth: "130px" }}>
                <div className="fw-bold text-secondary small">👥 Öğrenci</div>
                <div className="fs-5">{profile.totalStudent}</div>
              </div>
              <div className="border rounded p-3 bg-light text-center shadow-sm" style={{ minWidth: "130px" }}>
                <div className="fw-bold text-secondary small">💰 Saatlik Ücret</div>
                <div className="fs-5">
                  <input
                    type="number"
                    className="form-control form-control-sm text-center"
                    style={{ width: "100px", display: "inline-block" }}
                    value={form.pricePerHour}
                    onChange={(e) => updateField("pricePerHour", Number(e.target.value))}
                  />
                </div>
              </div>
            </div>
          </div>


                
          <h5 className="mt-4">Uzmanlık Alanları</h5>
          <div className="mb-3">
            {editSection === "expertise" ? (
              <ExpertiseTopicEditor
                selected={form.expertiseTopics}
                onSave={(updated) => {
                  updateField("expertiseTopics", updated);
                  setEditSection(null); // burada doğru
                }}
                onCancel={() => setEditSection(null)}
              />
            ) : (
            
              <>
                <div className="d-flex align-items-center flex-wrap gap-2 mb-3">
                  {form.expertiseTopics.map((t, i) => (
                    <span
                      key={i}
                      className="badge bg-info rounded-pill px-3 py-2 fs-6"
                    >
                      {t}
                    </span>
                  ))}

                  <button
                    className="btn btn-outline-info btn-sm d-flex align-items-center"
                    onClick={() => setEditSection("expertise")}
                    title="Uzmanlık Alanlarını Düzenle"
                    style={{ lineHeight: 1 }}
                  >
                    <span className="fs-5">＋</span>
                  </button>
                </div>

              </>

            
            )}
          </div>

          <h5 className="mt-4">Konuştuğu Diller</h5>
            {editSection === "language" ? (
              <LanguageEditor
                selected={form.language}
                onSave={(updated) => {
                  updateField("language", updated);
                  setEditSection(null);
                }}
                onCancel={() => setEditSection(null)}
              />
            ) : (
              <>
                <div className="d-flex align-items-center flex-wrap gap-2 mb-3">
                  {form.language.map((l, i) => (
                    <span
                      key={i}
                      className="badge bg-info rounded-pill px-3 py-2 fs-6"
                    >
                      {l}
                    </span>
                  ))}

                  <button
                    className="btn btn-outline-info btn-sm d-flex align-items-center"
                    onClick={() => setEditSection("language")}
                    title="Dilleri Düzenle"
                    style={{ lineHeight: 1 }}
                  >
                    <span className="fs-5">＋</span>
                  </button>
                </div>

              </>
            )}




          <h5 className="mt-4">Öğrenme Stili</h5>
         <div className="d-flex flex-wrap gap-2 mb-3">
          {styles.map((style, i) => {
            const isSelected = form.preferredStyles.includes(style);
            return (
              <button
                key={i}
                type="button"
                className={`btn btn-outline-info ${
                  isSelected ? "btn-info text-white" : "btn-outline-secondary"
                }`}
                onClick={() => handleCheckbox("preferredStyles", style)}
              >
                {style}
              </button>
            );
          })}
        </div>



          
          <div className="mb-3">
            <h5 className="mt-4">Uygunluk Saatleri</h5>
            <AvailabilityGrid
              availability={form.availability}
              setAvailability={(updated) => updateField("availability", updated)}
            />
          </div>

          <button className="btn btn-info text-white" onClick={handleProfileUpdate}>Genel Bilgileri Güncelle</button>
        </div>
      )}

      {success && <div className="alert alert-info mt-3">{success}</div>}
    </div>
  );
};

export default TutorProfile;

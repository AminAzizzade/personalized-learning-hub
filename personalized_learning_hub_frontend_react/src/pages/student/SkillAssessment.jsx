import React, { useState, useEffect } from 'react';
import {
  createSkillAssessment,
  getSkillAssessmentsByStudentId,
  deleteSkillAssessment
} from '../../services/skillAssessmentService';

import 'bootstrap/dist/css/bootstrap.min.css';

const SkillAssessment = () => {
  const [form, setForm] = useState({
    topic: '',
    learningGoal: '',
    preferredStyle: '',
    language: '',
    priceRange: '',
    totalDuration: '',
    startDate: '', // ✅ Eklendi
    availability: [],
    studentId: '',
  });

  const [slot, setSlot] = useState({ day: '', time: '' });
  const [confirmedSlots, setConfirmedSlots] = useState([]);
  const [message, setMessage] = useState('');
  const [matching, setMatching] = useState(false);
  const [matchedMessage, setMatchedMessage] = useState('');
  const [assessments, setAssessments] = useState([]);
  const [showForm, setShowForm] = useState(false);

  useEffect(() => {
    const storedStudentId = localStorage.getItem('studentId');
    if (storedStudentId) {
      setForm((prev) => ({ ...prev, studentId: storedStudentId }));
      fetchAssessments(storedStudentId);
    }
  }, []);

  const fetchAssessments = async (studentId) => {
    try {
      const response = await getSkillAssessmentsByStudentId(studentId);
      setAssessments(Array.isArray(response.data) ? response.data : []);
    } catch (err) {
      console.error('Değerlendirmeler alınamadı', err);
      setAssessments([]); // fallback
    }
  };


  const handleDelete = async (id) => {
    try {
      await deleteSkillAssessment(id);
      fetchAssessments(form.studentId);
    } catch (err) {
      console.error('Silinemedi', err);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const addSlot = () => {
    if (slot.day && slot.time) {
      setConfirmedSlots([...confirmedSlots, `${slot.day},${slot.time}`]);
      setSlot({ day: '', time: '' });
    }
  };

  const removeSlot = (index) => {
    const updated = [...confirmedSlots];
    updated.splice(index, 1);
    setConfirmedSlots(updated);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!form.studentId) {
      setMessage('❌ Giriş yapmış öğrenci bulunamadı.');
      return;
    }

    const submissionData = {
      ...form,
      availability: confirmedSlots,
    };

    try {
      setMatching(true);
      setMessage('');
      setMatchedMessage('');

      await createSkillAssessment(form.studentId, submissionData);

      setTimeout(() => {
        setMatching(false);
        setMatchedMessage('📢 Uygun öğretmen başarıyla atandı!');
        fetchAssessments(form.studentId);
      }, 3000);
    } catch (error) {
      console.error(error);
      setMatching(false);

      if (error.response?.data?.message?.includes("Uygun öğretmen bulunamadı")) {
        setMessage("❌ Uygun öğretmen bulunamadı. Lütfen tercihlerinizi değiştirin.");
      } else {
        setMessage("❌ Bir hata oluştu.");
      }
    }
  };

  const timeOptions = [
    "09:00-10:00", "10:00-11:00", "11:00-12:00", "12:00-13:00",
    "13:00-14:00", "14:00-15:00", "15:00-16:00", "16:00-17:00",
    "17:00-18:00", "18:00-19:00", "19:00-20:00", "20:00-21:00"
  ];

  return (
    <div className="container mt-5">
      <div className="card shadow-sm mb-4">
        <div className="card-body d-flex justify-content-between align-items-center">
          <h3 className="card-title mb-0">🎯 <span className="ms-1">Skill Assessment</span></h3>
          <button className="btn btn-outline-primary" onClick={() => setShowForm(!showForm)}>
            {showForm ? 'Formu Gizle' : 'Yeni Değerlendirme Oluştur'}
          </button>
        </div>
      </div>

      {showForm && (
        <div className="card shadow-sm mb-4">
          <div className="card-body">
            <h4 className="card-title mb-4 text-center">📄 Yeni Skill Assessment Oluştur</h4>
            <form onSubmit={handleSubmit}>
              <div className="mb-3">
                <label className="form-label">Topic</label>
                <select name="topic" value={form.topic} onChange={handleChange} className="form-select" required>
                  <option value="">Seçiniz</option>
                  <option value="Matematik">Matematik</option>
                  <option value="Türkçe">Türkçe</option>
                  <option value="Web Geliştirme">Web Geliştirme</option>
                  <option value="Android">Android</option>
                  <option value="Yapay Zeka">Yapay Zeka</option>
                  <option value="İngilizce">İngilizce</option>
                  <option value="Fizik">Fizik</option>
                  <option value="Biyoloji">Biyoloji</option>
                  <option value="Kimya">Kimya</option>
                  <option value="Algoritma">Algoritma</option>
                </select>
              </div>

              <div className="mb-3">
                <label className="form-label">Learning Goal</label>
                <textarea name="learningGoal" value={form.learningGoal} onChange={handleChange} className="form-control" required />
              </div>

              <div className="mb-3">
                <label className="form-label">Preferred Style</label>
                <select name="preferredStyle" value={form.preferredStyle} onChange={handleChange} className="form-select" required>
                  <option value="">Seçiniz</option>
                  <option value="Görsel">Görsel</option>
                  <option value="İşitsel">İşitsel</option>
                  <option value="Kinestetik">Kinestetik</option>
                </select>
              </div>

              <div className="mb-3">
                <label className="form-label">Anlatım Dili</label>
                <select name="language" value={form.language} onChange={handleChange} className="form-select" required>
                  <option value="">Seçiniz</option>
                  <option value="Türkçe">Türkçe</option>
                  <option value="İngilizce">İngilizce</option>
                  <option value="Almanca">Almanca</option>
                  <option value="Fransızca">Fransızca</option>
                  <option value="Arapça">Arapça</option>
                </select>
              </div>

              <div className="mb-3">
                <label className="form-label">Saatlik Fiyat Aralığı</label>
                <select name="priceRange" value={form.priceRange} onChange={handleChange} className="form-select" required>
                  <option value="">Seçiniz</option>
                  <option value="0-250">250₺ ve altı</option>
                  <option value="250-500">250 - 500₺</option>
                  <option value="500-750">500 - 750₺</option>
                  <option value="750-1000">750 - 1000₺</option>
                </select>
              </div>

              <div className="mb-3">
                <label className="form-label">Eğitim Süresi</label>
                <select name="totalDuration" value={form.totalDuration} onChange={handleChange} className="form-select" required>
                  <option value="">Seçiniz</option>
                  <option value="1 Ay">1 Ay</option>
                  <option value="2 Ay">2 Ay</option>
                  <option value="3 Ay">3 Ay</option>
                  <option value="6 Ay">6 Ay</option>
                  <option value="12 Ay">12 Ay</option>
                </select>
              </div>

              <div className="mb-3">
                <label className="form-label">Başlangıç Tarihi</label>
                <input
                  type="date"
                  name="startDate"
                  value={form.startDate}
                  onChange={handleChange}
                  className="form-control"
                  required
                />
              </div>

              <div className="mb-3">
                <label className="form-label">Haftalık Uygun Gün & Saat</label>
                <div className="d-flex gap-2 mb-2">
                  <select value={slot.day} onChange={(e) => setSlot({ ...slot, day: e.target.value })} className="form-select">
                    <option value="">Gün Seç</option>
                    <option value="Pazartesi">Pazartesi</option>
                    <option value="Salı">Salı</option>
                    <option value="Çarşamba">Çarşamba</option>
                    <option value="Perşembe">Perşembe</option>
                    <option value="Cuma">Cuma</option>
                    <option value="Cumartesi">Cumartesi</option>
                    <option value="Pazar">Pazar</option>
                  </select>
                  <select value={slot.time} onChange={(e) => setSlot({ ...slot, time: e.target.value })} className="form-select">
                    <option value="">Saat Seç</option>
                    {timeOptions.map((time, index) => (
                      <option key={index} value={time}>{time}</option>
                    ))}
                  </select>
                  <button type="button" onClick={addSlot} className="btn btn-outline-primary">Ekle</button>
                </div>
                {confirmedSlots.length > 0 && (
                  <ul className="list-group">
                    {confirmedSlots.map((s, i) => (
                      <li key={i} className="list-group-item d-flex justify-content-between align-items-center">
                        {s}
                        <button type="button" className="btn btn-sm btn-danger" onClick={() => removeSlot(i)}>Sil</button>
                      </li>
                    ))}
                  </ul>
                )}
              </div>

              <div className="d-grid">
                <button type="submit" className="btn btn-primary">Gönder</button>
              </div>
              {matching && <div className="alert alert-warning mt-3">⏳ Uygun öğretmen eşleştiriliyor...</div>}
              {matchedMessage && <div className="alert alert-success mt-3">{matchedMessage}</div>}
              {message && <div className="alert alert-info mt-3">{message}</div>}
            </form>
          </div>
        </div>
      )}

      <div className="card shadow-sm">
        <div className="card-body">
          <h4 className="card-title mb-3">📁 Geçmiş Skill Assessment Kayıtları</h4>
          {Array.isArray(assessments) && assessments.length === 0 ? (
            <p>Henüz kayıt bulunmuyor.</p>
          ) : (
            <ul className="list-group">
              {assessments.map((a, i) => (
                <li key={i} className="list-group-item d-flex justify-content-between align-items-center">
                  <div>
                    <strong>{a.topic}</strong> - {a.learningGoal}<br />
                    {a.totalDuration} süresince {a.language}, {a.priceRange}₺
                  </div>
                  <button className="btn btn-sm btn-danger" onClick={() => handleDelete(a.id)}>Sil</button>
                </li>
              ))}
            </ul>
          )}

        </div>
      </div>
    </div>
  );
};

export default SkillAssessment;

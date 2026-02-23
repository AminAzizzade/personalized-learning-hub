import React, { useEffect, useState } from 'react';
import {
  getSessionsByTutor,
} from '../../../services/sessionService';

import api from '../../../utils/axiosConfig'; // ← api import edilmeli

const initialForm = {
  resourceName: '',
  description: '',
  category: 'PDF',
  isPublic: false,
  teacherFilePath: '',
  type: 'MATERIAL',
  sessionId: '',
  deadLine: '',
  url: ''
};

const ResourceForm = ({ tutorId, selected, onSaved }) => {
  const [formData, setFormData] = useState(initialForm);
  const [sessions, setSessions] = useState([]);
  const [editMode, setEditMode] = useState(false);

  useEffect(() => {
    if (selected) {
      setFormData({
        ...selected,
        sessionId: selected.session?.id || ''
      });
      setEditMode(true);
    } else {
      setFormData(initialForm);
      setEditMode(false);
    }
  }, [selected]);

  useEffect(() => {
    if (tutorId) {
      getSessionsByTutor(tutorId).then(setSessions).catch(console.error);
    }
  }, [tutorId]);

  const [selectedFile, setSelectedFile] = useState(null); // ← dosya state’i

  const handleChange = (e) => {
    const { name, value, type, checked, files } = e.target;

    if (name === 'file' && files[0]) {
      setSelectedFile(files[0]); // ← dosyayı state’e al
    } else {
      const val = type === 'checkbox' ? checked : value;
      setFormData(prev => ({ ...prev, [name]: val }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!selectedFile) {
      alert("Lütfen bir dosya yükleyin.");
      return;
    }

    const multipart = new FormData();
    multipart.append("file", selectedFile);
    multipart.append("dto", new Blob([JSON.stringify({
      ...formData,
      sessionId: parseInt(formData.sessionId)
    })], { type: "application/json" }));

    try {
      await api.post("/resources", multipart, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });
      alert("Kaynak oluşturuldu.");
      setFormData(initialForm);
      setSelectedFile(null);
      onSaved();
    } catch (err) {
      console.error("Kaynak oluşturulamadı:", err);
      alert("Bir hata oluştu.");
    }
  };

  

  return (
    <div className="card mb-4">
      <div className="card-body">
        <h5>{editMode ? "✏️ Kaynağı Güncelle" : "➕ Yeni Kaynak Ekle"}</h5>
        <form onSubmit={handleSubmit}>

          <input
            name="resourceName"
            className="form-control mb-2"
            placeholder="Kaynak Adı"
            value={formData.resourceName}
            onChange={handleChange}
            required
          />

          <textarea
            name="description"
            className="form-control mb-2"
            placeholder="Açıklama"
            value={formData.description}
            onChange={handleChange}
          />

          {/* Dosya yükleme */}
          <label className="form-label">Dosya Yükle (Öğretmen)</label>
          <input
            name="file"
            type="file"
            className="form-control mb-2"
            onChange={handleChange}
          />

          <div className="row">
            <div className="col">
              <label className="form-label">Kategori</label>
              <select
                name="category"
                className="form-select mb-2"
                value={formData.category}
                onChange={handleChange}
              >
                <option value="PDF">PDF</option>
                <option value="Video">Video</option>
                <option value="Link">Link</option>
              </select>
            </div>

            <div className="col">
              <label className="form-label">Tip</label>
              <select
                name="type"
                className="form-select mb-2"
                value={formData.type}
                onChange={handleChange}
              >
                <option value="MATERIAL">Ders Materyali</option>
                <option value="ASSIGNMENT">Ödev</option>
              </select>
            </div>
          </div>

          <label className="form-label">Oturum</label>
          <select
            name="sessionId"
            className="form-select mb-2"
            value={formData.sessionId}
            onChange={handleChange}
            required
          >
            <option value="">Oturum Seçin</option>
            {sessions.map(s => (
              <option key={s.id} value={s.id}>
                {s.title || `Session ${s.id}`}
              </option>
            ))}
          </select>

          <div className="form-check form-switch mb-3">
            <input
              type="checkbox"
              className="form-check-input"
              name="isPublic"
              checked={formData.isPublic}
              onChange={handleChange}
            />
            <label className="form-check-label">Herkese Açık</label>
          </div>

          {formData.type === 'ASSIGNMENT' && (
            <div className="mb-3">
              <label className="form-label">Teslim Tarihi</label>
              <input
                type="date"
                name="deadLine"
                className="form-control"
                value={formData.deadLine?.split('T')[0] || ''}
                onChange={handleChange}
              />
            </div>
          )}

          <button className="btn btn-success w-100">
            {editMode ? "Kaynağı Güncelle" : "Kaynak Ekle"}
          </button>
        </form>
      </div>
    </div>
  );
};

export default ResourceForm;

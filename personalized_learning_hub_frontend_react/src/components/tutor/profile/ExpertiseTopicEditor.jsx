import React, { useState, useEffect } from "react";
import Select from "react-select";

const topics = [
  "Türkçe", "Matematik", "Yapay Zeka", "Web Geliştirme", "Algoritma",
  "Fizik", "Kimya", "Biyoloji", "İngilizce", "Android"
];
const topicOptions = topics.map(topic => ({ label: topic, value: topic }));

const ExpertiseTopicEditor = ({ selected, onSave, onCancel }) => {
  const [localSelection, setLocalSelection] = useState(Array.isArray(selected) ? selected : []);

  useEffect(() => {
    setLocalSelection(Array.isArray(selected) ? selected : []);
  }, [selected]);

  return (
    <div className="card p-4 mt-3">
      <h5>🔧 Uzmanlık Alanlarını Düzenle</h5>

      <div className="mb-3">
        <Select
          isMulti
          options={topicOptions}
          value={topicOptions.filter(opt => localSelection.includes(opt.value))}
          onChange={(selectedOptions) => {
            const selectedValues = selectedOptions.map(opt => opt.value);
            setLocalSelection(selectedValues);
          }}
          placeholder="Uzmanlık alanı ara ve seç..."
        />
      </div>

      {/* Daha canlı, büyük, yuvarlak etiketler */}
      <div className="mb-4">
        {localSelection.map((t, i) => (
          <span
            key={i}
            className="badge bg-primary rounded-pill px-3 py-2 me-2 mb-2 fs-6"
          >
            {t}
          </span>
        ))}
      </div>

      <div className="d-flex gap-2">
        <button
          className="btn btn-primary btn-md"
          onClick={() => onSave(localSelection)}
        >
          Kaydet
        </button>
        <button
          className="btn btn-danger btn-md"
          onClick={onCancel}
        >
          İptal Et
        </button>
      </div>
      
    </div>
  );
};

export default ExpertiseTopicEditor;

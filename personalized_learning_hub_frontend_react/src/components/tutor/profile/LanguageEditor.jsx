import React, { useState, useEffect } from "react";
import Select from "react-select";

const languages = ["Türkçe", "İngilizce", "Almanca", "Fransızca", "Arapça"];
const languageOptions = languages.map(lang => ({ label: lang, value: lang }));

const LanguageEditor = ({ selected, onSave, onCancel }) => {
  const [localSelection, setLocalSelection] = useState(Array.isArray(selected) ? selected : []);

  useEffect(() => {
    setLocalSelection(Array.isArray(selected) ? selected : []);
  }, [selected]);

  return (
    <div className="card p-4 mt-3">
      <h5>🌐 Konuşulan Dilleri Düzenle</h5>

      <div className="mb-3">
        <Select
          isMulti
          options={languageOptions}
          value={languageOptions.filter(opt => localSelection.includes(opt.value))}
          onChange={(selectedOptions) => {
            const selectedValues = selectedOptions.map(opt => opt.value);
            setLocalSelection(selectedValues);
          }}
          placeholder="Dil ara ve seç..."
        />
      </div>

      <div className="mb-4">
        {localSelection.map((lang, i) => (
          <span
            key={i}
            className="badge bg-info rounded-pill px-3 py-2 me-2 mb-2 fs-6"
          >
            {lang}
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

export default LanguageEditor;

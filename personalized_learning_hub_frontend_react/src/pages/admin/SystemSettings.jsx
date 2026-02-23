import { useState } from "react";
import { Form, Button, Alert } from "react-bootstrap";

export default function SystemSettings() {
  const [form, setForm] = useState({
    maxStudentsPerTutor: 5,
    autoAssignTutors: true,
  });

  const [success, setSuccess] = useState(false);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // axios.post('/admin/system-settings', form) gibi gerçek kayıt yapılabilir
    setSuccess(true);
    setTimeout(() => setSuccess(false), 3000);
  };

  return (
    <div className="container">
      <h3 className="mb-4">⚙️ Sistem Ayarları</h3>
      {success && <Alert variant="success">Ayarlar başarıyla kaydedildi.</Alert>}
      <Form onSubmit={handleSubmit}>
        <Form.Group className="mb-3">
          <Form.Label>Tutor başına maksimum öğrenci</Form.Label>
          <Form.Control
            type="number"
            name="maxStudentsPerTutor"
            value={form.maxStudentsPerTutor}
            onChange={handleChange}
          />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Check
            type="checkbox"
            name="autoAssignTutors"
            label="Otomatik tutor atama aktif olsun"
            checked={form.autoAssignTutors}
            onChange={handleChange}
          />
        </Form.Group>

        <Button type="submit" variant="primary">Kaydet</Button>
      </Form>
    </div>
  );
}

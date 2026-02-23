import { useEffect, useState } from "react";
import { getAssignedStudents } from "../../services/tutorService";

export default function AssignedStudents() {
  const [students, setStudents] = useState([]);

  useEffect(() => {
    const fetchStudents = async () => {                 
      const tutorId = localStorage.getItem("tutorId"); // 🔹 Tutor ID'yi otomatik al
      try {
        const data = await getAssignedStudents(tutorId); // 🔹 API isteği gönder
        setStudents(data);
      } catch (err) {
        console.error("Öğrenciler alınamadı", err);
      }
    };

    fetchStudents();
  }, []);

  return (
    <div className="container mt-4">
      <h3 className="mb-3">🎯 Atanan Öğrenciler</h3>
      <p>Size atanan öğrencilerin listesi:</p>

      {students.length === 0 ? (
        <div className="alert alert-warning">Henüz atanan öğrenciniz yok.</div>
      ) : (
        <ul className="list-group mt-4">
          {students.map((student) => (
            <li
              key={student.id}
              className="list-group-item d-flex justify-content-between align-items-center"
            >
              <div>
                <strong>{student.fullName}</strong><br />
              </div>
              <span className="badge bg-primary">{student.learningStyle}</span>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

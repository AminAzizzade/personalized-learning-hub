import { useEffect, useState } from "react";
import { getTutorProgressTracking } from "../../services/sessionService";
import {
  LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer
} from "recharts";

export default function TutorProgressTracking() {
  const [progressData, setProgressData] = useState([]);
  const [selectedStudentId, setSelectedStudentId] = useState(null);
  const [selectedSessionId, setSelectedSessionId] = useState(null);
  const [selectedWeekIndex, setSelectedWeekIndex] = useState(0);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const tutorId = localStorage.getItem("tutorId");
        const data = await getTutorProgressTracking(tutorId);
        const filtered = data.filter(w => new Date(w.startDate) <= new Date());

        const groupedByStudent = groupByStudent(filtered);
        setProgressData(groupedByStudent);

        if (Object.keys(groupedByStudent).length > 0) {
          const firstStudent = Object.keys(groupedByStudent)[0];
          setSelectedStudentId(firstStudent);
          const firstSession = groupedByStudent[firstStudent][0];
          setSelectedSessionId(firstSession.sessionId);
        }
      } catch (err) {
        console.error("İlerleme verisi getirilemedi:", err);
      }
    };
    fetchData();
  }, []);

  const groupByStudent = (data) => {
    const grouped = {};
    data.forEach(wrapper => {
      const weeklyData = groupByWeek(wrapper.progressList, wrapper.startDate);
      if (!grouped[wrapper.studentId]) grouped[wrapper.studentId] = [];
      grouped[wrapper.studentId].push({ ...wrapper, weeklyData });
    });
    return grouped;
  };

  const groupByWeek = (items, startDateRaw) => {
    const start = new Date(startDateRaw);
    const weeks = {};
    items.forEach(({ date, grade, type, name = '', time = '' }) => {
      const d = new Date(date);
      const weekIndex = Math.floor((d - start) / (1000 * 60 * 60 * 24 * 7));
      if (!weeks[weekIndex]) {
        weeks[weekIndex] = { total: 0, assignmentCount: 0, reservationCount: 0, assignments: [], reservations: [] };
      }
      weeks[weekIndex].total += grade;
      if (type === "resource") {
        weeks[weekIndex].assignmentCount++;
        weeks[weekIndex].assignments.push({ name, grade });
      } else if (type === "reservation") {
        weeks[weekIndex].reservationCount++;
        weeks[weekIndex].reservations.push({ time, grade });
      }
    });

    const result = [];
    const max = Math.max(...Object.keys(weeks).map(Number), 0);
    for (let i = 0; i <= max; i++) {
      result.push({ week: `Hafta ${i + 1}`, ...weeks[i] ?? { total: 0, assignmentCount: 0, reservationCount: 0, assignments: [], reservations: [] } });
    }
    return result;
  };

  const studentOptions = Object.entries(progressData);
  const currentSessions = progressData[selectedStudentId] || [];
  const currentSession = currentSessions.find(s => s.sessionId === selectedSessionId);
  const currentWeek = currentSession?.weeklyData[selectedWeekIndex];

  const navigateWeek = (dir) => {
    if (!currentSession) return;
    const max = currentSession.weeklyData.length - 1;
    const next = selectedWeekIndex + (dir === "next" ? 1 : -1);
    if (next >= 0 && next <= max) setSelectedWeekIndex(next);
  };

  return (
    <div className="container mt-4">
      <h3>🎓 Öğrencilerin İlerlemesi</h3>
      {/* <div className="mb-3">
        <label className="form-label">👤 Öğrenci Seç</label>
        <select className="form-select" value={selectedStudentId || ''}
          onChange={e => {
            const id = e.target.value;
            setSelectedStudentId(id);
            const first = progressData[id]?.[0];
            setSelectedSessionId(first?.sessionId);
            setSelectedWeekIndex(0);
          }}>
          {studentOptions.map(([id, sessions]) => (
            <option key={id} value={id}>
              {sessions?.[0]?.studentName
                ? `${sessions[0].studentName} (#${id})`
                : `Öğrenci #${id}`}
            </option>
          ))}
        </select>
      </div> */}

      {currentSessions.length > 0 && (
        <div className="mb-3">
          <label className="form-label">📘 Oturum Seç</label>
          <select className="form-select" value={selectedSessionId || ''}
            onChange={e => {
              setSelectedSessionId(Number(e.target.value));
              setSelectedWeekIndex(0);
            }}>
            {currentSessions.map(w => (
              <option key={w.sessionId} value={w.sessionId}>
                Oturum #{w.sessionId} - {new Date(w.startDate).toLocaleDateString()}
              </option>
            ))}
          </select>
        </div>
      )}

      {currentSession && (
        <>
          <ResponsiveContainer width="100%" height={300}>
            <LineChart
              data={currentSession.weeklyData}
              margin={{ top: 20, right: 30, left: 20, bottom: 30 }}
            >
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="week" />
              <YAxis />
              <Tooltip />
              <Legend verticalAlign="top" />
              <Line
                type="monotone"
                dataKey="total"
                stroke="#28a745"
                strokeWidth={2}
                name="Haftalık Puan"
              />
            </LineChart>
          </ResponsiveContainer>

          <div className="d-flex justify-content-between align-items-center mt-4">
            <button className="btn btn-outline-secondary" onClick={() => navigateWeek("prev")}>◀</button>
            <h6 className="mb-0">📅 {currentWeek?.week}</h6>
            <button className="btn btn-outline-secondary" onClick={() => navigateWeek("next")}>▶</button>
          </div>

          <ul className="mt-3 list-unstyled text-center">
            <li>📝 Ödev Sayısı: {currentWeek?.assignmentCount}</li>
            <li>📌 Rezervasyon Sayısı: {currentWeek?.reservationCount}</li>
            <li>📊 Toplam Puan: <strong>{currentWeek?.total}</strong></li>
          </ul>
        </>
      )}
    </div>
  );
}

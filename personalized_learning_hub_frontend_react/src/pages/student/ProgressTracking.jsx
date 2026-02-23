import { useEffect, useState } from "react";
import { getStudentProgress } from "../../services/sessionService";
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from "recharts";

export default function ProgressTracking() {
  const [wrappers, setWrappers] = useState([]);
  const [selectedSessionId, setSelectedSessionId] = useState(null);
  const [selectedWeekIndex, setSelectedWeekIndex] = useState(0);

  useEffect(() => {
    const fetchProgress = async () => {
      const studentId = localStorage.getItem("studentId");
      try {
        const data = await getStudentProgress(studentId); // expects List<DtoProgressWrapper>
        const filtered = data.filter(w => new Date(w.startDate) <= new Date());
        const processed = filtered.map(wrapper => {
          const weeklyData = groupByWeek(wrapper.progressList, wrapper.startDate);
          return {
            ...wrapper,
            weeklyData,
          };
        });
        setWrappers(processed);
        if (processed.length > 0) {
          setSelectedSessionId(processed[0].sessionId);
          setSelectedWeekIndex(0);
        }
      } catch (err) {
        console.error("İlerleme getirilemedi:", err);
      }
    };

    fetchProgress();
  }, []);

  const groupByWeek = (items, startDateRaw) => {
    const start = new Date(startDateRaw);
    if (isNaN(start.getTime())) {
      console.warn("Geçersiz başlangıç tarihi:", startDateRaw);
      return [];
    }

    const weeks = {};
    items.forEach(({ date, grade, type, name = '', time = '' }) => {
      const d = new Date(date);
      if (isNaN(d.getTime())) return;
      const diffInDays = Math.floor((d - start) / (1000 * 60 * 60 * 24));
      const weekIndex = Math.floor(diffInDays / 7);
      if (!weeks[weekIndex]) {
        weeks[weekIndex] = {
          total: 0,
          assignmentCount: 0,
          reservationCount: 0,
          assignments: [],
          reservations: []
        };
      }
      weeks[weekIndex].total += grade;
      if (type === "resource") {
        weeks[weekIndex].assignmentCount += 1;
        weeks[weekIndex].assignments.push({ name: name || "İsim belirtilmemiş", grade });
      } else if (type === "reservation") {
        weeks[weekIndex].reservationCount += 1;
        weeks[weekIndex].reservations.push({ time, grade });
      }
    });

    const maxWeek = Math.max(...Object.keys(weeks).map(w => parseInt(w)), 0);
    const fullWeeks = [];
    for (let i = 0; i <= maxWeek; i++) {
      fullWeeks.push({
        week: `Hafta ${i + 1}`,
        ...(weeks[i] || {
          total: 0,
          assignmentCount: 0,
          reservationCount: 0,
          assignments: [],
          reservations: []
        })
      });
    }

    return fullWeeks;
  };

  const selectedSession = wrappers.find(w => w.sessionId === selectedSessionId);
  const selectedWeek = selectedWeekIndex !== null && selectedSession?.weeklyData[selectedWeekIndex];

  const navigateWeek = (direction) => {
    if (!selectedSession || !selectedSession.weeklyData) return;
    const maxIndex = selectedSession.weeklyData.length - 1;
    const newIndex = selectedWeekIndex === null
      ? direction === "next" ? 0 : maxIndex
      : selectedWeekIndex + (direction === "next" ? 1 : -1);

    if (newIndex >= 0 && newIndex <= maxIndex) {
      setSelectedWeekIndex(newIndex);
    }
  };

  return (
    <div className="container mt-4">
      <h3 className="mb-2">📊 Kişisel İlerleme</h3>
      <p className="mb-3">Oturum bazlı ilerlemelerin haftalık olarak analiz edilmiştir.</p>

      {wrappers.length === 0 ? (
        <div className="text-muted">Veri yükleniyor...</div>
      ) : (
        <>
          <div className="mb-4">
            <label className="form-label fw-bold">📘 Oturum Seç:</label>
            <select
              className="form-select border-primary"
              value={selectedSessionId || ''}
              onChange={(e) => {
                setSelectedSessionId(Number(e.target.value));
                setSelectedWeekIndex(0);
              }}
            >
              {wrappers.map(w => (
                <option key={w.sessionId} value={w.sessionId}>
                  Oturum #{w.sessionId} - {new Date(w.startDate).toLocaleDateString("tr-TR")}
                </option>
              ))}
            </select>
          </div>

          {selectedSession && (
            <div className="card shadow-sm p-4">
              <h5 className="mb-1">📚 Oturum #{selectedSession.sessionId}</h5>
              <p className="text-muted">
                <strong>Başlangıç Tarihi:</strong> {new Date(selectedSession.startDate).toLocaleDateString("tr-TR")}
              </p>
              <ResponsiveContainer width="100%" height={300}>
                <LineChart
                  data={selectedSession.weeklyData}
                  margin={{ top: 20, right: 30, left: 20, bottom: 30 }}
                  onClick={(e) => {
                    if (e && e.activeTooltipIndex !== undefined) {
                      setSelectedWeekIndex(e.activeTooltipIndex);
                    }
                  }}
                >
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="week" />
                  <YAxis />
                  <Tooltip />
                  <Legend verticalAlign="top" />
                  <Line
                    type="monotone"
                    dataKey="total"
                    stroke="#007bff"
                    strokeWidth={2}
                    name="Haftalık Puan"
                    dot={(props) => {
                      const index = selectedSession.weeklyData.findIndex(w => w.week === props.payload.week);
                      return (
                        <circle
                          cx={props.cx}
                          cy={props.cy}
                          r={index === selectedWeekIndex ? 6 : 4}
                          fill={index === selectedWeekIndex ? "#007bff" : "white"}
                          stroke="#007bff"
                          strokeWidth={2}
                        />
                      );
                    }}
                  />
                </LineChart>
              </ResponsiveContainer>

              {selectedWeek && (
                <div className="mt-4 border-top pt-3">
                  <div className="d-flex justify-content-between align-items-center mb-3">
                    <button className="btn btn-outline-secondary btn-sm" onClick={() => navigateWeek("prev")}>◀</button>
                    <h6 className="mb-0 text-center flex-grow-1">📅 {selectedWeek.week} Özeti:</h6>
                    <button className="btn btn-outline-secondary btn-sm" onClick={() => navigateWeek("next")}>▶</button>
                  </div>
                  <ul className="mb-0 text-center list-unstyled">
                    <li className="mb-1">📝 Ödev Tamamlaması: {selectedWeek.assignmentCount}</li>
                    {(selectedWeek.assignments || []).map((a, idx) => (
                      <li key={`a-${idx}`} className="text-muted small">📄 {a.name} - Puan: {a.grade}</li>
                    ))}
                    <li className="mt-2">📌 Rezervasyon Katılımı: {selectedWeek.reservationCount}</li>
                    {(selectedWeek.reservations || []).map((r, idx) => (
                      <li key={`r-${idx}`} className="text-muted small">🕒 {new Date(r.time).toLocaleString("tr-TR")} - Puan: {r.grade}</li>
                    ))}
                    <li className="mt-2">📊 Toplam Puan: <strong className={selectedWeek.total === 0 ? "text-muted" : selectedWeek.total > 80 ? "text-success" : "text-warning"}>{selectedWeek.total}</strong></li>
                  </ul>
                </div>
              )}
            </div>
          )}
        </>
      )}
    </div>
  );
}

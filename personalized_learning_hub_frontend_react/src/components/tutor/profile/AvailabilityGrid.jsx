import React from "react";
import { Accordion, Table } from "react-bootstrap";

const days = ["Pazartesi", "Salı", "Çarşamba", "Perşembe", "Cuma", "Cumartesi", "Pazar"];
const timeSlots = [
  "08:00-09:00", "09:00-10:00", "10:00-11:00", "11:00-12:00",
  "13:00-14:00", "14:00-15:00", "15:00-16:00", "16:00-17:00",
  "17:00-18:00", "18:00-19:00", "19:00-20:00", "20:00-21:00",
];

const AvailabilityGrid = ({ availability, setAvailability }) => {
  const toggleSlot = (day, time) => {
    const slot = `${day},${time}`;
    setAvailability(
      availability.includes(slot)
        ? availability.filter((s) => s !== slot)
        : [...availability, slot]
    );
  };

  return (
    <div className="mt-4">
      <Accordion defaultActiveKey="0">
        <Accordion.Item eventKey="0">
          <Accordion.Header>📅 Uygunluk Takvimi</Accordion.Header>
          <Accordion.Body>
            <div className="table-responsive">
              <Table bordered hover className="text-center align-middle">

                <thead style={{ backgroundColor: "#fdfdfd", borderBottom: "1px solid #e3e4e6" }}>
                  <tr>
                    <th className="fw-semibold text-secondary" style={{ width: "12.5%" }}>Saat \ Gün</th>
                    {days.map((day) => (
                      <th key={day} className="fw-semibold text-secondary" style={{ width: "12.5%" }}>
                        {day}
                      </th>
                    ))}
                  </tr>
                </thead>


                <tbody>
                  {timeSlots.map((time) => (
                    <tr key={time}>
                      <th style={{
                        backgroundColor: "#fdfdfd",
                        color: "#6c757d", // soft text
                        border: "1px solid #e3e4e6", // soft border
                        width: "12.5%"
                      }}>
                        {time}
                      </th>

                      {days.map((day) => {
                        const slotKey = `${day},${time}`;
                        const isSelected = availability.includes(slotKey);
                        return (

                          <td
                            key={slotKey}
                            onClick={() => toggleSlot(day, time)}
                            style={{
                              width: "12.5%",
                              backgroundColor: isSelected ? "#d1f3f9" : "#ffffff",
                              border: isSelected ? "2px solid #78dce8" : "1px solid #dee2e6",
                              color: isSelected ? "#087990" : "#6c757d",
                              fontWeight: isSelected ? "600" : "normal",
                              cursor: "pointer",
                              userSelect: "none",
                              transition: "all 0.2s ease"
                            }}
                          >
                            {isSelected ? "✓" : ""}
                          </td>


                          // <td
                          //   key={slotKey}
                          //   onClick={() => toggleSlot(day, time)}
                          //   style={{
                          //     width: "12.5%",
                          //     backgroundColor: isSelected ? "rgba(0, 88, 112, 0.25)" : "#ffffff",  // 🟦 koyu lacivertimsi, ama %25 opak
                          //     border: isSelected ? "2px solid rgba(0, 88, 112, 0.4)" : "1px solid #e3e4e6",
                          //     color: isSelected ? "#00424d" : "#6c757d",
                          //     fontWeight: isSelected ? "600" : "normal",
                          //     cursor: "pointer",
                          //     userSelect: "none",
                          //     transition: "all 0.2s ease"
                          //   }}
                          // >
                          //   {isSelected ? "✓" : ""}
                          // </td>




                        );
                      })}
                    </tr>
                  ))}
                </tbody>
              </Table>
            </div>
          </Accordion.Body>
        </Accordion.Item>
      </Accordion>
    </div>
  );
};

export default AvailabilityGrid;

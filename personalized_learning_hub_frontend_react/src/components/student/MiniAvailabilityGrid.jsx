const days = [
  "Pazartesi", "Salı", "Çarşamba", "Perşembe", "Cuma", "Cumartesi", "Pazar"
];
const timeSlots = [
  "08:00-09:00", 
  "09:00-10:00", 
  "10:00-11:00", 
  "11:00-12:00",
  "13:00-14:00", 
  "14:00-15:00", 
  "15:00-16:00", 
  "16:00-17:00",
  "17:00-18:00", 
  "18:00-19:00", 
  "19:00-20:00", 
  "20:00-21:00",
];


const MiniAvailabilityGrid = ({ slots, selected, onSelect }) => {
  return (
    <table className="table table-bordered text-center">

      <thead>
        <tr>
          <th></th>
          {days.map((d) => <th key={d}>{d}</th>)}
        </tr>
      </thead>
      <tbody>
        {timeSlots.map((time) => (
          <tr key={time}>
            <th >{time}</th>
            {days.map((day) => {
              const slotKey = `${day},${time}`;
              const isAvailable = slots.includes(slotKey);
              const isSelected = selected === slotKey;

              return (
                <td
                  key={slotKey}
                  onClick={() => isAvailable && onSelect(slotKey)}
                  style={{
                    backgroundColor: isSelected ? "#0dcaf0" : isAvailable ? "#e3f7fb" : "#f8f9fa",
                    cursor: isAvailable ? "pointer" : "not-allowed",
                    border: isSelected ? "2px solid #0dcaf0" : "1px solid #dee2e6",
                    fontWeight: isSelected ? "bold" : "normal"
                  }}
                >
                 {isAvailable && isSelected && <span style={{ fontSize: '0.6rem' }}>✓</span>}

                </td>
              );
            })}
          </tr>
        ))}
      </tbody>
    </table>
  );
};

export default MiniAvailabilityGrid;
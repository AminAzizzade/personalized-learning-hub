// components/student/SessionCard.js
import React from "react";
import MiniAvailabilityGrid from "./MiniAvailabilityGrid";
const today = new Date(); // her render'da güncellenir

const SessionCard = ({ session, selectedSessionId, availabilitySlots, selectedSlot, onFetchAvailability, onSelectSlot, onCreateReservation, renderStatusBadge }) => (
  <div className="col-md-8 mb-4">
    <div className="card shadow h-100">
      <div className="card-body d-flex flex-column justify-content-between">
        <div className="row align-items-start">
          <div className="col-7">
            <h5 className="text-primary fw-bold">{session.topic}</h5>
            <p className="mb-1"><strong>Süre:</strong> {session.totalDuration}</p>
            <p className="mb-1"><strong>Toplam Oturum:</strong> {session.totalMeetings}</p>
            <p className="mb-1"><strong>Dil:</strong> {session.language}</p>
            <p className="mb-1"><strong>Durum:</strong> {renderStatusBadge(session.status)}</p>
            <p className="mb-1"><strong>Başlangıç:</strong> {new Date(session.startDate).toLocaleDateString()}</p>
            <p className="mb-1"><strong>Bitiş:</strong> {new Date(session.endDate).toLocaleDateString()}</p>
            <p className="mb-1"><strong>Saatlik Ücret:</strong> {session.hourlyRate} ₺</p>
            <p className="mb-1"><strong>Toplam Tutar:</strong> {session.totalPrice} ₺</p>
            <p className="mb-1"><strong>Toplam Puan:</strong> {session.finalScore} p</p>
          </div>
          <div className="col-5 d-flex flex-column justify-content-between">
            <button
              className="btn btn-outline-info mb-2"
              onClick={() => onFetchAvailability(session.id)}
            >
              + Rezervasyon
            </button>
            {selectedSessionId === session.id && availabilitySlots.length > 0 && (
              <div>
                <MiniAvailabilityGrid
                  slots={availabilitySlots}
                  selected={selectedSlot}
                  onSelect={onSelectSlot}
                  sessionStartDate={new Date(session.startDate)}
                  today={today}
                />


                <button
                  className="btn btn-info mt-2 w-100"
                  onClick={onCreateReservation}
                >
                  Gönder
                </button>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  </div>
);

export default SessionCard;
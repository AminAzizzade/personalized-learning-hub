import React from "react";
import { Modal, Button } from "react-bootstrap";

const RatingModal = ({ show, onClose, onSubmit, selectedRating, setSelectedRating }) => {
  return (
    <Modal show={show} onHide={onClose} centered>
      <Modal.Header closeButton>
        <Modal.Title>⭐ Eğitmeni Puanla</Modal.Title>
      </Modal.Header>
      <Modal.Body className="text-center">
        {[1, 2, 3, 4, 5].map((val) => (
          <i
            key={val}
            className={`bi bi-star${val <= selectedRating ? "-fill" : ""} fs-2 mx-1 cursor-pointer text-warning`}
            onClick={() => setSelectedRating(val)}
            style={{ cursor: "pointer" }}
          ></i>
        ))}
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onClose}>
          Vazgeç
        </Button>
        <Button variant="primary" onClick={() => onSubmit(selectedRating)} disabled={selectedRating === 0}>
          Gönder
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default RatingModal;

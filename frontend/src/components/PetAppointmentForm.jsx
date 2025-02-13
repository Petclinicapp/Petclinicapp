// PetAppointmentForm.js
import React, { useState, useEffect } from "react";

const PetAppointmentForm = ({ petList, onPetChange, onReasonChange }) => {
  const [selectedPet, setSelectedPet] = useState("");
  const [reason, setReason] = useState("");

  const handlePetChange = (e) => {
    setSelectedPet(e.target.value);
    onPetChange(e.target.value);
  };

  const handleReasonChange = (e) => {
    setReason(e.target.value);
    onReasonChange(e.target.value);
  };

  return (
    <div className="mt-4">
      <label className="block mb-2 text-sm font-semibold text-gray-700">
        Pet
      </label>
      <select
        className="px-4 py-2 border rounded w-full"
        value={selectedPet}
        onChange={handlePetChange}
      >
        <option value="" disabled>
          Select a Pet
        </option>
        {petList.map((pet) => (
          <option key={pet.id} value={pet.id}>
            {pet.name}
          </option>
        ))}
      </select>

      <label className="block mt-4 mb-2 text-sm font-semibold text-gray-700">
        Reason
      </label>
      <textarea
        className="px-4 py-2 border rounded w-full"
        value={reason}
        onChange={handleReasonChange}
        placeholder="Enter the reason for the appointment"
      />
    </div>
  );
};

export default PetAppointmentForm;

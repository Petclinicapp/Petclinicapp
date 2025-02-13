import React, { useState } from "react";
import Calendar from "react-calendar";
import "react-calendar/dist/Calendar.css";
import "../app.css"; // Ensure the styles are loaded
import { useAuth } from "../context/UserContext";
import { availableAppointments } from "../data";
import { addVisit } from "../services/postService";

const AppointmentBooking = () => {
  const [appointment, setAppointment] = useState({
    date: new Date(),
    selectedTime: null,
    selectedPet: "",
    reason: "",
  });

  const { pets } = useAuth();

  const formatDate = (date) => {
    const yyyy = date.getFullYear();
    const mm = String(date.getMonth() + 1).padStart(2, "0");
    const dd = String(date.getDate()).padStart(2, "0");
    return `${yyyy}-${mm}-${dd}`;
  };

  const formattedDate = formatDate(appointment.date);

  const getAvailableDates = () => {
    const allSlots = availableAppointments.flatMap(
      (appt) => appt.availableSlots
    );
    return new Set(
      allSlots.filter((slot) => !slot.booked).map((slot) => slot.availableDate)
    );
  };

  const availableDates = getAvailableDates();

  const getAvailableTimes = (dateStr) => {
    const doctorAppointments = availableAppointments.find((appt) =>
      appt.availableSlots.some((slot) => slot.availableDate === dateStr)
    );

    return doctorAppointments
      ? doctorAppointments.availableSlots
          .filter((slot) => slot.availableDate === dateStr && !slot.booked)
          .map((slot) => slot.availableTime)
      : [];
  };

  const availableTimes = getAvailableTimes(formattedDate);

  const handleDateChange = (newDate) => {
    setAppointment((prev) => ({ ...prev, date: newDate, selectedTime: null }));
  };

  const handleTimeSelect = (time) => {
    setAppointment((prev) => ({ ...prev, selectedTime: time }));
  };

  const handlePetChange = (e) => {
    setAppointment((prev) => ({ ...prev, selectedPet: e.target.value }));
  };

  const handleReasonChange = (e) => {
    setAppointment((prev) => ({ ...prev, reason: e.target.value }));
  };

  const handleConfirmAppointment = async () => {
    if (
      !appointment.selectedPet ||
      !appointment.reason.trim() ||
      !appointment.selectedTime
    ) {
      alert(
        "Please select a pet, time, and provide a reason before confirming."
      );
      return;
    }

    const formattedDate = formatDate(appointment.date);
    const visitDateTime = `${formattedDate}T${appointment.selectedTime}`;

    // Log the data before sending to the backend
    const appointmentDetails = {
      petId: appointment.selectedPet,
      visitDateTime,
      reason: appointment.reason,
    };

    console.log(
      "Appointment details before sending to the backend:",
      appointmentDetails
    );

    try {
      const response = await addVisit(appointmentDetails);

      if (response) {
        alert("Appointment successfully booked!");
        // Reset the form after successful submission
        setAppointment({
          date: new Date(),
          selectedTime: null,
          selectedPet: "",
          reason: "",
        });
      }
    } catch (error) {
      alert(`Error: ${error.message}`);
    }
  };

  return (
    <div className="max-w-4xl mx-auto p-6 bg-white shadow-lg rounded-lg">
      <h1 className="text-xl font-semibold text-center mb-4">
        Book an Appointment
      </h1>

      <div className="flex flex-col md:flex-row gap-6">
        {/* Left Side - Calendar and Time Selection */}
        <div className="md:w-1/2">
          <Calendar
            onChange={handleDateChange}
            value={appointment.date}
            locale="en-US"
            minDate={new Date()}
            tileClassName={({ date }) => {
              const dateStr = formatDate(date);
              if (availableDates.has(dateStr)) {
                return "available-day";
              }
              return null;
            }}
          />
          <div className="mt-4">
            <h2 className="text-lg font-semibold text-gray-700">
              Available Times for {formattedDate}
            </h2>
            {availableTimes.length > 0 ? (
              <div className="flex flex-wrap gap-2 mt-2">
                {availableTimes.map((time) => (
                  <button
                    key={time}
                    className={`px-4 py-2 rounded transition ${
                      appointment.selectedTime === time
                        ? "bg-green-600 text-white"
                        : "bg-blue-500 text-white hover:bg-blue-600"
                    }`}
                    onClick={() => handleTimeSelect(time)}
                  >
                    {time}
                  </button>
                ))}
              </div>
            ) : (
              <p className="text-gray-500 mt-2">
                No available times for this date.
              </p>
            )}
          </div>
        </div>

        {/* Right Side - Appointment Details */}
        <div className="md:w-1/2 p-4 bg-green-100 border border-green-400 rounded">
          <h2 className="text-lg font-semibold text-green-800">
            Your Appointment
          </h2>
          <p className="text-gray-700">📅 Date: {formattedDate}</p>
          <p className="text-gray-700">
            ⏰ Time: {appointment.selectedTime || "Not selected"}
          </p>

          <label className="block mt-4 mb-2 text-sm font-semibold text-gray-700">
            Select Your Pet
          </label>
          <select
            className="px-4 py-2 border rounded w-full"
            value={appointment.selectedPet}
            onChange={handlePetChange}
          >
            <option value="" disabled>
              Select a Pet
            </option>
            {pets.map((pet) => (
              <option key={pet.petId} value={pet.petId} className="capitalize">
                {pet.petName}
              </option>
            ))}
          </select>

          <label className="block mt-4 mb-2 text-sm font-semibold text-gray-700">
            Reason for Appointment
          </label>
          <textarea
            className="px-4 py-2 border rounded w-full"
            rows="3"
            value={appointment.reason}
            onChange={handleReasonChange}
            placeholder="Describe the reason for the visit..."
          ></textarea>

          <button
            className="mt-3 px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600 transition"
            onClick={handleConfirmAppointment}
          >
            Confirm Appointment
          </button>
        </div>
      </div>
    </div>
  );
};

export default AppointmentBooking;

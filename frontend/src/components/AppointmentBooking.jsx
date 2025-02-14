import React, { useState, useEffect } from "react";
import Calendar from "react-calendar";
import "react-calendar/dist/Calendar.css";
import "../app.css"; // Ensure the styles are loaded
import { useAuth } from "../context/UserContext";
import { addVisit } from "../services/postService";
import { getAllAvailableTimes } from "../services/getService";
import { toast } from "react-toastify";

const AppointmentBooking = () => {
  const [appointment, setAppointment] = useState({
    date: new Date(),
    selectedTime: null,
    selectedPet: "",
    reason: "",
  });
  const [availableAppointments, setAvailableAppointments] = useState({});
  const { pets } = useAuth();

  const formatDate = (date) => {
    const yyyy = date.getFullYear();
    const mm = String(date.getMonth() + 1).padStart(2, "0");
    const dd = String(date.getDate()).padStart(2, "0");
    return `${yyyy}-${mm}-${dd}`;
  };

  const formatTime = (timeString) => {
    if (!timeString) return timeString;
    return timeString.slice(0, 5);
  };

  useEffect(() => {
    const fetchTimes = async () => {
      try {
        const doctorId = "6772c09e-dc34-418f-965d-65b83467a9fc";
        const data = await getAllAvailableTimes(doctorId);

        // Group appointments by date
        const groupedAppointments = data.reduce((acc, appt) => {
          if (!acc[appt.availableDate]) {
            acc[appt.availableDate] = [];
          }
          acc[appt.availableDate].push({
            time: appt.availableTime,
            booked: appt.booked,
          });
          return acc;
        }, {});

        setAvailableAppointments(groupedAppointments);
      } catch (error) {
        console.error("Error fetching appointments:", error.message);
      }
    };

    fetchTimes();
  }, [availableAppointments]);

  const formattedDate = formatDate(appointment.date);

  const availableDates = new Set(Object.keys(availableAppointments));

  const availableTimes = availableAppointments[formattedDate]
    ? availableAppointments[formattedDate]
        .filter((slot) => !slot.booked)
        .map((slot) => slot.time)
    : [];

  const handleDateChange = (newDate) => {
    setAppointment((prev) => ({ ...prev, date: newDate, selectedTime: null }));
  };

  const isTimeAvailable = (time) => {
    const now = new Date();
    const selectedDate = new Date(appointment.date);
    const [hours, minutes] = time.split(":").map(Number);

    selectedDate.setHours(hours);
    selectedDate.setMinutes(minutes);
    selectedDate.setSeconds(0);
    selectedDate.setMilliseconds(0);

    return selectedDate > now;
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

    const visitDateTime = `${formattedDate}T${formatTime(
      appointment.selectedTime
    )}`;

    const doctorId = "6772c09e-dc34-418f-965d-65b83467a9fc";

    const appointmentDetails = {
      petId: appointment.selectedPet,
      visitDateTime,
      reason: appointment.reason,
      doctorId,
    };

    try {
      const response = await addVisit(appointmentDetails);
      if (response) {
        toast.success("Appointment successfully booked!");
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
      <h1 className="text-2xl font-semibold text-center mb-4">
        Book an Appointment
      </h1>

      <div className="flex flex-col md:flex-row gap-6">
        <div className="md:w-1/2">
          <Calendar
            onChange={handleDateChange}
            value={appointment.date}
            locale="en-US"
            minDate={new Date()}
            tileClassName={({ date }) => {
              return availableDates.has(formatDate(date))
                ? "available-day"
                : "";
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
                        : isTimeAvailable(time)
                        ? "bg-[#8ECAE6] text-white hover:bg-blue-600"
                        : "bg-gray-100 text-gray-400 cursor-not-allowed"
                    }`}
                    onClick={() => handleTimeSelect(time)}
                    disabled={!isTimeAvailable(time)}
                  >
                    {formatTime(time)}
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

        <div className="md:w-1/2 p-4 bg-green-100 border border-green-400 rounded">
          <h2 className="text-lg font-semibold text-green-800">
            Your Appointment
          </h2>
          <p className="text-gray-700 font-bold">📅 Date: {formattedDate}</p>
          <p className="text-gray-700 font-bold">
            ⏰ Time: {formatTime(appointment.selectedTime) || "Not selected"}
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

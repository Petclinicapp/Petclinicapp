import React, { useState, useEffect } from "react";
import Calendar from "react-calendar";
import "react-calendar/dist/Calendar.css";
import "../app.css"; // Ensure the styles are loaded

const AppointmentBooking = () => {
  const [date, setDate] = useState(new Date());
  const [selectedTime, setSelectedTime] = useState(null);

  // Function to generate available times for the next two months
  const generateAvailableTimes = () => {
    const times = [
      "9:00 AM",
      "10:00 AM",
      "11:00 AM",
      "12:00 PM",
      "1:00 PM",
      "2:00 PM",
      "3:00 PM",
      "4:00 PM",
      "5:00 PM",
      "6:00 PM",
      "7:00 PM",
    ];
    const availableTimes = {};
    const currentDate = new Date();
    const twoMonthsLater = new Date();
    twoMonthsLater.setMonth(currentDate.getMonth() + 2); // Set the date for two months later

    // Loop through each day in the next two months
    while (currentDate <= twoMonthsLater) {
      const dateString = formatDate(currentDate);
      availableTimes[dateString] = times;
      currentDate.setDate(currentDate.getDate() + 1); // Move to the next day
    }

    return availableTimes;
  };

  // Function to format date to 'YYYY-MM-DD'
  const formatDate = (date) => {
    const yyyy = date.getFullYear();
    const mm = String(date.getMonth() + 1).padStart(2, "0");
    const dd = String(date.getDate()).padStart(2, "0");
    return `${yyyy}-${mm}-${dd}`;
  };

  const [availableTimes, setAvailableTimes] = useState(
    generateAvailableTimes()
  );

  // Check if a date has available times
  const isDateAvailable = (date) => {
    const dateStr = formatDate(date);
    return availableTimes[dateStr] && availableTimes[dateStr].length > 0;
  };

  const handleDateChange = (newDate) => {
    setDate(newDate);
    setSelectedTime(null);
  };

  const handleTimeSelect = (time) => {
    setSelectedTime(time);
  };

  const handleConfirmAppointment = () => {
    const dateStr = formatDate(date); // Get the selected date string
    setAvailableTimes((prevTimes) => {
      const newTimes = { ...prevTimes };

      // Remove the selected time from the availableTimes for this date
      if (newTimes[dateStr]) {
        newTimes[dateStr] = newTimes[dateStr].filter(
          (time) => time !== selectedTime
        );
      }

      return newTimes;
    });

    setSelectedTime(null); // Reset selected time
    alert("Appointment booked!");
  };

  const formattedDate = formatDate(date);

  return (
    <div className="max-w-md mx-auto p-6 bg-white shadow-lg rounded-lg">
      <h1 className="text-xl font-semibold text-center mb-4">
        Book an Appointment
      </h1>
      <Calendar
        onChange={handleDateChange}
        value={date}
        locale="en-US"
        minDate={new Date()} // Disable past dates
        tileClassName={({ date }) =>
          isDateAvailable(date) ? "available-day" : ""
        }
      />
      <div className="mt-4">
        <h2 className="text-lg font-semibold text-gray-700">
          Available Times for {formattedDate}
        </h2>
        {availableTimes[formattedDate] ? (
          <div className="flex flex-wrap gap-2 mt-2">
            {availableTimes[formattedDate].map((time) => (
              <button
                key={time}
                className="px-4 py-2 bg-[#219EBC] text-white rounded hover:bg-blue-600 transition"
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
      {selectedTime && (
        <div className="mt-6 p-4 bg-green-100 border border-green-400 rounded">
          <h2 className="text-lg font-semibold text-green-800">
            Your Appointment
          </h2>
          <p className="text-gray-700">📅 Date: {formattedDate}</p>
          <p className="text-gray-700">⏰ Time: {selectedTime}</p>
          <button
            className="mt-3 px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600 transition"
            onClick={handleConfirmAppointment}
          >
            Confirm Appointment
          </button>
        </div>
      )}
    </div>
  );
};

export default AppointmentBooking;

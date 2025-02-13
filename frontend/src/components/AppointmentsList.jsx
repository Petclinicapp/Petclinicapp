import { useAuth } from "../context/UserContext";

function AppointmentsList() {
  const { visits, pets } = useAuth(); // Get visits and pets from context

  return (
    <div className="w-1/2 m-auto">
      <h2 className="font-semibold text-white py-6 text-4xl">
        Your Appointments
      </h2>
      <ul className="mb-4">
        {visits.length === 0 ? (
          <p>No appointments found.</p>
        ) : (
          visits.map((visit) => {
            // Find the pet by petId
            const pet = pets.find((p) => p.petId === visit.petId);
            return (
              <li
                key={visit.visitId}
                className="mb-4 p-4 border rounded shadow bg-white"
              >
                <p className="capitalize">
                  <strong>Pet:</strong> {pet ? pet.petName : "Unknown"}
                </p>
                <p>
                  <strong>Visit Date:</strong>{" "}
                  {new Date(visit.visitDateTime).toLocaleString()}
                </p>
                <p>
                  <strong>Reason:</strong> {visit.reason}
                </p>
                <p>
                  <strong>Status:</strong>{" "}
                  <span
                    className={`px-2 py-1 rounded-md text-white ${
                      visit.status === "PENDING"
                        ? "bg-[#FFB703]"
                        : visit.status === "CONFIRMED"
                        ? "bg-green-500"
                        : visit.status === "CANCELLED"
                        ? "bg-[#FB8500]"
                        : "bg-gray-500"
                    }`}
                  >
                    {visit.status}
                  </span>
                </p>
              </li>
            );
          })
        )}
      </ul>
    </div>
  );
}

export default AppointmentsList;

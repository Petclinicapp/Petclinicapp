import { useAuth } from "../context/UserContext";
import { IoPawOutline } from "react-icons/io5";

function AppointmentsList() {
  const { visits, pets } = useAuth();

  return (
    <div className="w-1/2 m-auto">
      <h2 className="font-semibold text-white py-6 text-4xl">
        Your Appointments
      </h2>
      <ul className="mb-10 grid grid-cols-2 gap-10">
        {visits.length === 0 ? (
          <p>No appointments found.</p>
        ) : (
          visits.map((visit) => {
            const pet = pets.find((p) => p.petId === visit.petId);
            return (
              <li
                key={visit.visitId}
                className="bg-white rounded-xl border-2 border-[#FB8500] shadow-lg"
              >
                <div className="flex justify-end">
                  <p
                    className={`w-1/2 p-2 text-center border-l-2 border-b-2 border-[#FB8500] font-bold ${
                      visit.status === "CONFIRMED"
                        ? "bg-[#023047] text-green-600 rounded-bl-3xl rounded-tr-lg"
                        : visit.status === "PENDING"
                        ? "bg-[#023047] text-yellow-600 rounded-bl-3xl rounded-tr-lg"
                        : "bg-[#023047] text-red-600 rounded-bl-3xl rounded-tr-lg"
                    }`}
                  >
                    {visit.status}
                  </p>
                </div>
                <div className="flex items-center">
                  <div className="text-[#FB8500] p-6 m-6 rounded-full border-2 border-[#FB8500] bg-[#023047]">
                    <IoPawOutline size={40} />
                  </div>
                  <div>
                    <p className="font-bold">
                      Visit Date:{" "}
                      {new Date(visit.visitDateTime).toLocaleString()}
                    </p>

                    <>
                      <p className=" capitalize text-gray-400">
                        Pet name: {pet ? pet.petName : "Unknown"}
                      </p>
                      <p className=" capitalize text-gray-400">
                        Pet Type: {pet ? pet.species : "Unknown"}
                      </p>
                    </>
                  </div>
                </div>
                <div className="px-4">
                  <h4 className="text-[#023047] border-b-1 border-gray-300 mb-2 font-bold">
                    Reason for a visit
                  </h4>
                  <p className="mb-4"> {visit.reason}</p>
                </div>
              </li>
            );
          })
        )}
      </ul>
    </div>
  );
}

export default AppointmentsList;

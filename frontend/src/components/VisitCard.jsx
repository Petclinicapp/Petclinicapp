import { IoPawOutline } from "react-icons/io5";

function VisitCard({
  visit,
  petDetails,
  user,
  statusUpdating,
  handleStatusChange,
}) {
  const formattedDate = new Date(visit.visitDateTime).toLocaleString("en-GB", {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
    hour12: false,
  });

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
          <p className="font-bold">Visit Date: {formattedDate}</p>
          {petDetails[visit.petId] ? (
            petDetails[visit.petId] !== null ? (
              <>
                <p className=" capitalize text-gray-400">
                  Pet name: {petDetails[visit.petId].petName}
                </p>
                <p className=" capitalize text-gray-400">
                  Pet Type: {petDetails[visit.petId].species}
                </p>
              </>
            ) : (
              <p>Pet no longer exists in the system.</p> // Show a message if pet doesn't exist
            )
          ) : (
            <p>Loading pet details...</p>
          )}
        </div>
      </div>
      <div className="px-4">
        <h4 className="text-[#023047] border-b-1 border-gray-300 mb-2 font-bold">
          Reason for a visit
        </h4>
        <p className="mb-4"> {visit.reason}</p>
      </div>

      {user.role === "ROLE_DOCTOR" &&
        visit.status !== "CONFIRMED" &&
        visit.status !== "CANCELLED" && (
          <div className="my-4 flex justify-center gap-6">
            <button
              onClick={() => handleStatusChange(visit.visitId, "CONFIRMED")}
              disabled={statusUpdating}
              className="border-[1px] border-[#023047] text-[#023047] uppercase cursor-pointer px-4 py-2 rounded-lg transition-all duration-200 ease-in-out transform hover:scale-105 focus:outline-none"
            >
              Confirm
            </button>
            <button
              onClick={() => handleStatusChange(visit.visitId, "CANCELLED")}
              disabled={statusUpdating}
              className="border-[1px] border-[#023047] text-[#023047] uppercase cursor-pointer px-4 py-2 rounded-lg transition-all duration-200 ease-in-out transform hover:scale-105 focus:outline-none"
            >
              Cancel
            </button>
          </div>
        )}
    </li>
  );
}
export default VisitCard;

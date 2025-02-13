import { useEffect, useState } from "react";
import { getAllVisits, getPetById } from "../services/getService";
import { useAuth } from "../context/UserContext";
import { updateVisitStatus } from "../services/putService";
import VisitCard from "./VisitCard";
import { toast } from "react-toastify";

const VisitsPage = () => {
  const { user } = useAuth();
  const [visits, setVisits] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [petDetails, setPetDetails] = useState({});
  const [statusUpdating, setStatusUpdating] = useState(false);
  const [sortedVisits, setSortedVisits] = useState([]);

  useEffect(() => {
    setSortedVisits(visits);
  }, [visits]);

  useEffect(() => {
    const fetchVisits = async () => {
      if (user.role !== "ROLE_DOCTOR") {
        setError("You do not have permission to view these visits.");
        setLoading(false);
        return;
      }

      try {
        const visitsData = await getAllVisits(user.role);
        setVisits(visitsData);

        const petDetailsMap = {};
        for (const visit of visitsData) {
          try {
            const petData = await getPetById(visit.petId);
            petDetailsMap[visit.petId] = petData;
          } catch (error) {
            petDetailsMap[visit.petId] = null;
          }
        }
        setPetDetails(petDetailsMap);
      } catch (error) {
        setError(error.message);
      } finally {
        setLoading(false);
      }
    };

    fetchVisits();
  }, [user]);

  const handleStatusChange = async (visitId, newStatus) => {
    setStatusUpdating(true);
    try {
      await updateVisitStatus(visitId, { status: newStatus }, user.role);
      // Update the status in the state after successful update
      setVisits((prevVisits) =>
        prevVisits.map((visit) =>
          visit.visitId === visitId ? { ...visit, status: newStatus } : visit
        )
      );
      toast.success("Status updated successfully!");
    } catch (error) {
      setError("Error updating status: " + error.message);
    } finally {
      setStatusUpdating(false);
    }
  };

  const handleSort = (status) => {
    if (status === "ALL") {
      setSortedVisits(visits); // Reset to all visits
    } else {
      const filteredVisits = visits.filter((visit) => visit.status === status);
      setSortedVisits(filteredVisits); // Update the state with filtered visits
    }
  };

  if (loading) {
    return (
      <p className="text-2xl flex justify-center pt-48 text-white">
        Loading visits...
      </p>
    );
  }

  if (error) {
    return <p>Error: {error}</p>;
  }

  return (
    <div className="mx-10">
      <h1 className="text-white text-5xl pt-32 text-center">
        All Visits ({visits.length})
      </h1>

      <div className="flex justify-center gap-8 my-10 p-6">
        <button
          onClick={() => handleSort("ALL")}
          className="flex flex-col border-2 border-[#FB8500] text-white bg-[#023047] cursor-pointer items-center  p-4 rounded-lg shadow-md transition-transform duration-200 ease-in-out hover:scale-105"
        >
          <h3 className="text-lg">All Visits</h3>
        </button>
        <button
          onClick={() => handleSort("PENDING")}
          className="flex flex-col border-2 border-[#FB8500] text-white bg-[#023047] cursor-pointer items-center  p-4 rounded-lg shadow-md transition-transform duration-200 ease-in-out hover:scale-105"
        >
          <h3 className="text-lg">
            Pending:{" "}
            {visits.filter((visit) => visit.status === "PENDING").length}
          </h3>
        </button>
        <button
          onClick={() => handleSort("CONFIRMED")}
          className="flex flex-col border-2 border-[#FB8500] text-white bg-[#023047] cursor-pointer items-center  p-4 rounded-lg shadow-md transition-transform duration-200 ease-in-out hover:scale-105"
        >
          <h3 className="text-lg">
            Confirmed:{" "}
            {visits.filter((visit) => visit.status === "CONFIRMED").length}
          </h3>
        </button>
        <button
          onClick={() => handleSort("CANCELLED")}
          className="flex flex-col border-2 border-[#FB8500] text-white bg-[#023047] cursor-pointer items-center  p-4 rounded-lg shadow-md transition-transform duration-200 ease-in-out hover:scale-105"
        >
          <h3 className="text-lg">
            Canceled:{" "}
            {visits.filter((visit) => visit.status === "CANCELLED").length}
          </h3>
        </button>
      </div>

      <ul className="grid grid-cols-4 gap-10">
        {sortedVisits.length > 0 ? (
          sortedVisits.map((visit) => (
            <VisitCard
              key={visit.visitId}
              visit={visit}
              petDetails={petDetails}
              user={user}
              statusUpdating={statusUpdating}
              handleStatusChange={handleStatusChange}
            />
          ))
        ) : (
          <p>No visits available.</p>
        )}
      </ul>
    </div>
  );
};

export default VisitsPage;

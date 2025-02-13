import { useEffect, useState } from "react";
import { getAllVisits, getPetById } from "../services/getService";

const VisitsPage = () => {
  const [visits, setVisits] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [petDetails, setPetDetails] = useState({});

  useEffect(() => {
    const fetchVisits = async () => {
      try {
        const visitsData = await getAllVisits();
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
  }, []);

  if (loading) {
    return <p>Loading visits...</p>;
  }

  if (error) {
    return <p>Error: {error}</p>;
  }

  return (
    <div>
      <h1>All Visits</h1>
      <h2 className="text-white text-2xl mb-10">Visits: {visits.length}</h2>
      <h3 className="text-white text-xl">
        Pending: {visits.filter((visit) => visit.status === "pending").length}
      </h3>
      <ul>
        {visits.length > 0 ? (
          visits.map((visit) => (
            <li key={visit.visitId} className="bg-white mb-10">
              <p>Visit ID: {visit.visitId}</p>
              <p>Visit Date: {visit.visitDateTime}</p>
              <p>Status: {visit.status}</p>
              <p>Reason: {visit.reason}</p>

              <div>
                {petDetails[visit.petId] ? (
                  petDetails[visit.petId] !== null ? (
                    <>
                      <p>Pet Name: {petDetails[visit.petId].petName}</p>
                      <p>Pet Type: {petDetails[visit.petId].species}</p>
                      <p>Pet Age: {petDetails[visit.petId].age}</p>
                    </>
                  ) : (
                    <p>Pet no longer exists in the system.</p> // Show a message if pet doesn't exist
                  )
                ) : (
                  <p>Loading pet details...</p>
                )}
              </div>
            </li>
          ))
        ) : (
          <p>No visits available.</p>
        )}
      </ul>
    </div>
  );
};

export default VisitsPage;

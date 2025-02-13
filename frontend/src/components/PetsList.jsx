import PetCard from "./PetCard";
import { useAuth } from "../context/UserContext";

function PetsList() {
  const { pets, user } = useAuth();

  if (!user) return <p>Loading user...</p>;
  if (!pets) return <p>Loading pets...</p>;

  return (
    <>
      {user.role === "ROLE_USER" && (
        <>
          <h1 className="text-white text-5xl font-bold mb-14">Your Pets</h1>
          {pets.length > 0 ? (
            <div className="grid grid-cols-3 gap-10 w-full lg:w-2/3 mb-10">
              {pets.map((pet) => (
                <PetCard key={pet.petId} pet={pet} />
              ))}
            </div>
          ) : (
            <h2 className="text-white text-xl mb-14">
              You have no pets to display.
            </h2>
          )}
        </>
      )}
    </>
  );
}
export default PetsList;

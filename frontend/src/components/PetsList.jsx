import { pets } from "../data";
import PetCard from "./PetCard";

function PetsList() {
  return (
    <div className="grid grid-cols-3 gap-10 w-full lg:w-2/3 mb-20">
      {pets.map((pet) => (
        <PetCard key={pet.petId} pet={pet} />
      ))}
    </div>
  );
}
export default PetsList;

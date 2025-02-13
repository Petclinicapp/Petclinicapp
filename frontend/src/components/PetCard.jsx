import { IoPawOutline } from "react-icons/io5";
import { FaEdit } from "react-icons/fa";
import { FaTrashAlt } from "react-icons/fa";
import { useState } from "react";
import { updatePet } from "../services/putService";
import PetForm from "./CreatePetForm";
import { useAuth } from "../context/UserContext";
import DeletePetModal from "./DeletePetModal";

function PetCard({ pet }) {
  const [isFormOpen, setIsFormOpen] = useState(false);
  const { fetchPets, handleDeletePet } = useAuth();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [petToDelete, setPetToDelete] = useState(null);

  const toggleForm = () => {
    setIsFormOpen(!isFormOpen);
  };

  const closeModal = () => {
    setIsFormOpen(false);
  };

  const openModal = (pet) => {
    setPetToDelete(pet);
    setIsModalOpen(true);
  };

  const closeDeleteModal = () => {
    setPetToDelete(null);
    setIsModalOpen(false);
  };

  const confirmDelete = async () => {
    if (petToDelete) {
      await handleDeletePet(petToDelete.petId);
      closeDeleteModal(); // Close the modal after deletion
      fetchPets();
    }
  };

  const handleUpdate = async (updatedData) => {
    if (!pet.petId || !updatedData) {
      console.error("Missing petId or updated data", {
        petId: pet.petId,
        updatedData,
      });
      return;
    }

    // Exclude petId from updatedData
    const { petId, ...dataWithoutPetId } = updatedData;

    // Convert age and weight to numbers before sending to the backend
    const updatedPetData = {
      ...dataWithoutPetId,
      age: Number(dataWithoutPetId.age), // Convert age to a number
      weight: Number(dataWithoutPetId.weight), // Convert weight to a number
    };

    try {
      await updatePet(pet.petId, updatedPetData);
      fetchPets();
      closeModal();
    } catch (error) {
      console.error("Error updating pet:", error.message);
    }
  };

  return (
    <div className="bg-white rounded-lg shadow-lg">
      <div className="flex justify-end p-4 text-lg gap-2 text-[#023047]">
        <button
          onClick={toggleForm}
          className="hover:text-[#FB8500] cursor-pointer transition-all duration-300 ease-in-out"
        >
          <FaEdit />
        </button>
        <button
          onClick={() => openModal(pet)}
          className="hover:text-[#FB8500] cursor-pointer transition-all duration-300 ease-in-out"
        >
          <FaTrashAlt />
        </button>
      </div>
      <div className="flex px-4 pb-10 gap-10">
        <div className="flex items-center">
          <div className="text-[#FB8500] p-6 rounded-full border-2 border-[#FB8500]">
            <IoPawOutline size={40} />
          </div>
        </div>
        <div className="text-[#023047] ">
          <h2 className="text-2xl font-bold mb-4 capitalize">{pet.petName}</h2>
          <div>
            <h3 className="uppercase">
              <span className="font-bold">Type:</span> {pet.species}
            </h3>
            <h3 className="uppercase">
              <span className="font-bold">Breed:</span> {pet.breed}
            </h3>
            <h3 className="uppercase">
              <span className="font-bold">Gender:</span>{" "}
              {pet.gender === "MALE" ? "Male" : "Female"}
            </h3>
            <h3 className="uppercase">
              <span className="font-bold">Age:</span> {pet.age}
            </h3>
            <h3 className="uppercase">
              <span className="font-bold">Weight:</span> {pet.weight} kg
            </h3>
          </div>
        </div>
      </div>

      <button className="bg-[#FB8500] text-white font-bold rounded-b-lg border-t-2 w-full py-4 cursor-pointer hover:text-[#FB8500] hover:bg-white hover:border-t-2 border-[#FB8500] transition-all duration-300 ease-in-out">
        Make an Appointment
      </button>

      {isFormOpen && (
        <PetForm
          existingPet={pet}
          handleCancel={closeModal}
          handleSave={handleUpdate}
        />
      )}

      {/* Delete Modal */}
      <DeletePetModal
        isOpen={isModalOpen}
        onClose={closeDeleteModal}
        onConfirm={confirmDelete}
        petName={petToDelete?.petName}
      />
    </div>
  );
}

export default PetCard;

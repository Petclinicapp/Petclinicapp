import { useAuth } from "../context/UserContext";
import { FaEdit } from "react-icons/fa";
import { BiUser } from "react-icons/bi";
import { useState } from "react";
import CreatePetForm from "./CreatePetForm";
import { addPet } from "../services/postService";
import { Link } from "react-router-dom";

function UserCard() {
  const { user, pets, fetchPets } = useAuth();
  const [isFormOpen, setIsFormOpen] = useState(false);

  const toggleForm = () => {
    setIsFormOpen(!isFormOpen);
  };

  const handleSave = async (petData) => {
    try {
      await addPet(petData);
      setIsFormOpen(false);
      fetchPets();
    } catch (error) {
      console.error("Error adding pet:", error.message);
    }
  };

  const handleCancel = () => {
    setIsFormOpen(false);
  };

  if (!user) {
    return <h1>Loading...</h1>;
  }

  return (
    <section className="flex flex-col md:flex-row shadow-lg rounded-lg w-full mt-32 mb-14 mx-10 lg:w-2/3">
      <div className="bg-[#219EBC] rounded-t-lg md:rounded-none md:rounded-l-lg flex flex-col items-center px-10 py-20 gap-6 w-full md:w-1/3">
        <div className="bg-[#023047] p-10 rounded-full text-white border-2 border-[#FB8500]">
          <BiUser size={120} />
        </div>
        <div className="flex items-center gap-2">
          <h3 className="font-bold text-lg text-white">
            {user.firstname} {user.lastname}
          </h3>
          {/* <FaEdit
            size={20}
            className="cursor-pointer text-white hover:text-[#FB8500] transition-all duration-300 ease-in-out"
          /> */}
        </div>
        <button
          onClick={toggleForm}
          className="cursor-pointer bg-[#FB8500] text-white font-bold px-6 py-2 rounded-lg shadow-lg hover:text-[#FB8500] hover:bg-white transition-all duration-300 ease-in-out"
        >
          Add Pet
        </button>
        <Link
          to={`/visits/${user.id}`}
          className="cursor-pointer bg-[#FB8500] text-white font-bold px-6 py-2 rounded-lg shadow-lg hover:text-[#FB8500] hover:bg-white transition-all duration-300 ease-in-out"
        >
          Appointments
        </Link>
      </div>
      <div className="bg-white rounded-b-lg md:rounded-none md:rounded-r-lg p-10 w-full md:w-2/3">
        <h4 className="text-[#023047] border-b-1 border-gray-300 mb-4 font-bold">
          Information
        </h4>
        <div className="flex gap-10">
          <div>
            <h3 className="font-bold text-[#023047] text-lg">Username</h3>
            <h4 className="text-gray-400">{user.username}</h4>
          </div>
          <div>
            <h3 className="font-bold text-[#023047] text-lg">Email</h3>
            <h4 className="text-gray-400">{user.email}</h4>
          </div>
        </div>

        <h4 className="text-[#023047] border-b-1 border-gray-300 mt-6 mb-4 font-bold">
          Pets
        </h4>
        <ul className="list-disc ml-5">
          {pets && pets.length > 0 ? (
            pets.map((pet) => (
              <li className="marker:text-[#FB8500] capitalize" key={pet.petId}>
                {pet.petName}
              </li>
            ))
          ) : (
            <li className="text-gray-400 -ml-5 list-none">
              <p>You have no pets.</p>
            </li>
          )}
        </ul>
      </div>

      {/* Modal (Create Pet Form) */}
      {isFormOpen && (
        <CreatePetForm handleCancel={handleCancel} handleSave={handleSave} />
      )}
    </section>
  );
}

export default UserCard;

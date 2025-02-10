import { IoPawOutline } from "react-icons/io5";
import { FaEdit } from "react-icons/fa";
import { FaTrashAlt } from "react-icons/fa";
import { FaCat } from "react-icons/fa";

function PetCard({ pet }) {
  return (
    <div className="bg-white rounded-lg shadow-lg">
      <div className="flex justify-end p-4 text-lg gap-2 text-[#023047]">
        <button className="hover:text-[#FB8500] cursor-pointer transition-all duration-300 ease-in-out">
          <FaEdit />
        </button>
        <button className="hover:text-[#FB8500] cursor-pointer transition-all duration-300 ease-in-out">
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
          <h2 className="text-2xl font-bold mb-4">{pet.petName}</h2>
          <div>
            <div>
              <h3 className="uppercase">
                <span className="font-bold">Type:</span> {pet.species}
              </h3>
              <h3 className="uppercase">
                <span className="font-bold">Breed:</span> {pet.breed}
              </h3>
              <h3 className="uppercase">
                <span className="font-bold">Gender:</span>{" "}
                {pet.gender ? "Male" : "Female"}
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
      </div>

      <button className="bg-[#FB8500] text-white font-bold rounded-b-lg border-t-2 w-full py-4 cursor-pointer hover:text-[#FB8500] hover:bg-white hover:border-t-2 border-[#FB8500] transition-all duration-300 ease-in-out">
        Make an Appointment
      </button>
    </div>
  );
}
export default PetCard;

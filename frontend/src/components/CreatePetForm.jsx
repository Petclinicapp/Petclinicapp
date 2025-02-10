import { useState } from "react";
import { FaTimes } from "react-icons/fa";
import Input from "./Input";

function CreatePetForm({ handleCancel, handleSave }) {
  const [petInfo, setPetInfo] = useState({
    petName: "",
    species: "",
    breed: "",
    gender: true, // Default to male
    age: "",
    weight: "",
  });

  const [errors, setErrors] = useState({});

  // Handle input changes with trimming
  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setPetInfo((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value.trim(), // Trim on input change
    }));
    setErrors((prevErrors) => ({ ...prevErrors, [name]: "" }));
  };

  // Form validation function
  const validateForm = () => {
    let formErrors = {};
    let isValid = true;

    // Check if required fields are empty or invalid
    if (!petInfo.petName || petInfo.petName.length < 2) {
      formErrors.petName = "Pet Name must be at least 2 characters long";
      isValid = false;
    }
    if (!petInfo.species || petInfo.species.length < 3) {
      formErrors.species = "Species is required and at least 2 characters long";
      isValid = false;
    }
    if (!petInfo.breed || petInfo.breed.length) {
      formErrors.breed = "Breed is required and at least 2 characters long";
      isValid = false;
    }
    if (!petInfo.age || petInfo.age <= 0) {
      formErrors.age = "Age must be a positive number";
      isValid = false;
    }
    if (!petInfo.weight || petInfo.weight <= 0) {
      formErrors.weight = "Weight must be a positive number";
      isValid = false;
    }

    setErrors(formErrors);
    return isValid;
  };

  // Handle form submission
  const handleFormSubmit = (e) => {
    e.preventDefault();

    if (validateForm()) {
      handleSave(petInfo); // Pass the petInfo state to handleSave
    }
  };

  return (
    <div className="fixed inset-0 flex items-center justify-center bg-black/50 bg-opacity-50 z-20">
      <div className="bg-white p-6 rounded-lg shadow-lg w-full mx-5 sm:w-2/3 lg:w-1/3">
        <div className="flex justify-between items-center text-[#023047] mb-4">
          <h2 className="text-3xl font-bold">Create a Pet</h2>
          <button
            className="hover:text-[#FB8500] transition-all duration-300 cursor-pointer font-bold"
            onClick={handleCancel}
          >
            <FaTimes size={20} />
          </button>
        </div>
        <form onSubmit={handleFormSubmit} className="flex flex-col gap-4">
          <Input
            label="Pet Name"
            type="text"
            name="petName"
            value={petInfo.petName}
            onChange={handleChange}
          />
          {errors.petName && (
            <p className="text-red-800 text-sm uppercase -mt-3">
              {errors.petName}
            </p>
          )}
          <Input
            label="Species"
            type="text"
            name="species"
            value={petInfo.species}
            onChange={handleChange}
          />
          {errors.species && (
            <p className="text-red-800 text-sm uppercase -mt-3">
              {errors.species}
            </p>
          )}
          <Input
            label="Breed"
            type="text"
            name="breed"
            value={petInfo.breed}
            onChange={handleChange}
          />
          {errors.breed && (
            <p className="text-red-800 text-sm uppercase -mt-3">
              {errors.breed}
            </p>
          )}
          <div className="mb-4 ">
            <label className="mb-1 text-gray-500">Gender</label>
            <div className="flex gap-4 bg-[#eeeee9] w-48  p-2 rounded-lg">
              <label
                className={`flex items-center w-16 justify-center py-2 rounded-lg text-center cursor-pointer transition-all duration-300 ${
                  petInfo.gender === true
                    ? "bg-[#FB8500] text-white font-bold "
                    : "bg-white text-[#023047]"
                }`}
              >
                <input
                  type="radio"
                  name="gender"
                  value="male"
                  checked={petInfo.gender === true}
                  onChange={() => setPetInfo({ ...petInfo, gender: true })}
                  className="hidden" // Hide the default radio button
                />
                <span>Male</span>
              </label>
              <label
                className={`flex items-center py-2 w-24 justify-center rounded-lg cursor-pointer transition-all duration-300 ${
                  petInfo.gender === false
                    ? "bg-[#FB8500] text-white font-bold"
                    : "bg-white text-[#023047]"
                }`}
              >
                <input
                  type="radio"
                  name="gender"
                  value="female"
                  checked={petInfo.gender === false}
                  onChange={() => setPetInfo({ ...petInfo, gender: false })}
                  className="hidden" // Hide the default radio button
                />
                <span>Female</span>
              </label>
            </div>
          </div>
          <div className="flex gap-10">
            <div>
              <Input
                label="Age"
                type="number"
                name="age"
                value={petInfo.age}
                onChange={handleChange}
              />
              {errors.age && (
                <p className="text-red-800 text-sm uppercase">{errors.age}</p>
              )}
            </div>
            <div>
              <Input
                label="Weight"
                type="number"
                name="weight"
                value={petInfo.weight}
                onChange={handleChange}
                error={errors.weight}
              />
              {errors.weight && (
                <p className="text-red-800 text-sm uppercase">
                  {errors.weight}
                </p>
              )}
            </div>
          </div>
          <div className="flex justify-end gap-4">
            <button
              type="button"
              onClick={handleCancel}
              className="bg-gray-300 text-white py-2 px-4 rounded-lg hover:bg-gray-400 transition-all duration-300 cursor-pointer font-bold"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="bg-[#FB8500] text-white py-2 px-4 rounded-lg hover:bg-[#023047] transition-all duration-300 cursor-pointer font-bold"
            >
              Save
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default CreatePetForm;

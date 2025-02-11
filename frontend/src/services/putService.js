import axios from "axios";

const API_URL = import.meta.env.VITE_API_URL;

export const updatePet = async (petId, data) => {
  try {
    if (!API_URL) {
      throw new Error(
        "API_URL is not defined. Check your environment variables."
      );
    }

    if (!petId || !data) {
      throw new Error("Missing required parameters: petId and data.");
    }

    const token = localStorage.getItem("token");

    if (!token) {
      throw new Error("User is not authenticated.");
    }

    const response = await axios.patch(
      `${API_URL}/api/v1/pets/${petId}`,
      data,
      {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      }
    );

    return response.data;
  } catch (error) {
    throw new Error(
      `Failed to update pet: ${error.response?.data?.message || error.message}`
    );
  }
};

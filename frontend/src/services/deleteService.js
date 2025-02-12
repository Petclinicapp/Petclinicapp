import axios from "axios";

const API_URL = import.meta.env.VITE_API_URL;

export const deletePet = async (petId) => {
  try {
    const token = localStorage.getItem("token");
    const response = await axios.delete(`${API_URL}/api/v1/pets/${petId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error deleting pet:", error);
    throw new Error(error.response?.data?.message || "Failed to delete pet");
  }
};

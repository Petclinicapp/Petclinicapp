import axios from "axios";

const API_URL = import.meta.env.VITE_API_URL;
const token = localStorage.getItem("token");

export const getPetsByUserId = async (userId) => {
  try {
    const resp = await axios.get(`${API_URL}/api/v1/pets/user/${userId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return resp.data;
  } catch (error) {
    throw new Error(`Error fetching user data: ${error.message}`);
  }
};

export const getVisitsByUserId = async (userId) => {
  try {
    const resp = await axios.get(`${API_URL}/api/v1/visits/user/${userId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return resp.data;
  } catch (error) {
    throw new Error(`Error fetching user data: ${error.message}`);
  }
};

export const getAllVisits = async (userRole) => {
  if (userRole !== "ROLE_DOCTOR") {
    throw new Error("You do not have permission to view this data.");
  }

  try {
    const resp = await axios.get(`${API_URL}/api/v1/visits/all`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return resp.data;
  } catch (error) {
    throw new Error(`Error fetching visits: ${error.message}`);
  }
};

export const getPetById = async (petId) => {
  try {
    const response = await axios.get(`${API_URL}/api/v1/pets/${petId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    throw new Error(`Error fetching pet data: ${error.message}`);
  }
};

export const getAllAvailableTimes = async (doctorId) => {
  try {
    const response = await axios.get(`${API_URL}/api/v1/availability/all`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
      params: {
        doctorId: doctorId, // Add the required parameter
      },
    });

    return response.data;
  } catch (error) {
    console.error("Error Status:", error.response?.status);
    console.error("Error Data:", error.response?.data);
    throw new Error(`Error fetching available times: ${error.message}`);
  }
};

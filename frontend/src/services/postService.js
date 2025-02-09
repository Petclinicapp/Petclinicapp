import axios from "axios";
const API_URL = import.meta.env.VITE_API_URL;

const token = localStorage.getItem("token");

export const postLogin = async (data) => {
  try {
    const response = await axios.post(`${API_URL}/api/v1/auth/login`, data, {
      headers: { "Content-Type": "application/json" },
    });
    return response.data;
  } catch (error) {
    throw new Error(
      `Failed to login: ${error.response?.data?.message || error.message}`
    );
  }
};

export const postRegister = async (data) => {
  try {
    const response = await axios.post(`${API_URL}/api/v1/auth/register`, {
      ...data,
    });
    return response.data;
  } catch (error) {
    throw new Error(`Failed to register: ${error.message}`);
  }
};

export const logout = async (token) => {
  try {
    await axios.post(`${API_URL}/api/v1/auth/logout`, null, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    localStorage.removeItem("token");
  } catch (error) {
    console.error("Logout error:", error);
    toast.error("Logout failed. Please try again.");
  }
};

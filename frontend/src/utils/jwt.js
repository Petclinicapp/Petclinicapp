import { jwtDecode } from "jwt-decode";

export const getUserIdFromToken = (token) => {
  try {
    return jwtDecode(token).id;
  } catch (error) {
    console.error("Invalid token", error);
    return null;
  }
};

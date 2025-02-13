import { createContext, useState, useEffect, useContext } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { jwtDecode } from "jwt-decode";
import { logout, postLogin, postRegister } from "../services/postService";
import { getPetsByUserId, getVisitsByUserId } from "../services/getService";
import { deletePet } from "../services/deleteService";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(localStorage.getItem("token"));
  const [pets, setPets] = useState([]);
  const [visits, setVisits] = useState([]);
  const isLoggedIn = !!token;
  const navigate = useNavigate();

  useEffect(() => {
    if (token) {
      try {
        const { exp, id } = jwtDecode(token);
        const currentTime = Date.now() / 1000;

        if (exp < currentTime) {
          toast.error("Your session has expired. Please log in again.");
          logoutHandler();
        } else {
          setUser({ id }); // Store user ID
          const timeout = setTimeout(logoutHandler, (exp - currentTime) * 1000);
          return () => clearTimeout(timeout);
        }
      } catch (error) {
        console.error("Invalid token:", error);
        logoutHandler();
      }
    }
  }, [token]);

  // Fetch user data when the token changes
  useEffect(() => {
    if (token) {
      try {
        const userData = jwtDecode(token);
        setUser(userData); // Set the user based on the decoded token
      } catch (error) {
        console.error("Invalid token", error);
        setUser(null);
      }
    } else {
      setUser(null); // Clear user if no token
    }
  }, [token]); // Runs when token changes

  useEffect(() => {
    if (!user || !user.id) return; // Ensure user exists before fetching

    const fetchPets = async () => {
      try {
        const data = await getPetsByUserId(user.id);
        setPets(data);
      } catch (err) {
        setError(err.message);
      }
    };

    fetchPets();
  }, [user]); // Fetch pets whenever user changes

  useEffect(() => {
    if (!user || !user.id) return; // Ensure user exists before fetching

    const fetchVisits = async () => {
      try {
        const visitData = await getVisitsByUserId(user.id); // Fetch visits from the backend
        setVisits(visitData); // Set visits in state
      } catch (err) {
        console.error("Error fetching visits:", err.message);
      }
    };

    fetchVisits();
  }, [user, visits]); // Fetch visits whenever user changes

  const login = async (userData) => {
    console.log("Logging in with:", userData);
    try {
      const { token } = await postLogin(userData);
      localStorage.setItem("token", token);
      setToken(token); // Set token in state

      toast.success("Logged in successfully");
      navigate("/");
      // window.location.replace("/");
    } catch (error) {
      console.error("Login error:", error.message);
      toast.error(error.message);
    }
  };

  const register = async (userData) => {
    try {
      const response = await postRegister({ ...userData, role: "ROLE_USER" });
      setUser(response.user);
      toast.success("Registration successful");
      navigate("/signin");
    } catch (error) {
      toast.error(error.message);
    }
  };

  const logoutHandler = () => {
    const token = localStorage.getItem("token");

    logout(token);

    setToken(null);
    setUser(null);

    toast.success("Logged out successfully");

    navigate("/");
  };

  const fetchPets = async () => {
    if (!user || !user.id) return;

    try {
      const data = await getPetsByUserId(user.id);
      setPets(data);
    } catch (err) {
      console.error("Error fetching pets:", err.message);
    }
  };

  const handleDeletePet = async (petId) => {
    try {
      await deletePet(petId); // Call the API to delete the pet
      setPets((prevPets) => prevPets.filter((pet) => pet.petId !== petId)); // Remove from the local pets list
      toast.success("Pet deleted successfully");
    } catch (error) {
      toast.error("Failed to delete pet");
    }
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        login,
        register,
        logoutHandler,
        isLoggedIn,
        pets,
        fetchPets,
        handleDeletePet,
        visits,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);

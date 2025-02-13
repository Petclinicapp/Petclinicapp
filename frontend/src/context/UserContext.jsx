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

  useEffect(() => {
    if (token) {
      try {
        const userData = jwtDecode(token);
        setUser(userData);
      } catch (error) {
        console.error("Invalid token", error);
        setUser(null);
      }
    } else {
      setUser(null);
    }
  }, [token]);

  useEffect(() => {
    if (!user || !user.id) return;

    const fetchPets = async () => {
      try {
        const data = await getPetsByUserId(user.id);
        setPets(data);
      } catch (err) {
        console.log(err.message);
      }
    };

    fetchPets();
  }, [user]);

  useEffect(() => {
    if (!user || !user.id) return;

    const fetchVisits = async () => {
      try {
        const visitData = await getVisitsByUserId(user.id);
        setVisits(visitData);
      } catch (err) {
        console.error("Error fetching visits:", err.message);
      }
    };

    fetchVisits();
  }, [user, visits]);

  const login = async (userData) => {
    try {
      const { token } = await postLogin(userData);
      localStorage.setItem("token", token);
      setToken(token);

      toast.success("Logged in successfully");
      navigate("/");
    } catch (error) {
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
      await deletePet(petId);
      setPets((prevPets) => prevPets.filter((pet) => pet.petId !== petId));
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

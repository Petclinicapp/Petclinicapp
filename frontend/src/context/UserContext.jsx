import { createContext, useState, useEffect, useContext } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { jwtDecode } from "jwt-decode";
import { postLogin, postRegister } from "../services/postService";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(localStorage.getItem("token"));
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

  const login = async (userData) => {
    console.log("Logging in with:", userData);
    try {
      const { token } = await postLogin(userData);
      localStorage.setItem("token", token);
      setToken(token);
      setUser(jwtDecode(token));
      toast.success("Logged in successfully");

      console.log("User data:", userData);
      console.log("Token:", token);

      navigate("/");
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
    // localStorage.removeItem("token");
    // setToken(null);
    // setUser(null);
    // toast.success("Logged out successfully");
    // navigate("/");
  };

  return (
    <AuthContext.Provider value={{ user, login, register, logoutHandler }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);

import { Navigate, Route, Routes } from "react-router-dom";
import "./App.css";
import HomePage from "./pages/HomePage";
import SignInPage from "./pages/SignInPage";
import SignUpPage from "./pages/SignUpPage";
import { ToastContainer } from "react-toastify";
import ProfilePage from "./pages/ProfilePage";
import { useAuth } from "./context/UserContext";
import VisitPage from "./pages/VisitPage";
import DoctorsVisitPage from "./pages/DoctorsVisitPage";

function App() {
  const { isLoggedIn, user } = useAuth();

  if (!isLoggedIn || !user) return <p>Loading...</p>;

  const ProtectedRoute = ({ element, allowedRoles }) => {
    if (!isLoggedIn) return <Navigate to="/signin" />;
    if (!allowedRoles.includes(user.role)) return <Navigate to="/" />; // Redirect if not authorized

    return element;
  };

  return (
    <>
      <ToastContainer autoClose={2000} position="top-right" />
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route
          path="/signin"
          element={isLoggedIn ? <Navigate to="/" /> : <SignInPage />}
        />
        <Route
          path="/register"
          element={isLoggedIn ? <Navigate to="/" /> : <SignUpPage />}
        />
        <Route path="/profile/:id" element={<ProfilePage />} />
        <Route
          path="/visits/:id"
          element={
            <ProtectedRoute
              element={<VisitPage />}
              allowedRoles={["ROLE_USER"]}
            />
          }
        />
        <Route
          path="/visits/all-visits"
          element={
            <ProtectedRoute
              element={<DoctorsVisitPage />}
              allowedRoles={["ROLE_DOCTOR"]}
            />
          }
        />
      </Routes>
    </>
  );
}

export default App;

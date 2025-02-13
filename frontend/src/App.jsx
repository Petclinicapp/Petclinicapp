import { Navigate, Route, Routes } from "react-router-dom";
import "./App.css";
import HomePage from "./pages/HomePage";
import SignInPage from "./pages/SignInPage";
import SignUpPage from "./pages/SignUpPage";
import { ToastContainer } from "react-toastify";
import ProfilePage from "./pages/ProfilePage";
import { useAuth } from "./context/UserContext";
import VisitPage from "./pages/VisitPage";

function App() {
  const { isLoggedIn } = useAuth();
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
        <Route path="/visits/:id" element={<VisitPage />} />
      </Routes>
    </>
  );
}

export default App;

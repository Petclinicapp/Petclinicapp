import { Route, Routes } from "react-router-dom";
import "./App.css";
import HomePage from "./pages/HomePage";
import SignInPage from "./pages/SignInPage";
import SignUpPage from "./pages/SignUpPage";
import { ToastContainer } from "react-toastify";
import ProfilePage from "./pages/ProfilePage";

function App() {
  return (
    <>
      <ToastContainer autoClose={2000} position="top-right" />
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/signin" element={<SignInPage />} />
        <Route path="/register" element={<SignUpPage />} />
        <Route path="/profile/:id" element={<ProfilePage />} />
      </Routes>
    </>
  );
}

export default App;

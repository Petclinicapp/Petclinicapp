import { Link } from "react-router-dom";
import { useState } from "react";
import Input from "../components/Input";
import Navbar from "../components/Navbar";
import Footer from "../components/Footer";

function AuthPage({ title, buttonText, onSubmit, fields, linkText, linkPath }) {
  // 🔹 Initialize form state
  const initialState = fields.reduce((acc, field) => {
    acc[field.name] = "";
    return acc;
  }, {});
  const [userData, setUserData] = useState(initialState);
  const [errors, setErrors] = useState({});

  // Handle input change
  const handleForm = (e) => {
    const { name, value } = e.target;
    setUserData((prevUser) => ({ ...prevUser, [name]: value }));
    setErrors((prevErrors) => ({ ...prevErrors, [name]: "" })); // Clear error when typing
  };

  // Validation logic
  const validate = () => {
    let newErrors = {};
    fields.forEach(({ name, type }) => {
      if (!userData[name].trim()) {
        newErrors[name] = `${name} is required`;
      } else if (type === "password" && userData[name].length < 6) {
        newErrors[name] = "Password must be at least 6 characters";
      } else if (type === "email" && !/^\S+@\S+\.\S+$/.test(userData[name])) {
        newErrors[name] = "Invalid email format";
      }
    });

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  // Handle form submission
  const handleSubmit = (e) => {
    e.preventDefault();
    if (!validate()) return; // Stop if validation fails

    onSubmit(userData); // Call auth function
    setUserData(initialState); // Reset form
  };

  return (
    <>
      <Navbar />
      <div className="h-screen flex bg-gradient-to-r from-slate-300 to-[#1B8FBE]">
        <section className="w-full md:w-2/3 flex justify-center items-center">
          <div className="w-2/3 flex flex-col gap-8">
            <h1 className="text-5xl font-bold text-[#1B8FBE]">{title}</h1>
            <form className="flex flex-col gap-4" onSubmit={handleSubmit}>
              {fields.map((field) => (
                <div key={field.name}>
                  <Input
                    onChange={handleForm}
                    value={userData[field.name]}
                    type={field.type}
                    name={field.name}
                    label={field.label}
                  />
                  {errors[field.name] && (
                    <p className="text-red-800 text-sm uppercase">
                      {errors[field.name]}
                    </p>
                  )}
                </div>
              ))}

              <button
                type="submit"
                className="relative mt-6 cursor-pointer text-lg rounded flex h-[50px] w-40 items-center justify-center overflow-hidden bg-[#1B8FBE] text-white shadow-2xl transition-all hover:bg-[#016891]"
              >
                <span className="relative z-10">{buttonText}</span>
              </button>
            </form>

            <h3>
              {linkText}{" "}
              <Link
                to={linkPath}
                className="text-[#016891] sm:text-[#1B8FBE] hover:text-[#EAB308] font-bold"
              >
                here
              </Link>
            </h3>
          </div>
        </section>

        <section className="hidden md:w-1/3 md:flex justify-center items-center text-center">
          <div className="bg-white/20 flex flex-col justify-center items-center gap-4 p-10 m-10 rounded-lg">
            <img src="/images/image3.png" className="w-2/3" alt="Welcome" />
            <h1 className="text-white text-4xl border-b-2 pb-4 border-white">
              Hello, Friend!
            </h1>
            <p className="text-xl text-white">
              Our team is eager to assist you and your pet!
            </p>
          </div>
        </section>
      </div>
      <Footer />
    </>
  );
}

export default AuthPage;

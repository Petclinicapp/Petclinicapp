import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { FaBars, FaTimes } from "react-icons/fa";
import Logo from "./Logo";

const Navbar = () => {
  const [isScrolled, setIsScrolled] = useState(false);
  const [isOpen, setIsOpen] = useState(false);

  // Detect scroll event and update state
  const handleScroll = () => {
    setIsScrolled(window.scrollY > 50);
  };

  useEffect(() => {
    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  return (
    <nav
      className={`fixed top-0 left-0 w-full z-20 transition-all duration-300 ease-in-out ${
        isScrolled
          ? "bg-[#1B8FBE] bg-opacity-80 p-4 shadow-md"
          : "bg-transparent p-6"
      }`}
    >
      <div className="container mx-auto flex justify-between items-center">
        <Logo bg="dark" />
        <ul className="hidden md:flex space-x-6 text-lg">
          {["Home", "About Us", "Prices", "Contact"].map((item) => (
            <li key={item}>
              <a
                href="#"
                className="text-white hover:text-gray-300 border-b-2 border-transparent hover:border-white pb-1 transition-all duration-300"
              >
                {item}
              </a>
            </li>
          ))}
          <li>
            <Link
              to="/signin"
              className="text-white bg-[#016891] px-6 py-2 rounded transition-colors duration-300 hover:bg-[#2c6181] uppercase font-bold"
            >
              Sign In
            </Link>
          </li>
        </ul>
        <button
          className="md:hidden z-10 text-white text-2xl"
          onClick={() => setIsOpen(!isOpen)}
        >
          {isOpen ? <FaTimes /> : <FaBars />}
        </button>
      </div>
      <div
        className={`md:hidden fixed top-0 left-0 w-full h-screen bg-[#1B8FBE] bg-opacity-90 flex flex-col items-center justify-center transform ${
          isOpen ? "translate-x-0" : "-translate-x-full"
        } transition-transform duration-300`}
      >
        {["Home", "About Us", "Prices", "Contact"].map((item) => (
          <a
            key={item}
            href="#"
            className="text-white text-2xl py-4 hover:text-gray-300 transition"
            onClick={() => setIsOpen(false)}
          >
            {item}
          </a>
        ))}
        <Link
          to="/signin"
          className="text-white bg-[#016891] px-6 py-3 rounded transition-colors duration-300 hover:bg-[#2c6181] uppercase font-bold mt-6"
          onClick={() => setIsOpen(false)}
        >
          Sign In
        </Link>
      </div>
    </nav>
  );
};

export default Navbar;

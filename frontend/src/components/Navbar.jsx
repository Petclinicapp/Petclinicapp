import React, { useState, useEffect } from "react";
import { IoPawOutline } from "react-icons/io5";
import { Link } from "react-router-dom";
import Logo from "./Logo";

const Navbar = () => {
  const [isScrolled, setIsScrolled] = useState(false);

  // Detect scroll event and update state
  const handleScroll = () => {
    if (window.scrollY > 50) {
      // Adjust the threshold as needed
      setIsScrolled(true);
    } else {
      setIsScrolled(false);
    }
  };

  useEffect(() => {
    window.addEventListener("scroll", handleScroll);

    // Cleanup the event listener on unmount
    return () => {
      window.removeEventListener("scroll", handleScroll);
    };
  }, []);

  return (
    <nav
      className={`fixed top-0 left-0 w-full p-4 z-10 transition-all duration-300 ease-in-out ${
        isScrolled ? "bg-[#1B8FBE] bg-opacity-80 p-8" : "bg-transparent"
      }`}
    >
      <div className="container mx-auto flex justify-between items-center">
        <Logo bg="dark" />
        <ul className="flex space-x-6 text-lg">
          <li>
            <a
              href="#"
              className="text-white hover:text-gray-300 border-b-2 border-transparent hover:border-white pb-1 transition-all duration-300"
            >
              Home
            </a>
          </li>
          <li>
            <a
              href="#"
              className="text-white hover:text-gray-300 border-b-2 border-transparent hover:border-white pb-1 transition-all duration-300"
            >
              About Us
            </a>
          </li>
          <li>
            <a
              href="#"
              className="text-white hover:text-gray-300 border-b-2 border-transparent hover:border-white pb-1 transition-all duration-300"
            >
              Prices
            </a>
          </li>
          <li>
            <a
              href="#"
              className="text-white hover:text-gray-300 border-b-2 border-transparent hover:border-white pb-1 transition-all duration-300"
            >
              Contact
            </a>
          </li>
          <li>
            <Link
              to="/signin"
              className="text-white bg-[#016891] px-6 py-2 rounded transition-colors duration-300 hover:bg-[#2c6181] uppercase font-bold"
            >
              Sign In
            </Link>
          </li>
        </ul>
      </div>
    </nav>
  );
};

export default Navbar;

import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { FaBars, FaTimes } from "react-icons/fa";
import Logo from "./Logo";
import { useAuth } from "../context/UserContext";
import { headerLinks } from "../data";
import { IoIosLogOut } from "react-icons/io";
import { BiUser } from "react-icons/bi";

const Navbar = () => {
  const [isScrolled, setIsScrolled] = useState(false);
  const [isOpen, setIsOpen] = useState(false);
  const { logoutHandler, isLoggedIn, user } = useAuth();

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
          ? "bg-[#219EBC] bg-opacity-80 p-4 shadow-md"
          : "bg-transparent p-6"
      }`}
    >
      <div className="container mx-auto flex justify-between items-center">
        <Logo bg="dark" />
        <ul className="hidden md:flex space-x-6 text-lg items-center justify-center">
          {headerLinks.map((item) => (
            <li key={item.id}>
              <Link
                to={item.link}
                className="text-white hover:text-gray-300 border-b-2 border-transparent hover:border-white pb-1 transition-all duration-300"
              >
                {item.title}
              </Link>
            </li>
          ))}

          {isLoggedIn && user && (
            <Link
              to={`/profile/${user.id}`}
              className="flex gap-2 text-white text-sm px-6 py-3 transition-colors duration-300 border-b-2 border-transparent hover:border-white uppercase font-bold"
              onClick={() => setIsOpen(false)}
            >
              <BiUser size={20} /> Hello, {user.username}
            </Link>
          )}
          <li className="flex">
            {isLoggedIn ? (
              <button
                onClick={logoutHandler}
                className="text-white transition-colors duration-300 hover:text-[#FB8500] uppercase font-bold cursor-pointer"
                title="Logout"
              >
                <IoIosLogOut size={30} />
              </button>
            ) : (
              <Link
                to="/signin"
                className="text-white bg-transparent border-2 border-white hover:bg-white text-sm px-6 py-3 rounded transition-colors duration-300 hover:text-[#023047] uppercase font-bold"
                onClick={() => setIsOpen(false)}
              >
                Sign In
              </Link>
            )}
          </li>
        </ul>
        <button
          className="md:hidden z-10 text-white text-2xl cursor-pointer"
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
        {isLoggedIn && user && (
          <Link
            to={`/profile/${user.id}`}
            className="flex gap-2 text-white text-sm px-6 py-3  transition-colors duration-300 border-b-2 border-transparent hover:border-white uppercase font-bold"
            onClick={() => setIsOpen(false)}
          >
            <BiUser size={20} /> Hello, {user.username}
          </Link>
        )}
        {headerLinks.map((item) => (
          <Link
            key={item.id}
            to={item.link}
            className="text-white text-2xl py-4 hover:text-gray-300 transition"
            onClick={() => setIsOpen(false)}
          >
            {item.title}
          </Link>
        ))}

        {isLoggedIn ? (
          <button
            onClick={logoutHandler}
            className="text-white transition-colors duration-300 hover:text-[#FB8500] mt-6 uppercase font-bold cursor-pointer"
            title="Logout"
          >
            <IoIosLogOut size={40} />
          </button>
        ) : (
          <Link
            to="/signin"
            className="text-white bg-[#016891] px-6 py-3 rounded transition-colors duration-300 hover:bg-[#2c6181] uppercase font-bold mt-6"
            onClick={() => setIsOpen(false)}
          >
            Sign In
          </Link>
        )}
      </div>
    </nav>
  );
};

export default Navbar;

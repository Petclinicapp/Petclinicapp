import { Link } from "react-router-dom";
import Navbar from "./Navbar";
import { useAuth } from "../context/UserContext";

function Banner() {
  const { isLoggedIn } = useAuth();

  return (
    <div
      className="relative w-full h-screen bg-cover bg-center"
      style={{
        backgroundImage: `url('https://images.pexels.com/photos/2870510/pexels-photo-2870510.jpeg')`,
      }}
    >
      <Navbar />
      <div className="absolute inset-0 flex justify-center mt-16 lg:ml-34 items-left text-white flex-col">
        <div className=" p-10 rounded-lg w-full md:w-2/3 lg:w-1/2">
          <h1 className="text-3xl lg:text-5xl">Welcome to Our Vet Clinic!</h1>
          <p className=" mt-4 text-2xl">
            At our clinic, we provide high-quality veterinary care to ensure
            your pets stay healthy and happy. Our team of experienced
            professionals is dedicated to offering compassionate care for your
            furry friends. Whether it's a routine check-up or emergency care,
            we're here to help!
          </p>
          {!isLoggedIn && (
            <Link
              to="/signin"
              className="text-white uppercase font-bold justify-center flex w-32 bg-[#016891] text-lg mt-6 py-2 rounded transition-colors duration-300 hover:bg-[#2c6181]"
            >
              Sign In
            </Link>
          )}
        </div>
      </div>
    </div>
  );
}
export default Banner;

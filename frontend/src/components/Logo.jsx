import { Link } from "react-router-dom";
import { IoPawOutline } from "react-icons/io5";

function Logo({ bg }) {
  return bg === "dark" ? (
    <Link
      to="/"
      className="text-4xl font-bold text-white flex justify-center items-center gap-2"
    >
      <p>Pet Clinic</p>
      <IoPawOutline />
    </Link>
  ) : (
    <Link
      to="/"
      className="text-4xl font-bold text-[#1B8FBE] flex justify-center items-center gap-2"
    >
      <p>Pet Clinic</p>
      <IoPawOutline />
    </Link>
  );
}

export default Logo;

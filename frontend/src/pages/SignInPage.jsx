import { Link } from "react-router-dom";
import Input from "../components/Input";
import Logo from "../components/Logo";
import { useState } from "react";

function SignInPage() {
  const [user, setUser] = useState({ username: "", password: "" });

  const handleForm = (e) => {
    const { name, value } = e.target;
    setUser((prevUser) => ({
      ...prevUser,
      [name]: value,
    }));
  };

  const submit = () => {
    console.log("Success", user);
    setUser({ username: "", password: "" });
  };

  return (
    <div className=" h-screen flex bg-gradient-to-r from-slate-100 to-[#1B8FBE]">
      <section className="w-full md:w-2/3 relative flex justify-center items-center">
        <div className="flex justify-start p-4 absolute top-0 left-0">
          <Logo />
        </div>

        <div className="w-2/3 flex flex-col gap-10">
          <h1 className="text-5xl text-gray-800 font-bold">Sing In</h1>
          <form className="flex flex-col gap-4">
            <Input
              onChange={handleForm}
              value={user.username}
              type="text"
              name="text"
              label="Username *"
            />
            <Input
              onChange={handleForm}
              value={user.password}
              type="password"
              name="password"
              label="Password *"
            />
            <button
              onClick={submit}
              type="button"
              className="relative mt-6 cursor-pointer text-lg rounded flex h-[50px] w-40 items-center justify-center overflow-hidden bg-[#1B8FBE] text-white shadow-2xl transition-all before:absolute before:h-0 before:w-0 before:rounded-full before:bg-[#016891] before:duration-500 before:ease-out hover:before:h-56 hover:before:w-56"
            >
              <span className="relative z-10">Sign In</span>
            </button>
          </form>
          <h3>
            Have an account? Login{" "}
            <Link to="/login" className="text-[#1B8FBE] hover:text-[#EAB308]">
              here
            </Link>
          </h3>
        </div>
      </section>
      <section className="hidden md:w-1/3  md:flex justify-center items-center text-center">
        <div className="bg-white/20 flex flex-col justify-center items-center gap-4 p-10 m-10 rounded-lg">
          <img src="/images/image3.png" className="w-2/3" />
          <h1 className="text-white text-4xl border-b-2 pb-4 border-white">
            Hello, Friend!
          </h1>
          <p className="text-xl text-white">
            Our team is eager to assist you and your pet!
          </p>
        </div>
      </section>
    </div>
  );
}
export default SignInPage;

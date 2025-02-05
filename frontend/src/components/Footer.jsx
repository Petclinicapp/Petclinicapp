import { IoPawOutline } from "react-icons/io5";
import { footerLinks, footerServices } from "../data";
import { Link } from "react-router-dom";

function Footer() {
  return (
    <footer className="bg-[#1B8FBE] mt-10 px-10  md:px-16 lg:px-32">
      <h1 className="py-10 text-2xl lg:text-4xl text-white">
        Have a Question? Need to book an appointment?
      </h1>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-[2fr_1fr_1fr_2fr] gap-16 pb-10">
        <div className="text-white">
          <a
            href="/"
            className="text-5xl font-bold text-white flex items-center gap-2 mb-2"
          >
            <p>Pet Clinic</p>
            <IoPawOutline />
          </a>
          <p>
            We’re here to care for your pets and answer all your questions!
            Whether you’re a new pet parent or want to schedule an appointment.
          </p>
          <p className="mt-2 text-[#EAB308]">
            <span className="text-white mr-1">Contact Us:</span>
            vetclinic@vetclinic.com
          </p>
        </div>
        <div className="text-white">
          <h2 className="text-2xl font-bold mb-4">Our Services</h2>
          <ul className="list-disc">
            {footerServices.map((li) => (
              <li key={li.id} className="marker:text-[#EAB308]">
                {li.title}
              </li>
            ))}
          </ul>
        </div>
        <div className="text-white">
          <h2 className="text-2xl font-bold mb-4">Quicklinks</h2>
          <ul className="list-disc">
            {footerLinks.map((li) => (
              <li
                key={li.id}
                className="marker:text-[#EAB308] hover:text-[#EAB308] transition-all duration-300 ease-in-out"
              >
                <Link to={li.link}>{li.title}</Link>
              </li>
            ))}
          </ul>
        </div>
        <div className="text-white">
          <h2 className="text-2xl font-bold mb-4">Opening Hour</h2>
          <div className="space-y-2">
            <div className="flex justify-between border-b border-white/30 pb-2">
              <span>Mon - Tues</span>
              <span>09:00AM - 06:00PM</span>
            </div>
            <div className="flex justify-between border-b border-white/30 pb-2">
              <span>Wed - Thu</span>
              <span>09:00AM - 06:00PM</span>
            </div>
            <div className="flex justify-between border-b border-white/30 pb-2">
              <span>Fri - Sat</span>
              <span>09:00AM - 06:00PM</span>
            </div>
            <div className="flex justify-between pt-2">
              <span>Sunday</span>
              <span className="text-[#EAB308]">Emergency Only</span>
            </div>
          </div>
        </div>
      </div>
    </footer>
  );
}
export default Footer;

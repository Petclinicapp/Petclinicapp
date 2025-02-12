import AppointmentBooking from "../components/AppointmentBooking";
import Navbar from "../components/Navbar";

function VisitPage() {
  return (
    <div className="bg-[#023047] h-screen">
      <Navbar />
      <div className="pt-20">
        <AppointmentBooking />
      </div>
    </div>
  );
}
export default VisitPage;

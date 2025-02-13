import AppointmentBooking from "../components/AppointmentBooking";
import AppointmentsList from "../components/AppointmentsList";
import Footer from "../components/Footer";
import Navbar from "../components/Navbar";

function VisitPage() {
  return (
    <>
      <Navbar />
      <div className="min-h-screen pt-32 bg-[#023047] flex flex-col">
        <AppointmentBooking />
        <AppointmentsList />
      </div>
      <Footer />
    </>
  );
}
export default VisitPage;

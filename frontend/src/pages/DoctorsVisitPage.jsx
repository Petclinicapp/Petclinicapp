import AllVisits from "../components/AllVisits";
import Footer from "../components/Footer";
import Navbar from "../components/Navbar";

function DoctorsVisitPage() {
  return (
    <>
      <Navbar />
      <div className="bg-[#023047] min-h-screen">
        <h1 className="text-white text-5xl pt-32 text-center">All Visits</h1>
        <AllVisits />
      </div>
    </>
  );
}
export default DoctorsVisitPage;

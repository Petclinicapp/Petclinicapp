import AllVisits from "../components/AllVisits";
import Navbar from "../components/Navbar";

function DoctorsVisitPage() {
  return (
    <>
      <Navbar />
      <div className="bg-[#023047] min-h-screen pb-20">
        <AllVisits />
      </div>
    </>
  );
}
export default DoctorsVisitPage;

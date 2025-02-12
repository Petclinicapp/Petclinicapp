import Navbar from "../components/Navbar";
import PetsList from "../components/PetsList";
import UserCard from "../components/UserCard";
import Footer from "../components/Footer";

function ProfilePage() {
  return (
    <>
      <Navbar />
      <div className="bg-[#023047] flex flex-col items-center h-full">
        <UserCard />
        <PetsList />
      </div>
      <Footer />
    </>
  );
}
export default ProfilePage;

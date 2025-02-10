import Navbar from "../components/Navbar";
import PetsList from "../components/PetsList";
import UserCard from "../components/UserCard";

function ProfilePage() {
  return (
    <>
      <Navbar />
      <div className="bg-[#023047] flex flex-col items-center">
        <UserCard />
        <PetsList />
      </div>
    </>
  );
}
export default ProfilePage;

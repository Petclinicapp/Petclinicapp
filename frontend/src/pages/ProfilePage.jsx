import Navbar from "../components/Navbar";
import UserCard from "../components/UserCard";

function ProfilePage() {
  return (
    <div className="bg-[#8ECAE6] flex flex-col items-center">
      <Navbar />
      <UserCard />
    </div>
  );
}
export default ProfilePage;

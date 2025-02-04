import { Link } from "react-router-dom";

function SignInPage() {
  return (
    <div>
      <p>SignInPage</p>
      <Link to="/" className="bg-gray-300 p-4 flex w-32">
        Back Home{" "}
      </Link>
    </div>
  );
}
export default SignInPage;

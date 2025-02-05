import { useAuth } from "../context/UserContext";
import AuthPage from "./AuthPage";

function SignUpPage() {
  const { register } = useAuth();

  return (
    <AuthPage
      title="Register"
      buttonText="Register"
      onSubmit={register}
      linkText="Already have an account? Sign in"
      linkPath="/signin"
      fields={[
        { name: "firstname", type: "text", label: "Firstname *" },
        { name: "lastname", type: "text", label: "Lastname *" },
        { name: "email", type: "email", label: "Email *" },
        { name: "username", type: "text", label: "Username *" },
        { name: "password", type: "password", label: "Password *" },
      ]}
    />
  );
}
export default SignUpPage;

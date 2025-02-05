import { useAuth } from "../context/UserContext";
import AuthPage from "./AuthPage";

function SignInPage() {
  const { login } = useAuth();

  return (
    <AuthPage
      title="Sign In"
      buttonText="Sign In"
      onSubmit={login}
      linkText="Don't have an account? Register"
      linkPath="/register"
      fields={[
        { name: "username", type: "text", label: "Username *" },
        { name: "password", type: "password", label: "Password *" },
      ]}
    />
  );
}

export default SignInPage;

import React, { useState } from "react";
import { Link } from "react-router-dom";
import { Container, Form, Button } from "react-bootstrap";
import { FaGoogle, FaGithub } from "react-icons/fa";
import { useAuth } from "../provider/AuthProvider";
import axios from "axios";
// import UseFetch from "../hooks/useFetch";

export default function Login() {
  const [validated, setValidated] = useState(false);
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const { setToken } = useAuth();

  //Handle submit
  const handleSubmit = async (e) => {
    e.preventDefault();
    const form = e.currentTarget;
    if (form.checkValidity() === false) {
      e.stopPropagation();
    } else {
      //Send user's input to backend
      axios({
        method: "POST",
        url: "https://swp391-g5-jewelry-production-order-system.onrender.com/api/registration/login",
        headers: { "Content-Type": "application/json" },
        data: { email, password },
      })
        .then((response) => {
          if (response.status === "OK") {
            alert(response.message);
            setToken(response.token);
          } else if (response.status === "BAD REQUEST") {
            throw new Error(response.message);
          }
        })
        .catch((error) => {
          alert(error);
          console.error("There was an error!", error);
        });
    }
    setValidated(true);
  };

  return (
    <Container
      style={{ paddingTop: "10%", paddingBottom: "10%" }}
      className="d-flex justify-content-center align-items-center"
    >
      <div
        className="p-4"
        style={{
          width: "30%",
          backgroundColor: "rgba(217, 217, 217, 0.7)",
          borderRadius: 20,
        }}
      >
        <h2 className="text-center mb-4">Sign in</h2>
        <Form noValidate validated={validated} onSubmit={handleSubmit}>
          <Form.Group className="mb-3">
            <Form.Label>Username:</Form.Label>
            <Form.Control
              required
              type="email"
              placeholder="Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="border-2"
              style={{ borderColor: "#000", borderRadius: 10 }}
            />
            <Form.Control.Feedback type="invalid">
              Please provide a valid email.
            </Form.Control.Feedback>
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Password:</Form.Label>
            <Form.Control
              required
              type="password"
              className="border-2"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              style={{ borderColor: "#000", borderRadius: 10 }}
            />
            <Form.Control.Feedback type="invalid">
              Please provide a password.
            </Form.Control.Feedback>
            <div className="text-end mt-2">
              <Link
                to="/reset_password"
                className="text-muted text-decoration-none"
              >
                Forget password
              </Link>
            </div>
          </Form.Group>
          <div className="d-flex justify-content-center mb-3">
            <Button
              type="submit"
              className="w-75 border-2"
              style={{
                backgroundColor: "rgba(201, 201, 201, 1)",
                borderColor: "#000",
                color: "#000",
                borderRadius: 10,
              }}
            >
              Sign in
            </Button>
          </div>
        </Form>
        <div className="d-flex align-items-center my-3">
          <div style={{ flex: 1, height: "1px", backgroundColor: "#000" }} />
          <span className="mx-3 text-muted">Or</span>
          <div style={{ flex: 1, height: "1px", backgroundColor: "#000" }} />
        </div>
        <div className="d-flex justify-content-center mb-3">
          <Button variant="outline-dark" className="mx-2">
            <FaGoogle />
          </Button>
          <Button variant="outline-dark" className="mx-2">
            <FaGithub />
          </Button>
        </div>
        <p className="text-center text-muted">
          Don’t have an account?{" "}
          <Link to="/signup" className="text-decoration-underline text-muted">
            Sign up
          </Link>
        </p>
      </div>
    </Container>
  );
}

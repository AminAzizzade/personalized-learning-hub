import TutorNavbar from "../components/navbar/TutorNavbar";
import { Outlet } from "react-router-dom";

export default function TutorLayout() {
  return (
    <>
      <TutorNavbar />
      <div className="p-4">
        <Outlet />
      </div>
    </>
  );
}

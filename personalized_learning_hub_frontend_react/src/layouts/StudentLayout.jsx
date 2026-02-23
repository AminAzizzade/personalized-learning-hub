import StudentNavbar from "../components/navbar/StudentNavbar";
import { Outlet } from "react-router-dom";

export default function StudentLayout() {
  return (
    <>
      <StudentNavbar />
      <div className="p-4">
        <Outlet />
      </div>
    </>
  );
}

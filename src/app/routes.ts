import { createBrowserRouter } from "react-router";
import LoginPage from "./pages/LoginPage";
import TeacherDashboard from "./pages/TeacherDashboard";
import QuestionBank from "./pages/QuestionBank";
import ExerciseGenerator from "./pages/ExerciseGenerator";
import ExamGenerator from "./pages/ExamGenerator";
import OCRGrading from "./pages/OCRGrading";
import Analytics from "./pages/Analytics";
import AdminDashboard from "./pages/AdminDashboard";
import ManagerDashboard from "./pages/ManagerDashboard";
import StaffDashboard from "./pages/StaffDashboard";
import DashboardLayout from "./components/DashboardLayout";

export const router = createBrowserRouter([
  {
    path: "/",
    Component: LoginPage,
  },
  {
    path: "/login",
    Component: LoginPage,
  },
  {
    path: "/teacher",
    Component: DashboardLayout,
    children: [
      { index: true, Component: TeacherDashboard },
      { path: "question-bank", Component: QuestionBank },
      { path: "exercise-generator", Component: ExerciseGenerator },
      { path: "exam-generator", Component: ExamGenerator },
      { path: "ocr-grading", Component: OCRGrading },
      { path: "analytics", Component: Analytics },
    ],
  },
  {
    path: "/admin",
    Component: DashboardLayout,
    children: [
      { index: true, Component: AdminDashboard },
    ],
  },
  {
    path: "/manager",
    Component: DashboardLayout,
    children: [
      { index: true, Component: ManagerDashboard },
    ],
  },
  {
    path: "/staff",
    Component: DashboardLayout,
    children: [
      { index: true, Component: StaffDashboard },
    ],
  },
]);

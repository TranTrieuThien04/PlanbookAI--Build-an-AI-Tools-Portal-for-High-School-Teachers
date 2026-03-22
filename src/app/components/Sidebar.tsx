import { Link, useLocation } from "react-router";
import {
  LayoutDashboard,
  BookOpen,
  Wand2,
  FileText,
  ScanLine,
  BarChart3,
  Users,
  Settings,
  Package,
  Database,
  LogOut,
  GraduationCap,
} from "lucide-react";
import { cn } from "../components/ui/utils";
import type { UserRole } from "../lib/auth";

interface SidebarProps {
  role: UserRole;
  onLogout: () => void;
}

const navigationByRole: Record<
  UserRole,
  Array<{ icon: any; label: string; href: string }>
> = {
  teacher: [
    { icon: LayoutDashboard, label: "Dashboard", href: "/teacher" },
    { icon: BookOpen, label: "Question Bank", href: "/teacher/question-bank" },
    {
      icon: Wand2,
      label: "Exercise Generator",
      href: "/teacher/exercise-generator",
    },
    { icon: FileText, label: "Exam Generator", href: "/teacher/exam-generator" },
    { icon: ScanLine, label: "OCR Grading", href: "/teacher/ocr-grading" },
    { icon: BarChart3, label: "Analytics", href: "/teacher/analytics" },
  ],
  admin: [
    { icon: LayoutDashboard, label: "Dashboard", href: "/admin" },
    { icon: Users, label: "User Management", href: "/admin" },
    { icon: Settings, label: "System Settings", href: "/admin" },
    { icon: Database, label: "Curriculum Templates", href: "/admin" },
    { icon: BarChart3, label: "Revenue Reports", href: "/admin" },
  ],
  manager: [
    { icon: LayoutDashboard, label: "Dashboard", href: "/manager" },
    { icon: Package, label: "Subscriptions", href: "/manager" },
    { icon: FileText, label: "Orders", href: "/manager" },
    { icon: BookOpen, label: "Content Approval", href: "/manager" },
  ],
  staff: [
    { icon: LayoutDashboard, label: "Dashboard", href: "/staff" },
    { icon: BookOpen, label: "Lesson Plans", href: "/staff" },
    { icon: Database, label: "Question Bank", href: "/staff" },
    { icon: Wand2, label: "AI Prompts", href: "/staff" },
  ],
};

export function Sidebar({ role, onLogout }: SidebarProps) {
  const location = useLocation();
  const navigation = navigationByRole[role];

  return (
    <div className="flex h-full w-64 flex-col border-r bg-white">
      {/* Logo */}
      <div className="flex h-16 items-center gap-2 border-b px-6">
        <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-gradient-to-br from-indigo-600 to-cyan-500">
          <GraduationCap className="h-6 w-6 text-white" />
        </div>
        <div>
          <h1 className="font-semibold text-gray-900">PlanbookAI</h1>
          <p className="text-xs text-gray-500 capitalize">{role} Portal</p>
        </div>
      </div>

      {/* Navigation */}
      <nav className="flex-1 space-y-1 p-4">
        {navigation.map((item) => {
          const Icon = item.icon;
          const isActive = location.pathname === item.href;

          return (
            <Link
              key={item.href}
              to={item.href}
              className={cn(
                "flex items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-medium transition-colors",
                isActive
                  ? "bg-indigo-50 text-indigo-700"
                  : "text-gray-700 hover:bg-gray-50 hover:text-gray-900"
              )}
            >
              <Icon className="h-5 w-5" />
              {item.label}
            </Link>
          );
        })}
      </nav>

      {/* Logout */}
      <div className="border-t p-4">
        <button
          onClick={onLogout}
          className="flex w-full items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-medium text-gray-700 transition-colors hover:bg-gray-50 hover:text-gray-900"
        >
          <LogOut className="h-5 w-5" />
          Logout
        </button>
      </div>
    </div>
  );
}

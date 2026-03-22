// Mock authentication system
export type UserRole = "teacher" | "admin" | "manager" | "staff";

export interface User {
  id: string;
  name: string;
  email: string;
  role: UserRole;
  avatar?: string;
}

// Mock user data
const mockUsers: Record<string, User> = {
  teacher: {
    id: "1",
    name: "Dr. Sarah Johnson",
    email: "sarah.johnson@school.edu",
    role: "teacher",
  },
  admin: {
    id: "2",
    name: "Michael Chen",
    email: "michael.chen@planbookai.com",
    role: "admin",
  },
  manager: {
    id: "3",
    name: "Emily Rodriguez",
    email: "emily.rodriguez@planbookai.com",
    role: "manager",
  },
  staff: {
    id: "4",
    name: "James Williams",
    email: "james.williams@planbookai.com",
    role: "staff",
  },
};

export const login = (email: string, password: string): User | null => {
  // Mock login - in real app, this would call an API
  const role = email.split("@")[0].split(".")[0].toLowerCase() as UserRole;
  return mockUsers[role] || mockUsers.teacher;
};

export const logout = () => {
  localStorage.removeItem("user");
};

export const getCurrentUser = (): User | null => {
  const userStr = localStorage.getItem("user");
  return userStr ? JSON.parse(userStr) : null;
};

export const saveUser = (user: User) => {
  localStorage.setItem("user", JSON.stringify(user));
};

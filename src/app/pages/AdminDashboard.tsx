import { Users, Settings, Database, DollarSign, UserPlus, Shield, Activity } from "lucide-react";
import { StatCard } from "../components/StatCard";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "../components/ui/card";
import { Button } from "../components/ui/button";
import { Badge } from "../components/ui/badge";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "../components/ui/table";
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, LineChart, Line } from "recharts";

// Mock data
const revenueData = [
  { month: "Oct", revenue: 12500 },
  { month: "Nov", revenue: 15200 },
  { month: "Dec", revenue: 18900 },
  { month: "Jan", revenue: 21400 },
  { month: "Feb", revenue: 24800 },
  { month: "Mar", revenue: 28300 },
];

const userGrowth = [
  { month: "Oct", users: 45 },
  { month: "Nov", users: 62 },
  { month: "Dec", users: 78 },
  { month: "Jan", users: 95 },
  { month: "Feb", users: 124 },
  { month: "Mar", users: 156 },
];

const recentUsers = [
  { id: "U156", name: "Dr. Sarah Johnson", email: "sarah.j@school.edu", role: "Teacher", status: "Active", joinDate: "2026-03-18" },
  { id: "U155", name: "Prof. Michael Chen", email: "m.chen@college.edu", role: "Teacher", status: "Active", joinDate: "2026-03-17" },
  { id: "U154", name: "Emily Rodriguez", email: "emily.r@school.edu", role: "Manager", status: "Active", joinDate: "2026-03-15" },
  { id: "U153", name: "James Williams", email: "james.w@edu.org", role: "Staff", status: "Pending", joinDate: "2026-03-14" },
  { id: "U152", name: "Lisa Anderson", email: "lisa.a@school.edu", role: "Teacher", status: "Active", joinDate: "2026-03-12" },
];

export default function AdminDashboard() {
  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Admin Dashboard</h1>
          <p className="mt-1 text-gray-600">
            System overview and user management
          </p>
        </div>
        <Button className="bg-indigo-600 hover:bg-indigo-700">
          <UserPlus className="mr-2 h-4 w-4" />
          Add New User
        </Button>
      </div>

      {/* Stats */}
      <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-4">
        <StatCard
          title="Total Users"
          value="156"
          icon={Users}
          trend={{ value: "12%", isPositive: true }}
        />
        <StatCard
          title="Monthly Revenue"
          value="$28.3K"
          icon={DollarSign}
          trend={{ value: "14%", isPositive: true }}
        />
        <StatCard
          title="Active Teachers"
          value="124"
          icon={Shield}
          description="Out of 156 users"
        />
        <StatCard
          title="System Uptime"
          value="99.9%"
          icon={Activity}
          description="Last 30 days"
        />
      </div>

      {/* Charts */}
      <div className="grid gap-6 lg:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle>Revenue Growth</CardTitle>
            <CardDescription>Monthly revenue over the past 6 months</CardDescription>
          </CardHeader>
          <CardContent>
            <ResponsiveContainer width="100%" height={250}>
              <LineChart data={revenueData}>
                <CartesianGrid strokeDasharray="3 3" className="stroke-gray-200" />
                <XAxis dataKey="month" className="text-xs" />
                <YAxis className="text-xs" />
                <Tooltip formatter={(value) => `$${value}`} />
                <Line type="monotone" dataKey="revenue" stroke="#10b981" strokeWidth={2} />
              </LineChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>User Growth</CardTitle>
            <CardDescription>New user registrations per month</CardDescription>
          </CardHeader>
          <CardContent>
            <ResponsiveContainer width="100%" height={250}>
              <BarChart data={userGrowth}>
                <CartesianGrid strokeDasharray="3 3" className="stroke-gray-200" />
                <XAxis dataKey="month" className="text-xs" />
                <YAxis className="text-xs" />
                <Tooltip />
                <Bar dataKey="users" fill="#4f46e5" radius={[8, 8, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>
      </div>

      {/* User Management */}
      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <div>
              <CardTitle>Recent Users</CardTitle>
              <CardDescription>Latest user registrations</CardDescription>
            </div>
            <Button variant="outline" size="sm">View All</Button>
          </div>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>User ID</TableHead>
                <TableHead>Name</TableHead>
                <TableHead>Email</TableHead>
                <TableHead>Role</TableHead>
                <TableHead>Status</TableHead>
                <TableHead>Join Date</TableHead>
                <TableHead className="text-right">Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {recentUsers.map((user) => (
                <TableRow key={user.id}>
                  <TableCell className="font-medium">{user.id}</TableCell>
                  <TableCell>{user.name}</TableCell>
                  <TableCell className="text-gray-600">{user.email}</TableCell>
                  <TableCell>
                    <Badge variant="outline" className="text-indigo-600 border-indigo-200">
                      {user.role}
                    </Badge>
                  </TableCell>
                  <TableCell>
                    {user.status === "Active" ? (
                      <Badge className="bg-green-50 text-green-700">Active</Badge>
                    ) : (
                      <Badge className="bg-yellow-50 text-yellow-700">Pending</Badge>
                    )}
                  </TableCell>
                  <TableCell className="text-gray-600">{user.joinDate}</TableCell>
                  <TableCell className="text-right">
                    <div className="flex justify-end gap-2">
                      <Button variant="ghost" size="sm">Edit</Button>
                      <Button variant="ghost" size="sm" className="text-red-600">
                        Delete
                      </Button>
                    </div>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>

      {/* System Settings Quick Links */}
      <div className="grid gap-6 md:grid-cols-3">
        <Card className="hover:shadow-md transition-shadow cursor-pointer">
          <CardContent className="p-6">
            <div className="flex items-center gap-4">
              <div className="flex h-12 w-12 items-center justify-center rounded-lg bg-indigo-50">
                <Settings className="h-6 w-6 text-indigo-600" />
              </div>
              <div>
                <h3 className="font-semibold text-gray-900">System Settings</h3>
                <p className="text-sm text-gray-600">Configure platform settings</p>
              </div>
            </div>
          </CardContent>
        </Card>

        <Card className="hover:shadow-md transition-shadow cursor-pointer">
          <CardContent className="p-6">
            <div className="flex items-center gap-4">
              <div className="flex h-12 w-12 items-center justify-center rounded-lg bg-cyan-50">
                <Database className="h-6 w-6 text-cyan-600" />
              </div>
              <div>
                <h3 className="font-semibold text-gray-900">Curriculum Templates</h3>
                <p className="text-sm text-gray-600">Manage curriculum content</p>
              </div>
            </div>
          </CardContent>
        </Card>

        <Card className="hover:shadow-md transition-shadow cursor-pointer">
          <CardContent className="p-6">
            <div className="flex items-center gap-4">
              <div className="flex h-12 w-12 items-center justify-center rounded-lg bg-green-50">
                <DollarSign className="h-6 w-6 text-green-600" />
              </div>
              <div>
                <h3 className="font-semibold text-gray-900">Revenue Reports</h3>
                <p className="text-sm text-gray-600">View financial analytics</p>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}

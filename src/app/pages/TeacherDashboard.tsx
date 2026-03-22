import { Link } from "react-router";
import {
  BookOpen,
  Wand2,
  FileText,
  ScanLine,
  TrendingUp,
  Clock,
  CheckCircle2,
  Users,
} from "lucide-react";
import { StatCard } from "../components/StatCard";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "../components/ui/card";
import { Button } from "../components/ui/button";
import { Badge } from "../components/ui/badge";
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, LineChart, Line } from "recharts";

// Mock data
const recentExams = [
  { id: 1, title: "Chemical Bonding Quiz", date: "2026-03-18", students: 28, graded: 28, avgScore: 85 },
  { id: 2, title: "Periodic Table Test", date: "2026-03-15", students: 28, graded: 25, avgScore: 78 },
  { id: 3, title: "Stoichiometry Exam", date: "2026-03-12", students: 30, graded: 30, avgScore: 82 },
];

const performanceData = [
  { week: "Week 1", score: 75 },
  { week: "Week 2", score: 78 },
  { week: "Week 3", score: 82 },
  { week: "Week 4", score: 85 },
];

const topicDistribution = [
  { topic: "Bonding", count: 45 },
  { topic: "Reactions", count: 38 },
  { topic: "States", count: 32 },
  { topic: "Periodic", count: 28 },
];

const quickActions = [
  {
    icon: Wand2,
    title: "Generate Exercise",
    description: "Create AI-powered exercises",
    href: "/teacher/exercise-generator",
    color: "bg-purple-50 text-purple-600",
  },
  {
    icon: FileText,
    title: "Create Exam",
    description: "Build multiple choice exams",
    href: "/teacher/exam-generator",
    color: "bg-blue-50 text-blue-600",
  },
  {
    icon: ScanLine,
    title: "Grade Papers",
    description: "OCR-based auto grading",
    href: "/teacher/ocr-grading",
    color: "bg-green-50 text-green-600",
  },
  {
    icon: BookOpen,
    title: "Question Bank",
    description: "Manage your questions",
    href: "/teacher/question-bank",
    color: "bg-orange-50 text-orange-600",
  },
];

export default function TeacherDashboard() {
  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-3xl font-bold text-gray-900">Dashboard</h1>
        <p className="mt-1 text-gray-600">
          Welcome back! Here's what's happening with your classes.
        </p>
      </div>

      {/* Stats */}
      <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-4">
        <StatCard
          title="Total Questions"
          value="247"
          icon={BookOpen}
          trend={{ value: "12%", isPositive: true }}
        />
        <StatCard
          title="Exams Created"
          value="18"
          icon={FileText}
          description="This month"
        />
        <StatCard
          title="Avg. Class Score"
          value="82%"
          icon={TrendingUp}
          trend={{ value: "3%", isPositive: true }}
        />
        <StatCard
          title="Students"
          value="84"
          icon={Users}
          description="Across 3 classes"
        />
      </div>

      {/* Quick Actions */}
      <div>
        <h2 className="text-lg font-semibold text-gray-900 mb-4">Quick Actions</h2>
        <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
          {quickActions.map((action) => {
            const Icon = action.icon;
            return (
              <Link key={action.href} to={action.href}>
                <Card className="hover:shadow-md transition-shadow cursor-pointer">
                  <CardContent className="p-6">
                    <div className={`inline-flex h-12 w-12 items-center justify-center rounded-lg ${action.color} mb-4`}>
                      <Icon className="h-6 w-6" />
                    </div>
                    <h3 className="font-semibold text-gray-900 mb-1">{action.title}</h3>
                    <p className="text-sm text-gray-600">{action.description}</p>
                  </CardContent>
                </Card>
              </Link>
            );
          })}
        </div>
      </div>

      {/* Charts Row */}
      <div className="grid gap-6 lg:grid-cols-2">
        {/* Performance Trend */}
        <Card>
          <CardHeader>
            <CardTitle>Class Performance Trend</CardTitle>
            <CardDescription>Average scores over the past 4 weeks</CardDescription>
          </CardHeader>
          <CardContent>
            <ResponsiveContainer width="100%" height={250}>
              <LineChart data={performanceData}>
                <CartesianGrid strokeDasharray="3 3" className="stroke-gray-200" />
                <XAxis dataKey="week" className="text-xs" />
                <YAxis className="text-xs" />
                <Tooltip />
                <Line type="monotone" dataKey="score" stroke="#4f46e5" strokeWidth={2} />
              </LineChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>

        {/* Topic Distribution */}
        <Card>
          <CardHeader>
            <CardTitle>Questions by Topic</CardTitle>
            <CardDescription>Distribution of questions in your bank</CardDescription>
          </CardHeader>
          <CardContent>
            <ResponsiveContainer width="100%" height={250}>
              <BarChart data={topicDistribution}>
                <CartesianGrid strokeDasharray="3 3" className="stroke-gray-200" />
                <XAxis dataKey="topic" className="text-xs" />
                <YAxis className="text-xs" />
                <Tooltip />
                <Bar dataKey="count" fill="#06b6d4" radius={[8, 8, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>
      </div>

      {/* Recent Exams */}
      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <div>
              <CardTitle>Recent Exams</CardTitle>
              <CardDescription>Your latest exam activities</CardDescription>
            </div>
            <Link to="/teacher/analytics">
              <Button variant="outline" size="sm">View All</Button>
            </Link>
          </div>
        </CardHeader>
        <CardContent>
          <div className="space-y-4">
            {recentExams.map((exam) => (
              <div
                key={exam.id}
                className="flex items-center justify-between p-4 rounded-lg border bg-white hover:bg-gray-50 transition-colors"
              >
                <div className="flex-1">
                  <h4 className="font-semibold text-gray-900">{exam.title}</h4>
                  <div className="flex items-center gap-4 mt-1 text-sm text-gray-600">
                    <span className="flex items-center gap-1">
                      <Clock className="h-4 w-4" />
                      {exam.date}
                    </span>
                    <span className="flex items-center gap-1">
                      <Users className="h-4 w-4" />
                      {exam.students} students
                    </span>
                  </div>
                </div>
                <div className="flex items-center gap-6">
                  <div className="text-right">
                    <p className="text-sm text-gray-600">Graded</p>
                    <p className="font-semibold text-gray-900">
                      {exam.graded}/{exam.students}
                    </p>
                  </div>
                  <div className="text-right">
                    <p className="text-sm text-gray-600">Avg. Score</p>
                    <p className="text-2xl font-bold text-indigo-600">{exam.avgScore}%</p>
                  </div>
                  {exam.graded === exam.students ? (
                    <Badge className="bg-green-50 text-green-700 hover:bg-green-50">
                      <CheckCircle2 className="mr-1 h-3 w-3" />
                      Complete
                    </Badge>
                  ) : (
                    <Badge variant="outline" className="text-orange-600 border-orange-200">
                      Pending
                    </Badge>
                  )}
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    </div>
  );
}

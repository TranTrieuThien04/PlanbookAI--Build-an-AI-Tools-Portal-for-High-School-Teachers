import { TrendingUp, Users, BookOpen, Award, Calendar } from "lucide-react";
import { StatCard } from "../components/StatCard";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "../components/ui/card";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "../components/ui/select";
import { Badge } from "../components/ui/badge";
import {
  LineChart,
  Line,
  BarChart,
  Bar,
  PieChart,
  Pie,
  Cell,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
  RadarChart,
  PolarGrid,
  PolarAngleAxis,
  PolarRadiusAxis,
  Radar,
} from "recharts";

// Mock data
const performanceTrend = [
  { month: "Sep", class1: 78, class2: 82, class3: 75 },
  { month: "Oct", class1: 82, class2: 85, class3: 79 },
  { month: "Nov", class1: 85, class2: 87, class3: 82 },
  { month: "Dec", class1: 83, class2: 88, class3: 84 },
  { month: "Jan", class1: 87, class2: 90, class3: 86 },
  { month: "Feb", class1: 89, class2: 91, class3: 88 },
  { month: "Mar", class1: 91, class2: 93, class3: 90 },
];

const topicPerformance = [
  { topic: "Bonding", score: 92 },
  { topic: "Periodic", score: 85 },
  { topic: "Reactions", score: 88 },
  { topic: "Acids/Bases", score: 78 },
  { topic: "Stoichiometry", score: 82 },
  { topic: "Thermo", score: 75 },
];

const scoreDistribution = [
  { range: "90-100", count: 24, color: "#10b981" },
  { range: "80-89", count: 32, color: "#3b82f6" },
  { range: "70-79", count: 18, color: "#f59e0b" },
  { range: "60-69", count: 8, color: "#ef4444" },
  { range: "<60", count: 2, color: "#dc2626" },
];

const studentPerformance = [
  { name: "Emma Wilson", avgScore: 95, exams: 12, trend: "up" },
  { name: "James Rodriguez", avgScore: 92, exams: 12, trend: "up" },
  { name: "Sophia Chen", avgScore: 89, exams: 11, trend: "stable" },
  { name: "Michael Zhang", avgScore: 86, exams: 12, trend: "up" },
  { name: "Isabella Garcia", avgScore: 84, exams: 12, trend: "down" },
];

const radarData = [
  { subject: "Understanding", A: 85, fullMark: 100 },
  { subject: "Application", A: 90, fullMark: 100 },
  { subject: "Analysis", A: 75, fullMark: 100 },
  { subject: "Problem Solving", A: 88, fullMark: 100 },
  { subject: "Critical Thinking", A: 82, fullMark: 100 },
];

export default function Analytics() {
  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Analytics & Reports</h1>
          <p className="mt-1 text-gray-600">
            Track student performance and exam insights
          </p>
        </div>
        <Select defaultValue="all">
          <SelectTrigger className="w-48">
            <Calendar className="mr-2 h-4 w-4" />
            <SelectValue />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="all">All Time</SelectItem>
            <SelectItem value="semester">This Semester</SelectItem>
            <SelectItem value="month">This Month</SelectItem>
            <SelectItem value="week">This Week</SelectItem>
          </SelectContent>
        </Select>
      </div>

      {/* Stats */}
      <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-4">
        <StatCard
          title="Average Class Score"
          value="85.4%"
          icon={TrendingUp}
          trend={{ value: "4.2%", isPositive: true }}
        />
        <StatCard
          title="Total Students"
          value="84"
          icon={Users}
          description="Across 3 classes"
        />
        <StatCard
          title="Exams Graded"
          value="247"
          icon={BookOpen}
          trend={{ value: "18", isPositive: true }}
        />
        <StatCard
          title="Top Performer"
          value="95%"
          icon={Award}
          description="Emma Wilson"
        />
      </div>

      {/* Performance Trends */}
      <div className="grid gap-6 lg:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle>Performance Trend by Class</CardTitle>
            <CardDescription>
              Average scores over the academic year
            </CardDescription>
          </CardHeader>
          <CardContent>
            <ResponsiveContainer width="100%" height={300}>
              <LineChart data={performanceTrend}>
                <CartesianGrid strokeDasharray="3 3" className="stroke-gray-200" />
                <XAxis dataKey="month" className="text-xs" />
                <YAxis className="text-xs" domain={[60, 100]} />
                <Tooltip />
                <Legend />
                <Line
                  type="monotone"
                  dataKey="class1"
                  stroke="#4f46e5"
                  strokeWidth={2}
                  name="Class A"
                />
                <Line
                  type="monotone"
                  dataKey="class2"
                  stroke="#06b6d4"
                  strokeWidth={2}
                  name="Class B"
                />
                <Line
                  type="monotone"
                  dataKey="class3"
                  stroke="#8b5cf6"
                  strokeWidth={2}
                  name="Class C"
                />
              </LineChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Topic Performance</CardTitle>
            <CardDescription>
              Average scores by chemistry topic
            </CardDescription>
          </CardHeader>
          <CardContent>
            <ResponsiveContainer width="100%" height={300}>
              <BarChart data={topicPerformance}>
                <CartesianGrid strokeDasharray="3 3" className="stroke-gray-200" />
                <XAxis dataKey="topic" className="text-xs" />
                <YAxis className="text-xs" domain={[0, 100]} />
                <Tooltip />
                <Bar dataKey="score" fill="#4f46e5" radius={[8, 8, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>
      </div>

      {/* Score Distribution and Skills Radar */}
      <div className="grid gap-6 lg:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle>Score Distribution</CardTitle>
            <CardDescription>
              Number of students by score range
            </CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              <ResponsiveContainer width="100%" height={200}>
                <PieChart>
                  <Pie
                    data={scoreDistribution}
                    dataKey="count"
                    nameKey="range"
                    cx="50%"
                    cy="50%"
                    outerRadius={80}
                    label
                  >
                    {scoreDistribution.map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={entry.color} />
                    ))}
                  </Pie>
                  <Tooltip />
                </PieChart>
              </ResponsiveContainer>
              <div className="grid grid-cols-5 gap-2">
                {scoreDistribution.map((item) => (
                  <div key={item.range} className="text-center">
                    <div
                      className="h-3 w-full rounded mb-1"
                      style={{ backgroundColor: item.color }}
                    ></div>
                    <p className="text-xs font-medium text-gray-900">{item.range}</p>
                    <p className="text-xs text-gray-500">{item.count} students</p>
                  </div>
                ))}
              </div>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Skills Assessment</CardTitle>
            <CardDescription>
              Class average across different skill areas
            </CardDescription>
          </CardHeader>
          <CardContent>
            <ResponsiveContainer width="100%" height={280}>
              <RadarChart data={radarData}>
                <PolarGrid />
                <PolarAngleAxis dataKey="subject" className="text-xs" />
                <PolarRadiusAxis angle={90} domain={[0, 100]} />
                <Radar
                  name="Class Average"
                  dataKey="A"
                  stroke="#4f46e5"
                  fill="#4f46e5"
                  fillOpacity={0.6}
                />
                <Tooltip />
              </RadarChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>
      </div>

      {/* Top Performers */}
      <Card>
        <CardHeader>
          <CardTitle>Top Performing Students</CardTitle>
          <CardDescription>
            Students with highest average scores
          </CardDescription>
        </CardHeader>
        <CardContent>
          <div className="space-y-4">
            {studentPerformance.map((student, index) => (
              <div
                key={student.name}
                className="flex items-center justify-between p-4 rounded-lg border bg-white hover:bg-gray-50 transition-colors"
              >
                <div className="flex items-center gap-4">
                  <div className="flex h-10 w-10 items-center justify-center rounded-full bg-indigo-50 font-bold text-indigo-600">
                    #{index + 1}
                  </div>
                  <div>
                    <p className="font-semibold text-gray-900">{student.name}</p>
                    <p className="text-sm text-gray-600">
                      {student.exams} exams completed
                    </p>
                  </div>
                </div>
                <div className="flex items-center gap-4">
                  <div className="text-right">
                    <p className="text-sm text-gray-600">Average Score</p>
                    <p className="text-2xl font-bold text-indigo-600">
                      {student.avgScore}%
                    </p>
                  </div>
                  <Badge
                    variant="outline"
                    className={
                      student.trend === "up"
                        ? "bg-green-50 text-green-700 border-green-200"
                        : student.trend === "down"
                        ? "bg-red-50 text-red-700 border-red-200"
                        : "bg-gray-50 text-gray-700 border-gray-200"
                    }
                  >
                    {student.trend === "up" ? "↑" : student.trend === "down" ? "↓" : "→"}{" "}
                    {student.trend === "up"
                      ? "Improving"
                      : student.trend === "down"
                      ? "Declining"
                      : "Stable"}
                  </Badge>
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    </div>
  );
}

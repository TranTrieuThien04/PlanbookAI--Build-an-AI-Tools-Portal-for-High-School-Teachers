import { FileText, Database, Wand2, CheckCircle, Clock, Plus } from "lucide-react";
import { StatCard } from "../components/StatCard";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "../components/ui/card";
import { Button } from "../components/ui/button";
import { Badge } from "../components/ui/badge";
import { Progress } from "../components/ui/progress";

// Mock data
const lessonPlans = [
  {
    id: 1,
    title: "Introduction to Chemical Bonding",
    topic: "Chemical Bonding",
    grade: "Grade 10",
    status: "Published",
    lastModified: "2026-03-18",
  },
  {
    id: 2,
    title: "Periodic Table Structure",
    topic: "Periodic Table",
    grade: "Grade 9",
    status: "Draft",
    lastModified: "2026-03-17",
  },
  {
    id: 3,
    title: "Stoichiometry Fundamentals",
    topic: "Stoichiometry",
    grade: "Grade 11",
    status: "Under Review",
    lastModified: "2026-03-15",
  },
];

const aiPrompts = [
  {
    id: 1,
    name: "Exercise Generator - Chemistry",
    category: "Exercise",
    uses: 245,
    lastUpdated: "2026-03-10",
  },
  {
    id: 2,
    name: "Exam Creator - Multiple Choice",
    category: "Exam",
    uses: 189,
    lastUpdated: "2026-03-08",
  },
  {
    id: 3,
    name: "Lab Report Grading Assistant",
    category: "Grading",
    uses: 156,
    lastUpdated: "2026-03-05",
  },
];

const questionBankStats = [
  { topic: "Chemical Bonding", count: 78, difficulty: { easy: 25, medium: 35, hard: 18 } },
  { topic: "Periodic Table", count: 65, difficulty: { easy: 22, medium: 28, hard: 15 } },
  { topic: "Stoichiometry", count: 54, difficulty: { easy: 18, medium: 24, hard: 12 } },
  { topic: "Acids & Bases", count: 42, difficulty: { easy: 15, medium: 18, hard: 9 } },
];

export default function StaffDashboard() {
  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-3xl font-bold text-gray-900">Staff Dashboard</h1>
        <p className="mt-1 text-gray-600">
          Manage lesson plans, question banks, and AI prompts
        </p>
      </div>

      {/* Stats */}
      <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-4">
        <StatCard
          title="Lesson Plans"
          value="24"
          icon={FileText}
          trend={{ value: "3", isPositive: true }}
        />
        <StatCard
          title="Questions Created"
          value="239"
          icon={Database}
          description="Across all topics"
        />
        <StatCard
          title="AI Prompts"
          value="12"
          icon={Wand2}
          trend={{ value: "2", isPositive: true }}
        />
        <StatCard
          title="Pending Reviews"
          value="3"
          icon={Clock}
          description="Awaiting approval"
        />
      </div>

      {/* Quick Actions */}
      <div className="grid gap-4 md:grid-cols-3">
        <Card className="hover:shadow-md transition-shadow cursor-pointer">
          <CardContent className="p-6">
            <div className="flex items-center gap-4">
              <div className="flex h-12 w-12 items-center justify-center rounded-lg bg-indigo-50">
                <Plus className="h-6 w-6 text-indigo-600" />
              </div>
              <div>
                <h3 className="font-semibold text-gray-900">New Lesson Plan</h3>
                <p className="text-sm text-gray-600">Create lesson content</p>
              </div>
            </div>
          </CardContent>
        </Card>

        <Card className="hover:shadow-md transition-shadow cursor-pointer">
          <CardContent className="p-6">
            <div className="flex items-center gap-4">
              <div className="flex h-12 w-12 items-center justify-center rounded-lg bg-cyan-50">
                <Plus className="h-6 w-6 text-cyan-600" />
              </div>
              <div>
                <h3 className="font-semibold text-gray-900">Add Questions</h3>
                <p className="text-sm text-gray-600">Build question bank</p>
              </div>
            </div>
          </CardContent>
        </Card>

        <Card className="hover:shadow-md transition-shadow cursor-pointer">
          <CardContent className="p-6">
            <div className="flex items-center gap-4">
              <div className="flex h-12 w-12 items-center justify-center rounded-lg bg-purple-50">
                <Plus className="h-6 w-6 text-purple-600" />
              </div>
              <div>
                <h3 className="font-semibold text-gray-900">New AI Prompt</h3>
                <p className="text-sm text-gray-600">Create prompt template</p>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Lesson Plans */}
      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <div>
              <CardTitle>Recent Lesson Plans</CardTitle>
              <CardDescription>Your latest lesson plan activities</CardDescription>
            </div>
            <Button className="bg-indigo-600 hover:bg-indigo-700">
              <Plus className="mr-2 h-4 w-4" />
              Create New
            </Button>
          </div>
        </CardHeader>
        <CardContent>
          <div className="space-y-4">
            {lessonPlans.map((plan) => (
              <div
                key={plan.id}
                className="flex items-center justify-between p-4 rounded-lg border bg-white hover:bg-gray-50 transition-colors"
              >
                <div className="flex-1">
                  <h3 className="font-semibold text-gray-900 mb-2">{plan.title}</h3>
                  <div className="flex items-center gap-3">
                    <Badge variant="outline" className="text-indigo-600 border-indigo-200">
                      {plan.topic}
                    </Badge>
                    <Badge variant="outline">{plan.grade}</Badge>
                    <span className="text-sm text-gray-600">
                      Modified: {plan.lastModified}
                    </span>
                  </div>
                </div>
                <div className="flex items-center gap-3">
                  {plan.status === "Published" ? (
                    <Badge className="bg-green-50 text-green-700">
                      <CheckCircle className="mr-1 h-3 w-3" />
                      Published
                    </Badge>
                  ) : plan.status === "Draft" ? (
                    <Badge variant="outline" className="text-gray-600">
                      Draft
                    </Badge>
                  ) : (
                    <Badge className="bg-yellow-50 text-yellow-700">
                      <Clock className="mr-1 h-3 w-3" />
                      Under Review
                    </Badge>
                  )}
                  <Button variant="outline" size="sm">Edit</Button>
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>

      {/* Question Bank Overview */}
      <Card>
        <CardHeader>
          <CardTitle>Question Bank Overview</CardTitle>
          <CardDescription>Distribution by topic and difficulty</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="space-y-4">
            {questionBankStats.map((stat) => (
              <div key={stat.topic} className="space-y-2">
                <div className="flex items-center justify-between">
                  <div>
                    <h4 className="font-semibold text-gray-900">{stat.topic}</h4>
                    <p className="text-sm text-gray-600">{stat.count} questions total</p>
                  </div>
                  <Button variant="outline" size="sm">View All</Button>
                </div>
                <div className="grid grid-cols-3 gap-2">
                  <div className="p-3 rounded-lg bg-green-50 border border-green-200">
                    <p className="text-xs text-green-700 font-medium">Easy</p>
                    <p className="text-lg font-bold text-green-900">{stat.difficulty.easy}</p>
                  </div>
                  <div className="p-3 rounded-lg bg-yellow-50 border border-yellow-200">
                    <p className="text-xs text-yellow-700 font-medium">Medium</p>
                    <p className="text-lg font-bold text-yellow-900">{stat.difficulty.medium}</p>
                  </div>
                  <div className="p-3 rounded-lg bg-red-50 border border-red-200">
                    <p className="text-xs text-red-700 font-medium">Hard</p>
                    <p className="text-lg font-bold text-red-900">{stat.difficulty.hard}</p>
                  </div>
                </div>
                <Progress value={(stat.count / 300) * 100} className="h-2" />
              </div>
            ))}
          </div>
        </CardContent>
      </Card>

      {/* AI Prompt Templates */}
      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <div>
              <CardTitle>AI Prompt Templates</CardTitle>
              <CardDescription>Manage your prompt templates</CardDescription>
            </div>
            <Button className="bg-purple-600 hover:bg-purple-700">
              <Plus className="mr-2 h-4 w-4" />
              New Template
            </Button>
          </div>
        </CardHeader>
        <CardContent>
          <div className="space-y-4">
            {aiPrompts.map((prompt) => (
              <div
                key={prompt.id}
                className="flex items-center justify-between p-4 rounded-lg border bg-white hover:bg-gray-50 transition-colors"
              >
                <div className="flex-1">
                  <h3 className="font-semibold text-gray-900 mb-1">{prompt.name}</h3>
                  <div className="flex items-center gap-3 text-sm text-gray-600">
                    <Badge variant="outline" className="text-purple-600 border-purple-200">
                      {prompt.category}
                    </Badge>
                    <span>{prompt.uses} uses</span>
                    <span>Updated: {prompt.lastUpdated}</span>
                  </div>
                </div>
                <div className="flex gap-2">
                  <Button variant="outline" size="sm">Edit</Button>
                  <Button variant="outline" size="sm">Duplicate</Button>
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    </div>
  );
}

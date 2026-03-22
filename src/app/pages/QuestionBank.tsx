import { useState } from "react";
import { Plus, Search, Filter, Edit, Trash2, Copy } from "lucide-react";
import { Button } from "../components/ui/button";
import { Input } from "../components/ui/input";
import { Card, CardContent, CardHeader, CardTitle } from "../components/ui/card";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "../components/ui/select";
import { Badge } from "../components/ui/badge";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "../components/ui/dialog";
import { Label } from "../components/ui/label";
import { Textarea } from "../components/ui/textarea";
import { toast } from "sonner";

interface Question {
  id: string;
  question: string;
  topic: string;
  difficulty: "Easy" | "Medium" | "Hard";
  type: "Multiple Choice" | "Short Answer" | "Essay";
  answer: string;
  options?: string[];
  createdAt: string;
}

const mockQuestions: Question[] = [
  {
    id: "1",
    question: "What is the atomic number of Carbon?",
    topic: "Periodic Table",
    difficulty: "Easy",
    type: "Multiple Choice",
    answer: "6",
    options: ["4", "6", "8", "12"],
    createdAt: "2026-03-15",
  },
  {
    id: "2",
    question: "Explain the difference between ionic and covalent bonding.",
    topic: "Chemical Bonding",
    difficulty: "Medium",
    type: "Essay",
    answer: "Ionic bonding involves electron transfer...",
    createdAt: "2026-03-14",
  },
  {
    id: "3",
    question: "Balance the equation: H₂ + O₂ → H₂O",
    topic: "Chemical Reactions",
    difficulty: "Medium",
    type: "Short Answer",
    answer: "2H₂ + O₂ → 2H₂O",
    createdAt: "2026-03-13",
  },
  {
    id: "4",
    question: "What is Avogadro's number?",
    topic: "Stoichiometry",
    difficulty: "Easy",
    type: "Multiple Choice",
    answer: "6.022 × 10²³",
    options: ["3.14 × 10²³", "6.022 × 10²³", "1.60 × 10⁻¹⁹", "9.81 × 10⁸"],
    createdAt: "2026-03-12",
  },
  {
    id: "5",
    question: "Describe the process of electrolysis in detail.",
    topic: "Electrochemistry",
    difficulty: "Hard",
    type: "Essay",
    answer: "Electrolysis is a process...",
    createdAt: "2026-03-10",
  },
];

export default function QuestionBank() {
  const [questions, setQuestions] = useState(mockQuestions);
  const [searchTerm, setSearchTerm] = useState("");
  const [filterTopic, setFilterTopic] = useState("all");
  const [filterDifficulty, setFilterDifficulty] = useState("all");
  const [isAddDialogOpen, setIsAddDialogOpen] = useState(false);

  const filteredQuestions = questions.filter((q) => {
    const matchesSearch = q.question.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesTopic = filterTopic === "all" || q.topic === filterTopic;
    const matchesDifficulty = filterDifficulty === "all" || q.difficulty === filterDifficulty;
    return matchesSearch && matchesTopic && matchesDifficulty;
  });

  const handleDeleteQuestion = (id: string) => {
    setQuestions(questions.filter((q) => q.id !== id));
    toast.success("Question deleted successfully");
  };

  const handleDuplicateQuestion = (question: Question) => {
    const newQuestion = {
      ...question,
      id: Date.now().toString(),
      createdAt: new Date().toISOString().split("T")[0],
    };
    setQuestions([newQuestion, ...questions]);
    toast.success("Question duplicated successfully");
  };

  const topics = Array.from(new Set(mockQuestions.map((q) => q.topic)));

  const difficultyColors = {
    Easy: "bg-green-50 text-green-700 border-green-200",
    Medium: "bg-yellow-50 text-yellow-700 border-yellow-200",
    Hard: "bg-red-50 text-red-700 border-red-200",
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Question Bank</h1>
          <p className="mt-1 text-gray-600">
            Manage and organize your chemistry questions
          </p>
        </div>
        <Dialog open={isAddDialogOpen} onOpenChange={setIsAddDialogOpen}>
          <DialogTrigger asChild>
            <Button className="bg-indigo-600 hover:bg-indigo-700">
              <Plus className="mr-2 h-4 w-4" />
              Add Question
            </Button>
          </DialogTrigger>
          <DialogContent className="max-w-2xl">
            <DialogHeader>
              <DialogTitle>Add New Question</DialogTitle>
              <DialogDescription>
                Create a new question for your question bank
              </DialogDescription>
            </DialogHeader>
            <div className="space-y-4">
              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-2">
                  <Label>Topic</Label>
                  <Select>
                    <SelectTrigger>
                      <SelectValue placeholder="Select topic" />
                    </SelectTrigger>
                    <SelectContent>
                      {topics.map((topic) => (
                        <SelectItem key={topic} value={topic}>
                          {topic}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>
                <div className="space-y-2">
                  <Label>Difficulty</Label>
                  <Select>
                    <SelectTrigger>
                      <SelectValue placeholder="Select difficulty" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="Easy">Easy</SelectItem>
                      <SelectItem value="Medium">Medium</SelectItem>
                      <SelectItem value="Hard">Hard</SelectItem>
                    </SelectContent>
                  </Select>
                </div>
              </div>
              <div className="space-y-2">
                <Label>Question Type</Label>
                <Select>
                  <SelectTrigger>
                    <SelectValue placeholder="Select type" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="Multiple Choice">Multiple Choice</SelectItem>
                    <SelectItem value="Short Answer">Short Answer</SelectItem>
                    <SelectItem value="Essay">Essay</SelectItem>
                  </SelectContent>
                </Select>
              </div>
              <div className="space-y-2">
                <Label>Question</Label>
                <Textarea placeholder="Enter your question here..." rows={4} />
              </div>
              <div className="space-y-2">
                <Label>Answer</Label>
                <Textarea placeholder="Enter the correct answer..." rows={3} />
              </div>
            </div>
            <DialogFooter>
              <Button variant="outline" onClick={() => setIsAddDialogOpen(false)}>
                Cancel
              </Button>
              <Button
                className="bg-indigo-600 hover:bg-indigo-700"
                onClick={() => {
                  setIsAddDialogOpen(false);
                  toast.success("Question added successfully");
                }}
              >
                Add Question
              </Button>
            </DialogFooter>
          </DialogContent>
        </Dialog>
      </div>

      {/* Filters */}
      <Card>
        <CardContent className="p-6">
          <div className="grid gap-4 md:grid-cols-3">
            <div className="relative">
              <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-gray-400" />
              <Input
                placeholder="Search questions..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-9"
              />
            </div>
            <Select value={filterTopic} onValueChange={setFilterTopic}>
              <SelectTrigger>
                <div className="flex items-center gap-2">
                  <Filter className="h-4 w-4" />
                  <SelectValue placeholder="All Topics" />
                </div>
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">All Topics</SelectItem>
                {topics.map((topic) => (
                  <SelectItem key={topic} value={topic}>
                    {topic}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
            <Select value={filterDifficulty} onValueChange={setFilterDifficulty}>
              <SelectTrigger>
                <SelectValue placeholder="All Difficulties" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">All Difficulties</SelectItem>
                <SelectItem value="Easy">Easy</SelectItem>
                <SelectItem value="Medium">Medium</SelectItem>
                <SelectItem value="Hard">Hard</SelectItem>
              </SelectContent>
            </Select>
          </div>
        </CardContent>
      </Card>

      {/* Stats */}
      <div className="grid gap-4 md:grid-cols-4">
        <Card>
          <CardContent className="p-4">
            <p className="text-sm text-gray-600">Total Questions</p>
            <p className="text-2xl font-bold text-gray-900">{questions.length}</p>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <p className="text-sm text-gray-600">Easy</p>
            <p className="text-2xl font-bold text-green-600">
              {questions.filter((q) => q.difficulty === "Easy").length}
            </p>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <p className="text-sm text-gray-600">Medium</p>
            <p className="text-2xl font-bold text-yellow-600">
              {questions.filter((q) => q.difficulty === "Medium").length}
            </p>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <p className="text-sm text-gray-600">Hard</p>
            <p className="text-2xl font-bold text-red-600">
              {questions.filter((q) => q.difficulty === "Hard").length}
            </p>
          </CardContent>
        </Card>
      </div>

      {/* Questions List */}
      <div className="space-y-4">
        {filteredQuestions.map((question) => (
          <Card key={question.id} className="hover:shadow-md transition-shadow">
            <CardContent className="p-6">
              <div className="flex items-start justify-between gap-4">
                <div className="flex-1">
                  <div className="flex items-center gap-2 mb-2">
                    <Badge variant="outline" className="text-indigo-600 border-indigo-200">
                      {question.topic}
                    </Badge>
                    <Badge variant="outline" className={difficultyColors[question.difficulty]}>
                      {question.difficulty}
                    </Badge>
                    <Badge variant="outline" className="text-gray-600">
                      {question.type}
                    </Badge>
                  </div>
                  <h3 className="text-lg font-semibold text-gray-900 mb-2">
                    {question.question}
                  </h3>
                  {question.options && (
                    <div className="grid grid-cols-2 gap-2 mt-3">
                      {question.options.map((option, idx) => (
                        <div
                          key={idx}
                          className={`rounded-lg border p-2 text-sm ${
                            option === question.answer
                              ? "border-green-500 bg-green-50 text-green-900"
                              : "border-gray-200 bg-gray-50 text-gray-700"
                          }`}
                        >
                          {String.fromCharCode(65 + idx)}. {option}
                        </div>
                      ))}
                    </div>
                  )}
                  <p className="text-xs text-gray-500 mt-3">
                    Created: {question.createdAt}
                  </p>
                </div>
                <div className="flex gap-2">
                  <Button
                    variant="ghost"
                    size="icon"
                    onClick={() => handleDuplicateQuestion(question)}
                  >
                    <Copy className="h-4 w-4" />
                  </Button>
                  <Button variant="ghost" size="icon">
                    <Edit className="h-4 w-4" />
                  </Button>
                  <Button
                    variant="ghost"
                    size="icon"
                    onClick={() => handleDeleteQuestion(question.id)}
                  >
                    <Trash2 className="h-4 w-4 text-red-600" />
                  </Button>
                </div>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  );
}

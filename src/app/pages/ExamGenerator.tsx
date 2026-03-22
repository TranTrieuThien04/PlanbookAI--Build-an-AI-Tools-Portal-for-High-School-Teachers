import { useState } from "react";
import { FileText, Download, Eye, Shuffle, Plus, X } from "lucide-react";
import { Button } from "../components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "../components/ui/card";
import { Label } from "../components/ui/label";
import { Input } from "../components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "../components/ui/select";
import { Checkbox } from "../components/ui/checkbox";
import { Badge } from "../components/ui/badge";
import { Switch } from "../components/ui/switch";
import { toast } from "sonner";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "../components/ui/dialog";

interface SelectedTopic {
  topic: string;
  count: number;
}

export default function ExamGenerator() {
  const [examTitle, setExamTitle] = useState("");
  const [selectedTopics, setSelectedTopics] = useState<SelectedTopic[]>([]);
  const [totalQuestions, setTotalQuestions] = useState(20);
  const [randomizeQuestions, setRandomizeQuestions] = useState(true);
  const [includeAnswerKey, setIncludeAnswerKey] = useState(true);
  const [numberOfVersions, setNumberOfVersions] = useState(1);
  const [showPreview, setShowPreview] = useState(false);

  const availableTopics = [
    "Periodic Table",
    "Chemical Bonding",
    "Chemical Reactions",
    "Stoichiometry",
    "Acids and Bases",
    "Electrochemistry",
    "Thermodynamics",
    "Organic Chemistry",
  ];

  const handleAddTopic = (topic: string) => {
    if (!selectedTopics.find((t) => t.topic === topic)) {
      setSelectedTopics([...selectedTopics, { topic, count: 5 }]);
    }
  };

  const handleRemoveTopic = (topic: string) => {
    setSelectedTopics(selectedTopics.filter((t) => t.topic !== topic));
  };

  const handleUpdateCount = (topic: string, count: number) => {
    setSelectedTopics(
      selectedTopics.map((t) => (t.topic === topic ? { ...t, count } : t))
    );
  };

  const handleGenerateExam = () => {
    if (!examTitle || selectedTopics.length === 0) {
      toast.error("Please enter exam title and select topics");
      return;
    }
    setShowPreview(true);
    toast.success("Exam generated successfully!");
  };

  const handleDownloadExam = () => {
    toast.success("Exam downloaded as PDF!");
  };

  const totalSelectedQuestions = selectedTopics.reduce((sum, t) => sum + t.count, 0);

  // Mock exam preview data
  const mockExamQuestions = [
    {
      id: 1,
      question: "What is the atomic number of Oxygen?",
      options: ["6", "7", "8", "9"],
      answer: "8",
    },
    {
      id: 2,
      question: "Which type of bond involves the sharing of electrons?",
      options: ["Ionic", "Covalent", "Metallic", "Hydrogen"],
      answer: "Covalent",
    },
    {
      id: 3,
      question: "What is the chemical formula for water?",
      options: ["H₂O", "CO₂", "O₂", "H₂O₂"],
      answer: "H₂O",
    },
  ];

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-3xl font-bold text-gray-900">Exam Generator</h1>
        <p className="mt-1 text-gray-600">
          Create multiple choice exams with customizable topics and settings
        </p>
      </div>

      <div className="grid gap-6 lg:grid-cols-3">
        {/* Configuration Panel */}
        <div className="lg:col-span-1 space-y-6">
          {/* Basic Settings */}
          <Card>
            <CardHeader>
              <CardTitle>Basic Settings</CardTitle>
              <CardDescription>Configure your exam details</CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="space-y-2">
                <Label htmlFor="examTitle">Exam Title</Label>
                <Input
                  id="examTitle"
                  placeholder="e.g., Chemistry Midterm Exam"
                  value={examTitle}
                  onChange={(e) => setExamTitle(e.target.value)}
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="totalQuestions">Total Questions</Label>
                <Input
                  id="totalQuestions"
                  type="number"
                  min="5"
                  max="100"
                  value={totalQuestions}
                  onChange={(e) => setTotalQuestions(Number(e.target.value))}
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="versions">Number of Versions</Label>
                <Select
                  value={numberOfVersions.toString()}
                  onValueChange={(v) => setNumberOfVersions(Number(v))}
                >
                  <SelectTrigger id="versions">
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="1">1 Version</SelectItem>
                    <SelectItem value="2">2 Versions</SelectItem>
                    <SelectItem value="3">3 Versions</SelectItem>
                    <SelectItem value="4">4 Versions</SelectItem>
                  </SelectContent>
                </Select>
              </div>
            </CardContent>
          </Card>

          {/* Options */}
          <Card>
            <CardHeader>
              <CardTitle>Options</CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="flex items-center justify-between">
                <div className="space-y-0.5">
                  <Label>Randomize Questions</Label>
                  <p className="text-xs text-gray-500">
                    Shuffle question order
                  </p>
                </div>
                <Switch
                  checked={randomizeQuestions}
                  onCheckedChange={setRandomizeQuestions}
                />
              </div>

              <div className="flex items-center justify-between">
                <div className="space-y-0.5">
                  <Label>Include Answer Key</Label>
                  <p className="text-xs text-gray-500">
                    Generate separate answer sheet
                  </p>
                </div>
                <Switch
                  checked={includeAnswerKey}
                  onCheckedChange={setIncludeAnswerKey}
                />
              </div>
            </CardContent>
          </Card>
        </div>

        {/* Topic Selection Panel */}
        <div className="lg:col-span-2 space-y-6">
          {/* Add Topics */}
          <Card>
            <CardHeader>
              <CardTitle>Select Topics</CardTitle>
              <CardDescription>
                Choose topics and specify question count for each
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="space-y-2">
                <Label>Available Topics</Label>
                <div className="flex flex-wrap gap-2">
                  {availableTopics.map((topic) => {
                    const isSelected = selectedTopics.find((t) => t.topic === topic);
                    return (
                      <Button
                        key={topic}
                        variant={isSelected ? "default" : "outline"}
                        size="sm"
                        onClick={() =>
                          isSelected
                            ? handleRemoveTopic(topic)
                            : handleAddTopic(topic)
                        }
                        className={
                          isSelected
                            ? "bg-indigo-600 hover:bg-indigo-700"
                            : ""
                        }
                      >
                        {isSelected && <X className="mr-1 h-3 w-3" />}
                        {topic}
                      </Button>
                    );
                  })}
                </div>
              </div>

              {selectedTopics.length > 0 && (
                <div className="space-y-3 pt-4 border-t">
                  <Label>Question Distribution</Label>
                  {selectedTopics.map((topic) => (
                    <div
                      key={topic.topic}
                      className="flex items-center justify-between p-3 rounded-lg border bg-gray-50"
                    >
                      <div className="flex-1">
                        <p className="font-medium text-gray-900">{topic.topic}</p>
                      </div>
                      <div className="flex items-center gap-3">
                        <Input
                          type="number"
                          min="1"
                          max="50"
                          value={topic.count}
                          onChange={(e) =>
                            handleUpdateCount(topic.topic, Number(e.target.value))
                          }
                          className="w-20"
                        />
                        <span className="text-sm text-gray-600">questions</span>
                        <Button
                          variant="ghost"
                          size="icon"
                          onClick={() => handleRemoveTopic(topic.topic)}
                        >
                          <X className="h-4 w-4 text-red-600" />
                        </Button>
                      </div>
                    </div>
                  ))}
                  <div className="flex items-center justify-between p-3 rounded-lg border bg-indigo-50 border-indigo-200">
                    <p className="font-semibold text-indigo-900">Total</p>
                    <p className="font-bold text-indigo-900">
                      {totalSelectedQuestions} questions
                    </p>
                  </div>
                </div>
              )}
            </CardContent>
          </Card>

          {/* Actions */}
          <Card>
            <CardContent className="p-6">
              <div className="flex items-center gap-3">
                <Button
                  className="flex-1 bg-indigo-600 hover:bg-indigo-700"
                  onClick={handleGenerateExam}
                >
                  <FileText className="mr-2 h-4 w-4" />
                  Generate Exam
                </Button>
                <Button variant="outline" onClick={() => setShowPreview(true)}>
                  <Eye className="mr-2 h-4 w-4" />
                  Preview
                </Button>
                <Button variant="outline" onClick={handleDownloadExam}>
                  <Download className="mr-2 h-4 w-4" />
                  Download
                </Button>
              </div>
            </CardContent>
          </Card>

          {/* Summary */}
          <Card>
            <CardHeader>
              <CardTitle>Exam Summary</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="grid grid-cols-2 gap-4">
                <div className="p-4 rounded-lg bg-gray-50">
                  <p className="text-sm text-gray-600">Topics Selected</p>
                  <p className="text-2xl font-bold text-gray-900">
                    {selectedTopics.length}
                  </p>
                </div>
                <div className="p-4 rounded-lg bg-gray-50">
                  <p className="text-sm text-gray-600">Total Questions</p>
                  <p className="text-2xl font-bold text-gray-900">
                    {totalSelectedQuestions}
                  </p>
                </div>
                <div className="p-4 rounded-lg bg-gray-50">
                  <p className="text-sm text-gray-600">Versions</p>
                  <p className="text-2xl font-bold text-gray-900">
                    {numberOfVersions}
                  </p>
                </div>
                <div className="p-4 rounded-lg bg-gray-50">
                  <p className="text-sm text-gray-600">Randomize</p>
                  <p className="text-2xl font-bold text-gray-900">
                    {randomizeQuestions ? "Yes" : "No"}
                  </p>
                </div>
              </div>
            </CardContent>
          </Card>
        </div>
      </div>

      {/* Preview Dialog */}
      <Dialog open={showPreview} onOpenChange={setShowPreview}>
        <DialogContent className="max-w-3xl max-h-[80vh] overflow-y-auto">
          <DialogHeader>
            <DialogTitle>{examTitle || "Exam Preview"}</DialogTitle>
            <DialogDescription>Preview of generated exam</DialogDescription>
          </DialogHeader>
          <div className="space-y-6 mt-4">
            <div className="flex items-center justify-between p-4 rounded-lg bg-gray-50">
              <div>
                <p className="text-sm text-gray-600">Total Questions</p>
                <p className="text-lg font-semibold">
                  {mockExamQuestions.length}
                </p>
              </div>
              <div>
                <p className="text-sm text-gray-600">Version</p>
                <p className="text-lg font-semibold">A</p>
              </div>
              <div>
                <p className="text-sm text-gray-600">Points</p>
                <p className="text-lg font-semibold">100</p>
              </div>
            </div>

            {mockExamQuestions.map((q, index) => (
              <div key={q.id} className="space-y-3">
                <p className="font-semibold">
                  {index + 1}. {q.question}
                </p>
                <div className="space-y-2 pl-4">
                  {q.options.map((option, idx) => (
                    <div key={idx} className="flex items-center gap-2">
                      <div className="w-6 h-6 rounded-full border-2 border-gray-300 flex items-center justify-center text-xs">
                        {String.fromCharCode(65 + idx)}
                      </div>
                      <span>{option}</span>
                      {includeAnswerKey && option === q.answer && (
                        <Badge className="bg-green-600 ml-2">Correct</Badge>
                      )}
                    </div>
                  ))}
                </div>
              </div>
            ))}
          </div>
        </DialogContent>
      </Dialog>
    </div>
  );
}

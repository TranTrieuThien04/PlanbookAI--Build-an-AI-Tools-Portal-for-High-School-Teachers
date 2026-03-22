import { useState } from "react";
import { Wand2, Settings, Sparkles, Download, Save } from "lucide-react";
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
import { Textarea } from "../components/ui/textarea";
import { Slider } from "../components/ui/slider";
import { Badge } from "../components/ui/badge";
import { Progress } from "../components/ui/progress";
import { toast } from "sonner";

interface GeneratedQuestion {
  id: number;
  question: string;
  options: string[];
  answer: string;
  explanation: string;
}

export default function ExerciseGenerator() {
  const [topic, setTopic] = useState("");
  const [difficulty, setDifficulty] = useState("Medium");
  const [questionCount, setQuestionCount] = useState([5]);
  const [additionalContext, setAdditionalContext] = useState("");
  const [isGenerating, setIsGenerating] = useState(false);
  const [progress, setProgress] = useState(0);
  const [generatedQuestions, setGeneratedQuestions] = useState<GeneratedQuestion[]>([]);

  const handleGenerate = async () => {
    setIsGenerating(true);
    setProgress(0);
    setGeneratedQuestions([]);

    // Simulate AI generation with progress
    const interval = setInterval(() => {
      setProgress((prev) => {
        if (prev >= 100) {
          clearInterval(interval);
          return 100;
        }
        return prev + 10;
      });
    }, 200);

    // Simulate API call
    setTimeout(() => {
      clearInterval(interval);
      setProgress(100);
      
      // Mock generated questions
      const mockQuestions: GeneratedQuestion[] = [
        {
          id: 1,
          question: `What is the primary characteristic of ${topic || "chemical reactions"}?`,
          options: [
            "Formation of new substances",
            "Change in color only",
            "Temperature remains constant",
            "No energy change occurs",
          ],
          answer: "Formation of new substances",
          explanation: "Chemical reactions involve the breaking and forming of chemical bonds, resulting in new substances with different properties.",
        },
        {
          id: 2,
          question: `In the context of ${topic || "chemistry"}, which statement is most accurate?`,
          options: [
            "Atoms are created during reactions",
            "Mass is conserved in reactions",
            "Energy is always released",
            "Products equal reactants in volume",
          ],
          answer: "Mass is conserved in reactions",
          explanation: "According to the Law of Conservation of Mass, the total mass of reactants equals the total mass of products.",
        },
        {
          id: 3,
          question: `What role does activation energy play in ${topic || "chemical processes"}?`,
          options: [
            "It prevents all reactions",
            "It's the energy needed to start a reaction",
            "It's only present in endothermic reactions",
            "It equals the final energy state",
          ],
          answer: "It's the energy needed to start a reaction",
          explanation: "Activation energy is the minimum energy required for reactants to overcome the energy barrier and form products.",
        },
        {
          id: 4,
          question: `How does temperature affect ${topic || "reaction rates"}?`,
          options: [
            "No effect on reaction speed",
            "Higher temperature increases reaction rate",
            "Lower temperature speeds reactions",
            "Temperature only affects color",
          ],
          answer: "Higher temperature increases reaction rate",
          explanation: "Increased temperature provides more kinetic energy to molecules, leading to more frequent and energetic collisions.",
        },
        {
          id: 5,
          question: `Which factor is most important when considering ${topic || "chemical equilibrium"}?`,
          options: [
            "Reaction never completes",
            "Forward and reverse rates are equal",
            "Only products remain",
            "Temperature has no effect",
          ],
          answer: "Forward and reverse rates are equal",
          explanation: "At equilibrium, the rate of the forward reaction equals the rate of the reverse reaction, resulting in constant concentrations.",
        },
      ].slice(0, questionCount[0]);

      setGeneratedQuestions(mockQuestions);
      setIsGenerating(false);
      toast.success(`Successfully generated ${questionCount[0]} questions!`);
    }, 2500);
  };

  const handleSaveToBank = () => {
    toast.success("Questions saved to Question Bank!");
  };

  const handleDownload = () => {
    toast.success("Exercises downloaded as PDF!");
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-3xl font-bold text-gray-900">Exercise Generator</h1>
        <p className="mt-1 text-gray-600">
          Generate AI-powered exercises tailored to your curriculum
        </p>
      </div>

      <div className="grid gap-6 lg:grid-cols-3">
        {/* Configuration Panel */}
        <div className="lg:col-span-1">
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Settings className="h-5 w-5" />
                Configuration
              </CardTitle>
              <CardDescription>
                Customize your exercise parameters
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-6">
              <div className="space-y-2">
                <Label htmlFor="topic">Topic</Label>
                <Input
                  id="topic"
                  placeholder="e.g., Chemical Bonding, Stoichiometry"
                  value={topic}
                  onChange={(e) => setTopic(e.target.value)}
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="difficulty">Difficulty Level</Label>
                <Select value={difficulty} onValueChange={setDifficulty}>
                  <SelectTrigger id="difficulty">
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="Easy">Easy</SelectItem>
                    <SelectItem value="Medium">Medium</SelectItem>
                    <SelectItem value="Hard">Hard</SelectItem>
                  </SelectContent>
                </Select>
              </div>

              <div className="space-y-2">
                <Label>Number of Questions: {questionCount[0]}</Label>
                <Slider
                  value={questionCount}
                  onValueChange={setQuestionCount}
                  min={3}
                  max={20}
                  step={1}
                  className="mt-2"
                />
                <p className="text-xs text-gray-500">Range: 3-20 questions</p>
              </div>

              <div className="space-y-2">
                <Label htmlFor="context">Additional Context (Optional)</Label>
                <Textarea
                  id="context"
                  placeholder="Add specific requirements, learning objectives, or context..."
                  rows={4}
                  value={additionalContext}
                  onChange={(e) => setAdditionalContext(e.target.value)}
                />
              </div>

              <Button
                className="w-full bg-indigo-600 hover:bg-indigo-700"
                onClick={handleGenerate}
                disabled={isGenerating || !topic}
              >
                {isGenerating ? (
                  <>
                    <Sparkles className="mr-2 h-4 w-4 animate-spin" />
                    Generating...
                  </>
                ) : (
                  <>
                    <Wand2 className="mr-2 h-4 w-4" />
                    Generate Exercises
                  </>
                )}
              </Button>

              {isGenerating && (
                <div className="space-y-2">
                  <div className="flex items-center justify-between text-sm">
                    <span className="text-gray-600">Generating...</span>
                    <span className="font-medium text-indigo-600">{progress}%</span>
                  </div>
                  <Progress value={progress} className="h-2" />
                </div>
              )}
            </CardContent>
          </Card>
        </div>

        {/* Generated Questions Panel */}
        <div className="lg:col-span-2">
          {generatedQuestions.length === 0 ? (
            <Card className="h-full">
              <CardContent className="flex flex-col items-center justify-center h-full min-h-[500px] text-center">
                <div className="w-20 h-20 rounded-full bg-indigo-50 flex items-center justify-center mb-4">
                  <Wand2 className="h-10 w-10 text-indigo-600" />
                </div>
                <h3 className="text-lg font-semibold text-gray-900 mb-2">
                  Ready to Generate Exercises
                </h3>
                <p className="text-gray-600 max-w-md">
                  Configure your exercise parameters on the left and click "Generate Exercises" to create AI-powered questions.
                </p>
              </CardContent>
            </Card>
          ) : (
            <div className="space-y-4">
              {/* Action Bar */}
              <Card>
                <CardContent className="p-4">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-2">
                      <Badge variant="outline" className="bg-green-50 text-green-700 border-green-200">
                        {generatedQuestions.length} Questions Generated
                      </Badge>
                      <Badge variant="outline" className="text-indigo-600 border-indigo-200">
                        {difficulty}
                      </Badge>
                    </div>
                    <div className="flex gap-2">
                      <Button variant="outline" size="sm" onClick={handleSaveToBank}>
                        <Save className="mr-2 h-4 w-4" />
                        Save to Bank
                      </Button>
                      <Button size="sm" onClick={handleDownload} className="bg-indigo-600 hover:bg-indigo-700">
                        <Download className="mr-2 h-4 w-4" />
                        Download PDF
                      </Button>
                    </div>
                  </div>
                </CardContent>
              </Card>

              {/* Questions List */}
              {generatedQuestions.map((q, index) => (
                <Card key={q.id}>
                  <CardHeader>
                    <CardTitle className="text-base">
                      Question {index + 1}
                    </CardTitle>
                  </CardHeader>
                  <CardContent className="space-y-4">
                    <p className="text-gray-900 font-medium">{q.question}</p>
                    
                    <div className="grid gap-2">
                      {q.options.map((option, idx) => (
                        <div
                          key={idx}
                          className={`rounded-lg border p-3 text-sm transition-colors ${
                            option === q.answer
                              ? "border-green-500 bg-green-50"
                              : "border-gray-200 bg-gray-50"
                          }`}
                        >
                          <span className="font-medium">
                            {String.fromCharCode(65 + idx)}.
                          </span>{" "}
                          {option}
                          {option === q.answer && (
                            <Badge className="ml-2 bg-green-600 text-white">
                              Correct
                            </Badge>
                          )}
                        </div>
                      ))}
                    </div>

                    <div className="rounded-lg bg-blue-50 border border-blue-200 p-3">
                      <p className="text-sm font-medium text-blue-900 mb-1">
                        Explanation:
                      </p>
                      <p className="text-sm text-blue-800">{q.explanation}</p>
                    </div>
                  </CardContent>
                </Card>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

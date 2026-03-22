import { useState } from "react";
import { Upload, ScanLine, CheckCircle2, XCircle, FileCheck, Download } from "lucide-react";
import { Button } from "../components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "../components/ui/card";
import { Progress } from "../components/ui/progress";
import { Badge } from "../components/ui/badge";
import { toast } from "sonner";
import { ImageWithFallback } from "../components/figma/ImageWithFallback";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "../components/ui/table";

interface GradingResult {
  studentId: string;
  studentName: string;
  totalQuestions: number;
  correctAnswers: number;
  score: number;
  answers: Array<{
    question: number;
    studentAnswer: string;
    correctAnswer: string;
    isCorrect: boolean;
  }>;
}

export default function OCRGrading() {
  const [isProcessing, setIsProcessing] = useState(false);
  const [progress, setProgress] = useState(0);
  const [uploadedFile, setUploadedFile] = useState<File | null>(null);
  const [gradingResults, setGradingResults] = useState<GradingResult[]>([]);
  const [selectedStudent, setSelectedStudent] = useState<GradingResult | null>(null);

  const handleFileUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      setUploadedFile(file);
      toast.success("File uploaded successfully");
    }
  };

  const handleProcessOCR = () => {
    if (!uploadedFile) {
      toast.error("Please upload a file first");
      return;
    }

    setIsProcessing(true);
    setProgress(0);

    // Simulate OCR processing
    const interval = setInterval(() => {
      setProgress((prev) => {
        if (prev >= 100) {
          clearInterval(interval);
          return 100;
        }
        return prev + 5;
      });
    }, 150);

    setTimeout(() => {
      clearInterval(interval);
      setProgress(100);
      setIsProcessing(false);

      // Mock grading results
      const mockResults: GradingResult[] = [
        {
          studentId: "S001",
          studentName: "Emma Wilson",
          totalQuestions: 20,
          correctAnswers: 18,
          score: 90,
          answers: Array.from({ length: 20 }, (_, i) => ({
            question: i + 1,
            studentAnswer: ["A", "B", "C", "D"][Math.floor(Math.random() * 4)],
            correctAnswer: ["A", "B", "C", "D"][Math.floor(Math.random() * 4)],
            isCorrect: Math.random() > 0.1,
          })),
        },
        {
          studentId: "S002",
          studentName: "James Rodriguez",
          totalQuestions: 20,
          correctAnswers: 16,
          score: 80,
          answers: Array.from({ length: 20 }, (_, i) => ({
            question: i + 1,
            studentAnswer: ["A", "B", "C", "D"][Math.floor(Math.random() * 4)],
            correctAnswer: ["A", "B", "C", "D"][Math.floor(Math.random() * 4)],
            isCorrect: Math.random() > 0.2,
          })),
        },
        {
          studentId: "S003",
          studentName: "Sophia Chen",
          totalQuestions: 20,
          correctAnswers: 19,
          score: 95,
          answers: Array.from({ length: 20 }, (_, i) => ({
            question: i + 1,
            studentAnswer: ["A", "B", "C", "D"][Math.floor(Math.random() * 4)],
            correctAnswer: ["A", "B", "C", "D"][Math.floor(Math.random() * 4)],
            isCorrect: Math.random() > 0.05,
          })),
        },
        {
          studentId: "S004",
          studentName: "Michael Zhang",
          totalQuestions: 20,
          correctAnswers: 15,
          score: 75,
          answers: Array.from({ length: 20 }, (_, i) => ({
            question: i + 1,
            studentAnswer: ["A", "B", "C", "D"][Math.floor(Math.random() * 4)],
            correctAnswer: ["A", "B", "C", "D"][Math.floor(Math.random() * 4)],
            isCorrect: Math.random() > 0.25,
          })),
        },
      ];

      setGradingResults(mockResults);
      toast.success("Grading completed successfully!");
    }, 3000);
  };

  const handleExportResults = () => {
    toast.success("Results exported as CSV!");
  };

  const averageScore =
    gradingResults.length > 0
      ? Math.round(
          gradingResults.reduce((sum, r) => sum + r.score, 0) / gradingResults.length
        )
      : 0;

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-3xl font-bold text-gray-900">OCR Grading System</h1>
        <p className="mt-1 text-gray-600">
          Upload scanned answer sheets for automated grading
        </p>
      </div>

      <div className="grid gap-6 lg:grid-cols-3">
        {/* Upload Panel */}
        <div className="lg:col-span-1">
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Upload className="h-5 w-5" />
                Upload Answer Sheets
              </CardTitle>
              <CardDescription>
                Upload scanned PDF or images
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              {/* Upload Area */}
              <div className="border-2 border-dashed border-gray-300 rounded-lg p-8 text-center hover:border-indigo-400 transition-colors cursor-pointer">
                <input
                  type="file"
                  id="file-upload"
                  className="hidden"
                  accept=".pdf,.jpg,.jpeg,.png"
                  onChange={handleFileUpload}
                />
                <label htmlFor="file-upload" className="cursor-pointer">
                  <ImageWithFallback
                    src="https://images.unsplash.com/photo-1554224155-cfa08c2a758f?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxzY2FubmluZyUyMGRvY3VtZW50JTIwcGFwZXIlMjB0ZXN0fGVufDF8fHx8MTc3Mzk5Mjc3Nnww&ixlib=rb-4.1.0&q=80&w=1080"
                    alt="Upload"
                    className="mx-auto h-24 w-24 object-cover rounded-lg mb-4 opacity-50"
                  />
                  <p className="font-medium text-gray-900">Click to upload</p>
                  <p className="text-sm text-gray-500 mt-1">
                    PDF, JPG, PNG up to 10MB
                  </p>
                </label>
              </div>

              {uploadedFile && (
                <div className="flex items-center gap-3 p-3 rounded-lg bg-green-50 border border-green-200">
                  <FileCheck className="h-5 w-5 text-green-600" />
                  <div className="flex-1 min-w-0">
                    <p className="text-sm font-medium text-green-900 truncate">
                      {uploadedFile.name}
                    </p>
                    <p className="text-xs text-green-700">
                      {(uploadedFile.size / 1024).toFixed(2)} KB
                    </p>
                  </div>
                </div>
              )}

              <Button
                className="w-full bg-indigo-600 hover:bg-indigo-700"
                onClick={handleProcessOCR}
                disabled={isProcessing || !uploadedFile}
              >
                {isProcessing ? (
                  <>
                    <ScanLine className="mr-2 h-4 w-4 animate-pulse" />
                    Processing...
                  </>
                ) : (
                  <>
                    <ScanLine className="mr-2 h-4 w-4" />
                    Start OCR Grading
                  </>
                )}
              </Button>

              {isProcessing && (
                <div className="space-y-2">
                  <div className="flex items-center justify-between text-sm">
                    <span className="text-gray-600">Processing...</span>
                    <span className="font-medium text-indigo-600">{progress}%</span>
                  </div>
                  <Progress value={progress} className="h-2" />
                  <p className="text-xs text-gray-500 text-center">
                    Scanning and extracting answers...
                  </p>
                </div>
              )}
            </CardContent>
          </Card>

          {gradingResults.length > 0 && (
            <Card className="mt-6">
              <CardHeader>
                <CardTitle>Class Statistics</CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="p-4 rounded-lg bg-indigo-50">
                  <p className="text-sm text-indigo-700">Average Score</p>
                  <p className="text-3xl font-bold text-indigo-900">
                    {averageScore}%
                  </p>
                </div>
                <div className="grid grid-cols-2 gap-4">
                  <div className="p-3 rounded-lg bg-gray-50">
                    <p className="text-xs text-gray-600">Students</p>
                    <p className="text-xl font-bold text-gray-900">
                      {gradingResults.length}
                    </p>
                  </div>
                  <div className="p-3 rounded-lg bg-gray-50">
                    <p className="text-xs text-gray-600">Questions</p>
                    <p className="text-xl font-bold text-gray-900">
                      {gradingResults[0]?.totalQuestions || 0}
                    </p>
                  </div>
                </div>
              </CardContent>
            </Card>
          )}
        </div>

        {/* Results Panel */}
        <div className="lg:col-span-2">
          {gradingResults.length === 0 ? (
            <Card className="h-full">
              <CardContent className="flex flex-col items-center justify-center h-full min-h-[500px] text-center">
                <div className="w-20 h-20 rounded-full bg-indigo-50 flex items-center justify-center mb-4">
                  <ScanLine className="h-10 w-10 text-indigo-600" />
                </div>
                <h3 className="text-lg font-semibold text-gray-900 mb-2">
                  No Results Yet
                </h3>
                <p className="text-gray-600 max-w-md">
                  Upload answer sheets and start OCR grading to see results here.
                </p>
              </CardContent>
            </Card>
          ) : (
            <div className="space-y-6">
              {/* Results Summary */}
              <Card>
                <CardHeader>
                  <div className="flex items-center justify-between">
                    <div>
                      <CardTitle>Grading Results</CardTitle>
                      <CardDescription>
                        {gradingResults.length} students graded
                      </CardDescription>
                    </div>
                    <Button onClick={handleExportResults}>
                      <Download className="mr-2 h-4 w-4" />
                      Export CSV
                    </Button>
                  </div>
                </CardHeader>
                <CardContent>
                  <Table>
                    <TableHeader>
                      <TableRow>
                        <TableHead>Student ID</TableHead>
                        <TableHead>Name</TableHead>
                        <TableHead className="text-center">Correct</TableHead>
                        <TableHead className="text-center">Total</TableHead>
                        <TableHead className="text-center">Score</TableHead>
                        <TableHead className="text-center">Status</TableHead>
                        <TableHead></TableHead>
                      </TableRow>
                    </TableHeader>
                    <TableBody>
                      {gradingResults.map((result) => (
                        <TableRow key={result.studentId}>
                          <TableCell className="font-medium">
                            {result.studentId}
                          </TableCell>
                          <TableCell>{result.studentName}</TableCell>
                          <TableCell className="text-center">
                            {result.correctAnswers}
                          </TableCell>
                          <TableCell className="text-center">
                            {result.totalQuestions}
                          </TableCell>
                          <TableCell className="text-center">
                            <span className="text-lg font-bold text-indigo-600">
                              {result.score}%
                            </span>
                          </TableCell>
                          <TableCell className="text-center">
                            {result.score >= 70 ? (
                              <Badge className="bg-green-50 text-green-700">
                                Pass
                              </Badge>
                            ) : (
                              <Badge className="bg-red-50 text-red-700">
                                Fail
                              </Badge>
                            )}
                          </TableCell>
                          <TableCell>
                            <Button
                              variant="outline"
                              size="sm"
                              onClick={() => setSelectedStudent(result)}
                            >
                              Details
                            </Button>
                          </TableCell>
                        </TableRow>
                      ))}
                    </TableBody>
                  </Table>
                </CardContent>
              </Card>

              {/* Selected Student Details */}
              {selectedStudent && (
                <Card>
                  <CardHeader>
                    <CardTitle>
                      Answer Details - {selectedStudent.studentName}
                    </CardTitle>
                    <CardDescription>
                      Question-by-question breakdown
                    </CardDescription>
                  </CardHeader>
                  <CardContent>
                    <div className="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-5 gap-3">
                      {selectedStudent.answers.map((answer) => (
                        <div
                          key={answer.question}
                          className={`p-3 rounded-lg border-2 ${
                            answer.isCorrect
                              ? "border-green-500 bg-green-50"
                              : "border-red-500 bg-red-50"
                          }`}
                        >
                          <div className="flex items-center justify-between mb-2">
                            <span className="text-xs font-medium text-gray-600">
                              Q{answer.question}
                            </span>
                            {answer.isCorrect ? (
                              <CheckCircle2 className="h-4 w-4 text-green-600" />
                            ) : (
                              <XCircle className="h-4 w-4 text-red-600" />
                            )}
                          </div>
                          <div className="text-sm">
                            <p className="text-gray-600">
                              Answer:{" "}
                              <span className="font-bold">
                                {answer.studentAnswer}
                              </span>
                            </p>
                            {!answer.isCorrect && (
                              <p className="text-green-700 mt-1">
                                Correct:{" "}
                                <span className="font-bold">
                                  {answer.correctAnswer}
                                </span>
                              </p>
                            )}
                          </div>
                        </div>
                      ))}
                    </div>
                  </CardContent>
                </Card>
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

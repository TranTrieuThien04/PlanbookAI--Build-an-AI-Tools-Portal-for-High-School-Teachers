import { useState } from "react";
import { useNavigate } from "react-router";
import { GraduationCap } from "lucide-react";
import { Button } from "../components/ui/button";
import { Input } from "../components/ui/input";
import { Label } from "../components/ui/label";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "../components/ui/card";
import { login, saveUser } from "../lib/auth";
import { toast } from "sonner";
import { ImageWithFallback } from "../components/figma/ImageWithFallback";

export default function LoginPage() {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    // Simulate API call
    setTimeout(() => {
      const user = login(email, password);
      if (user) {
        saveUser(user);
        toast.success("Welcome back!");
        navigate(`/${user.role}`);
      } else {
        toast.error("Invalid credentials");
      }
      setLoading(false);
    }, 1000);
  };

  return (
    <div className="flex min-h-screen">
      {/* Left side - Image */}
      <div className="hidden lg:flex lg:w-1/2 relative overflow-hidden bg-gradient-to-br from-indigo-600 via-indigo-700 to-cyan-600">
        <ImageWithFallback
          src="https://images.unsplash.com/photo-1758685848084-fc51214f3cd0?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxjaGVtaXN0cnklMjBjbGFzc3Jvb20lMjB0ZWFjaGluZyUyMGVkdWNhdGlvbnxlbnwxfHx8fDE3NzM5OTI1OTd8MA&ixlib=rb-4.1.0&q=80&w=1080"
          alt="Chemistry classroom"
          className="absolute inset-0 h-full w-full object-cover opacity-20"
        />
        <div className="relative z-10 flex flex-col justify-center px-16 text-white">
          <div className="flex items-center gap-3 mb-8">
            <div className="flex h-16 w-16 items-center justify-center rounded-2xl bg-white/20 backdrop-blur-sm">
              <GraduationCap className="h-10 w-10" />
            </div>
            <div>
              <h1 className="text-4xl font-bold">PlanbookAI</h1>
              <p className="text-indigo-200">AI-Powered Teaching Platform</p>
            </div>
          </div>
          <h2 className="text-3xl font-bold mb-4">
            Transform Your Teaching Experience
          </h2>
          <p className="text-xl text-indigo-100 mb-8">
            Automate lesson planning, generate exercises, and grade with AI-powered tools designed for chemistry teachers.
          </p>
          <div className="space-y-3">
            <div className="flex items-center gap-3">
              <div className="h-2 w-2 rounded-full bg-cyan-400"></div>
              <p className="text-indigo-100">AI-powered exercise generation</p>
            </div>
            <div className="flex items-center gap-3">
              <div className="h-2 w-2 rounded-full bg-cyan-400"></div>
              <p className="text-indigo-100">Automated OCR grading system</p>
            </div>
            <div className="flex items-center gap-3">
              <div className="h-2 w-2 rounded-full bg-cyan-400"></div>
              <p className="text-indigo-100">Comprehensive question bank management</p>
            </div>
          </div>
        </div>
      </div>

      {/* Right side - Login form */}
      <div className="flex flex-1 items-center justify-center p-8 bg-gray-50">
        <div className="w-full max-w-md">
          {/* Mobile logo */}
          <div className="flex lg:hidden items-center justify-center gap-2 mb-8">
            <div className="flex h-12 w-12 items-center justify-center rounded-xl bg-gradient-to-br from-indigo-600 to-cyan-500">
              <GraduationCap className="h-7 w-7 text-white" />
            </div>
            <h1 className="text-2xl font-bold text-gray-900">PlanbookAI</h1>
          </div>

          <Card>
            <CardHeader>
              <CardTitle>Welcome Back</CardTitle>
              <CardDescription>
                Sign in to your account to continue
              </CardDescription>
            </CardHeader>
            <CardContent>
              <form onSubmit={handleLogin} className="space-y-4">
                <div className="space-y-2">
                  <Label htmlFor="email">Email</Label>
                  <Input
                    id="email"
                    type="email"
                    placeholder="your.email@school.edu"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="password">Password</Label>
                  <Input
                    id="password"
                    type="password"
                    placeholder="••••••••"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                  />
                </div>
                <Button
                  type="submit"
                  className="w-full bg-indigo-600 hover:bg-indigo-700"
                  disabled={loading}
                >
                  {loading ? "Signing in..." : "Sign In"}
                </Button>
              </form>

              <div className="mt-6 border-t pt-6">
                <p className="text-sm text-gray-600 mb-3">Demo accounts:</p>
                <div className="grid grid-cols-2 gap-2 text-xs">
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={() => {
                      setEmail("sarah.johnson@school.edu");
                      setPassword("password");
                    }}
                  >
                    Teacher
                  </Button>
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={() => {
                      setEmail("michael.chen@planbookai.com");
                      setPassword("password");
                    }}
                  >
                    Admin
                  </Button>
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={() => {
                      setEmail("emily.rodriguez@planbookai.com");
                      setPassword("password");
                    }}
                  >
                    Manager
                  </Button>
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={() => {
                      setEmail("james.williams@planbookai.com");
                      setPassword("password");
                    }}
                  >
                    Staff
                  </Button>
                </div>
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
}

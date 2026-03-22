# PlanbookAI - AI-Powered Teaching Platform

##  Overview

PlanbookAI is a comprehensive web-based platform designed for high school chemistry teachers, focusing on reducing manual workload through AI-powered automation. The platform features a role-based access system supporting four user types: Teachers, Admins, Managers, and Staff.

##  Architecture

### Technology Stack
- **Frontend Framework**: React 18.3.1
- **Routing**: React Router v7 (Data Mode)
- **Styling**: Tailwind CSS v4
- **UI Components**: Radix UI primitives
- **Charts**: Recharts
- **Icons**: Lucide React
- **State Management**: React Hooks + Local Storage

### File Structure

```
/src/app/
├── App.tsx                      # Main entry point with RouterProvider
├── routes.ts                    # Centralized route configuration
├── lib/
│   └── auth.ts                  # Authentication & user management
├── components/
│   ├── DashboardLayout.tsx      # Main layout wrapper
│   ├── Sidebar.tsx              # Role-based navigation sidebar
│   ├── Header.tsx               # Top header with search & user profile
│   ├── StatCard.tsx             # Reusable statistics card
│   └── ui/                      # Shadcn UI components
└── pages/
    ├── LoginPage.tsx            # Authentication page
    ├── TeacherDashboard.tsx     # Teacher home dashboard
    ├── QuestionBank.tsx         # Question management
    ├── ExerciseGenerator.tsx    # AI exercise generation
    ├── ExamGenerator.tsx        # Exam creation tool
    ├── OCRGrading.tsx          # Automated grading system
    ├── Analytics.tsx            # Performance analytics
    ├── AdminDashboard.tsx       # Admin control panel
    ├── ManagerDashboard.tsx     # Manager portal
    └── StaffDashboard.tsx       # Staff content management
```

##  User Roles & Features

### 1. Teacher (Primary User)
**Route**: `/teacher`

**Features**:
- **Dashboard**: Overview of classes, exams, and performance metrics
- **Question Bank**: Create, edit, filter, and manage chemistry questions
- **Exercise Generator**: AI-powered exercise creation with customizable parameters
- **Exam Generator**: Create multiple-choice exams with randomization
- **OCR Grading**: Upload and auto-grade scanned answer sheets
- **Analytics**: View student performance, trends, and insights

**Navigation**:
- Dashboard
- Question Bank
- Exercise Generator
- Exam Generator
- OCR Grading
- Analytics

### 2. Admin
**Route**: `/admin`

**Features**:
- User management (create, edit, delete users)
- System configuration
- Curriculum template management
- Revenue tracking and reports
- User growth analytics

**Key Metrics**:
- Total users
- Monthly revenue
- Active teachers
- System uptime

### 3. Manager
**Route**: `/manager`

**Features**:
- Subscription package management
- Order processing and tracking
- Content approval workflow
- Revenue analytics by plan
- Customer relationship management

**Key Metrics**:
- Total revenue
- Active subscriptions
- Pending orders
- Content approval queue

### 4. Staff
**Route**: `/staff`

**Features**:
- Lesson plan creation and management
- Question bank development
- AI prompt template creation
- Content submission for review

**Key Metrics**:
- Lesson plans created
- Questions authored
- AI prompts developed
- Pending reviews

##  Design System

### Color Palette
- **Primary**: Indigo (#4f46e5) - Main brand color
- **Secondary**: Cyan (#06b6d4) - Accent color
- **Success**: Green (#10b981)
- **Warning**: Yellow (#f59e0b)
- **Error**: Red (#ef4444)
- **Neutral**: Gray scale

### Typography
- System font stack with fallbacks
- Clear hierarchy (H1-H6)
- Consistent spacing (8px grid)

### Components
- **Cards**: Content containers with shadows
- **Buttons**: Primary, secondary, outline variants
- **Badges**: Status indicators with color coding
- **Tables**: Data display with sorting
- **Forms**: Input fields with validation
- **Modals/Dialogs**: Contextual actions
- **Charts**: Recharts for data visualization

##  Authentication Flow

1. **Login Page** (`/login`)
   - Email/password authentication
   - Demo accounts for each role
   - JWT-based session (mock implementation)

2. **Role-Based Redirect**
   - Teacher → `/teacher`
   - Admin → `/admin`
   - Manager → `/manager`
   - Staff → `/staff`

3. **Protected Routes**
   - All dashboard routes require authentication
   - User data stored in localStorage
   - Auto-redirect to login if not authenticated

## Key Features Implementation

### 1. Question Bank Management
- **CRUD Operations**: Create, read, update, delete questions
- **Filtering**: By topic, difficulty, question type
- **Search**: Full-text search across questions
- **Stats**: Visual breakdown by difficulty level
- **Bulk Actions**: Duplicate, export questions

### 2. AI Exercise Generator
- **Input Parameters**:
  - Topic selection
  - Difficulty level (Easy/Medium/Hard)
  - Number of questions (3-20)
  - Additional context/requirements

- **Output**:
  - AI-generated questions with multiple choice options
  - Correct answers with explanations
  - Save to question bank option
  - PDF export functionality

- **UI Features**:
  - Real-time progress indicator
  - Preview before saving
  - Individual question editing

### 3. Exam Generator
- **Configuration**:
  - Exam title and metadata
  - Multiple topic selection
  - Question distribution per topic
  - Number of exam versions

- **Options**:
  - Randomize question order
  - Include answer key
  - Generate multiple versions (1-4)

- **Output**:
  - Preview modal with exam structure
  - PDF export with student version
  - Separate answer key generation

### 4. OCR Grading System
- **Upload**:
  - Support for PDF, JPG, PNG
  - Drag-and-drop interface
  - File validation

- **Processing**:
  - OCR text extraction simulation
  - Answer comparison with key
  - Automated scoring

- **Results**:
  - Individual student scores
  - Class statistics and averages
  - Detailed answer breakdown
  - Question-by-question analysis
  - CSV export for gradebook

### 5. Analytics Dashboard
- **Visualizations**:
  - Line charts for performance trends
  - Bar charts for topic analysis
  - Pie charts for score distribution
  - Radar charts for skill assessment

- **Metrics**:
  - Class average scores
  - Individual student performance
  - Topic-wise analysis
  - Improvement trends

##  Data Flow

### Mock Data Structure
All data is currently mocked for demonstration purposes:

```typescript
// User Authentication
interface User {
  id: string;
  name: string;
  email: string;
  role: UserRole;
}

// Question Bank
interface Question {
  id: string;
  question: string;
  topic: string;
  difficulty: "Easy" | "Medium" | "Hard";
  type: string;
  answer: string;
  options?: string[];
}

// Grading Results
interface GradingResult {
  studentId: string;
  studentName: string;
  totalQuestions: number;
  correctAnswers: number;
  score: number;
  answers: Answer[];
}
```

##  User Experience Design

### Design Principles
1. **Simplicity**: Minimal steps to complete tasks
2. **Productivity**: Fast workflows with keyboard shortcuts
3. **Clarity**: Clear visual hierarchy and navigation
4. **Consistency**: Uniform UI patterns across pages
5. **Feedback**: Immediate response to user actions

### Navigation Pattern
- **Sidebar**: Persistent left navigation (role-based)
- **Header**: Search, notifications, user profile
- **Breadcrumbs**: Context awareness (where applicable)
- **Quick Actions**: Dashboard shortcuts to common tasks

### Responsive Design
- Desktop-first approach (primary use case)
- Tablet-friendly layouts
- Mobile navigation with hamburger menu
- Adaptive grid systems

## User Flows

### Teacher - Create and Grade Exam Flow
1. Login → Teacher Dashboard
2. Navigate to Exam Generator
3. Configure exam parameters
4. Select topics and question counts
5. Preview generated exam
6. Download PDF
7. Distribute to students
8. Navigate to OCR Grading
9. Upload scanned answer sheets
10. Review automated results
11. Export grades

### Admin - User Management Flow
1. Login → Admin Dashboard
2. View system statistics
3. Navigate to user management
4. Create/edit/delete users
5. Assign roles and permissions
6. Monitor system activity

##  Future Enhancements

### Backend Integration
- Real authentication with JWT
- Database integration (PostgreSQL/MongoDB)
- RESTful API endpoints
- File upload to cloud storage
- Real-time collaboration

### AI Integration
- OpenAI GPT for question generation
- OCR engine (Tesseract/Google Vision)
- Natural language processing
- Adaptive learning algorithms

### Additional Features
- Student portal
- Parent dashboard
- Mobile applications
- Email notifications
- Calendar integration
- Collaboration tools
- Video content support

##  Educational Value

This platform demonstrates:
- Modern React development patterns
- Role-based access control (RBAC)
- Responsive SaaS dashboard design
- Data visualization best practices
- User experience optimization
- Component-driven architecture
- State management strategies

##  Demo Accounts

### Test Credentials

**Teacher Account**:
- Email: `sarah.johnson@school.edu`
- Password: `password`

**Admin Account**:
- Email: `michael.chen@planbookai.com`
- Password: `password`

**Manager Account**:
- Email: `emily.rodriguez@planbookai.com`
- Password: `password`

**Staff Account**:
- Email: `james.williams@planbookai.com`
- Password: `password`

##  Getting Started

1. **Login**: Use any demo account
2. **Explore**: Navigate through role-specific features
3. **Interact**: Try creating questions, generating exams
4. **Analyze**: View analytics and reports

##  Use Cases

1. **Teacher**: Daily lesson planning and grading
2. **Admin**: Platform management and oversight
3. **Manager**: Business operations and content review
4. **Staff**: Content creation and curation

---

**Built with**: React, TypeScript, Tailwind CSS, Recharts, Radix UI  
**Design Pattern**: SaaS Dashboard  
**Architecture**: Component-based, Role-based Access Control  
**Purpose**: Capstone Project / Educational Platform Prototype

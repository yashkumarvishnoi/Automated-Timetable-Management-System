
import java.util.*;

class DayTimeSlot {
    private String day;
    private int timeSlot;

    public DayTimeSlot(String day, int timeSlot) {
        this.day = day;
        this.timeSlot = timeSlot;
    }

    public String getDay() {
        return day;
    }

    public int getTimeSlot() {
        return timeSlot;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DayTimeSlot that = (DayTimeSlot) obj;
        return timeSlot == that.timeSlot && day.equals(that.day);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, timeSlot);
    }

    @Override
    public String toString() {
        return day + " - " + timeSlot + "h";
    }
}

class Student {
    public String batchName;
    public int capacity;
    Set<String> subjects;
    LinkedHashMap<DayTimeSlot, StudentSession> schedule;
    int sessionCount=0;

    public Student(String batchName, int capacity) {
        this.batchName = batchName;
        this.capacity = capacity;
        this.subjects = new HashSet<>();
        this.schedule = new LinkedHashMap<>();
    }

    public void addSubjects(String subject) {
        subjects.add(subject);
    }

    public Set<String> getSubjects() {
        return subjects;
    }
    public void incrementSessionCount() {
        sessionCount++;
    }

    public void decrementSessionCount() {
        sessionCount--;
    }

    public void addStudentSession(String day, int timeSlot, String className, String teacherName) {
        DayTimeSlot slot = new DayTimeSlot(day, timeSlot);
        schedule.put(slot, new StudentSession(className, teacherName));
    }
    public void printSchedule() {
        System.out.println("Schedule for Student Batch: " + batchName);
        for (Map.Entry<DayTimeSlot, StudentSession> entry : schedule.entrySet()) {
            DayTimeSlot dayTime = entry.getKey();
            StudentSession session = entry.getValue();
            System.out.println(dayTime + " - " + session);
        }
        System.out.println();
    }

    class StudentSession {
        public String classroomName;
        public String teacherName;

        public StudentSession(String classroomName, String teacherName) {
            this.classroomName = classroomName;
            this.teacherName = teacherName;
        }

        @Override
        public String toString() {
            return "Classroom: " + classroomName + ", Teacher: " + teacherName;
        }
    }
}

class ClassRoom {
    String className;
    int capacity;
    boolean isLab;
    LinkedHashMap<DayTimeSlot, ClassRoomSession> classSchedule;
    int sessionCount=0;

    ClassRoom(String name, int capacity, boolean lab) {
        this.className = name;
        this.capacity = capacity;
        this.isLab = lab;
        this.classSchedule = new LinkedHashMap<>();
    }

    public void addClassSession(String day, int timeSlot, String batchName, String teacherName) {
        DayTimeSlot slot = new DayTimeSlot(day, timeSlot);
        classSchedule.put(slot, new ClassRoomSession(batchName, teacherName));
    }
    public void incrementSessionCount() {
        sessionCount++;
    }

    public void decrementSessionCount() {
        sessionCount--;
    }

    public void printSchedule() {
        System.out.println("Schedule for Classroom: " + className);
        for (Map.Entry<DayTimeSlot, ClassRoomSession> entry : classSchedule.entrySet()) {
            DayTimeSlot dayTime = entry.getKey();
            ClassRoomSession session = entry.getValue();
            System.out.println(dayTime + " - " + session);
        }
        System.out.println();
    }

    class ClassRoomSession {
        public String batchName;
        public String teacherName;

        public ClassRoomSession(String batchName, String teacherName) {
            this.batchName = batchName;
            this.teacherName = teacherName;
        }

        @Override
        public String toString() {
            return "Batch: " + batchName + ", Teacher: " + teacherName;
        }
    }
}

class Teacher {
    String teacherName;
    Set<String> subjects;
    LinkedHashMap<DayTimeSlot, TeacherSession> teacherSchedule;
    int sessionCount=0;

    Teacher(String name) {
        this.teacherName = name;
        this.subjects = new HashSet<>();
        this.teacherSchedule = new LinkedHashMap<>();
    }

    public void addSubjects(String subject) {
        subjects.add(subject);
    }

    public Set<String> getSubjects() {
        return subjects;
    }

    public void incrementSessionCount() {
        sessionCount++;
    }

    public void decrementSessionCount() {
        sessionCount--;
    }

    public void addTeacherSession(String day, int timeSlot, String batchName, String classroomName) {
        DayTimeSlot slot = new DayTimeSlot(day, timeSlot);
        teacherSchedule.put(slot, new TeacherSession(batchName, classroomName));
    }

    public void printSchedule() {
        System.out.println("Schedule for Teacher: " + teacherName);
        for (Map.Entry<DayTimeSlot, TeacherSession> entry : teacherSchedule.entrySet()) {
            DayTimeSlot dayTime = entry.getKey();
            TeacherSession session = entry.getValue();
            System.out.println(dayTime + " - " + session);
        }
        System.out.println();
    }

    class TeacherSession {
        String batchName;
        String classroomName;

        TeacherSession(String batchName, String classroomName) {
            this.batchName = batchName;
            this.classroomName = classroomName;
        }

        @Override
        public String toString() {
            return "Batch: " + batchName + ", Classroom: " + classroomName;
        }
    }
}


class TimetableGeneration {

    private Map<String, ClassRoom> classrooms;  // Map of classroom name to ClassRoom object
    private Map<String, Student> students;      // Map of batch name to Student object
    private Map<String, Teacher> teachers;      // Map of teacher name to Teacher object
    private int maxClassesPerBatch;             // Maximum number of classes a batch can take

    public TimetableGeneration(int maxClassesPerBatch) {
        this.classrooms = new HashMap<>();
        this.students = new HashMap<>();
        this.teachers = new HashMap<>();
        this.maxClassesPerBatch = maxClassesPerBatch;
    }

    // Add Classroom to the system
    public void addClassRoom(ClassRoom classRoom) {
        classrooms.put(classRoom.className, classRoom);
    }

    // Add Student batch to the system
    public void addStudent(Student student) {
        students.put(student.batchName, student);
    }

    // Add Teacher to the system
    public void addTeacher(Teacher teacher) {
        teachers.put(teacher.teacherName, teacher);
    }

    // Allocate classes to the timetable based on limits
    public void generateTimetable() {
        // Iterate over all students and assign their classes
        for (Student student : students.values()) {
            // Ensure each batch does not exceed the maximum class limit
            if (student.sessionCount >= maxClassesPerBatch) {
                System.out.println("Max class limit reached for batch: " + student.batchName);
                continue;
            }

            // Iterate over each subject for the student and try to assign a class
            for (String subject : student.getSubjects()) {
                // Try to allocate a classroom and a teacher
                boolean allocated = false;

                for (ClassRoom classRoom : classrooms.values()) {
                    // Check classroom availability (e.g., capacity and session count)
                    if (classRoom.sessionCount < classRoom.capacity) {
                        // Now find a teacher who can teach this subject
                        for (Teacher teacher : teachers.values()) {
                            if (teacher.getSubjects().contains(subject)) {
                                // Assign a day and time slot to the class
                                DayTimeSlot dayTimeSlot = getAvailableTimeSlot(classRoom, teacher);
                                if (dayTimeSlot != null) {
                                    // Assign session to student, classroom, and teacher
                                    student.addStudentSession(dayTimeSlot.getDay(), dayTimeSlot.getTimeSlot(),
                                            classRoom.className, teacher.teacherName);
                                    classRoom.addClassSession(dayTimeSlot.getDay(), dayTimeSlot.getTimeSlot(),
                                            student.batchName, teacher.teacherName);
                                    teacher.addTeacherSession(dayTimeSlot.getDay(), dayTimeSlot.getTimeSlot(),
                                            student.batchName, classRoom.className);

                                    student.incrementSessionCount();
                                    classRoom.incrementSessionCount();
                                    teacher.incrementSessionCount();
                                    allocated = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (allocated) break;
                }

                if (!allocated) {
                    System.out.println("Unable to allocate class for subject: " + subject + " for batch: " + student.batchName);
                }
            }
        }
    }

    // Find an available time slot for a class, ensuring no conflict
    private DayTimeSlot getAvailableTimeSlot(ClassRoom classRoom, Teacher teacher) {
        for (int timeSlot = 1; timeSlot <= 6; timeSlot++) { // Assuming 6 time slots per day
            for (String day : Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")) {
                DayTimeSlot slot = new DayTimeSlot(day, timeSlot);
                // Check if classroom, teacher, and batch are free at this time
                if (!classRoom.classSchedule.containsKey(slot) && !teacher.teacherSchedule.containsKey(slot)) {
                    return slot;  // Return the first available slot
                }
            }
        }
        return null;  // No available slot found
    }

    // Print the generated timetable for students, teachers, and classrooms
    public void printTimetable() {
        for (Student student : students.values()) {
            student.printSchedule();
        }

        for (ClassRoom classRoom : classrooms.values()) {
            classRoom.printSchedule();
        }

        for (Teacher teacher : teachers.values()) {
            teacher.printSchedule();
        }
    }
}

public class timetable {
    public static void main(String[] args) {
        TimetableGeneration timetable = new TimetableGeneration(4);
    
        // Adding classrooms
        for (int i = 1; i <= 6; i++) {
            timetable.addClassRoom(new ClassRoom("1020" + i, 60, false));
        }
    
        // Adding students
        String[] subjects = {"PPL", "Linux", "Maths", "Learning Conversation"};
        for (int i = 1; i <= 10; i++) {
            Student student = new Student("Batch" + i, 60);
            for (String subject : subjects) {
                student.addSubjects(subject);
            }
            timetable.addStudent(student);
        }
    
        // Adding teachers
        Teacher teacher1 = new Teacher("Mr. Sandeep Chaurasiya");
        teacher1.addSubjects("PPL");
        teacher1.addSubjects("Linux");
    
        Teacher teacher2 = new Teacher("Ms. Mohini Gupta");
        teacher2.addSubjects("Maths");
    
        Teacher teacher3 = new Teacher("Ms. Sheetal Oberoi");
        teacher3.addSubjects("Learning Conversation");
    
        timetable.addTeacher(teacher1);
        timetable.addTeacher(teacher2);
        timetable.addTeacher(teacher3);
    
        // Generate timetable
        timetable.generateTimetable();
    
        // Print timetable
        timetable.printTimetable();
    }
    
    }

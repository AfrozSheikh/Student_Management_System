import java.io.*;
import java.util.*;

// ------------------------ Person (Base Class) ------------------------
class Person {
    private String name;
    private int age;

    public Person() {}

    public Person(String name, int age) {
        this.name = name;
        this.age  = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

// ------------------------ Student (Child Class) ------------------------
class Student extends Person {
    private String rollNumber;
    private String course;

    public Student(String rollNumber, String name, int age, String course) {
        super(name, age);
        this.rollNumber = rollNumber;
        this.course     = course;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    // Polymorphism: custom display
    public void displayInfo() {
        System.out.println("Roll No  : " + rollNumber);
        System.out.println("Name     : " + getName());
        System.out.println("Age      : " + getAge());
        System.out.println("Course   : " + course);
        System.out.println("---------------------------");
    }

    // For saving in file (CSV style)
    public String toFileString() {
        return rollNumber + "," + getName() + "," + getAge() + "," + course;
    }

    // For debugging / printing in one line if needed
    @Override
    public String toString() {
        return "Student{" +
                "rollNumber='" + rollNumber + '\'' +
                ", name='" + getName() + '\'' +
                ", age=" + getAge() +
                ", course='" + course + '\'' +
                '}';
    }
}

// ------------------------ Repository Abstraction ------------------------
interface StudentRepository {
    void addStudent(Student student) throws IOException;
    List<Student> getAllStudents() throws IOException;
    boolean updateStudent(String rollNumber, Student updatedStudent) throws IOException;
    boolean deleteStudent(String rollNumber) throws IOException;
}

// ------------------------ File-based Implementation ------------------------
class FileStudentRepository implements StudentRepository {

    private final File file;

    public FileStudentRepository(String fileName) {
        this.file = new File(fileName);
        try {
            if (!file.exists()) {
                file.createNewFile(); // create empty file if not present
            }
        } catch (IOException e) {
            System.out.println("Error while creating data file: " + e.getMessage());
        }
    }

    @Override
    public void addStudent(Student student) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(student.toFileString());
            writer.newLine();
        }
    }

    @Override
    public List<Student> getAllStudents() throws IOException {
        List<Student> students = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                // Each line format: roll,name,age,course
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String roll = parts[0];
                    String name = parts[1];
                    int age;
                    try {
                        age = Integer.parseInt(parts[2]);
                    } catch (NumberFormatException e) {
                        age = 0; // fallback
                    }
                    String course = parts[3];

                    Student s = new Student(roll, name, age, course);
                    students.add(s);
                }
            }
        }

        return students;
    }

    @Override
    public boolean updateStudent(String rollNumber, Student updatedStudent) throws IOException {
        List<Student> students = getAllStudents();
        boolean found = false;

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getRollNumber().equals(rollNumber)) {
                students.set(i, updatedStudent);
                found = true;
                break;
            }
        }

        if (found) {
            writeAll(students);
        }

        return found;
    }

    @Override
    public boolean deleteStudent(String rollNumber) throws IOException {
        List<Student> students = getAllStudents();
        boolean removed = students.removeIf(s -> s.getRollNumber().equals(rollNumber));

        if (removed) {
            writeAll(students);
        }

        return removed;
    }

    // Helper: rewrite the full file from list
    private void writeAll(List<Student> students) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            for (Student s : students) {
                writer.write(s.toFileString());
                writer.newLine();
            }
        }
    }
}

// ------------------------ Main SMS Logic (Menu) ------------------------
class StudentManagementSystem {

    private final StudentRepository repository;
    private final Scanner scanner;

    public StudentManagementSystem(StudentRepository repository) {
        this.repository = repository;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        int choice;

        do {
            System.out.println("\n===== Student Management System =====");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            // handle wrong input safely
            while (!scanner.hasNextInt()) {
                System.out.print("Please enter a valid number: ");
                scanner.next();
            }
            choice = scanner.nextInt();
            scanner.nextLine(); // consume leftover newline

            switch (choice) {
                case 1:
                    handleAddStudent();
                    break;
                case 2:
                    handleViewStudents();
                    break;
                case 3:
                    handleUpdateStudent();
                    break;
                case 4:
                    handleDeleteStudent();
                    break;
                case 5:
                    System.out.println("Exiting... Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }

        } while (choice != 5);
    }

    private void handleAddStudent() {
        try {
            System.out.print("Enter Roll Number: ");
            String roll = scanner.nextLine();

            System.out.print("Enter Name: ");
            String name = scanner.nextLine();

            System.out.print("Enter Age: ");
            int age = readIntInput();

            System.out.print("Enter Course: ");
            String course = scanner.nextLine();

            Student student = new Student(roll, name, age, course);
            repository.addStudent(student);
            System.out.println("Student added successfully!");

        } catch (IOException e) {
            System.out.println("Error adding student: " + e.getMessage());
        }
    }

    private void handleViewStudents() {
        try {
            List<Student> students = repository.getAllStudents();

            if (students.isEmpty()) {
                System.out.println("No students found.");
            } else {
                System.out.println("\n--- Student List ---");
                for (Student s : students) {
                    s.displayInfo();
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading students: " + e.getMessage());
        }
    }

    private void handleUpdateStudent() {
        try {
            System.out.print("Enter Roll Number of student to update: ");
            String roll = scanner.nextLine();

            System.out.println("Enter new details:");

            System.out.print("New Name: ");
            String name = scanner.nextLine();

            System.out.print("New Age: ");
            int age = readIntInput();

            System.out.print("New Course: ");
            String course = scanner.nextLine();

            Student updated = new Student(roll, name, age, course);
            boolean success = repository.updateStudent(roll, updated);

            if (success) {
                System.out.println("Student updated successfully!");
            } else {
                System.out.println("Student with roll number " + roll + " not found.");
            }

        } catch (IOException e) {
            System.out.println("Error updating student: " + e.getMessage());
        }
    }

    private void handleDeleteStudent() {
        try {
            System.out.print("Enter Roll Number of student to delete: ");
            String roll = scanner.nextLine();

            boolean success = repository.deleteStudent(roll);

            if (success) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("Student with roll number " + roll + " not found.");
            }

        } catch (IOException e) {
            System.out.println("Error deleting student: " + e.getMessage());
        }
    }

    // small helper to safely read int input
    private int readIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // consume leftover newline
        return value;
    }
}

// ------------------------ Main Class ------------------------
public class StudentManagementApp {
    public static void main(String[] args) {
        // Data will be stored in students.txt in the same folder
        StudentRepository repository = new FileStudentRepository("students.txt");
        StudentManagementSystem sms = new StudentManagementSystem(repository);
        sms.start();
    }
}


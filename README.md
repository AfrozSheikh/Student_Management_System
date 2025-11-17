# Student Management System (Java, OOP-Based)


---

## 📌 Project Overview

The **Student Management System (SMS)** is a terminal-based Java application that allows you to manage student records. It uses:

* ✔️ **Encapsulation** (private fields + getters/setters)
* ✔️ **Inheritance** (`Person` → `Student`)
* ✔️ **Polymorphism** (`displayInfo()` overriding)
* ✔️ **Abstraction** (`StudentRepository` interface)
* ✔️ **File Handling** (persistent storage via `students.txt`)

The project includes all CRUD operations:

* **Add a student**
* **View all students**
* **Update an existing student**
* **Delete a student**

All data is stored in a local file named `students.txt`, ensuring the records remain even after the program exits.

---

## 🧱 Project Architecture

The system is designed using clean, modular OOP architecture:

### **1. Person (Base Class)**

Contains properties common to all people:

* `name`
* `age`

### **2. Student (Child Class extending Person)**

Additional properties:

* `rollNumber`
* `course`

Overrides method:

* `displayInfo()` → To show student details in a formatted way.

### **3. StudentRepository (Interface)**

Defines abstract operations:

* `addStudent()`
* `getAllStudents()`
* `updateStudent()`
* `deleteStudent()`

### **4. FileStudentRepository (Implementation Class)**

Handles actual file operations:

* Stores each student as a **comma-separated line**
* Reads/writes from `students.txt`

Ensures data persistence.

### **5. StudentManagementSystem (Menu + Application Logic)**

Provides user-friendly menu:

* Add
* View
* Update
* Delete
* Exit

Handles all inputs and calls repository functions.

### **6. StudentManagementApp (Main Class)**

* Entry point of the program
* Initializes repository + system
* Starts the menu

---

## 📂 File Structure

```
StudentManagementApp.java
students.txt  (auto-created)
README.md
```

---

## 📄 How Data is Stored

Data is saved as CSV format:

```
rollNumber,name,age,course
101,Afroz,21,Computer Science
102,Rahul,20,Mechanical
```

This makes it easy to read/write and manually inspect data.

---

## 🔄 Application Flow

### **1. User sees menu in console:**

```
1. Add Student
2. View All Students
3. Update Student
4. Delete Student
5. Exit
```

### **2. Add Student**

User enters details → saved to file.

### **3. View All Students**

Reads file → converts each line into a Student object → displays.

### **4. Update Student**

* Search by roll number
* Replace the record
* Rewrite full file

### **5. Delete Student**

* Remove selected record
* Rewrite remaining data

---

## 🎯 Why This Project is Interview-Friendly

This project shows you understand:

### ✔ OOP fundamentals

### ✔ File handling

### ✔ Clean architecture


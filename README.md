# Student Management System

Student Management System is a desktop dashboard application built with plain Java Swing. It helps manage student records through a clean admin-style interface with form-based editing, searchable tables, file persistence, sorting, and live GPA statistics.

The project is intentionally simple enough for beginners to understand while using a more professional package structure and user interface layout.

## Features

- Add, update, delete, and search student records
- Prevent duplicate student IDs
- Display all students in a scrollable table
- Click table rows to fill the edit form automatically
- Live search by ID, name, or GPA
- Sort students by GPA descending
- Sort students by name ascending
- Show dashboard statistics for total students, highest GPA, lowest GPA, and average GPA
- Save and load records from `data/students.txt`
- Validate empty fields, invalid IDs, invalid GPA values, and CSV-breaking names
- Confirm before deleting a student
- Show success and error messages with dialog boxes

## Technologies

- Java
- Java Swing
- JTable and DefaultTableModel
- File I/O with CSV-style text storage
- Collections including ArrayList and HashMap

## Project Structure

```text
student-management-system/
|-- data/
|   `-- students.txt
|-- src/
|   `-- main/
|       `-- java/
|           |-- app/
|           |   `-- Main.java
|           |-- model/
|           |   `-- Student.java
|           |-- service/
|           |   `-- StudentManager.java
|           `-- ui/
|               `-- StudentDashboard.java
|-- .gitignore
`-- README.md
```

## How to Run

From the project folder, compile the Java files:

```bash
javac -d out src/main/java/app/Main.java src/main/java/model/Student.java src/main/java/service/StudentManager.java src/main/java/ui/StudentDashboard.java
```

Run the application:

```bash
java -cp out app.Main
```

Student data is stored in CSV-like format:

```text
101,Sara Ahmed,3.80
102,Omar Ali,3.40
```

## Screenshots

Add screenshots to the project later using these placeholders:

```text
docs/screenshots/dashboard.png
docs/screenshots/student-form.png
docs/screenshots/statistics.png
```

## Future Improvements

- Export student records to CSV from the GUI
- Add editable table rows with validation
- Add GPA scale settings for different grading systems
- Add a database option for larger datasets
- Add login roles for administrators and staff
- Add automated tests for student management logic

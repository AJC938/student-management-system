# Student Management System

A desktop **Student Management System** built with Java Swing. The application provides an admin-style interface for managing student records with form-based editing, searchable tables, file persistence, sorting, validation, and live GPA statistics.

## Features

- Add, update, delete, and search student records
- Prevent duplicate student IDs
- Scrollable student table
- Click table rows to populate the edit form
- Search by ID, name, or GPA
- Sort by GPA and name
- Dashboard statistics for total students, highest GPA, lowest GPA, and average GPA
- Save and load records from `data/students.txt`
- Input validation and confirmation dialogs

## Technologies

- Java
- Java Swing
- JTable and DefaultTableModel
- File I/O
- ArrayList and HashMap

## Project Structure

```text
student-management-system/
├── data/
│   └── students.txt
├── src/main/java/
│   ├── app/Main.java
│   ├── model/Student.java
│   ├── service/StudentManager.java
│   └── ui/StudentDashboard.java
├── screenshots/
└── README.md
```

## How to Run

```bash
javac -d out src/main/java/app/Main.java src/main/java/model/Student.java src/main/java/service/StudentManager.java src/main/java/ui/StudentDashboard.java
java -cp out app.Main
```

## Screenshots

The repository currently contains an empty `screenshots/` directory. Final UI captures should be added before this project is presented as a portfolio piece.

## Future Improvements

- Export student records to CSV
- Editable table rows with validation
- GPA scale configuration
- Database persistence
- Login roles for administrators and staff
- Automated tests

## Author

**Abdullah Almutairi**  
Electrical & Computer Engineering Student · King Abdulaziz University

# Student Management System

A desktop **Student Management System** built with **Java Swing** for managing student records through a structured, admin-style interface.

The application demonstrates CRUD workflows, searchable and sortable tabular data, file persistence, validation, and live academic statistics.

## Features

- Add, update, delete, and search student records
- Duplicate student-ID prevention
- Search by ID, name, or GPA
- Sort by GPA and name
- Scrollable student table
- Select table rows to populate the edit form
- Dashboard statistics for total students, highest GPA, lowest GPA, and average GPA
- Save and load records from `data/students.txt`
- Input validation and confirmation dialogs

## Tech Stack

- Java
- Java Swing
- JTable / DefaultTableModel
- File I/O
- ArrayList / HashMap

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

## Run Locally

```bash
javac -d out src/main/java/app/Main.java src/main/java/model/Student.java src/main/java/service/StudentManager.java src/main/java/ui/StudentDashboard.java
java -cp out app.Main
```

## Portfolio Status

The application functionality is implemented. The repository does **not currently contain final UI screenshots**, so screenshots have intentionally been omitted from this README rather than displaying broken or placeholder images.

## Future Improvements

- Export student records to CSV
- Editable table rows with stronger validation
- Configurable GPA scale
- Database persistence
- Role-based authentication
- Automated unit tests

## Author

**Abdullah Almutairi**  
Electrical & Computer Engineering Student · King Abdulaziz University

## License

MIT License

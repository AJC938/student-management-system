package service;

import model.Student;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentManager {
    private static final String FILE_PATH = "data/students.txt";

    private final ArrayList<Student> students;
    private final Map<Integer, Student> studentById;

    public StudentManager() {
        students = new ArrayList<>();
        studentById = new HashMap<>();
    }

    public boolean addStudent(Student student) {
        if (student == null || studentById.containsKey(student.getId())) {
            return false;
        }

        students.add(student);
        studentById.put(student.getId(), student);
        return true;
    }

    public boolean removeStudentById(int id) {
        Student student = searchStudentById(id);

        if (student == null) {
            return false;
        }

        students.remove(student);
        studentById.remove(id);
        return true;
    }

    public Student searchStudentById(int id) {
        return studentById.get(id);
    }

    public boolean updateStudent(int id, String newName, double newGpa) {
        Student student = searchStudentById(id);

        if (student == null) {
            return false;
        }

        student.setName(newName);
        student.setGpa(newGpa);
        return true;
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    public int getStudentCount() {
        return students.size();
    }

    public void displayAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        System.out.println("\nStudent List:");
        for (Student student : students) {
            System.out.println(student);
        }
    }

    public void sortStudentsByGpaDescending() {
        students.sort(Comparator.comparingDouble(Student::getGpa).reversed());
    }

    public void sortStudentsByNameAscending() {
        students.sort(Comparator.comparing(Student::getName, String.CASE_INSENSITIVE_ORDER));
    }

    public Student getHighestGpaStudent() {
        if (students.isEmpty()) {
            return null;
        }

        Student highest = students.get(0);

        for (Student student : students) {
            if (student.getGpa() > highest.getGpa()) {
                highest = student;
            }
        }

        return highest;
    }

    public Student getLowestGpaStudent() {
        if (students.isEmpty()) {
            return null;
        }

        Student lowest = students.get(0);

        for (Student student : students) {
            if (student.getGpa() < lowest.getGpa()) {
                lowest = student;
            }
        }

        return lowest;
    }

    public double calculateAverageGpa() {
        if (students.isEmpty()) {
            return 0.0;
        }

        double total = 0.0;
        for (Student student : students) {
            total += student.getGpa();
        }

        return total / students.size();
    }

    public boolean saveToFile() {
        File file = new File(FILE_PATH);

        try {
            createDataFileIfNeeded(file);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (Student student : students) {
                    writer.write(student.getId() + "," + student.getName() + "," + student.getGpa());
                    writer.newLine();
                }
            }

            return true;
        } catch (IOException e) {
            System.out.println("Error saving students: " + e.getMessage());
            return false;
        }
    }

    public boolean loadFromFile() {
        File file = new File(FILE_PATH);
        ArrayList<Student> loadedStudents = new ArrayList<>();
        Map<Integer, Student> loadedStudentById = new HashMap<>();

        try {
            createDataFileIfNeeded(file);

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    Student student = parseStudentLine(line);

                    // Duplicate IDs in the file are ignored to keep the first valid record.
                    if (student != null && !loadedStudentById.containsKey(student.getId())) {
                        loadedStudents.add(student);
                        loadedStudentById.put(student.getId(), student);
                    }
                }
            }

            students.clear();
            students.addAll(loadedStudents);
            studentById.clear();
            studentById.putAll(loadedStudentById);
            return true;
        } catch (IOException e) {
            System.out.println("Error loading students: " + e.getMessage());
            return false;
        }
    }

    private void createDataFileIfNeeded(File file) throws IOException {
        File parentDirectory = file.getParentFile();

        if (parentDirectory != null && !parentDirectory.exists()) {
            parentDirectory.mkdirs();
        }

        if (!file.exists()) {
            file.createNewFile();
        }
    }

    private Student parseStudentLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] parts = line.split(",", -1);

        if (parts.length != 3) {
            return null;
        }

        try {
            int id = Integer.parseInt(parts[0].trim());
            String name = parts[1].trim();
            double gpa = Double.parseDouble(parts[2].trim());

            if (name.isEmpty()) {
                return null;
            }

            return new Student(id, name, gpa);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

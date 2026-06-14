package ui;

import model.Student;
import service.StudentManager;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StudentDashboard extends JFrame {
    private static final Color BACKGROUND = new Color(246, 247, 249);
    private static final Color SIDEBAR = new Color(24, 31, 42);
    private static final Color SIDEBAR_HOVER = new Color(38, 48, 64);
    private static final Color CARD = Color.WHITE;
    private static final Color TEXT = new Color(30, 41, 59);
    private static final Color MUTED_TEXT = new Color(100, 116, 139);
    private static final Color BORDER = new Color(226, 232, 240);
    private static final Color PRIMARY = new Color(37, 99, 235);
    private static final Color SUCCESS = new Color(22, 163, 74);
    private static final Color DANGER = new Color(220, 38, 38);
    private static final Color WARNING = new Color(202, 138, 4);

    private final StudentManager studentManager;
    private final RoundedTextField idField;
    private final RoundedTextField nameField;
    private final RoundedTextField gpaField;
    private final RoundedTextField searchField;
    private final DefaultTableModel tableModel;
    private final JTable studentTable;
    private final JLabel totalStudentsValue;
    private final JLabel highestGpaValue;
    private final JLabel lowestGpaValue;
    private final JLabel averageGpaValue;

    public StudentDashboard() {
        studentManager = new StudentManager();
        idField = new RoundedTextField(18);
        nameField = new RoundedTextField(18);
        gpaField = new RoundedTextField(18);
        searchField = new RoundedTextField(22);
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "GPA"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentTable = new JTable(tableModel);
        totalStudentsValue = createStatValueLabel();
        highestGpaValue = createStatValueLabel();
        lowestGpaValue = createStatValueLabel();
        averageGpaValue = createStatValueLabel();

        setupWindow();
        setupTable();
        setupLiveSearch();
        studentManager.loadFromFile();
        refreshDashboard();
    }

    private void setupWindow() {
        setTitle("Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1180, 720));
        setSize(1220, 760);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND);
        setLayout(new BorderLayout());

        add(createSidebar(), BorderLayout.WEST);
        add(createContentPanel(), BorderLayout.CENTER);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new GridBagLayout());
        sidebar.setBackground(SIDEBAR);
        sidebar.setPreferredSize(new Dimension(270, 720));
        sidebar.setBorder(new EmptyBorder(28, 20, 28, 20));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;

        c.gridy = 0;
        c.insets = new Insets(0, 0, 18, 0);
        sidebar.add(new AvatarPanel(), c);

        JLabel title = new JLabel("<html>Student<br>Management System</html>");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        c.gridy = 1;
        c.insets = new Insets(0, 0, 4, 0);
        sidebar.add(title, c);

        JLabel subtitle = new JLabel("Student Records Dashboard");
        subtitle.setForeground(new Color(174, 184, 198));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        c.gridy = 2;
        c.insets = new Insets(0, 0, 28, 0);
        sidebar.add(subtitle, c);

        c.insets = new Insets(0, 0, 10, 0);
        addSidebarButton(sidebar, c, 3, "+  Add Student", () -> {
            idField.requestFocusInWindow();
            addStudent();
        });
        addSidebarButton(sidebar, c, 4, "?  Search Student", this::searchStudentById);
        addSidebarButton(sidebar, c, 5, "^  Update Student", this::updateStudent);
        addSidebarButton(sidebar, c, 6, "x  Delete Student", this::deleteStudent);
        addSidebarButton(sidebar, c, 7, "#  Statistics", this::showStatisticsDialog);
        addSidebarButton(sidebar, c, 8, "*  Save Data", this::saveData);

        c.gridy = 9;
        c.weighty = 1;
        sidebar.add(new JLabel(), c);

        JLabel footer = new JLabel("Plain Java Swing");
        footer.setForeground(new Color(134, 146, 166));
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        c.gridy = 10;
        c.weighty = 0;
        c.insets = new Insets(18, 0, 0, 0);
        sidebar.add(footer, c);

        return sidebar;
    }

    private void addSidebarButton(JPanel parent, GridBagConstraints c, int row, String text, Runnable action) {
        JButton button = createFlatButton(text, SIDEBAR_HOVER, Color.WHITE);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.addActionListener(event -> action.run());

        c.gridy = row;
        c.weighty = 0;
        parent.add(button, c);
    }

    private JPanel createContentPanel() {
        JPanel content = new JPanel(new BorderLayout(18, 18));
        content.setBackground(BACKGROUND);
        content.setBorder(new EmptyBorder(24, 24, 24, 24));

        JPanel centerPanel = new JPanel(new BorderLayout(18, 18));
        centerPanel.setOpaque(false);
        centerPanel.setPreferredSize(new Dimension(390, 640));
        centerPanel.add(createStudentCard(), BorderLayout.NORTH);
        centerPanel.add(createStatisticsPanel(), BorderLayout.CENTER);

        content.add(centerPanel, BorderLayout.CENTER);
        content.add(createTablePanel(), BorderLayout.EAST);

        return content;
    }

    private JPanel createStudentCard() {
        RoundedPanel card = new RoundedPanel(CARD, 18);
        card.setLayout(new GridBagLayout());
        card.setBorder(new EmptyBorder(24, 24, 24, 24));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 0, 6, 0);

        JLabel title = createSectionTitle("Student Information");
        card.add(title, c);

        JLabel subtitle = new JLabel("Create and edit records from one focused card.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(MUTED_TEXT);
        c.gridy = 1;
        c.insets = new Insets(0, 0, 22, 0);
        card.add(subtitle, c);

        addFormField(card, c, 2, "Student ID", idField);
        addFormField(card, c, 3, "Student Name", nameField);
        addFormField(card, c, 4, "GPA", gpaField);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.add(createActionButton("Add", PRIMARY, this::addStudent));
        buttonPanel.add(createActionButton("Update", SUCCESS, this::updateStudent));
        buttonPanel.add(createActionButton("Delete", DANGER, this::deleteStudent));
        buttonPanel.add(createActionButton("Clear", new Color(71, 85, 105), this::clearFields));

        c.gridy = 5;
        c.gridwidth = 2;
        c.insets = new Insets(18, 0, 0, 0);
        card.add(buttonPanel, c);

        return card;
    }

    private void addFormField(JPanel panel, GridBagConstraints c, int row, String labelText, JTextField field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT);

        c.gridy = row;
        c.gridwidth = 1;
        c.gridx = 0;
        c.weightx = 0;
        c.insets = new Insets(0, 0, 14, 14);
        panel.add(label, c);

        c.gridx = 1;
        c.weightx = 1;
        c.insets = new Insets(0, 0, 14, 0);
        panel.add(field, c);
    }

    private JPanel createStatisticsPanel() {
        RoundedPanel panel = new RoundedPanel(CARD, 18);
        panel.setLayout(new BorderLayout(0, 18));
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = createSectionTitle("Statistics");
        JLabel hint = new JLabel("Live overview of current records");
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        hint.setForeground(MUTED_TEXT);

        header.add(title, BorderLayout.NORTH);
        header.add(hint, BorderLayout.SOUTH);

        JPanel cards = new JPanel(new GridLayout(4, 1, 0, 12));
        cards.setOpaque(false);
        cards.add(createStatCard("Total Students", totalStudentsValue));
        cards.add(createStatCard("Highest GPA", highestGpaValue));
        cards.add(createStatCard("Lowest GPA", lowestGpaValue));
        cards.add(createStatCard("Average GPA", averageGpaValue));

        panel.add(header, BorderLayout.NORTH);
        panel.add(cards, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStatCard(String title, JLabel valueLabel) {
        RoundedPanel card = new RoundedPanel(new Color(248, 250, 252), 14);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titleLabel.setForeground(MUTED_TEXT);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private JPanel createTablePanel() {
        RoundedPanel panel = new RoundedPanel(CARD, 18);
        panel.setLayout(new BorderLayout(0, 16));
        panel.setPreferredSize(new Dimension(560, 640));
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));

        JPanel header = new JPanel(new BorderLayout(12, 12));
        header.setOpaque(false);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(createSectionTitle("Student Records"), BorderLayout.NORTH);

        JLabel subtitle = new JLabel("Search, sort, and select rows to edit.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(MUTED_TEXT);
        titlePanel.add(subtitle, BorderLayout.SOUTH);

        searchField.setToolTipText("Search by ID, name, or GPA");
        searchField.setText("");

        JPanel controls = new JPanel(new GridLayout(1, 3, 8, 0));
        controls.setOpaque(false);
        controls.add(createSmallButton("Load", new Color(71, 85, 105), this::loadData));
        controls.add(createSmallButton("Sort GPA", WARNING, this::sortByGpa));
        controls.add(createSmallButton("Sort Name", PRIMARY, this::sortByName));

        header.add(titlePanel, BorderLayout.NORTH);
        header.add(searchField, BorderLayout.CENTER);
        header.add(controls, BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER));
        scrollPane.getViewport().setBackground(Color.WHITE);

        panel.add(header, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void setupTable() {
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.setRowHeight(38);
        studentTable.setShowVerticalLines(false);
        studentTable.setGridColor(new Color(241, 245, 249));
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        studentTable.setForeground(TEXT);
        studentTable.setSelectionBackground(new Color(219, 234, 254));
        studentTable.setSelectionForeground(TEXT);

        JTableHeader header = studentTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(248, 250, 252));
        header.setForeground(TEXT);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setBorder(new EmptyBorder(0, 12, 0, 12));
        renderer.setForeground(TEXT);
        studentTable.setDefaultRenderer(Object.class, renderer);

        studentTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                fillFieldsFromSelectedRow();
            }
        });
    }

    private void setupLiveSearch() {
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                applyTableFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                applyTableFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                applyTableFilter();
            }
        });
    }

    private void addStudent() {
        Student student = readStudentFromFields();

        if (student == null) {
            return;
        }

        if (studentManager.addStudent(student)) {
            refreshDashboard();
            clearFields();
            showMessage("Student added successfully.");
        } else {
            showError("A student with this ID already exists.");
        }
    }

    private void updateStudent() {
        Student student = readStudentFromFields();

        if (student == null) {
            return;
        }

        if (studentManager.updateStudent(student.getId(), student.getName(), student.getGpa())) {
            refreshDashboard();
            clearFields();
            showMessage("Student updated successfully.");
        } else {
            showError("Student not found.");
        }
    }

    private void deleteStudent() {
        Integer id = readId();

        if (id == null) {
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Delete student ID " + id + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        if (studentManager.removeStudentById(id)) {
            refreshDashboard();
            clearFields();
            showMessage("Student deleted successfully.");
        } else {
            showError("Student not found.");
        }
    }

    private void searchStudentById() {
        Integer id = readId();

        if (id == null) {
            return;
        }

        Student student = studentManager.searchStudentById(id);

        if (student == null) {
            applyTableFilter();
            showError("Student not found.");
            return;
        }

        refreshTable(Collections.singletonList(student));
        setFields(student);
        showMessage("Student found.");
    }

    private void saveData() {
        if (studentManager.saveToFile()) {
            showMessage("Student data saved successfully.");
        } else {
            showError("Could not save student data.");
        }
    }

    private void loadData() {
        if (studentManager.loadFromFile()) {
            refreshDashboard();
            clearFields();
            showMessage("Student data loaded successfully.");
        } else {
            showError("Could not load student data.");
        }
    }

    private void sortByGpa() {
        studentManager.sortStudentsByGpaDescending();
        refreshDashboard();
    }

    private void sortByName() {
        studentManager.sortStudentsByNameAscending();
        refreshDashboard();
    }

    private void showStatisticsDialog() {
        Student highest = studentManager.getHighestGpaStudent();
        Student lowest = studentManager.getLowestGpaStudent();

        if (highest == null || lowest == null) {
            showError("No students available for statistics.");
            return;
        }

        String message = "Total Students: " + studentManager.getStudentCount() + "\n"
                + "Highest GPA: " + formatGpa(highest.getGpa()) + " (" + highest.getName() + ")\n"
                + "Lowest GPA: " + formatGpa(lowest.getGpa()) + " (" + lowest.getName() + ")\n"
                + "Average GPA: " + formatGpa(studentManager.calculateAverageGpa());

        JOptionPane.showMessageDialog(this, message, "Student Statistics", JOptionPane.INFORMATION_MESSAGE);
    }

    private Student readStudentFromFields() {
        Integer id = readId();

        if (id == null) {
            return null;
        }

        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            showError("Student name cannot be empty.");
            return null;
        }

        if (name.contains(",")) {
            showError("Student name cannot contain commas because data is saved as CSV.");
            return null;
        }

        Double gpa = readGpa();
        if (gpa == null) {
            return null;
        }

        return new Student(id, name, gpa);
    }

    private Integer readId() {
        String idText = idField.getText().trim();

        if (idText.isEmpty()) {
            showError("Student ID cannot be empty.");
            return null;
        }

        try {
            return Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            showError("Student ID must be a whole number.");
            return null;
        }
    }

    private Double readGpa() {
        String gpaText = gpaField.getText().trim();

        if (gpaText.isEmpty()) {
            showError("Student GPA cannot be empty.");
            return null;
        }

        try {
            double gpa = Double.parseDouble(gpaText);

            if (gpa < 0.0 || gpa > 4.0) {
                showError("Student GPA must be between 0.0 and 4.0.");
                return null;
            }

            return gpa;
        } catch (NumberFormatException e) {
            showError("Student GPA must be a valid number.");
            return null;
        }
    }

    private void refreshDashboard() {
        applyTableFilter();
        updateStatistics();
    }

    private void applyTableFilter() {
        String query = searchField.getText().trim().toLowerCase();

        if (query.isEmpty()) {
            refreshTable(studentManager.getAllStudents());
            return;
        }

        List<Student> filteredStudents = new ArrayList<>();
        for (Student student : studentManager.getAllStudents()) {
            String id = String.valueOf(student.getId());
            String name = student.getName().toLowerCase();
            String gpa = formatGpa(student.getGpa());

            if (id.contains(query) || name.contains(query) || gpa.contains(query)) {
                filteredStudents.add(student);
            }
        }

        refreshTable(filteredStudents);
    }

    private void refreshTable(List<Student> students) {
        tableModel.setRowCount(0);

        for (Student student : students) {
            tableModel.addRow(new Object[]{
                    student.getId(),
                    student.getName(),
                    formatGpa(student.getGpa())
            });
        }
    }

    private void updateStatistics() {
        Student highest = studentManager.getHighestGpaStudent();
        Student lowest = studentManager.getLowestGpaStudent();

        totalStudentsValue.setText(String.valueOf(studentManager.getStudentCount()));
        highestGpaValue.setText(highest == null ? "0.00" : formatGpa(highest.getGpa()));
        lowestGpaValue.setText(lowest == null ? "0.00" : formatGpa(lowest.getGpa()));
        averageGpaValue.setText(formatGpa(studentManager.calculateAverageGpa()));
    }

    private void fillFieldsFromSelectedRow() {
        int selectedRow = studentTable.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        idField.setText(tableModel.getValueAt(selectedRow, 0).toString());
        nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
        gpaField.setText(tableModel.getValueAt(selectedRow, 2).toString());
    }

    private void setFields(Student student) {
        idField.setText(String.valueOf(student.getId()));
        nameField.setText(student.getName());
        gpaField.setText(formatGpa(student.getGpa()));
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        gpaField.setText("");
        studentTable.clearSelection();
    }

    private JLabel createSectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 22));
        label.setForeground(TEXT);
        return label;
    }

    private JLabel createStatValueLabel() {
        JLabel label = new JLabel("0.00");
        label.setFont(new Font("Segoe UI", Font.BOLD, 28));
        label.setForeground(TEXT);
        return label;
    }

    private JButton createActionButton(String text, Color background, Runnable action) {
        JButton button = createFlatButton(text, background, Color.WHITE);
        button.addActionListener(event -> action.run());
        return button;
    }

    private JButton createSmallButton(String text, Color background, Runnable action) {
        JButton button = createFlatButton(text, background, Color.WHITE);
        button.setPreferredSize(new Dimension(110, 34));
        button.addActionListener(event -> action.run());
        return button;
    }

    private JButton createFlatButton(String text, Color background, Color foreground) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBorder(new EmptyBorder(10, 14, 10, 14));
        return button;
    }

    private String formatGpa(double gpa) {
        return String.format("%.2f", gpa);
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static class RoundedPanel extends JPanel {
        private final Color backgroundColor;
        private final int radius;

        RoundedPanel(Color backgroundColor, int radius) {
            this.backgroundColor = backgroundColor;
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.setColor(BORDER);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class RoundedTextField extends JTextField {
        RoundedTextField(int columns) {
            super(columns);
            setOpaque(false);
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setForeground(TEXT);
            setBorder(new EmptyBorder(10, 14, 10, 14));
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
            g2.setColor(BORDER);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class AvatarPanel extends JPanel {
        AvatarPanel() {
            setOpaque(false);
            setPreferredSize(new Dimension(104, 104));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size = 92;
            int x = (getWidth() - size) / 2;
            int y = 6;

            g2.setColor(new Color(59, 130, 246));
            g2.fillOval(x, y, size, size);
            g2.setColor(new Color(147, 197, 253));
            g2.setStroke(new BasicStroke(3));
            g2.drawOval(x + 8, y + 8, size - 16, size - 16);

            g2.setColor(Color.WHITE);
            g2.fillOval(x + 31, y + 24, 30, 30);
            g2.fillRoundRect(x + 22, y + 60, 48, 20, 20, 20);
            g2.dispose();
        }
    }
}

import java.io.*;
import java.util.Scanner;

public class StudentGradebook {
    private static final String FILE_NAME = "gradebook.txt";

    public static void main(String[] args) {
        Scanner input1 = new Scanner(System.in);
        int maxStudents = 50; // Maximum number of students
        String[] studentNames = new String[maxStudents];
        int[][] studentGrades = new int[maxStudents][5]; // Assuming 5 grades per student
        int numStudents = 0;

        // Load data from the file when the program starts
        loadGradebookFromFile(studentNames, studentGrades, maxStudents);
        numStudents = countStudents(studentNames);

        while (true) {
            System.out.println("\nGradebook System Menu:");
            System.out.println("1. Add a student");
            System.out.println("2. Assign grades to a student");
            System.out.println("3. Calculate and display class statistics");
            System.out.println("4. Retrieve individual student grades");
            System.out.println("5. Exit");

            int choice = input1.nextInt();

            switch (choice) {
                case 1:
                    if (numStudents < maxStudents) {
                        System.out.print("Enter the student's name: ");
                        input1.nextLine(); // Consume the newline character
                        String name = input1.nextLine();
                        studentNames[numStudents] = name;
                        numStudents++;
                        System.out.println(name + " has been added to the gradebook.");

                        // Save data to the file after adding a student
                        saveGradebookToFile(studentNames, studentGrades, numStudents);
                    } else {
                        System.out.println("The gradebook is full. Cannot add more students.");
                    }
                    break;
                case 2:
                    if (numStudents > 0) {
                        System.out.print("Enter the student's name: ");
                        input1.nextLine(); // Consume the newline character
                        String name = input1.nextLine();
                        int studentIndex = findStudentIndex(studentNames, numStudents, name);

                        if (studentIndex != -1) {
                            System.out.print("Enter 5 grades for " + name + ": ");
                            for (int i = 0; i < 5; i++) {
                                studentGrades[studentIndex][i] = input1.nextInt();
                            }

                            // Save data to the file after assigning grades
                            saveGradebookToFile(studentNames, studentGrades, numStudents);
                        } else {
                            System.out.println("Student not found in the gradebook.");
                        }
                    } else {
                        System.out.println("No students in the gradebook. Add a student first.");
                    }
                    break;
                case 3:
                    if (numStudents > 0) {
                        calculateAndDisplayStatistics(studentGrades, studentNames, numStudents);
                    } else {
                        System.out.println("No students in the gradebook. Add a student first.");
                    }
                    break;
                case 4:
                    if (numStudents > 0) {
                        System.out.print("Enter the student's name: ");
                        input1.nextLine(); // Consume the newline character
                        String name = input1.nextLine();
                        int studentIndex = findStudentIndex(studentNames, numStudents, name);

                        if (studentIndex != -1) {
                            displayStudentGrades(name, studentGrades[studentIndex]);
                        } else {
                            System.out.println("Student not found in the gradebook.");
                        }
                    } else {
                        System.out.println("No students in the gradebook. Add a student first.");
                    }
                    break;
                case 5:
                    System.out.println("Exiting the gradebook system.");

                    // Save data to the file before exiting
                    saveGradebookToFile(studentNames, studentGrades, numStudents);

                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    // Helper method to calculate and display class statistics
    public static void calculateAndDisplayStatistics(int[][] grades, String[] names, int numStudents) {
        double classAverage = 0;
        int highestGrade = Integer.MIN_VALUE;
        int lowestGrade = Integer.MAX_VALUE;

        for (int i = 0; i < numStudents; i++) {
            double studentAverage = 0;
            for (int j = 0; j < 5; j++) {
                studentAverage += grades[i][j];
                classAverage += grades[i][j];
                if (grades[i][j] > highestGrade) {
                    highestGrade = grades[i][j];
                }
                if (grades[i][j] < lowestGrade) {
                    lowestGrade = grades[i][j];
                }
            }
            studentAverage /= 5;
            System.out.println(names[i] + "'s average grade: " + studentAverage);
        }

        classAverage /= (numStudents * 5);
        System.out.println("Class average grade: " + classAverage);
        System.out.println("Highest grade: " + highestGrade);
        System.out.println("Lowest grade: " + lowestGrade);
    }

    // Helper method to display individual student grades
    public static void displayStudentGrades(String name, int[] grades) {
        System.out.println(name + "'s grades:");
        for (int i = 0; i < 5; i++) {
            System.out.println("Grade " + (i + 1) + ": " + grades[i]);
        }
    }

    // Helper method to find the index of a student by name
    public static int findStudentIndex(String[] names, int numStudents, String name) {
        for (int i = 0; i < numStudents; i++) {
            if (names[i] != null && names[i].equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1; // Student not found
    }

    // Helper method to save gradebook data to a file
    public static void saveGradebookToFile(String[] names, int[][] grades, int numStudents) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < numStudents; i++) {
                writer.write(names[i] + ",");
                for (int j = 0; j < 5; j++) {
                    writer.write(grades[i][j] + ",");
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to load gradebook data from a file
    public static void loadGradebookFromFile(String[] names, int[][] grades, int maxStudents) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                names[i] = parts[0];
                for (int j = 1; j <= 5; j++) {
                    grades[i][j - 1] = Integer.parseInt(parts[j]);
                }
                i++;
            }
        } catch (IOException e) {
            // File may not exist yet, ignore
        }
    }

    // Helper method to count the number of students in the gradebook
    public static int countStudents(String[] names) {
        int count = 0;
        for (String name : names) {
            if (name != null) {
                count++;
            }
        }
        return count;
    }
}



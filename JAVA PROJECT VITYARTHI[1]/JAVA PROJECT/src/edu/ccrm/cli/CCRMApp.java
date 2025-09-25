package edu.ccrm.cli;

import edu.ccrm.config.AppConfig;
import edu.ccrm.domain.*;
import edu.ccrm.exceptions.DuplicateEnrollmentException;
import edu.ccrm.io.BackupService;
import edu.ccrm.io.ImportExportService;
import edu.ccrm.service.*;
import edu.ccrm.util.ConsoleUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Main CLI application demonstrating menu-driven flow,
 * decision structures (switch), loops, labeled jump, etc.
 */
public class CCRMApp {
    private final StudentService studentService = new StudentServiceImpl();
    private final CourseService courseService = new CourseServiceImpl();
    private final EnrollmentService enrollmentService = new EnrollmentServiceImpl();
    private final ImportExportService ioService = new ImportExportService();
    private final BackupService backupService = new BackupService();
    private final AppConfig cfg = AppConfig.getInstance();

    public static void main(String[] args) {
        new CCRMApp().start();
    }

    public void start() {
        ConsoleUtil.bannerPrinter("CCRM - Campus Course & Records Manager").run();
        boolean running = true;
        while (running) {
            printMenu();
            int opt = ConsoleUtil.readInt("Choose option");
            switch (opt) {
                case 1 -> manageStudents();
                case 2 -> manageCourses();
                case 3 -> manageEnrollment();
                case 4 -> importExport();
                case 5 -> backup();
                case 6 -> {
                    System.out.println("Config: " + cfg);
                    running = false;
                }
                default -> System.out.println("Invalid option");
            }
        }
        System.out.println("Exiting... goodbye");
    }

    private void printMenu() {
        System.out.println("\n1. Manage Students\n2. Manage Courses\n3. Enrollment & Grades\n4. Import/Export\n5. Backup & Size\n6. Exit");
    }

    private void manageStudents() {
        boolean inner = true;
        while (inner) {
            System.out.println("\nStudents: 1-Add 2-List 3-PrintProfile 4-Back");
            int o = ConsoleUtil.readInt("Choose");
            switch (o) {
                case 1 -> {
                    String id = UUID.randomUUID().toString();
                    String reg = ConsoleUtil.readLine("RegNo");
                    String name = ConsoleUtil.readLine("Full name");
                    String email = ConsoleUtil.readLine("Email");
                    String dob = ConsoleUtil.readLine("DOB (yyyy-mm-dd)");
                    Student s = new Student(id, reg, name, email, LocalDate.parse(dob));
                    studentService.addStudent(s);
                    System.out.println("Added: " + s);
                }
                case 2 -> studentService.listAll().forEach(System.out::println);
                case 3 -> {
                    String id = ConsoleUtil.readLine("Student id");
                    studentService.findById(id).ifPresentOrElse(
                            st -> {
                                System.out.println("Profile: " + st);
                                System.out.println("Transcript:");
                                st.getEnrollments().forEach(System.out::println);
                                System.out.printf("GPA: %.2f%n", enrollmentService.computeGPA(id));
                            },
                            () -> System.out.println("Not found")
                    );
                }
                case 4 -> inner = false;
                default -> System.out.println("Invalid");
            }
        }
    }

    private void manageCourses() {
        boolean inner = true;
        while (inner) {
            System.out.println("\nCourses: 1-Add 2-List 3-SearchByInstructor 4-Back");
            int o = ConsoleUtil.readInt("Choose");
            switch (o) {
                case 1 -> {
                    String code = ConsoleUtil.readLine("Course code");
                    String title = ConsoleUtil.readLine("Title");
                    int credits = ConsoleUtil.readInt("Credits");
                    String dept = ConsoleUtil.readLine("Department");
                    Course c = new Course.Builder(code).title(title).credits(credits).department(dept).build();
                    courseService.addCourse(c);
                    System.out.println("Added: " + c);
                }
                case 2 -> courseService.listAll().forEach(System.out::println);
                case 3 -> {
                    String ins = ConsoleUtil.readLine("Instructor id");
                    courseService.searchByInstructor(ins).forEach(System.out::println);
                }
                case 4 -> inner = false;
                default -> System.out.println("Invalid");
            }
        }
    }

    private void manageEnrollment() {
        boolean inner = true;
        while (inner) {
            System.out.println("\nEnrollment: 1-Enroll 2-Unenroll 3-RecordMarks 4-Back");
            int o = ConsoleUtil.readInt("Choose");
            switch (o) {
                case 1 -> {
                    String sid = ConsoleUtil.readLine("Student id");
                    String code = ConsoleUtil.readLine("Course code");
                    try {
                        enrollmentService.enroll(sid, code);
                        System.out.println("Enrolled");
                    } catch (DuplicateEnrollmentException ex) {
                        System.out.println("Error: " + ex.getMessage());
                    } catch (RuntimeException rex) {
                        System.out.println("Business error: " + rex.getMessage());
                    }
                }
                case 2 -> {
                    String sid = ConsoleUtil.readLine("Student id");
                    String code = ConsoleUtil.readLine("Course code");
                    enrollmentService.unenroll(sid, code);
                    System.out.println("Unenrolled");
                }
                case 3 -> {
                    String sid = ConsoleUtil.readLine("Student id");
                    String code = ConsoleUtil.readLine("Course code");
                    double marks = Double.parseDouble(ConsoleUtil.readLine("Marks"));
                    enrollmentService.recordMarks(sid, code, marks);
                    System.out.println("Recorded");
                }
                case 4 -> inner = false;
                default -> System.out.println("Invalid");
            }
        }
    }

    private void importExport() {
        System.out.println("1-Export 2-Import 3-Back");
        int o = ConsoleUtil.readInt("Choice");
        Path folder = cfg.getDataFolder();
        try {
            if (o == 1) {
                Path out = folder.resolve("export");
                ioService.exportStudents(out);
                ioService.exportCourses(out);
                System.out.println("Exported to " + out.toAbsolutePath());
            } else if (o == 2) {
                Path inStudents = folder.resolve("sample").resolve("students.csv");
                Path inCourses = folder.resolve("sample").resolve("courses.csv");
                ioService.importStudents(inStudents);
                ioService.importCourses(inCourses);
                System.out.println("Imported sample files (if present)");
            }
        } catch (IOException e) {
            System.out.println("IO Error: " + e.getMessage());
        }
    }

    private void backup() {
        try {
            Path source = cfg.getDataFolder().resolve("export");
            Path backup = backupService.createBackup(source);
            long size = backupService.computeBackupSize(backup);
            System.out.println("Backup created at: " + backup.toAbsolutePath());
            System.out.println("Total backup size bytes: " + size);
        } catch (IOException e) {
            System.out.println("Backup error: " + e.getMessage());
        }
    }
}

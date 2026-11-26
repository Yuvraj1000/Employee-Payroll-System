import model.Employee;
import model.PaySlip;
import model.SalaryStructure;
import service.EmployeeService;
import service.PayrollService;
import service.SalaryCalculator;

import java.util.List;
import java.util.Scanner;

/**
 * Employee Payroll System — Main Application.
 * Console-based menu-driven interface for the admin to manage employees,
 * calculate salaries, generate pay slips, and view monthly payroll records.
 * 
 * Project: G12-JAVA-PID-23 | Group G12
 * Submitted by: Yuvraj Singh, Hardik Chhabra, Ishant Goyal
 * Submitted to: Mr. Rahul
 * Department of Computer Science and Engineering, Chitkara University, Punjab
 * 
 * Technologies: Core Java (backend), CSV (data storage), Collections, File I/O
 */
public class EmployeePayrollApp {

    // Data file paths (relative to project root)
    private static final String EMPLOYEE_CSV = "data/employees.csv";
    private static final String PAYROLL_CSV = "data/payroll.csv";

    // Service instances
    private static EmployeeService employeeService;
    private static PayrollService payrollService;

    public static void main(String[] args) {
        // Initialize services (loads existing data from CSV files)
        employeeService = new EmployeeService(EMPLOYEE_CSV);
        payrollService = new PayrollService(PAYROLL_CSV);

        Scanner scanner = new Scanner(System.in);

        printWelcomeBanner();

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readIntInput(scanner, "Enter your choice: ");

            switch (choice) {
                case 1:
                    addEmployee(scanner);
                    break;
                case 2:
                    viewAllEmployees();
                    break;
                case 3:
                    viewEmployeeById(scanner);
                    break;
                case 4:
                    updateEmployee(scanner);
                    break;
                case 5:
                    deleteEmployee(scanner);
                    break;
                case 6:
                    searchEmployee(scanner);
                    break;
                case 7:
                    calculateSalary(scanner);
                    break;
                case 8:
                    generatePaySlip(scanner);
                    break;
                case 9:
                    generateMonthlyPayroll(scanner);
                    break;
                case 10:
                    viewPayrollRecords(scanner);
                    break;
                case 11:
                    viewMonthlyPayrollSummary(scanner);
                    break;
                case 12:
                    viewEmployeesSortedBySalary();
                    break;
                case 0:
                    running = false;
                    System.out.println("\nThank you for using the Employee Payroll System. Goodbye!\n");
                    break;
                default:
                    System.out.println("\n[!] Invalid choice. Please enter a number from the menu.\n");
            }
        }

        scanner.close();
    }

    // ==================== Menu Display ====================

    private static void printWelcomeBanner() {
        System.out.println("╔═══════════════════════════════════════════════════╗");
        System.out.println("║                                                   ║");
        System.out.println("║         EMPLOYEE PAYROLL SYSTEM                   ║");
        System.out.println("║         Project ID: G12-JAVA-PID-23              ║");
        System.out.println("║         Chitkara University, Punjab               ║");
        System.out.println("║                                                   ║");
        System.out.println("╚═══════════════════════════════════════════════════╝");
        System.out.println();
    }

    private static void printMainMenu() {
        System.out.println("╔═══════════════════════════════════════════════════╗");
        System.out.println("║                   MAIN MENU                       ║");
        System.out.println("╠═══════════════════════════════════════════════════╣");
        System.out.println("║  [1]  Add Employee                                ║");
        System.out.println("║  [2]  View All Employees                          ║");
        System.out.println("║  [3]  View Employee by ID                         ║");
        System.out.println("║  [4]  Update Employee                             ║");
        System.out.println("║  [5]  Delete Employee                             ║");
        System.out.println("║  [6]  Search Employee                             ║");
        System.out.println("║  [7]  Calculate Salary                            ║");
        System.out.println("║  [8]  Generate Pay Slip                           ║");
        System.out.println("║  [9]  Generate Monthly Payroll (All Employees)    ║");
        System.out.println("║  [10] View Payroll Records for an Employee        ║");
        System.out.println("║  [11] View Monthly Payroll Summary                ║");
        System.out.println("║  [12] View Employees Sorted by Salary             ║");
        System.out.println("║  [0]  Exit                                        ║");
        System.out.println("╚═══════════════════════════════════════════════════╝");
    }

    // ==================== Feature Implementations ====================

    /** 1. Add Employee */
    private static void addEmployee(Scanner scanner) {
        System.out.println("\n--- Add New Employee ---");
        scanner.nextLine(); // Consume leftover newline (Scanner bug fix)

        System.out.print("Enter Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter Department: ");
        String department = scanner.nextLine().trim();

        System.out.print("Enter Designation: ");
        String designation = scanner.nextLine().trim();

        double basicSalary = readDoubleInput(scanner, "Enter Basic Salary: ");

        try {
            Employee emp = employeeService.addEmployee(name, department, designation, basicSalary);
            System.out.println("\n[+] Employee added successfully!");
            System.out.println(emp);
        } catch (IllegalArgumentException e) {
            System.out.println("\n[!] Error: " + e.getMessage());
        }
        System.out.println();
    }

    /** 2. View All Employees */
    private static void viewAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        if (employees.isEmpty()) {
            System.out.println("\n[i] No employees found in the system.\n");
            return;
        }

        System.out.println("\n--- All Employees (" + employees.size() + ") ---");
        System.out.printf("%-6s  %-20s  %-15s  %-15s  %12s%n",
                "ID", "Name", "Department", "Designation", "Basic Salary");
        System.out.println("-".repeat(76));

        for (Employee emp : employees) {
            System.out.printf("%-6d  %-20s  %-15s  %-15s  %,12.2f%n",
                    emp.getEmpId(), emp.getName(), emp.getDepartment(),
                    emp.getDesignation(), emp.getBasicSalary());
        }
        System.out.println();
    }

    /** 3. View Employee by ID */
    private static void viewEmployeeById(Scanner scanner) {
        int empId = readIntInput(scanner, "\nEnter Employee ID: ");
        Employee emp = employeeService.getEmployee(empId);

        if (emp == null) {
            System.out.println("[!] Employee with ID " + empId + " not found.\n");
        } else {
            System.out.println("\n" + emp + "\n");
        }
    }

    /** 4. Update Employee */
    private static void updateEmployee(Scanner scanner) {
        int empId = readIntInput(scanner, "\nEnter Employee ID to update: ");

        if (!employeeService.employeeExists(empId)) {
            System.out.println("[!] Employee with ID " + empId + " not found.\n");
            return;
        }

        System.out.println("(Press Enter to keep current value)");
        scanner.nextLine(); // Consume leftover newline

        System.out.print("New Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("New Department: ");
        String department = scanner.nextLine().trim();

        System.out.print("New Designation: ");
        String designation = scanner.nextLine().trim();

        System.out.print("New Basic Salary (enter -1 to keep current): ");
        double salary;
        try {
            salary = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            salary = -1; // Keep current
        }

        try {
            boolean updated = employeeService.updateEmployee(empId,
                    name.isEmpty() ? null : name,
                    department.isEmpty() ? null : department,
                    designation.isEmpty() ? null : designation,
                    salary);

            if (updated) {
                System.out.println("\n[+] Employee updated successfully!");
                System.out.println(employeeService.getEmployee(empId));
            }
        } catch (IllegalArgumentException e) {
            System.out.println("\n[!] Error: " + e.getMessage());
        }
        System.out.println();
    }

    /** 5. Delete Employee */
    private static void deleteEmployee(Scanner scanner) {
        int empId = readIntInput(scanner, "\nEnter Employee ID to delete: ");

        if (employeeService.deleteEmployee(empId)) {
            System.out.println("[+] Employee with ID " + empId + " deleted successfully.\n");
        } else {
            System.out.println("[!] Employee with ID " + empId + " not found.\n");
        }
    }

    /** 6. Search Employee */
    private static void searchEmployee(Scanner scanner) {
        System.out.println("\n--- Search Employee ---");
        System.out.println("[1] Search by Name");
        System.out.println("[2] Search by Department");
        int searchChoice = readIntInput(scanner, "Choose: ");

        scanner.nextLine(); // Consume leftover newline
        List<Employee> results;

        if (searchChoice == 1) {
            System.out.print("Enter name keyword: ");
            String keyword = scanner.nextLine().trim();
            results = employeeService.searchByName(keyword);
        } else if (searchChoice == 2) {
            System.out.print("Enter department: ");
            String dept = scanner.nextLine().trim();
            results = employeeService.searchByDepartment(dept);
        } else {
            System.out.println("[!] Invalid choice.\n");
            return;
        }

        if (results.isEmpty()) {
            System.out.println("[i] No matching employees found.\n");
        } else {
            System.out.println("\nSearch Results (" + results.size() + " found):");
            for (Employee emp : results) {
                System.out.println("  " + emp);
            }
            System.out.println();
        }
    }

    /** 7. Calculate Salary for an Employee */
    private static void calculateSalary(Scanner scanner) {
        int empId = readIntInput(scanner, "\nEnter Employee ID: ");
        Employee emp = employeeService.getEmployee(empId);

        if (emp == null) {
            System.out.println("[!] Employee with ID " + empId + " not found.\n");
            return;
        }

        System.out.println("\nSalary Calculation for: " + emp.getName());
        System.out.println(SalaryCalculator.getSalarySummary(emp));
    }

    /** 8. Generate Pay Slip for a Single Employee */
    private static void generatePaySlip(Scanner scanner) {
        int empId = readIntInput(scanner, "\nEnter Employee ID: ");
        Employee emp = employeeService.getEmployee(empId);

        if (emp == null) {
            System.out.println("[!] Employee with ID " + empId + " not found.\n");
            return;
        }

        scanner.nextLine(); // Consume leftover newline
        System.out.print("Enter Month (e.g., November): ");
        String month = scanner.nextLine().trim();

        int year = readIntInput(scanner, "Enter Year (e.g., 2026): ");

        PaySlip paySlip = payrollService.generatePaySlip(emp, month, year);
        if (paySlip != null) {
            System.out.println("\n[+] Pay Slip Generated:\n");
            System.out.println(paySlip.generateSlip());
        }
    }

    /** 9. Generate Monthly Payroll for All Employees */
    private static void generateMonthlyPayroll(Scanner scanner) {
        scanner.nextLine(); // Consume leftover newline
        System.out.print("\nEnter Month (e.g., November): ");
        String month = scanner.nextLine().trim();

        int year = readIntInput(scanner, "Enter Year (e.g., 2026): ");

        List<Employee> allEmployees = employeeService.getAllEmployees();
        if (allEmployees.isEmpty()) {
            System.out.println("[i] No employees in the system to generate payroll.\n");
            return;
        }

        List<PaySlip> generated = payrollService.generateMonthlyPayroll(allEmployees, month, year);
        System.out.println("\n[+] Monthly payroll generated for " + generated.size() + " employee(s).");
        System.out.println(payrollService.getMonthlyPayrollSummary(month, year));
    }

    /** 10. View Payroll Records for a Specific Employee */
    private static void viewPayrollRecords(Scanner scanner) {
        int empId = readIntInput(scanner, "\nEnter Employee ID: ");

        List<PaySlip> paySlips = payrollService.getPaySlipsByEmployee(empId);
        if (paySlips.isEmpty()) {
            System.out.println("[i] No payroll records found for Employee ID " + empId + ".\n");
            return;
        }

        System.out.println("\nPayroll Records for Employee ID " + empId + " (" + paySlips.size() + " records):\n");
        for (PaySlip ps : paySlips) {
            System.out.println(ps.generateSlip());
        }
    }

    /** 11. View Monthly Payroll Summary */
    private static void viewMonthlyPayrollSummary(Scanner scanner) {
        scanner.nextLine(); // Consume leftover newline
        System.out.print("\nEnter Month (e.g., November): ");
        String month = scanner.nextLine().trim();

        int year = readIntInput(scanner, "Enter Year (e.g., 2026): ");

        System.out.println(payrollService.getMonthlyPayrollSummary(month, year));
    }

    /** 12. View Employees Sorted by Salary (Descending) */
    private static void viewEmployeesSortedBySalary() {
        List<Employee> sorted = employeeService.getEmployeesSortedBySalary();
        if (sorted.isEmpty()) {
            System.out.println("\n[i] No employees found.\n");
            return;
        }

        System.out.println("\n--- Employees Sorted by Salary (Highest First) ---");
        System.out.printf("%-6s  %-20s  %-15s  %12s%n",
                "ID", "Name", "Department", "Basic Salary");
        System.out.println("-".repeat(60));

        for (Employee emp : sorted) {
            System.out.printf("%-6d  %-20s  %-15s  %,12.2f%n",
                    emp.getEmpId(), emp.getName(), emp.getDepartment(), emp.getBasicSalary());
        }
        System.out.println();
    }

    // ==================== Input Utilities ====================

    /**
     * Reads an integer from the scanner with input validation.
     * Uses Integer.parseInt with exception handling (Scanner newline bug fix).
     */
    private static int readIntInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("[!] Invalid input. Please enter a valid integer.");
            }
        }
    }

    /**
     * Reads a double from the scanner with input validation.
     * Uses Double.parseDouble with exception handling.
     */
    private static double readDoubleInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(input);
                if (value < 0) {
                    System.out.println("[!] Value cannot be negative. Try again.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("[!] Invalid input. Please enter a valid number.");
            }
        }
    }
}

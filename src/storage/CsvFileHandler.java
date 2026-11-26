package storage;

import model.Employee;
import model.PaySlip;
import model.SalaryStructure;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles reading and writing employee and payroll data to/from CSV files.
 * 
 * Demonstrates: File I/O (BufferedReader, BufferedWriter, FileReader, FileWriter),
 * try-with-resources for exception-safe resource management,
 * line-level validation with fault tolerance (skipping malformed rows),
 * String.split for CSV parsing, Integer.parseInt / Double.parseDouble (Wrapper classes),
 * LinkedHashMap for ordered deduplication.
 */
public class CsvFileHandler {

    // ==================== Employee CSV Operations ====================

    /** Employee CSV header */
    private static final String EMPLOYEE_CSV_HEADER = "empId,name,department,designation,basicSalary";

    /**
     * Imports employee records from a CSV file.
     * Uses try-with-resources to auto-close the BufferedReader.
     * Skips malformed rows with error logging (fault tolerance).
     * 
     * @param filePath path to the CSV file
     * @return a LinkedHashMap of empId -> Employee (insertion-order preserved)
     * @throws IOException if the file cannot be read
     */
    public static Map<Integer, Employee> importEmployees(String filePath) throws IOException {
        Map<Integer, Employee> records = new LinkedHashMap<>();

        // Try-with-resources: BufferedReader wraps FileReader (Decorator pattern)
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Skip CSV header row
            int lineNum = 1;

            while ((line = br.readLine()) != null) {
                lineNum++;
                if (line.isBlank()) continue; // Skip empty lines

                String[] parts = line.split(",", -1); // -1 preserves trailing empty fields
                if (parts.length != 5) {
                    System.err.println("Skipping malformed employee row at line " + lineNum
                            + ": expected 5 fields, found " + parts.length);
                    continue;
                }

                try {
                    // Wrapper class usage: Integer.parseInt, Double.parseDouble
                    int empId = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    String department = parts[2].trim();
                    String designation = parts[3].trim();
                    double basicSalary = Double.parseDouble(parts[4].trim());

                    // Input validation
                    if (name.isEmpty()) {
                        throw new IllegalArgumentException("Name is empty");
                    }
                    if (basicSalary < 0) {
                        throw new IllegalArgumentException("Negative salary: " + basicSalary);
                    }

                    records.put(empId, new Employee(empId, name, department, designation, basicSalary));

                } catch (IllegalArgumentException ex) {
                    // Catches both NumberFormatException and IllegalArgumentException
                    System.err.println("Validation error at line " + lineNum + ": " + ex.getMessage());
                }
            }
        }
        // BufferedReader is auto-closed here by try-with-resources

        return records;
    }

    /**
     * Exports employee records to a CSV file.
     * Uses try-with-resources with BufferedWriter wrapping FileWriter.
     * Creates parent directories if they do not exist.
     * 
     * @param filePath  path to the output CSV file
     * @param employees collection of employees to export
     * @throws IOException if the file cannot be written
     */
    public static void exportEmployees(String filePath, Collection<Employee> employees) throws IOException {
        ensureParentDirectory(filePath);

        // Try-with-resources: BufferedWriter wraps FileWriter
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(EMPLOYEE_CSV_HEADER);
            bw.newLine();

            for (Employee emp : employees) {
                bw.write(emp.toCsvRow());
                bw.newLine();
            }
        }
        // BufferedWriter is auto-closed here
    }

    // ==================== Payroll CSV Operations ====================

    /** Payroll CSV header */
    private static final String PAYROLL_CSV_HEADER =
            "empId,empName,department,designation,month,year,basic,hra,da,otherAllowance,gross,pf,tax,totalDeductions,net";

    /**
     * Imports payroll records from a CSV file.
     * Since PaySlip requires an Employee object for construction, we reconstruct
     * lightweight Employee instances from the CSV data.
     * 
     * @param filePath path to the payroll CSV file
     * @return list of PaySlip objects
     * @throws IOException if the file cannot be read
     */
    public static List<PaySlip> importPayroll(String filePath) throws IOException {
        List<PaySlip> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Skip header
            int lineNum = 1;

            while ((line = br.readLine()) != null) {
                lineNum++;
                if (line.isBlank()) continue;

                String[] parts = line.split(",", -1);
                if (parts.length != 15) {
                    System.err.println("Skipping malformed payroll row at line " + lineNum
                            + ": expected 15 fields, found " + parts.length);
                    continue;
                }

                try {
                    int empId = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    String department = parts[2].trim();
                    String designation = parts[3].trim();
                    String month = parts[4].trim();
                    int year = Integer.parseInt(parts[5].trim());
                    double basic = Double.parseDouble(parts[6].trim());

                    // Reconstruct a lightweight Employee for PaySlip creation
                    Employee emp = new Employee(empId, name, department, designation, basic);
                    PaySlip paySlip = new PaySlip(emp, month, year);
                    records.add(paySlip);

                } catch (IllegalArgumentException ex) {
                    System.err.println("Validation error at payroll line " + lineNum + ": " + ex.getMessage());
                }
            }
        }

        return records;
    }

    /**
     * Exports payroll records to a CSV file.
     * 
     * @param filePath path to the output CSV file
     * @param paySlips list of PaySlip records to export
     * @throws IOException if the file cannot be written
     */
    public static void exportPayroll(String filePath, List<PaySlip> paySlips) throws IOException {
        ensureParentDirectory(filePath);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(PAYROLL_CSV_HEADER);
            bw.newLine();

            for (PaySlip ps : paySlips) {
                bw.write(ps.toCsvRow());
                bw.newLine();
            }
        }
    }

    // ==================== Utility ====================

    /**
     * Ensures the parent directory of the given file path exists.
     * Creates it if necessary.
     * 
     * @param filePath the file path
     */
    private static void ensureParentDirectory(String filePath) {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
    }
}

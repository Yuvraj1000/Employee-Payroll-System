package service;

import model.Employee;
import model.PaySlip;
import model.SalaryStructure;
import storage.CsvFileHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages payroll operations: generate pay slips, maintain monthly payroll records,
 * and provide payroll summaries.
 * 
 * Demonstrates: Composition, ArrayList for ordered records,
 * Stream API, Lambda expressions, File I/O via CSV,
 * try-with-resources (delegated to CsvFileHandler), exception handling.
 */
public class PayrollService {

    /** In-memory list of all generated pay slips (monthly payroll records) */
    private final List<PaySlip> payrollRecords;

    /** Path to the payroll CSV file */
    private final String payrollCsvPath;

    /**
     * Constructs a PayrollService, loading existing payroll records from CSV.
     * 
     * @param payrollCsvPath path to the payroll CSV file
     */
    public PayrollService(String payrollCsvPath) {
        this.payrollCsvPath = payrollCsvPath;
        this.payrollRecords = new ArrayList<>();
        loadFromCsv();
    }

    /**
     * Generates a pay slip for a single employee for a given month and year.
     * Adds the pay slip to the monthly payroll records and saves to CSV.
     * 
     * @param employee the employee
     * @param month    pay month (e.g., "November")
     * @param year     pay year (e.g., 2026)
     * @return the generated PaySlip
     * @throws IllegalArgumentException if employee is null or month is empty
     */
    public PaySlip generatePaySlip(Employee employee, String month, int year) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null.");
        }
        if (month == null || month.trim().isEmpty()) {
            throw new IllegalArgumentException("Month cannot be null or empty.");
        }

        // Check for duplicate: same employee, same month, same year
        boolean alreadyExists = payrollRecords.stream()
                .anyMatch(ps -> ps.getEmpId() == employee.getEmpId()
                        && ps.getMonth().equalsIgnoreCase(month.trim())
                        && ps.getYear() == year);

        if (alreadyExists) {
            System.out.println("Pay slip for Employee ID " + employee.getEmpId()
                    + " for " + month + " " + year + " already exists. Skipping.");
            // Return the existing one
            return payrollRecords.stream()
                    .filter(ps -> ps.getEmpId() == employee.getEmpId()
                            && ps.getMonth().equalsIgnoreCase(month.trim())
                            && ps.getYear() == year)
                    .findFirst()
                    .orElse(null);
        }

        PaySlip paySlip = new PaySlip(employee, month, year);
        payrollRecords.add(paySlip);
        saveToCsv();
        return paySlip;
    }

    /**
     * Generates pay slips for all employees in the given list for a month/year.
     * 
     * @param employees list of employees
     * @param month     pay month
     * @param year      pay year
     * @return list of generated PaySlips
     */
    public List<PaySlip> generateMonthlyPayroll(List<Employee> employees, String month, int year) {
        List<PaySlip> generated = new ArrayList<>();
        for (Employee emp : employees) {
            PaySlip slip = generatePaySlip(emp, month, year);
            if (slip != null) {
                generated.add(slip);
            }
        }
        return generated;
    }

    /**
     * Retrieves all pay slips for a specific employee.
     * Uses Stream API with filter and collect.
     * 
     * @param empId the employee ID
     * @return list of pay slips for the employee
     */
    public List<PaySlip> getPaySlipsByEmployee(int empId) {
        return payrollRecords.stream()
                .filter(ps -> ps.getEmpId() == empId)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all pay slips for a given month and year.
     * 
     * @param month the month name
     * @param year  the year
     * @return list of pay slips for that pay period
     */
    public List<PaySlip> getPaySlipsByMonth(String month, int year) {
        return payrollRecords.stream()
                .filter(ps -> ps.getMonth().equalsIgnoreCase(month.trim()) && ps.getYear() == year)
                .collect(Collectors.toList());
    }

    /**
     * Returns all payroll records as an unmodifiable list.
     */
    public List<PaySlip> getAllPayrollRecords() {
        return Collections.unmodifiableList(payrollRecords);
    }

    /**
     * Calculates the total payroll cost for a given month and year.
     * Uses Stream API with mapToDouble and sum.
     * 
     * @param month the month name
     * @param year  the year
     * @return total net salary disbursed for that period
     */
    public double getMonthlyPayrollTotal(String month, int year) {
        return getPaySlipsByMonth(month, year).stream()
                .mapToDouble(ps -> ps.getSalaryStructure().getNetSalary())
                .sum();
    }

    /**
     * Prints a summary of the monthly payroll.
     */
    public String getMonthlyPayrollSummary(String month, int year) {
        List<PaySlip> monthSlips = getPaySlipsByMonth(month, year);
        if (monthSlips.isEmpty()) {
            return "No payroll records found for " + month + " " + year + ".";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\n╔═══════════════════════════════════════════════════════════════════════╗\n");
        sb.append("║               MONTHLY PAYROLL SUMMARY                               ║\n");
        sb.append(String.format("║               %s %d                                          ║\n", month.toUpperCase(), year));
        sb.append("╠═══════════════════════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║  %-6s  %-20s  %-12s  %-12s  %-12s║\n",
                "ID", "Name", "Gross", "Deductions", "Net Salary"));
        sb.append("╠═══════════════════════════════════════════════════════════════════════╣\n");

        double totalGross = 0;
        double totalDeductions = 0;
        double totalNet = 0;

        for (PaySlip ps : monthSlips) {
            SalaryStructure ss = ps.getSalaryStructure();
            sb.append(String.format("║  %-6d  %-20s  %,10.2f  %,10.2f    %,10.2f  ║\n",
                    ps.getEmpId(), ps.getEmpName(),
                    ss.getGrossSalary(), ss.getTotalDeductions(), ss.getNetSalary()));
            totalGross += ss.getGrossSalary();
            totalDeductions += ss.getTotalDeductions();
            totalNet += ss.getNetSalary();
        }

        sb.append("╠═══════════════════════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║  TOTAL:  %-20s  %,10.2f  %,10.2f    %,10.2f  ║\n",
                monthSlips.size() + " employee(s)", totalGross, totalDeductions, totalNet));
        sb.append("╚═══════════════════════════════════════════════════════════════════════╝\n");

        return sb.toString();
    }

    // ==================== CSV Persistence ====================

    /**
     * Loads payroll records from the CSV file.
     */
    private void loadFromCsv() {
        try {
            List<PaySlip> loaded = CsvFileHandler.importPayroll(payrollCsvPath);
            payrollRecords.addAll(loaded);
        } catch (IOException e) {
            System.out.println("No existing payroll records found. Starting fresh.");
        }
    }

    /**
     * Saves all payroll records to the CSV file.
     */
    private void saveToCsv() {
        try {
            CsvFileHandler.exportPayroll(payrollCsvPath, payrollRecords);
        } catch (IOException e) {
            System.err.println("Error saving payroll data: " + e.getMessage());
        }
    }
}

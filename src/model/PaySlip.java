package model;

/**
 * Represents a monthly pay slip for an employee.
 * Combines employee details with their salary structure for a given month/year.
 * 
 * Demonstrates: Composition (has-a Employee and SalaryStructure),
 * StringBuilder for efficient string building, String.format for formatting.
 */
public class PaySlip {

    private final int empId;
    private final String empName;
    private final String department;
    private final String designation;
    private final String month;
    private final int year;
    private final SalaryStructure salaryStructure;

    /**
     * Constructs a PaySlip from an Employee and a pay period.
     * 
     * @param employee the employee record
     * @param month    the pay month (e.g., "January")
     * @param year     the pay year (e.g., 2026)
     * @throws IllegalArgumentException if employee is null or month is empty
     */
    public PaySlip(Employee employee, String month, int year) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null.");
        }
        if (month == null || month.trim().isEmpty()) {
            throw new IllegalArgumentException("Month cannot be null or empty.");
        }
        this.empId = employee.getEmpId();
        this.empName = employee.getName();
        this.department = employee.getDepartment();
        this.designation = employee.getDesignation();
        this.month = month.trim();
        this.year = year;
        this.salaryStructure = new SalaryStructure(employee.getBasicSalary());
    }

    // ---- Getters ----

    public int getEmpId()                       { return empId; }
    public String getEmpName()                  { return empName; }
    public String getDepartment()               { return department; }
    public String getDesignation()              { return designation; }
    public String getMonth()                    { return month; }
    public int getYear()                        { return year; }
    public SalaryStructure getSalaryStructure() { return salaryStructure; }

    /**
     * Generates a formatted pay slip string for display/printing.
     */
    public String generateSlip() {
        StringBuilder sb = new StringBuilder();
        sb.append("╔═══════════════════════════════════════════════════╗\n");
        sb.append("║            EMPLOYEE SALARY SLIP                  ║\n");
        sb.append("╠═══════════════════════════════════════════════════╣\n");
        sb.append(String.format("║  Pay Period  : %-15s  Year: %-6d    ║\n", month, year));
        sb.append("╠═══════════════════════════════════════════════════╣\n");
        sb.append(String.format("║  Emp ID      : %-35d║\n", empId));
        sb.append(String.format("║  Name        : %-35s║\n", empName));
        sb.append(String.format("║  Department  : %-35s║\n", department));
        sb.append(String.format("║  Designation : %-35s║\n", designation));
        sb.append("╠═══════════════════════════════════════════════════╣\n");
        sb.append("║               EARNINGS                           ║\n");
        sb.append("╠═══════════════════════════════════════════════════╣\n");
        sb.append(String.format("║  Basic Salary       : Rs. %,14.2f        ║\n", salaryStructure.getBasicSalary()));
        sb.append(String.format("║  HRA                : Rs. %,14.2f        ║\n", salaryStructure.getHra()));
        sb.append(String.format("║  DA                 : Rs. %,14.2f        ║\n", salaryStructure.getDa()));
        sb.append(String.format("║  Other Allowance    : Rs. %,14.2f        ║\n", salaryStructure.getOtherAllowance()));
        sb.append("╠═══════════════════════════════════════════════════╣\n");
        sb.append(String.format("║  GROSS SALARY       : Rs. %,14.2f        ║\n", salaryStructure.getGrossSalary()));
        sb.append("╠═══════════════════════════════════════════════════╣\n");
        sb.append("║               DEDUCTIONS                         ║\n");
        sb.append("╠═══════════════════════════════════════════════════╣\n");
        sb.append(String.format("║  Provident Fund     : Rs. %,14.2f        ║\n", salaryStructure.getPfDeduction()));
        sb.append(String.format("║  Professional Tax   : Rs. %,14.2f        ║\n", salaryStructure.getTaxDeduction()));
        sb.append(String.format("║  Total Deductions   : Rs. %,14.2f        ║\n", salaryStructure.getTotalDeductions()));
        sb.append("╠═══════════════════════════════════════════════════╣\n");
        sb.append(String.format("║  NET SALARY         : Rs. %,14.2f        ║\n", salaryStructure.getNetSalary()));
        sb.append("╚═══════════════════════════════════════════════════╝\n");
        return sb.toString();
    }

    /**
     * Converts this pay slip to a CSV row for payroll records.
     * Format: empId,empName,department,designation,month,year,basic,hra,da,otherAllowance,gross,pf,tax,totalDeductions,net
     */
    public String toCsvRow() {
        SalaryStructure s = salaryStructure;
        return empId + "," + empName + "," + department + "," + designation + ","
                + month + "," + year + ","
                + String.format("%.2f", s.getBasicSalary()) + ","
                + String.format("%.2f", s.getHra()) + ","
                + String.format("%.2f", s.getDa()) + ","
                + String.format("%.2f", s.getOtherAllowance()) + ","
                + String.format("%.2f", s.getGrossSalary()) + ","
                + String.format("%.2f", s.getPfDeduction()) + ","
                + String.format("%.2f", s.getTaxDeduction()) + ","
                + String.format("%.2f", s.getTotalDeductions()) + ","
                + String.format("%.2f", s.getNetSalary());
    }

    @Override
    public String toString() {
        return generateSlip();
    }
}

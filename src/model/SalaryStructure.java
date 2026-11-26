package model;

/**
 * Represents the salary breakdown for an employee.
 * Contains basic salary, allowances (HRA, DA, Other),
 * deductions (PF, Professional Tax), and computed gross/net salary.
 * 
 * Demonstrates: Encapsulation, immutable computed fields,
 * String.format for currency formatting.
 */
public class SalaryStructure {

    // --- Allowance percentages (based on basic salary) ---
    public static final double HRA_PERCENT = 20.0;   // House Rent Allowance: 20% of basic
    public static final double DA_PERCENT = 15.0;     // Dearness Allowance: 15% of basic
    public static final double OTHER_ALLOWANCE_PERCENT = 10.0; // Other Allowance: 10% of basic

    // --- Deduction percentages (based on gross salary) ---
    public static final double PF_PERCENT = 12.0;     // Provident Fund: 12% of basic
    public static final double TAX_PERCENT = 5.0;      // Professional Tax: 5% of gross

    private final double basicSalary;
    private final double hra;
    private final double da;
    private final double otherAllowance;
    private final double grossSalary;
    private final double pfDeduction;
    private final double taxDeduction;
    private final double totalDeductions;
    private final double netSalary;

    /**
     * Constructs a SalaryStructure by computing all components from the basic salary.
     * 
     * @param basicSalary the employee's basic salary (must be >= 0)
     * @throws IllegalArgumentException if basicSalary is negative
     */
    public SalaryStructure(double basicSalary) {
        if (basicSalary < 0) {
            throw new IllegalArgumentException("Basic salary cannot be negative: " + basicSalary);
        }
        this.basicSalary = basicSalary;

        // Calculate allowances (percentage of basic salary)
        this.hra = basicSalary * HRA_PERCENT / 100.0;
        this.da = basicSalary * DA_PERCENT / 100.0;
        this.otherAllowance = basicSalary * OTHER_ALLOWANCE_PERCENT / 100.0;

        // Gross salary = Basic + all allowances
        this.grossSalary = basicSalary + hra + da + otherAllowance;

        // Calculate deductions
        this.pfDeduction = basicSalary * PF_PERCENT / 100.0;      // PF on basic
        this.taxDeduction = grossSalary * TAX_PERCENT / 100.0;      // Tax on gross

        this.totalDeductions = pfDeduction + taxDeduction;

        // Net salary = Gross - Total Deductions
        this.netSalary = grossSalary - totalDeductions;
    }

    // ---- Getters ----

    public double getBasicSalary()      { return basicSalary; }
    public double getHra()              { return hra; }
    public double getDa()               { return da; }
    public double getOtherAllowance()   { return otherAllowance; }
    public double getGrossSalary()      { return grossSalary; }
    public double getPfDeduction()      { return pfDeduction; }
    public double getTaxDeduction()     { return taxDeduction; }
    public double getTotalDeductions()  { return totalDeductions; }
    public double getNetSalary()        { return netSalary; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("+-----------------------------------------+\n");
        sb.append("|         SALARY BREAKDOWN                |\n");
        sb.append("+-----------------------------------------+\n");
        sb.append(String.format("| Basic Salary       : Rs. %,12.2f   |\n", basicSalary));
        sb.append(String.format("| HRA (%.0f%%)           : Rs. %,12.2f   |\n", HRA_PERCENT, hra));
        sb.append(String.format("| DA (%.0f%%)            : Rs. %,12.2f   |\n", DA_PERCENT, da));
        sb.append(String.format("| Other Allowance    : Rs. %,12.2f   |\n", otherAllowance));
        sb.append("+-----------------------------------------+\n");
        sb.append(String.format("| GROSS SALARY       : Rs. %,12.2f   |\n", grossSalary));
        sb.append("+-----------------------------------------+\n");
        sb.append(String.format("| PF Deduction (%.0f%%) : Rs. %,12.2f   |\n", PF_PERCENT, pfDeduction));
        sb.append(String.format("| Tax Deduction (%.0f%%): Rs. %,12.2f   |\n", TAX_PERCENT, taxDeduction));
        sb.append(String.format("| Total Deductions   : Rs. %,12.2f   |\n", totalDeductions));
        sb.append("+-----------------------------------------+\n");
        sb.append(String.format("| NET SALARY         : Rs. %,12.2f   |\n", netSalary));
        sb.append("+-----------------------------------------+\n");
        return sb.toString();
    }
}

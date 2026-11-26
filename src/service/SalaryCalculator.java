package service;

import model.Employee;
import model.SalaryStructure;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides salary calculation operations for employees.
 * Computes gross salary, applies deductions, and determines net salary.
 * 
 * Demonstrates: Composition, Stream API for aggregate calculations,
 * Lambda expressions, method references, immutable result objects.
 */
public class SalaryCalculator {

    /**
     * Calculates the full salary structure for a given employee.
     * 
     * @param employee the employee whose salary is to be calculated
     * @return a SalaryStructure with all computed components
     * @throws IllegalArgumentException if employee is null
     */
    public static SalaryStructure calculateSalary(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null.");
        }
        return new SalaryStructure(employee.getBasicSalary());
    }

    /**
     * Calculates the gross salary for a given basic salary.
     * Gross = Basic + HRA + DA + Other Allowance
     * 
     * @param basicSalary the basic salary amount
     * @return the gross salary
     */
    public static double calculateGrossSalary(double basicSalary) {
        double hra = basicSalary * SalaryStructure.HRA_PERCENT / 100.0;
        double da = basicSalary * SalaryStructure.DA_PERCENT / 100.0;
        double other = basicSalary * SalaryStructure.OTHER_ALLOWANCE_PERCENT / 100.0;
        return basicSalary + hra + da + other;
    }

    /**
     * Calculates total deductions for a given basic and gross salary.
     * Deductions = PF (on basic) + Tax (on gross)
     * 
     * @param basicSalary the basic salary
     * @param grossSalary the gross salary
     * @return total deductions
     */
    public static double calculateDeductions(double basicSalary, double grossSalary) {
        double pf = basicSalary * SalaryStructure.PF_PERCENT / 100.0;
        double tax = grossSalary * SalaryStructure.TAX_PERCENT / 100.0;
        return pf + tax;
    }

    /**
     * Calculates the net salary.
     * Net = Gross - Deductions
     * 
     * @param grossSalary    the gross salary
     * @param totalDeductions total deductions
     * @return the net (take-home) salary
     */
    public static double calculateNetSalary(double grossSalary, double totalDeductions) {
        return grossSalary - totalDeductions;
    }

    /**
     * Calculates the total monthly payroll cost for a list of employees.
     * Uses Stream API with mapToDouble and sum.
     * 
     * @param employees list of employees
     * @return total gross salary of all employees
     */
    public static double calculateTotalPayroll(List<Employee> employees) {
        return employees.stream()
                .mapToDouble(emp -> calculateGrossSalary(emp.getBasicSalary()))
                .sum();
    }

    /**
     * Calculates the average net salary across a list of employees.
     * Uses Stream API with mapToDouble and average.
     * 
     * @param employees list of employees
     * @return average net salary, or 0 if list is empty
     */
    public static double calculateAverageNetSalary(List<Employee> employees) {
        return employees.stream()
                .mapToDouble(emp -> {
                    SalaryStructure ss = new SalaryStructure(emp.getBasicSalary());
                    return ss.getNetSalary();
                })
                .average()
                .orElse(0.0);
    }

    /**
     * Returns a formatted summary of salary calculations for an employee.
     */
    public static String getSalarySummary(Employee employee) {
        SalaryStructure ss = calculateSalary(employee);
        return ss.toString();
    }
}

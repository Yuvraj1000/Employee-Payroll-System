package service;

import model.Employee;
import storage.CsvFileHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manages employee records: add, view, update, delete, and search.
 * Uses a LinkedHashMap for O(1) lookups by ID while preserving insertion order.
 * 
 * Demonstrates: HashMap/LinkedHashMap, ArrayList, Collections utilities,
 * Comparable/Comparator, Lambda expressions, Stream API, input validation,
 * exception handling.
 */
public class EmployeeService {

    /** Internal storage: empId -> Employee (insertion-order preserved) */
    private final Map<Integer, Employee> employeeMap;

    /** Path to the employee CSV file */
    private final String csvFilePath;

    /** Auto-increment ID counter */
    private int nextId;

    /**
     * Constructs the service, loading existing employees from CSV if available.
     * 
     * @param csvFilePath path to the employees CSV file
     */
    public EmployeeService(String csvFilePath) {
        this.csvFilePath = csvFilePath;
        this.employeeMap = new LinkedHashMap<>();
        this.nextId = 1;

        // Load existing data from CSV file
        loadFromCsv();
    }

    // ==================== CRUD Operations ====================

    /**
     * Adds a new employee to the system and persists to CSV.
     * 
     * @param name        employee name (non-null, non-empty)
     * @param department  department name
     * @param designation job designation
     * @param basicSalary basic salary (>= 0)
     * @return the newly created Employee
     * @throws IllegalArgumentException if validation fails
     */
    public Employee addEmployee(String name, String department, String designation, double basicSalary) {
        Employee emp = new Employee(nextId, name, department, designation, basicSalary);
        employeeMap.put(nextId, emp);
        nextId++;
        saveToCsv();
        return emp;
    }

    /**
     * Retrieves an employee by their ID.
     * 
     * @param empId the employee ID
     * @return the Employee, or null if not found
     */
    public Employee getEmployee(int empId) {
        return employeeMap.get(empId);
    }

    /**
     * Returns all employees as an unmodifiable list sorted by ID (natural order).
     * Uses Comparable (Employee implements Comparable by empId).
     * 
     * @return sorted list of all employees
     */
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>(employeeMap.values());
        Collections.sort(employees); // Uses Employee.compareTo (by empId)
        return Collections.unmodifiableList(employees);
    }

    /**
     * Updates an existing employee's details.
     * Only non-null, non-empty fields are updated.
     * 
     * @param empId          the employee ID to update
     * @param newName        new name (null to keep existing)
     * @param newDepartment  new department (null to keep existing)
     * @param newDesignation new designation (null to keep existing)
     * @param newBasicSalary new basic salary (negative value means keep existing)
     * @return true if employee was found and updated; false if not found
     * @throws IllegalArgumentException if validation of new values fails
     */
    public boolean updateEmployee(int empId, String newName, String newDepartment,
                                  String newDesignation, double newBasicSalary) {
        Employee emp = employeeMap.get(empId);
        if (emp == null) {
            return false;
        }
        if (newName != null && !newName.trim().isEmpty()) {
            emp.setName(newName);
        }
        if (newDepartment != null && !newDepartment.trim().isEmpty()) {
            emp.setDepartment(newDepartment);
        }
        if (newDesignation != null && !newDesignation.trim().isEmpty()) {
            emp.setDesignation(newDesignation);
        }
        if (newBasicSalary >= 0) {
            emp.setBasicSalary(newBasicSalary);
        }
        saveToCsv();
        return true;
    }

    /**
     * Deletes an employee by their ID.
     * 
     * @param empId the employee ID
     * @return true if removed; false if not found
     */
    public boolean deleteEmployee(int empId) {
        Employee removed = employeeMap.remove(empId);
        if (removed != null) {
            saveToCsv();
            return true;
        }
        return false;
    }

    /**
     * Searches employees by name (case-insensitive partial match).
     * Uses Stream API with filter and lambda expression.
     * 
     * @param keyword the search keyword
     * @return list of matching employees
     */
    public List<Employee> searchByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }
        String lowerKeyword = keyword.trim().toLowerCase();

        // Stream API: filter with lambda, collect to List
        return employeeMap.values().stream()
                .filter(emp -> emp.getName().toLowerCase().contains(lowerKeyword))
                .sorted() // natural order (by empId via Comparable)
                .collect(Collectors.toList());
    }

    /**
     * Searches employees by department (case-insensitive exact match).
     * 
     * @param department the department name
     * @return list of employees in the given department
     */
    public List<Employee> searchByDepartment(String department) {
        if (department == null || department.trim().isEmpty()) {
            return new ArrayList<>();
        }
        String lowerDept = department.trim().toLowerCase();

        return employeeMap.values().stream()
                .filter(emp -> emp.getDepartment().toLowerCase().equals(lowerDept))
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Returns employees sorted by basic salary (descending) using Comparator.
     * Demonstrates Comparator with method reference and reversed().
     * 
     * @return employees sorted by salary descending
     */
    public List<Employee> getEmployeesSortedBySalary() {
        List<Employee> employees = new ArrayList<>(employeeMap.values());

        // Comparator using method reference and reversed() for descending order
        Comparator<Employee> bySalaryDesc = Comparator
                .comparingDouble(Employee::getBasicSalary)
                .reversed();

        employees.sort(bySalaryDesc);
        return employees;
    }

    /**
     * Returns the total count of employees.
     */
    public int getEmployeeCount() {
        return employeeMap.size();
    }

    /**
     * Checks if an employee with the given ID exists.
     */
    public boolean employeeExists(int empId) {
        return employeeMap.containsKey(empId);
    }

    // ==================== CSV Persistence ====================

    /**
     * Loads employee data from CSV file into the map.
     * Uses CsvFileHandler with try-with-resources and line-level validation.
     */
    private void loadFromCsv() {
        try {
            Map<Integer, Employee> loaded = CsvFileHandler.importEmployees(csvFilePath);
            employeeMap.putAll(loaded);

            // Update nextId to be one more than the maximum existing ID
            if (!employeeMap.isEmpty()) {
                int maxId = employeeMap.keySet().stream()
                        .max(Integer::compareTo)
                        .orElse(0);
                nextId = maxId + 1;
            }
        } catch (IOException e) {
            // File may not exist yet on first run — this is expected
            System.out.println("No existing employee data found. Starting fresh.");
        }
    }

    /**
     * Saves all employee data to the CSV file.
     */
    private void saveToCsv() {
        try {
            CsvFileHandler.exportEmployees(csvFilePath, employeeMap.values());
        } catch (IOException e) {
            System.err.println("Error saving employee data: " + e.getMessage());
        }
    }
}

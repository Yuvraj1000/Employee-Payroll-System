package model;

/**
 * Represents an employee record in the payroll system.
 * Implements Comparable for natural ordering by employee ID.
 * 
 * Demonstrates: Encapsulation, Comparable interface, equals/hashCode contract,
 * String handling, Wrapper classes (Integer, Double).
 */
public class Employee implements Comparable<Employee> {

    private int empId;
    private String name;
    private String department;
    private String designation;
    private double basicSalary;

    // ---- Constructors ----

    public Employee(int empId, String name, String department, String designation, double basicSalary) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee name cannot be null or empty.");
        }
        if (basicSalary < 0) {
            throw new IllegalArgumentException("Basic salary cannot be negative.");
        }
        this.empId = empId;
        this.name = name.trim();
        this.department = (department != null) ? department.trim() : "General";
        this.designation = (designation != null) ? designation.trim() : "Staff";
        this.basicSalary = basicSalary;
    }

    // ---- Getters and Setters ----

    public int getEmpId() {
        return empId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee name cannot be null or empty.");
        }
        this.name = name.trim();
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = (department != null) ? department.trim() : "General";
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = (designation != null) ? designation.trim() : "Staff";
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(double basicSalary) {
        if (basicSalary < 0) {
            throw new IllegalArgumentException("Basic salary cannot be negative.");
        }
        this.basicSalary = basicSalary;
    }

    // ---- Comparable: Natural ordering by empId ----

    @Override
    public int compareTo(Employee other) {
        return Integer.compare(this.empId, other.empId);
    }

    // ---- equals and hashCode contract (critical for HashMap/HashSet) ----

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Employee other = (Employee) obj;
        return this.empId == other.empId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(empId);
    }

    // ---- String representation ----

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Employee{")
          .append("ID=").append(empId)
          .append(", Name='").append(name).append('\'')
          .append(", Dept='").append(department).append('\'')
          .append(", Designation='").append(designation).append('\'')
          .append(", BasicSalary=").append(String.format("%.2f", basicSalary))
          .append('}');
        return sb.toString();
    }

    /**
     * Converts this employee record to a CSV row string.
     * Format: empId,name,department,designation,basicSalary
     */
    public String toCsvRow() {
        return empId + "," + name + "," + department + "," + designation + "," + String.format("%.2f", basicSalary);
    }
}

# Employee Payroll System

**Project ID:** G12-JAVA-PID-23 | **Group:** G12  
**Submitted by:** Yuvraj Singh, Hardik Chhabra, Ishant Goyal  
**Submitted to:** Mr. Rahul  
**Department of Computer Science and Engineering, Chitkara University, Punjab**

---

## Project Description

The **Employee Payroll System** is a Java-based application that manages employee records, calculates salaries with allowances and deductions, generates pay slips, and maintains monthly payroll records. It features a Core Java console-based backend with CSV file storage and an HTML/CSS web interface for visual reference.

## Features

1. **Employee Management** — Add, view, update, delete, and search employee records
2. **Salary Structure** — Basic salary with HRA (20%), DA (15%), and Other Allowance (10%)
3. **Salary Calculation** — Compute gross salary, apply deductions (PF 12%, Tax 5%), calculate net salary
4. **Pay Slip Generation** — Generate formatted salary slips for employees
5. **Monthly Payroll Records** — Maintain and view monthly payroll summaries
6. **Data Persistence** — Employee and payroll data stored in CSV files using Java File I/O

## Technology Stack

| Component      | Technology                              |
|----------------|-----------------------------------------|
| **Backend**    | Core Java                               |
| **Frontend**   | HTML + CSS (forms, tables, salary slips)|
| **Data Storage** | CSV files (Java File I/O)             |
| **Collections** | ArrayList, HashMap, LinkedHashMap      |

## Project Structure

```
employee payroll/
├── src/
│   ├── model/
│   │   ├── Employee.java          # Employee record (Comparable, equals/hashCode)
│   │   ├── SalaryStructure.java   # Salary breakdown (allowances & deductions)
│   │   └── PaySlip.java           # Monthly pay slip (composition)
│   ├── service/
│   │   ├── EmployeeService.java   # CRUD operations (HashMap, Stream API)
│   │   ├── SalaryCalculator.java  # Salary computation (static methods, Streams)
│   │   └── PayrollService.java    # Payroll generation & records (ArrayList, Streams)
│   ├── storage/
│   │   └── CsvFileHandler.java    # CSV File I/O (try-with-resources, BufferedReader/Writer)
│   └── EmployeePayrollApp.java    # Main application (console menu)
├── data/
│   ├── employees.csv              # Employee data storage
│   └── payroll.csv                # Payroll records storage
├── web/
│   ├── index.html                 # Web interface (all pages)
│   └── style.css                  # Styling
└── README.md                      # This file
```

## How to Compile and Run

### Prerequisites
- Java JDK 8 or higher installed
- Command line / terminal access

### Compilation
```bash
cd "d:\UNIVERISTY\SEM3\JAVA\employee payroll"
javac -d out src/model/*.java src/storage/*.java src/service/*.java src/EmployeePayrollApp.java
```

### Running the Application
```bash
java -cp out EmployeePayrollApp
```

### Viewing the Web Interface
Open `web/index.html` in any web browser.

## Java Concepts Demonstrated

| Concept                    | Where Used                                            |
|----------------------------|-------------------------------------------------------|
| **OOP (Encapsulation)**    | Employee, SalaryStructure, PaySlip classes             |
| **Comparable interface**   | Employee.compareTo() for natural ordering by ID        |
| **Comparator**             | EmployeeService — sort by salary descending            |
| **equals() / hashCode()**  | Employee — contract for HashMap/HashSet usage          |
| **Collections (ArrayList)**| PayrollService — ordered payroll records               |
| **Collections (HashMap)**  | EmployeeService — O(1) lookup by employee ID           |
| **Collections (LinkedHashMap)** | CsvFileHandler — insertion-order preserved import |
| **Stream API & Lambdas**   | Search, filter, aggregate operations across services   |
| **Exception Handling**     | try-catch, IllegalArgumentException, input validation  |
| **Try-with-resources**     | CsvFileHandler — auto-close BufferedReader/Writer      |
| **File I/O (Buffered)**    | CsvFileHandler — BufferedReader, BufferedWriter        |
| **StringBuilder**          | PaySlip.generateSlip(), Employee.toString()            |
| **Wrapper Classes**        | Integer.parseInt, Double.parseDouble in CSV parsing    |
| **String Methods**         | trim(), split(), toLowerCase(), isEmpty(), contains()  |
| **Input Validation**       | Scanner bug fix, range checks, null checks             |

## Salary Calculation Formula

```
Allowances:
  HRA             = Basic × 20%
  DA              = Basic × 15%
  Other Allowance = Basic × 10%

Gross Salary = Basic + HRA + DA + Other Allowance

Deductions:
  PF  = Basic × 12%
  Tax = Gross × 5%

Net Salary = Gross Salary - (PF + Tax)
```

## Sample Output

```
╔═══════════════════════════════════════════════════╗
║         EMPLOYEE PAYROLL SYSTEM                   ║
║         Project ID: G12-JAVA-PID-23              ║
║         Chitkara University, Punjab               ║
╚═══════════════════════════════════════════════════╝

--- All Employees (5) ---
ID      Name                  Department       Designation      Basic Salary
----------------------------------------------------------------------------
1       Yuvraj Singh          Engineering      Software Dev       50,000.00
2       Hardik Chhabra        Engineering      Backend Dev        48,000.00
3       Ishant Goyal          Finance          Accounts Mgr       55,000.00
4       Priya Sharma          HR               HR Executive       42,000.00
5       Rahul Kumar           Marketing        Marketing Analyst  55,000.00
```

---

*Employee Payroll System © 2026 | Chitkara University, Punjab*

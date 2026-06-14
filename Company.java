import java.util.ArrayList;
import java.util.List;

public class Company {

    private String name;
    private double cash;
    private List<Employee> employees;
    private List<Project> projects;

    public Company(String name, double cash) {
        validateName(name);
        validateCash(cash);
        this.name = name;
        this.cash = cash;
        this.employees = new ArrayList<>();
        this.projects = new ArrayList<>();
    }

    public void hire(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null.");
        }
        employees.add(employee);
    }

    public void startProject(Project project) {
        if (project == null) {
            throw new IllegalArgumentException("Project cannot be null.");
        }
        projects.add(project);
    }

    public void showStatus() {
        System.out.println("=== STATUS FIRMY ===");
        System.out.println("Nazwa: " + name);
        System.out.println("Gotówka: " + cash);
        System.out.println("Pracownicy: " + employees.size());
        System.out.println("Projekty: " + projects.size());
        System.out.println();

        if (employees.isEmpty()) {
            System.out.println("Brak zatrudnionych pracowników.");
        } else {
            System.out.println("Pracownicy:");
            for (Employee employee : employees) {
                System.out.println("- " + employee.getRoleName()
                        + ": " + employee.getName()
                        + " | umiejętność: " + employee.getSkill()
                        + " | wynagrodzenie: " + employee.getSalary());
            }
        }

        System.out.println();

        if (projects.isEmpty()) {
            System.out.println("Brak aktywnych projektów.");
        } else {
            System.out.println("Projekty:");
            for (Project project : projects) {
                System.out.println("- " + project.getName()
                        + " | status: " + project.getStatus()
                        + " | postęp: " + project.getProgress() + "/" + project.getRequiredWork()
                        + " | ukończenie: " + project.getCompletionPercentage() + "%"
                        + " | ukończone: " + project.isFinished());
            }
        }
        System.out.println("======================");
    }

    public String getName() {
        return name;
    }

    public double getCash() {
        return cash;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void reduceCash(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Cannot reduce by negative amount.");
        }
        cash -= amount;
    }

    public void addCash(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Cannot add negative amount.");
        }
        cash += amount;
    }

    public void paySalaries() {
        double totalSalaries = 0;
        for (Employee employee : employees) {
            totalSalaries += employee.getSalary();
        }
        cash -= totalSalaries;
        System.out.println("Wypłacone pensje: " + totalSalaries + ". Pozostała gotówka: " + cash);
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Company name cannot be null or blank.");
        }
    }

    private void validateCash(double cash) {
        if (cash < 0) {
            throw new IllegalArgumentException("Company cash cannot be negative.");
        }
    }
}

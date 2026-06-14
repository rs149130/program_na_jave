public abstract class Employee implements Workable {

    private String name;
    private int skill;
    private double salary;
    private Project assignedProject;

    public Employee(String name, int skill, double salary) {
        validateName(name);
        validateSkill(skill);
        validateSalary(salary);
        this.name = name;
        this.skill = skill;
        this.salary = salary;
        this.assignedProject = null;
    }

    public abstract String getRoleName();

    public Project getAssignedProject() {
        return assignedProject;
    }

    public void setAssignedProject(Project assignedProject) {
        this.assignedProject = assignedProject;
    }

    public String getName() {
        return name;
    }

    public int getSkill() {
        return skill;
    }

    public double getSalary() {
        return salary;
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee name cannot be null or blank.");
        }
    }

    private void validateSkill(int skill) {
        if (skill <= 0) {
            throw new IllegalArgumentException("Employee skill must be greater than 0.");
        }
    }

    private void validateSalary(double salary) {
        if (salary < 0) {
            throw new IllegalArgumentException("Employee salary cannot be negative.");
        }
    }
}

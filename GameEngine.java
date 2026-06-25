import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameEngine {

    private Company company;
    private ConsoleUI ui;
    private boolean running;
    private int turn;
    private static final int MAX_TURNS = 20;
    private boolean won;
    private boolean lost;
    private List<GameEvent> events;
    private Random random;

    public GameEngine(Company company, ConsoleUI ui) {
        this.company = company;
        this.ui = ui;
        this.running = true;
        this.turn = 1;
        this.won = false;
        this.lost = false;
        this.random = new Random();
        this.events = new ArrayList<>();
        events.add(new MarketSlowdownEvent());
        events.add(new BonusEvent());
    }

    public void start() {
        while (running) {
            ui.showTurnHeader(turn);
            ui.showCompanyStatus(company);
            ui.showMainMenu();

            int choice = ui.readMenuChoice();

            handleChoice(choice);

            if (running) {
                advanceTurn();
                turn++;
            }
        }
    }

    private void handleChoice(int choice) {
        switch (choice) {
            case 1:
                company.showStatus();
                break;
            case 2:
                startPlannedProjects();
                break;
            case 3:
                workOnProjects();
                break;
            case 4:
                showActiveProjects();
                break;
            case 5:
                manageProject();
                break;
            case 6:
                assignEmployeeToProject();
                break;
            case 7:
                running = false;
                ui.showMessage("Gra zakończona przez gracza.");
                break;
            default:
                ui.showMessage("Nieprawidłowa opcja menu. Proszę wybrać 1-7.");
                break;
        }
    }

    private void startPlannedProjects() {
        boolean anyStarted = false;
        for (Project project : company.getProjects()) {
            if (project.getStatus() == ProjectStatus.PLANNED) {
                project.start();
                anyStarted = true;
            }
        }
        if (!anyStarted) {
            ui.showMessage("Brak planowanych projektów do rozpoczęcia.");
        }
    }

    private void workOnProjects() {
        for (Project project : company.getProjects()) {
            if (project.getStatus() == ProjectStatus.IN_PROGRESS) {
                project.workOneTurn();
            }
        }
        company.paySalaries();
    }

    private void showActiveProjects() {
        ui.showActiveProjects(company);
    }

    private void manageProject() {
        boolean managingProject = true;
        while (managingProject) {
            ui.showProjectManagementMenu();
            int subChoice = ui.readProjectManagementChoice();

            switch (subChoice) {
                case 1:
                    putProjectOnHold();
                    managingProject = false;
                    break;
                case 2:
                    resumeProject();
                    managingProject = false;
                    break;
                case 0:
                    managingProject = false;
                    break;
                default:
                    ui.showMessage("Nieprawidłowa opcja. Proszę wybrać 0, 1 lub 2.");
                    break;
            }
        }
    }

    private void putProjectOnHold() {
        int index = ui.selectProject(company);
        if (index < 0) {
            return;
        }

        Project project = company.getProjects().get(index);
        if (project.getStatus() == ProjectStatus.IN_PROGRESS) {
            project.putOnHold();
            reassignEmployeesFromInactiveProjects();
        } else {
            ui.showMessage("Nie można wstrzymać: projekt '" + project.getName()
                    + "' jest obecnie " + project.getStatus() + ".");
        }
    }

    private void resumeProject() {
        int index = ui.selectProject(company);
        if (index < 0) {
            return;
        }

        Project project = company.getProjects().get(index);
        if (project.getStatus() == ProjectStatus.ON_HOLD) {
            project.resume();
        } else {
            ui.showMessage("Nie można wznowić: projekt '" + project.getName()
                    + "' jest obecnie " + project.getStatus() + ".");
        }
    }

    private void assignEmployeeToProject() {
        int employeeIndex = ui.selectEmployee(company);
        if (employeeIndex < 0) {
            return;
        }

        int projectIndex = ui.selectProject(company);
        if (projectIndex < 0) {
            return;
        }

        Employee employee = company.getEmployees().get(employeeIndex);
        Project project = company.getProjects().get(projectIndex);

        if (project.getStatus() == ProjectStatus.FINISHED || project.getStatus() == ProjectStatus.CANCELLED) {
            ui.showMessage("Nie można przypisać pracownika do zakończonego projektu.");
            return;
        }

        moveEmployeeToProject(employee, project);
        ui.showMessage("Pracownik " + employee.getName() + " przypisany do projektu " + project.getName() + ".");
    }

    private void moveEmployeeToProject(Employee employee, Project project) {
        Project previousProject = employee.getAssignedProject();
        if (previousProject != null) {
            previousProject.unassignWorker(employee);
        }
        project.assignWorker(employee);
        employee.setAssignedProject(project);
    }

    private Project findNextAvailableProject(Project excludedProject) {
        for (Project project : company.getProjects()) {
            if (project != excludedProject && project.getStatus() == ProjectStatus.IN_PROGRESS) {
                return project;
            }
        }
        for (Project project : company.getProjects()) {
            if (project != excludedProject && project.getStatus() == ProjectStatus.PLANNED) {
                return project;
            }
        }
        return null;
    }

    private void reassignEmployeesFromInactiveProjects() {
        for (Employee employee : company.getEmployees()) {
            Project assignedProject = employee.getAssignedProject();
            if (assignedProject == null) {
                continue;
            }
            if (assignedProject.getStatus() == ProjectStatus.FINISHED || assignedProject.getStatus() == ProjectStatus.ON_HOLD) {
                Project nextProject = findNextAvailableProject(assignedProject);
                if (nextProject != null) {
                    moveEmployeeToProject(employee, nextProject);
                    ui.showMessage("Pracownik " + employee.getName() + " został przeniesiony do projektu " + nextProject.getName() + ".");
                } else {
                    assignedProject.unassignWorker(employee);
                    employee.setAssignedProject(null);
                    ui.showMessage("Pracownik " + employee.getName() + " nie ma aktywnego projektu.");
                }
            }
        }
    }

    private void advanceTurn() {
        triggerRandomEvent();

        if (company.getCash() < 0) {
            lost = true;
            running = false;
            ui.showMessage("Gotówka firmy jest ujemna. Przegrywasz!");
            return;
        }

        if (allProjectsFinished()) {
            won = true;
            running = false;
            ui.showMessage("Wszystkie projekty są ukończone. Wygrywasz!");
            return;
        }

        if (turn >= MAX_TURNS) {
            running = false;
            ui.showMessage("Maksymalna liczba tur (" + MAX_TURNS + ") osiągnięta. Koniec gry.");
        }
    }

    private void triggerRandomEvent() {
        if (random.nextDouble() < 0.3) {
            GameEvent event = events.get(random.nextInt(events.size()));
            event.apply(company);
        }
    }

    private boolean allProjectsFinished() {
        if (company.getProjects().isEmpty()) {
            return false;
        }

        for (Project project : company.getProjects()) {
            if (!project.isFinished()) {
                return false;
            }
        }

        return true;
    }

    public boolean isWon() {
        return won;
    }

    public boolean isLost() {
        return lost;
    }
}

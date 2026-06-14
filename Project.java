import java.util.ArrayList;
import java.util.List;

public class Project {

    private String name;
    private int requiredWork;
    private int progress;
    private List<Workable> team;
    private ProjectStatus status;

    public Project(String name, int requiredWork) {
        validateName(name);
        validateRequiredWork(requiredWork);
        this.name = name;
        this.requiredWork = requiredWork;
        this.progress = 0;
        this.team = new ArrayList<>();
        this.status = ProjectStatus.PLANNED;
    }

    public void addWorker(Workable workable) {
        if (workable == null) {
            throw new IllegalArgumentException("Worker cannot be null.");
        }
        team.add(workable);
    }

    public void assignWorker(Workable workable) {
        addWorker(workable);
    }

    public void unassignWorker(Workable workable) {
        team.remove(workable);
    }

    public void clearWorkers() {
        team.clear();
    }

    public void start() {
        if (status == ProjectStatus.PLANNED) {
            status = ProjectStatus.IN_PROGRESS;
            System.out.println("Projekt '" + name + "' zmienił status na: " + status);
        }
    }

    public void cancel() {
        if (status != ProjectStatus.FINISHED) {
            status = ProjectStatus.CANCELLED;
            System.out.println("Projekt '" + name + "' zmienił status na: " + status);
        }
    }

    public void putOnHold() {
        if (status == ProjectStatus.IN_PROGRESS) {
            status = ProjectStatus.ON_HOLD;
            System.out.println("Projekt '" + name + "' zmienił status na: " + status);
        }
    }

    public void resume() {
        if (status == ProjectStatus.ON_HOLD) {
            status = ProjectStatus.IN_PROGRESS;
            System.out.println("Projekt '" + name + "' zmienił status na: " + status);
        }
    }

    public void workOneTurn() {
        if (status != ProjectStatus.IN_PROGRESS) {
            return;
        }

        for (Workable worker : team) {
            progress += worker.work();
        }

        if (progress >= requiredWork) {
            progress = requiredWork;
            status = ProjectStatus.FINISHED;
            System.out.println("Projekt '" + name + "' zmienił status na: " + status);
        }
    }

    public int workWithAvailablePower(int availablePower) {
        // Pracujemy tylko na projektach IN_PROGRESS, ignorujemy ON_HOLD i inne statusy
        if (status != ProjectStatus.IN_PROGRESS || availablePower <= 0) {
            return availablePower;
        }

        int workNeeded = requiredWork - progress;
        int workDone = Math.min(availablePower, workNeeded);

        progress += workDone;

        if (progress >= requiredWork) {
            progress = requiredWork;
            status = ProjectStatus.FINISHED;
            System.out.println("Projekt '" + name + "' zmienił status na: " + status);
        }

        return availablePower - workDone;
    }

    public boolean isFinished() {
        return status == ProjectStatus.FINISHED;
    }

    public List<Workable> getTeam() {
        return team;
    }

    public int getCompletionPercentage() {
        return (progress * 100) / requiredWork;
    }

    public String getName() {
        return name;
    }

    public int getRequiredWork() {
        return requiredWork;
    }

    public int getProgress() {
        return progress;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Project name cannot be null or blank.");
        }
    }

    private void validateRequiredWork(int requiredWork) {
        if (requiredWork <= 0) {
            throw new IllegalArgumentException("Required work must be greater than 0.");
        }
    }
}

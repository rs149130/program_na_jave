import java.util.Scanner;

public class ConsoleUI {

    private final Scanner scanner = new Scanner(System.in);

    public void showTurnHeader(int turn) {
        System.out.println();
        System.out.println("=== TURA " + turn + " ===");
    }

    public void showMainMenu() {
        System.out.println("Wybierz akcję:");
        System.out.println("1. Pokaż pełny status firmy");
        System.out.println("2. Rozpocznij planowane projekty");
        System.out.println("3. Pracuj nad projektami");
        System.out.println("4. Pokaż tylko aktywne projekty");
        System.out.println("5. Zarządzaj projektem");
        System.out.println("6. Przypisz pracownika do projektu");
        System.out.println("7. Wyjdź z gry");
    }

    public int readMenuChoice() {
        System.out.print("Wpisz wybór: ");

        if (!scanner.hasNextInt()) {
            String invalid = scanner.nextLine();
            if (invalid.trim().isEmpty()) {
                System.out.println("Brak wejścia. Proszę wpisać liczbę.");
            } else {
                System.out.println("Nieprawidłowe wejście: '" + invalid + "'. Proszę wpisać liczbę.");
            }
            return -1;
        }

        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    public void showProjectManagementMenu() {
        System.out.println("Zarządzaj projektem:");
        System.out.println("1. Wstrzymaj projekt");
        System.out.println("2. Wznów projekt");
        System.out.println("0. Powrót do menu głównego");
    }

    public int readProjectManagementChoice() {
        System.out.print("Wpisz wybór: ");

        if (!scanner.hasNextInt()) {
            String invalid = scanner.nextLine();
            if (invalid.trim().isEmpty()) {
                System.out.println("Brak wejścia. Proszę wpisać liczbę.");
            } else {
                System.out.println("Nieprawidłowe wejście: '" + invalid + "'. Proszę wpisać liczbę.");
            }
            return -1;
        }

        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    public void showCompanyStatus(Company company) {
        System.out.println("Firma: " + company.getName());
        System.out.println("Gotówka: " + company.getCash());
        System.out.println("Projekty:");

        for (Project project : company.getProjects()) {
            System.out.println("- " + project.getName()
                    + " | status: " + project.getStatus()
                    + " | postęp: " + project.getProgress()
                    + "/" + project.getRequiredWork());
        }
        System.out.println();
    }

    public void showActiveProjects(Company company) {
        System.out.println("Aktywne (nieukończone) projekty:");
        boolean found = false;
        for (Project project : company.getProjects()) {
            if (!project.isFinished()) {
                System.out.println("- " + project.getName()
                        + " | status: " + project.getStatus()
                        + " | postęp: " + project.getProgress()
                        + "/" + project.getRequiredWork());
                found = true;
            }
        }
        if (!found) {
            System.out.println("Brak aktywnych projektów.");
        }
        System.out.println();
    }

    public int selectProject(Company company) {
        System.out.println("Wybierz projekt:");
        int index = 1;
        for (Project project : company.getProjects()) {
            System.out.println(index + ". " + project.getName()
                    + " | status: " + project.getStatus());
            index++;
        }
        if (index == 1) {
            System.out.println("Brak dostępnych projektów.");
            return -1;
        }

        System.out.print("Wpisz numer projektu: ");
        if (!scanner.hasNextInt()) {
            scanner.nextLine();
            System.out.println("Nieprawidłowe wejście.");
            return -1;
        }

        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice < 1 || choice > company.getProjects().size()) {
            System.out.println("Nieprawidłowy numer projektu.");
            return -1;
        }

        return choice - 1;
    }

    public int selectEmployee(Company company) {
        System.out.println("Wybierz pracownika:");
        int index = 1;
        for (Employee employee : company.getEmployees()) {
            String projectName = employee.getAssignedProject() != null
                    ? employee.getAssignedProject().getName()
                    : "brak";
            System.out.println(index + ". " + employee.getName()
                    + " | rola: " + employee.getRoleName()
                    + " | projekt: " + projectName);
            index++;
        }
        if (index == 1) {
            System.out.println("Brak pracowników.");
            return -1;
        }

        System.out.print("Wpisz numer pracownika: ");
        if (!scanner.hasNextInt()) {
            scanner.nextLine();
            System.out.println("Nieprawidłowe wejście.");
            return -1;
        }

        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice < 1 || choice > company.getEmployees().size()) {
            System.out.println("Nieprawidłowy numer pracownika.");
            return -1;
        }

        return choice - 1;
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}

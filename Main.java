public class Main {
    public static void main(String[] args) {
        Company company = new Company("TechCorp", 150_000);

        company.hire(new Developer("Robert", 9, 8_000));
        company.hire(new Tester("Piotr", 6, 6_500));
        company.hire(new Manager("Dawid", 3, 9_000));        
        company.hire(new Intern("Tomek", 2, 3_000));
        Project mobileApp = new Project("Mobile App", 30);
        Project website = new Project("Website", 20);

        company.startProject(mobileApp);
        company.startProject(website);

        ConsoleUI ui = new ConsoleUI();
        GameEngine engine = new GameEngine(company, ui);

        engine.start();
    }
}

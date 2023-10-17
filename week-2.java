import java.util.ArrayList;
import java.util.Scanner;

public class ToDoApp {
    private ArrayList<String> tasks = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);

    public void displayList() {
        if (tasks.isEmpty()) {
            System.out.println("\nYour to-do list is empty.");
        } else {
            System.out.println("\nTo-Do List:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println(" - " + tasks.get(i));
            }
        }
    }

    public void addTask(String task) {
        tasks.add(task);
        System.out.println("\nTask added: " + task);
    }

    public void removeTask(int taskIndex) {
        if (isValidIndex(taskIndex)) {
            String removedTask = tasks.remove(taskIndex);
            System.out.println("\nTask removed: " + removedTask);
        } else {
            System.out.println("\nInvalid task index.");
        }
    }

    private boolean isValidIndex(int taskIndex) {
        return taskIndex >= 0 && taskIndex < tasks.size();
    }

    public void run() {
        int choice;

        do {
            clearScreen();
            System.out.println("To-Do List Menu:");
            System.out.println("1. View To-Do List");
            System.out.println("2. Add Task");
            System.out.println("3. Remove Task");
            System.out.println("4. Quit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    displayList();
                    break;
                case 2:
                    System.out.print("\nEnter the task to add: ");
                    String newTask = scanner.nextLine();
                    addTask(newTask);
                    break;
                case 3:
                    System.out.print("\nEnter the task number to remove: ");
                    int taskNumber = scanner.nextInt();
                    removeTask(taskNumber - 1);  // Adjust for 0-based index
                    break;
                case 4:
                    System.out.println("\nGoodbye!");
                    break;
                default:
                    System.out.println("\nInvalid choice. Please try again.");
            }

            System.out.print("\nPress Enter to continue...");
            scanner.nextLine();
        } while (choice != 4);

        scanner.close();
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void main(String[] args) {
        ToDoApp todoApp = new ToDoApp();
        todoApp.run();
    }
}

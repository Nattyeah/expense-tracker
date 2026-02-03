import java.time.LocalDate;
import java.util.Arrays;

public class CLI {
    public static void main(String[] args) {
        ExpenseManager expenseManager = new ExpenseManager();

        if (args.length < 1) {
            printUsage();
            return;
        }

        String command = args[0].toLowerCase();

        try {
            switch (command) {
                case "add":
                    if (args.length != 4) {
                        System.out.println("Error: Missing arguments for add command!");
                        printUsage();
                        return;
                    }
                    try {
                        String description = args[1];
                        double amountToCreate = Double.parseDouble(args[2]);
                        ExpenseCategory category = ExpenseCategory.valueOf(args[3].toUpperCase());
                        Expense newExpense = expenseManager.addExpense(description, amountToCreate, LocalDate.now(), category);
                        expenseManager.saveExpenses();
                        System.out.println("Expense added successfully: " + newExpense);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: Invalid arguments. Please check the amount and category: " + e.getMessage());
                        printUsage();
                    }
                    break;
                case "update":
                    if (args.length < 4) {
                        System.out.println("Error: Missing arguments for update command!");
                        printUsage();
                        return;
                    }

                    try {
                        int idToUpdate = Integer.parseInt(args[1]);
                        String newDescription = args[2];
                        double newAmount = Double.parseDouble(args[3]);
                        if (expenseManager.updateExpense(idToUpdate, newDescription, newAmount)) {
                            expenseManager.saveExpenses();
                            System.out.println("Expense updated successfully: " + idToUpdate);
                        } else {
                            System.out.println("Expense not found: " + idToUpdate);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Invalid ID or amount format: " + e.getMessage());
                        printUsage();
                    }
                    break;
                case "delete":
                    if (args.length != 2) {
                        System.out.println("Error: Missing or invalid ID");
                        printUsage();
                        return;
                    }

                    try {
                        int idToDelete = Integer.parseInt(args[1]);
                        if (expenseManager.deleteExpense(idToDelete)) {
                            expenseManager.saveExpenses();
                            System.out.println("Expense deleted successfully: " + idToDelete);
                        } else {
                            System.out.println("Expense not found: " + idToDelete);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Invalid ID format: " + e.getMessage());
                        printUsage();
                    }
                    break;
                case "list":
                    if (args.length != 1) {
                        printUsage();
                        return;
                    }
                    expenseManager.loadExpenses();
                    expenseManager.getAllExpenses();
                    break;
                case "summary":
                    if (args.length != 3) {
                        System.out.println("Error: Please provide year and month for summary");
                        printUsage();
                        return;
                    }
                    try {
                        expenseManager.loadExpenses();
                        int year = Integer.parseInt(args[2]);
                        int month = Integer.parseInt(args[1]);
                        double total = expenseManager.getSummary(month, year);
                        System.out.println("Total spent in " + month + "/" + year + ": " + total);
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Invalid year or month format: " + e.getMessage());
                        printUsage();
                    }
                    break;
                case "budget":
                    if (args.length != 4) {
                        System.out.println("Error: Missing arguments for budget command");
                        printUsage();
                        return;
                    }

                    try {
                        double amountToCreateBudget = Double.parseDouble(args[1]);
                        int yearToCreateBudget = Integer.parseInt(args[2]);
                        int monthToCreateBudget = Integer.parseInt(args[args.length - 1]);
                        expenseManager.createBudget(amountToCreateBudget, yearToCreateBudget, monthToCreateBudget);
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Invalid amount, year, or month format: " + e.getMessage());
                        printUsage();
                    }
                    break;

                case "export":
                    if (args.length != 1) {
                        printUsage();
                        return;
                    }
                    expenseManager.loadExpenses();
                    expenseManager.exportExpenses();
                    break;
                default:
                    System.out.println("Error: Unknown command: " + command);
                    printUsage();
                    break;
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            printUsage();
        }
    }

    private static void printUsage() {
        System.out.println("\nExpense Tracker - Command Line Interface");
        System.out.println("Usage: java CLI <command> [arguments]");
        System.out.println("\nCommands:");
        System.out.println("  add <description> <amount> <category>     Add a new expense");
        System.out.println("  update <id> <description> <amount>        Update an expense");
        System.out.println("  delete <id>                               Delete an expense");
        System.out.println("  list                                      List all expenses");
        System.out.println("  summary <year> <month>                    Show monthly summary");
        System.out.println("  budget <amount> <year> <month>            Set monthly budget");
        System.out.println("  export            Export expenses to CSV");
        System.out.println("\nCategories: " + Arrays.toString(ExpenseCategory.values()));
    }
}

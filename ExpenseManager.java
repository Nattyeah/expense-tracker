import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ExpenseManager {

    private static final Path STORE = Paths.get("expenses.json");
    private static final Path EXPORT = Paths.get("expenses_export.csv");
    private List<Expense> expenses = new ArrayList<>();
    private final Map<String, Double> monthlyBudget = new HashMap<>();

    public void loadExpenses() {
        if (!Files.exists(STORE)) {
            this.expenses = new ArrayList<>();
            return;
        }
        try {
            String content = Files.readString(STORE);
            String[] expenseList = content.replace("[", "")
                    .replace("]", "")
                    .split("},");

            this.expenses = new ArrayList<>();
            for (String expense : expenseList) {
                if (!expense.endsWith("}")) {
                    expense = expense + "}";
                    this.expenses.add(Expense.fromJson(expense));
                } else {
                    this.expenses.add(Expense.fromJson(expense));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file!");
            this.expenses = new ArrayList<>();
        } catch (ParseException e) {
            System.err.println("Error parsing from json file!");
            this.expenses = new ArrayList<>();
        }
    }

    public void saveExpenses() {
        try { // TODO da pra melhorar a implementação
            String joiner = this.expenses.stream().map(Expense::toJson).collect(Collectors.joining(","));
            String joinedString = "[" + joiner + "]";
            Files.writeString(STORE, joinedString);
        } catch (IOException e) {
            System.err.println("Error writing file!");
        }
    }

    public Expense addExpense(String description, double amount, LocalDate date, ExpenseCategory category) {
        int maxId = this.expenses.stream().mapToInt(Expense::getId).max().orElse(0);
        Expense newExpense = new Expense(maxId + 1, description, amount, date, category);
        this.expenses.add(newExpense);
        return newExpense;
    }

    public boolean updateExpense(int id, String newDescription, double newAmount) {
        return this.expenses.stream()
                .filter(expense -> expense.getId() == id)
                .findFirst()
                .map(expense -> {
                    expense.setDescription(newDescription);
                    expense.setAmount(newAmount);
                    return true;
                }).orElse(false);
    }

    public boolean deleteExpense(int id) {
        return this.expenses.removeIf(expense -> expense.getId() == id);
    }

    public void getAllExpenses() {
        this.expenses.forEach(System.out::println);
    }

    public double getSummary(int month, int year) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Invalid month!");
        }

        return this.expenses.stream()
                .filter(expense -> expense.getDate().getYear() == year && expense.getDate().getMonthValue() == month)
                .peek(System.out::println)
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    public void createBudget(double amount, int year, int month) {
        String key = year + "-" + String.format("%02d", month);
        monthlyBudget.put(key, amount);
        checkBudgetStatus(year, month);
    }

    public void checkBudgetStatus(int year, int month) {
        String key = String.format("%04d-%02d", year, month);
        Double budget = monthlyBudget.get(key);

        if (Objects.isNull(budget)) {
            System.out.println("No budget set for " + key);
            return;
        }

        double total = this.expenses.stream()
                .filter(expense -> expense.getDate().getYear() == year && expense.getDate().getMonthValue() == month)
                .mapToDouble(Expense::getAmount)
                .sum();

        if (total > budget) {
            System.out.println("Budget of R$ " + budget + " exceeded! Actual: R$ " + total);
        } else {
            System.out.println("You're on track! Budget of R$ " + budget + " not exceeded! Actual: R$ " + total);
        }
    }

    public void exportExpenses() {
        try {
            String header = String.format("%s,%s,%s,%s", "Description", "Amount", "Date", "Category");
            String content = this.expenses.stream()
                    .map(expense ->
                            String.format("%s,%s,%s,%s",
                                    escapeCsv(expense.getDescription()),
                                    expense.getAmount(),
                                    expense.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                                    expense.getCategory())
                    ).collect(Collectors.joining("\n"));

            String csv = header + "\n" + content;
            Files.write(EXPORT, csv.getBytes(StandardCharsets.UTF_8));
            System.out.println("Expenses exported successfully to: " + EXPORT.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error exporting expenses!" + e.getMessage());
        }
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }

        String escaped = value.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\"") || escaped.contains("\n")) {
            return "\"" + escaped + "\"";
        }
        return escaped;
    }
}

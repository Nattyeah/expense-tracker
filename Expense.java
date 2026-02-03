import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Expense {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private final Integer id;
    private String description;
    private Double amount;
    private final LocalDate date;
    private final ExpenseCategory category;

    public Expense(Integer id, String description, Double amount, LocalDate date, ExpenseCategory category) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.category = category;
    }

    public String toJson() {
        return "{\"id\":" + id + ", " +
                "\"description\":\"" + description + "\", " +
                "\"amount\":" + amount + ", " +
                "\"date\":\"" + date.format(FORMATTER) + "\", " +
                "\"category\":\"" + category + "\"}";
    }

    public static Expense fromJson(String json) throws ParseException {
        json = json.replace("{", "").replace("}", "").replace("\"", "");
        String[] parts = json.split(",");

        int id = Integer.parseInt(parts[0].split(":")[1].strip());
        String description = parts[1].split(":")[1].strip();
        double amount = Double.parseDouble(parts[2].split(":")[1].strip());
        LocalDate date = LocalDate.parse(parts[3].split(":")[1].strip(), FORMATTER);
        String category = parts[4].split(":")[1].strip();

        ExpenseCategory expenseCategory = ExpenseCategory.valueOf(category.toUpperCase().replace(" ", "_"));

        return new Expense(id, description, amount, date, expenseCategory);
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return String.format("[Expense: %d | Description: %s | Amount: %s | Date: %s, Category: %s]",
                this.id,
                this.description,
                this.amount,
                this.date,
                this.category);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Expense expense)) return false;
        return Objects.equals(id, expense.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

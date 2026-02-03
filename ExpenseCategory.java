public enum ExpenseCategory {
    FOOD("Food"),
    TRANSPORTATION("Transportation"),
    HOUSING("Housing"),
    HEALTH("Health"),
    EDUCATION("Education"),
    ENTERTAINMENT("Entertainment"),
    GROCERIES("Groceries"),
    OTHER("Other");

    private final String category;

    ExpenseCategory(String category) {
        this.category = category;
    }
}

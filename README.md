# 📊 Expense Tracker

A command-line expense manager built with Java that helps you track your spending efficiently.

## 🚀 Features

- ✅ Add new expenses with description, amount, date, and category
- 📋 List all tracked expenses
- ✏️ Update existing expenses
- 🗑️ Delete expenses
- 📊 Set and track monthly budgets
- 📈 Generate monthly expense summaries
- 📤 Export data to CSV
- 💾 Data persistence with JSON storage

## 📋 Prerequisites

- Java JDK 11 or higher
- Git (optional, for cloning the repository)

## 🛠️ Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/Nattyeah/expense-tracker
   cd expense-tracker\src
   ```

2. Compile the project:
   ```bash
   javac *.java
   ```

## 🚀 Usage

### Add an expense
```bash
java CLI add "Expense description" 150.50 FOOD
```

### List all expenses
```bash
java CLI list
```

### Update an expense
```bash
java CLI update 1 "New description" 200.00
```

### Delete an expense
```bash
java CLI delete 1
```

### Set monthly budget
```bash
java CLI budget 3000 2026 02
```

### Check budget status
```bash
java CLI status 2026 02
```

### Export to CSV
```bash
java CLI export
```

### View monthly summary
```bash
java CLI summary 2026 02
```

## 📁 Project Structure

```
expense-tracker/
├── src/
│   ├── CLI.java           # Command line interface
│   ├── Expense.java       # Expense model class
│   ├── ExpenseManager.java # Main application logic
│   └── ExpenseCategory.java # Category enum
└── expenses.json         # Data storage file
```

## 📝 Available Categories

- FOOD
- TRANSPORTATION
- HOUSING
- HEALTH
- EDUCATION
- ENTERTAINMENT
- GROCERIES
- OTHER

## 📊 Data Storage

Data is automatically saved to `expenses.json` in the project root. The file is created on first run.

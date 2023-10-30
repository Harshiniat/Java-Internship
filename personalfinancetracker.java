import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PersonalFinanceTracker {
    private Map<String, Double> incomeMap = new HashMap<>();
    private Map<String, Double> expensesMap = new HashMap<>();
    private double currentBalance = 0;
    private Map<String, String> expenseCategoriesMap = new HashMap<>();
    private ArrayList<FinancialGoal> financialGoalsList = new ArrayList<>();
    private JTextArea transactionHistoryTextArea;

    public PersonalFinanceTracker() {
        JFrame frame = new JFrame("Personal Finance Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel incomePanel = createIncomePanel();
        JPanel expensesPanel = createExpensesPanel();
        JPanel goalsPanel = createGoalsPanel();
        JPanel transactionHistoryPanel = createTransactionHistoryPanel();

        tabbedPane.addTab("Income", incomePanel);
        tabbedPane.addTab("Expenses", expensesPanel);
        tabbedPane.addTab("Financial Goals", goalsPanel);
        tabbedPane.addTab("Transaction History", transactionHistoryPanel);

        frame.add(tabbedPane);

        frame.setVisible(true);
    }

    private JPanel createIncomePanel() {
        JPanel incomePanel = new JPanel();
        incomePanel.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        JLabel incomeSourceLabel = new JLabel("Income Source:");
        JTextField incomeSourceField = new JTextField(15);
        JLabel incomeAmountLabel = new JLabel("Amount (INR):");
        JTextField incomeAmountField = new JTextField(10);
        JButton addIncomeButton = new JButton("Add Income");

        inputPanel.add(incomeSourceLabel);
        inputPanel.add(incomeSourceField);
        inputPanel.add(incomeAmountLabel);
        inputPanel.add(incomeAmountField);
        inputPanel.add(addIncomeButton);

        incomePanel.add(inputPanel, BorderLayout.NORTH);

        JTextArea incomeTextArea = new JTextArea(10, 40);
        incomeTextArea.setEditable(false);
        incomePanel.add(new JScrollPane(incomeTextArea), BorderLayout.CENTER);

        addIncomeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String incomeSource = incomeSourceField.getText();
                double incomeAmount = Double.parseDouble(incomeAmountField.getText());
                incomeMap.put(incomeSource, incomeAmount);

                updateIncomeTextArea(incomeTextArea);

                incomeSourceField.setText("");
                incomeAmountField.setText("");
            }
        });

        return incomePanel;
    }

    private JPanel createExpensesPanel() {
        JPanel expensesPanel = new JPanel();
        expensesPanel.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        JLabel expenseCategoryLabel = new JLabel("Expense Category:");
        JComboBox<String> expenseCategoryComboBox = new JComboBox<>();
        JLabel expenseItemLabel = new JLabel("Expense Item:");
        JTextField expenseItemField = new JTextField(15);
        JLabel expenseAmountLabel = new JLabel("Amount (INR):");
        JTextField expenseAmountField = new JTextField(10);
        JButton addExpenseButton = new JButton("Add Expense");

        inputPanel.add(expenseCategoryLabel);
        inputPanel.add(expenseCategoryComboBox);
        inputPanel.add(expenseItemLabel);
        inputPanel.add(expenseItemField);
        inputPanel.add(expenseAmountLabel);
        inputPanel.add(expenseAmountField);
        inputPanel.add(addExpenseButton);

        expensesPanel.add(inputPanel, BorderLayout.NORTH);

        JTextArea expensesTextArea = new JTextArea(10, 40);
        expensesTextArea.setEditable(false);
        expensesPanel.add(new JScrollPane(expensesTextArea), BorderLayout.CENTER);

        // Initialize expense categories
        expenseCategoryComboBox.addItem("Groceries");
        expenseCategoryComboBox.addItem("Rent");
        expenseCategoryComboBox.addItem("Utilities");
        expenseCategoryComboBox.addItem("Entertainment");
        expenseCategoryComboBox.addItem("Others");

        addExpenseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String expenseCategory = expenseCategoryComboBox.getSelectedItem().toString();
                String expenseItem = expenseItemField.getText();
                double expenseAmount = Double.parseDouble(expenseAmountField.getText());
                expensesMap.put(expenseItem, expenseAmount);

                // Update transaction history
                addTransactionToHistory("Expense - " + expenseCategory + " - " + expenseItem, expenseAmount);

                updateExpensesTextArea(expensesTextArea);

                expenseItemField.setText("");
                expenseAmountField.setText("");
            }
        });

        return expensesPanel;
    }

    private JPanel createGoalsPanel() {
        JPanel goalsPanel = new JPanel();
        goalsPanel.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        JLabel goalNameLabel = new JLabel("Goal Name:");
        JTextField goalNameField = new JTextField(15);
        JLabel goalAmountLabel = new JLabel("Amount (INR):");
        JTextField goalAmountField = new JTextField(10);
        JLabel goalDeadlineLabel = new JLabel("Deadline:");
        JTextField goalDeadlineField = new JTextField(10);
        JButton addGoalButton = new JButton("Add Goal");

        inputPanel.add(goalNameLabel);
        inputPanel.add(goalNameField);
        inputPanel.add(goalAmountLabel);
        inputPanel.add(goalAmountField);
        inputPanel.add(goalDeadlineLabel);
        inputPanel.add(goalDeadlineField);
        inputPanel.add(addGoalButton);

        goalsPanel.add(inputPanel, BorderLayout.NORTH);

        JTextArea goalsTextArea = new JTextArea(10, 40);
        goalsTextArea.setEditable(false);
        goalsPanel.add(new JScrollPane(goalsTextArea), BorderLayout.CENTER);

        // Create "Generate Report" button
        JButton generateReportButton = new JButton("Generate Report");
        goalsPanel.add(generateReportButton, BorderLayout.SOUTH);

        generateReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateFinancialReport();
            }
        });

        addGoalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String goalName = goalNameField.getText();
                double goalAmount = Double.parseDouble(goalAmountField.getText());
                String goalDeadline = goalDeadlineField.getText();
                FinancialGoal goal = new FinancialGoal(goalName, goalAmount, goalDeadline);
                financialGoalsList.add(goal);

                updateGoalsTextArea(goalsTextArea);

                goalNameField.setText("");
                goalAmountField.setText("");
                goalDeadlineField.setText("");
            }
        });

        return goalsPanel;
    }

    private JPanel createTransactionHistoryPanel() {
        JPanel transactionHistoryPanel = new JPanel();
        transactionHistoryPanel.setLayout(new BorderLayout());

        transactionHistoryTextArea = new JTextArea(10, 40);
        transactionHistoryTextArea.setEditable(false);
        transactionHistoryPanel.add(new JScrollPane(transactionHistoryTextArea), BorderLayout.CENTER);

        return transactionHistoryPanel;
    }

    private void addTransactionToHistory(String description, double amount) {
        String transaction = description + ": ₹" + formatCurrency(amount) + "\n";
        transactionHistoryTextArea.append(transaction);
    }

    private void updateIncomeTextArea(JTextArea incomeTextArea) {
        incomeTextArea.setText("");
        for (Map.Entry<String, Double> entry : incomeMap.entrySet()) {
            incomeTextArea.append(entry.getKey() + ": ₹" + formatCurrency(entry.getValue()) + "\n");
        }
    }

    private void updateExpensesTextArea(JTextArea expensesTextArea) {
        expensesTextArea.setText("");
        for (Map.Entry<String, Double> entry : expensesMap.entrySet()) {
            expensesTextArea.append(entry.getKey() + ": ₹" + formatCurrency(entry.getValue()) + "\n");
        }
    }

    private void updateGoalsTextArea(JTextArea goalsTextArea) {
        goalsTextArea.setText("");
        for (FinancialGoal goal : financialGoalsList) {
            goalsTextArea.append(goal.getName() + ": ₹" + formatCurrency(goal.getAmount()));
            if (!goal.getDeadline().isEmpty()) {
                goalsTextArea.append(" (Deadline: " + goal.getDeadline() + ")");
            }
            goalsTextArea.append("\n");
        }
    }

    private void generateFinancialReport() {
        double totalIncome = incomeMap.values().stream().mapToDouble(Double::doubleValue).sum();
        double totalExpenses = expensesMap.values().stream().mapToDouble(Double::doubleValue).sum();
        currentBalance = totalIncome - totalExpenses;

        StringBuilder report = new StringBuilder("Financial Report\n\n");
        report.append("Total Income: ₹").append(formatCurrency(totalIncome)).append("\n");
        report.append("Total Expenses: ₹").append(formatCurrency(totalExpenses)).append("\n");
        report.append("Current Balance: ₹").append(formatCurrency(currentBalance)).append("\n\n");

        report.append("Financial Goals:\n");
        for (FinancialGoal goal : financialGoalsList) {
            report.append(goal.getName()).append(": ₹").append(formatCurrency(goal.getAmount()));
            if (!goal.getDeadline().isEmpty()) {
                report.append(" (Deadline: ").append(goal.getDeadline()).append(")");
            }
            report.append("\n");
        }

        JOptionPane.showMessageDialog(null, report.toString(), "Financial Report", JOptionPane.INFORMATION_MESSAGE);
    }

    private String formatCurrency(double amount) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(amount);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PersonalFinanceTracker();
            }
        });
    }
}

class FinancialGoal {
    private String name;
    private double amount;
    private String deadline;

    public FinancialGoal(String name, double amount, String deadline) {
        this.name = name;
        this.amount = amount;
        this.deadline = deadline;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public String getDeadline() {
        return deadline;
    }
}

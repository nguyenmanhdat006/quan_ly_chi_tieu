package com.nguyendat.quanlychiteu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.IntentCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_EXPENSE = "extra_expense";
    public static final String EXTRA_POSITION = "extra_position";

    private final ArrayList<Expense> expenses = new ArrayList<>();
    private ExpenseAdapter adapter;
    private TextView tvTotal;

    private final ActivityResultLauncher<Intent> addExpenseLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Expense expense = IntentCompat.getSerializableExtra(
                            result.getData(), EXTRA_EXPENSE, Expense.class);
                    if (expense != null) {
                        expenses.add(expense);
                        adapter.notifyItemInserted(expenses.size() - 1);
                        updateTotal();
                    }
                }
            });

    private final ActivityResultLauncher<Intent> detailLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    int position = result.getData().getIntExtra(EXTRA_POSITION, -1);
                    if (position >= 0 && position < expenses.size()) {
                        expenses.remove(position);
                        adapter.notifyItemRemoved(position);
                        updateTotal();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvTotal = findViewById(R.id.tvTotal);
        RecyclerView rvExpenses = findViewById(R.id.rvExpenses);
        Button btnAdd = findViewById(R.id.btnAdd);

        String today = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        expenses.add(new Expense("Ăn sáng", 30000, "Ăn uống", today, ""));
        expenses.add(new Expense("Đổ xăng", 50000, "Đi lại", today, ""));
        expenses.add(new Expense("Mua sách", 120000, "Học tập", today, ""));

        adapter = new ExpenseAdapter(expenses, position -> {
            Intent intent = new Intent(this, ExpenseDetailActivity.class);
            intent.putExtra(EXTRA_EXPENSE, expenses.get(position));
            intent.putExtra(EXTRA_POSITION, position);
            detailLauncher.launch(intent);
        });
        rvExpenses.setAdapter(adapter);

        btnAdd.setOnClickListener(v ->
                addExpenseLauncher.launch(new Intent(this, AddExpenseActivity.class)));

        updateTotal();
    }

    private void updateTotal() {
        long total = 0;
        for (Expense e : expenses) {
            total += e.getAmount();
        }
        tvTotal.setText(Expense.formatMoney(total));
    }
}

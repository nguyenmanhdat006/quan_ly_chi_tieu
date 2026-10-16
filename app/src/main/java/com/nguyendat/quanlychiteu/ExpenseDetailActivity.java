package com.nguyendat.quanlychiteu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.IntentCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;

public class ExpenseDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_expense_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        Intent intent = getIntent();
        Expense expense = IntentCompat.getSerializableExtra(intent, MainActivity.EXTRA_EXPENSE, Expense.class);
        int position = intent.getIntExtra(MainActivity.EXTRA_POSITION, -1);
        if (expense == null) {
            finish();
            return;
        }

        TextView tvName = findViewById(R.id.tvName);
        TextView tvAmount = findViewById(R.id.tvAmount);
        TextView tvCategory = findViewById(R.id.tvCategory);
        TextView tvDate = findViewById(R.id.tvDate);
        TextView tvNote = findViewById(R.id.tvNote);
        Button btnBack = findViewById(R.id.btnBack);
        Button btnDelete = findViewById(R.id.btnDelete);

        tvName.setText(expense.getName());
        tvAmount.setText(Expense.formatMoney(expense.getAmount()));
        tvCategory.setText(expense.getCategory());
        tvDate.setText(expense.getDate());
        tvNote.setText(expense.getNote());

        btnBack.setOnClickListener(v -> finish());

        btnDelete.setOnClickListener(v -> {
            Intent data = new Intent();
            data.putExtra(MainActivity.EXTRA_POSITION, position);
            setResult(RESULT_OK, data);
            finish();
        });
    }
}

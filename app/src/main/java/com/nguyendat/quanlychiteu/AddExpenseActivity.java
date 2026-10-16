package com.nguyendat.quanlychiteu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.Locale;

public class AddExpenseActivity extends AppCompatActivity {
    private EditText edtName;
    private EditText edtAmount;
    private Spinner spCategory;
    private DatePicker dpDate;
    private EditText edtNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_expense);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        edtName = findViewById(R.id.edtName);
        edtAmount = findViewById(R.id.edtAmount);
        spCategory = findViewById(R.id.spCategory);
        dpDate = findViewById(R.id.dpDate);
        edtNote = findViewById(R.id.edtNote);
        Button btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> saveExpense());
    }

    private void saveExpense() {
        String name = edtName.getText().toString().trim();
        String amountText = edtAmount.getText().toString().trim();

        if (name.isEmpty()) {
            edtName.setError(getString(R.string.error_name_empty));
            edtName.requestFocus();
            return;
        }

        long amount;
        try {
            amount = Long.parseLong(amountText);
        } catch (NumberFormatException e) {
            amount = 0;
        }
        if (amount <= 0) {
            edtAmount.setError(getString(R.string.error_amount_invalid));
            edtAmount.requestFocus();
            return;
        }

        String category = spCategory.getSelectedItem().toString();
        String date = String.format(Locale.getDefault(), "%02d/%02d/%04d",
                dpDate.getDayOfMonth(), dpDate.getMonth() + 1, dpDate.getYear());
        String note = edtNote.getText().toString().trim();

        Expense expense = new Expense(name, amount, category, date, note);
        Intent data = new Intent();
        data.putExtra(MainActivity.EXTRA_EXPENSE, expense);
        setResult(RESULT_OK, data);
        finish();
    }
}

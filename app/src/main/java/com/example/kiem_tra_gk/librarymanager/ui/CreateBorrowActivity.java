package com.example.kiem_tra_gk.librarymanager.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.kiem_tra_gk.R;
import com.example.kiem_tra_gk.librarymanager.database.LibraryDbHelper;
import com.example.kiem_tra_gk.librarymanager.model.Book;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CreateBorrowActivity extends AppCompatActivity {

    private Spinner spnAvailableBooks;
    private EditText edtBorrowerName, edtBorrowerPhone, edtBorrowDate, edtDueDate;
    private LibraryDbHelper dbHelper;
    private List<Book> availableBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_borrow);

        dbHelper = new LibraryDbHelper(this);
        spnAvailableBooks = findViewById(R.id.spnAvailableBooks);
        edtBorrowerName = findViewById(R.id.edtBorrowerName);
        edtBorrowerPhone = findViewById(R.id.edtBorrowerPhone);
        edtBorrowDate = findViewById(R.id.edtBorrowDate);
        edtDueDate = findViewById(R.id.edtDueDate);
        Button btnSaveBorrow = findViewById(R.id.btnSaveBorrow);

        // Chỉ load các sách có trạng thái "Có sẵn" (status == 0)
        availableBooks = dbHelper.getAvailableBooks();
        if (availableBooks.isEmpty()) {
            Toast.makeText(this, "Hiện không còn sách nào có sẵn để mượn!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        ArrayAdapter<Book> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, availableBooks);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAvailableBooks.setAdapter(spinnerAdapter);

        edtBorrowDate.setOnClickListener(v -> showDatePicker(edtBorrowDate));
        edtDueDate.setOnClickListener(v -> showDatePicker(edtDueDate));

        btnSaveBorrow.setOnClickListener(v -> saveBorrowRecord());
    }

    private void showDatePicker(EditText targetEditText) {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String formatted = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
            targetEditText.setText(formatted);
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void saveBorrowRecord() {
        Book selectedBook = (Book) spnAvailableBooks.getSelectedItem();
        String name = edtBorrowerName.getText().toString().trim();
        String phone = edtBorrowerPhone.getText().toString().trim();
        String borrowDate = edtBorrowDate.getText().toString().trim();
        String dueDate = edtDueDate.getText().toString().trim();

        if (selectedBook == null || name.isEmpty() || phone.isEmpty() || borrowDate.isEmpty() || dueDate.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin phiếu mượn!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!phone.matches("^0\\d{9,10}$")) {
            edtBorrowerPhone.setError("Số điện thoại không hợp lệ (10-11 chữ số, bắt đầu bằng 0)");
            return;
        }

        boolean success = dbHelper.createBorrowRecord(selectedBook.getBookId(), name, phone, borrowDate, dueDate);
        if (success) {
            Toast.makeText(this, "Lập phiếu mượn thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Sách này hiện không thể mượn!", Toast.LENGTH_SHORT).show();
        }
    }
}
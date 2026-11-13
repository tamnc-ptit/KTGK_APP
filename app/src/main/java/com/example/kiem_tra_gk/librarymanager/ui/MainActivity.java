package com.example.kiem_tra_gk.librarymanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kiem_tra_gk.R;
import com.example.kiem_tra_gk.librarymanager.adapter.BookAdapter;
import com.example.kiem_tra_gk.librarymanager.database.LibraryDbHelper;
import com.example.kiem_tra_gk.librarymanager.model.Book;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LibraryDbHelper dbHelper;
    private BookAdapter bookAdapter;
    private EditText edtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new LibraryDbHelper(this);
        edtSearch = findViewById(R.id.edtSearchBook);
        Button btnAddBook = findViewById(R.id.btnGoToAddBook);
        Button btnManageBorrow = findViewById(R.id.btnGoToBorrowList);
        RecyclerView rvBooks = findViewById(R.id.rvBooks);

        rvBooks.setLayoutManager(new LinearLayoutManager(this));
        bookAdapter = new BookAdapter(dbHelper.getAllBooks(""), new BookAdapter.OnBookActionListener() {
            @Override
            public void onEditBook(Book book) {
                Intent intent = new Intent(MainActivity.this, AddEditBookActivity.class);
                intent.putExtra("EDIT_BOOK", book);
                startActivity(intent);
            }

            @Override
            public void onDeleteBook(Book book) {
                if (book.getStatus() == 1) {
                    Toast.makeText(MainActivity.this, "Không thể xóa sách đang được mượn!", Toast.LENGTH_SHORT).show();
                    return;
                }
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc muốn xóa sách \"" + book.getTitle() + "\"?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            if (dbHelper.deleteBook(book.getBookId())) {
                                Toast.makeText(MainActivity.this, "Đã xóa sách", Toast.LENGTH_SHORT).show();
                                loadBooks(edtSearch.getText().toString());
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        });
        rvBooks.setAdapter(bookAdapter);

        // Tìm kiếm theo thời gian thực (Tên sách hoặc Tác giả)
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadBooks(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        btnAddBook.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddEditBookActivity.class));
        });

        btnManageBorrow.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, BorrowListActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBooks(edtSearch.getText().toString());
    }

    private void loadBooks(String keyword) {
        List<Book> books = dbHelper.getAllBooks(keyword);
        bookAdapter.updateData(books);
    }
}
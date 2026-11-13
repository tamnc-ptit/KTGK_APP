package com.example.kiem_tra_gk.librarymanager.ui;


import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.kiem_tra_gk.R;
import com.example.kiem_tra_gk.librarymanager.database.LibraryDbHelper;
import com.example.kiem_tra_gk.librarymanager.model.Book;

public class AddEditBookActivity extends AppCompatActivity {

    private EditText edtBookId, edtTitle, edtAuthor, edtCategory;
    private LibraryDbHelper dbHelper;
    private Book editingBook = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_book);

        dbHelper = new LibraryDbHelper(this);
        TextView tvHeader = findViewById(R.id.tvFormHeader);
        edtBookId = findViewById(R.id.edtBookId);
        edtTitle = findViewById(R.id.edtBookTitle);
        edtAuthor = findViewById(R.id.edtBookAuthor);
        edtCategory = findViewById(R.id.edtBookCategory);
        Button btnSave = findViewById(R.id.btnSaveBook);

        // Kiểm tra Intent xem có phải chế độ Sửa không
        if (getIntent().hasExtra("EDIT_BOOK")) {
            editingBook = (Book) getIntent().getSerializableExtra("EDIT_BOOK");
            if (editingBook != null) {
                tvHeader.setText("Cập nhật thông tin sách");
                edtBookId.setText(editingBook.getBookId());
                edtBookId.setEnabled(false); // Không cho sửa khóa chính Mã sách
                edtTitle.setText(editingBook.getTitle());
                edtAuthor.setText(editingBook.getAuthor());
                edtCategory.setText(editingBook.getCategory());
            }
        }

        btnSave.setOnClickListener(v -> saveBook());
    }

    private void saveBook() {
        String id = edtBookId.getText().toString().trim();
        String title = edtTitle.getText().toString().trim();
        String author = edtAuthor.getText().toString().trim();
        String category = edtCategory.getText().toString().trim();

        if (id.isEmpty() || title.isEmpty() || author.isEmpty() || category.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ tất cả thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (editingBook == null) {
            if (dbHelper.isBookIdExists(id)) {
                edtBookId.setError("Mã sách đã tồn tại!");
                return;
            }
            Book newBook = new Book(id, title, author, category, 0);
            if (dbHelper.addBook(newBook)) {
                Toast.makeText(this, "Thêm sách thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Lỗi khi lưu sách!", Toast.LENGTH_SHORT).show();
            }
        } else {
            editingBook.setTitle(title);
            editingBook.setAuthor(author);
            editingBook.setCategory(category);
            if (dbHelper.updateBook(editingBook)) {
                Toast.makeText(this, "Cập nhật sách thành công!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
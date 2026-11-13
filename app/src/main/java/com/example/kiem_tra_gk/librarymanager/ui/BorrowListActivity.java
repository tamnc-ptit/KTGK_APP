package com.example.kiem_tra_gk.librarymanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kiem_tra_gk.R;
import com.example.kiem_tra_gk.librarymanager.adapter.BorrowAdapter;
import com.example.kiem_tra_gk.librarymanager.database.LibraryDbHelper;

public class BorrowListActivity extends AppCompatActivity {

    private LibraryDbHelper dbHelper;
    private BorrowAdapter borrowAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_list);

        dbHelper = new LibraryDbHelper(this);
        Button btnCreateBorrow = findViewById(R.id.btnGoToCreateBorrow);
        RecyclerView rvBorrows = findViewById(R.id.rvBorrows);

        rvBorrows.setLayoutManager(new LinearLayoutManager(this));
        borrowAdapter = new BorrowAdapter(dbHelper.getActiveBorrowRecords(), record -> {
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận trả sách")
                    .setMessage("Xác nhận người mượn \"" + record.getBorrowerName() + "\" đã trả sách \"" + record.getBookTitle() + "\"?")
                    .setPositiveButton("Xác nhận trả", (dialog, which) -> {
                        if (dbHelper.returnBook(record.getRecordId(), record.getBookId())) {
                            Toast.makeText(this, "Đã trả sách. Trạng thái sách: Có sẵn.", Toast.LENGTH_SHORT).show();
                            borrowAdapter.updateData(dbHelper.getActiveBorrowRecords());
                        }
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
        rvBorrows.setAdapter(borrowAdapter);

        btnCreateBorrow.setOnClickListener(v -> {
            startActivity(new Intent(BorrowListActivity.this, CreateBorrowActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        borrowAdapter.updateData(dbHelper.getActiveBorrowRecords());
    }
}
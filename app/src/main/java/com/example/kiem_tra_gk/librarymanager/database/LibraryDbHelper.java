package com.example.kiem_tra_gk.librarymanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.kiem_tra_gk.librarymanager.model.Book;
import com.example.kiem_tra_gk.librarymanager.model.BorrowRecord;

import java.util.ArrayList;
import java.util.List;

public class LibraryDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "LibraryManager.db";
    private static final int DATABASE_VERSION = 1;

    // Bảng Sách
    private static final String TABLE_BOOKS = "books";
    private static final String COL_BOOK_ID = "book_id";
    private static final String COL_TITLE = "title";
    private static final String COL_AUTHOR = "author";
    private static final String COL_CATEGORY = "category";
    private static final String COL_STATUS = "status"; // 0 = Có sẵn, 1 = Đang mượn

    // Bảng Phiếu mượn
    private static final String TABLE_BORROWS = "borrow_records";
    private static final String COL_RECORD_ID = "record_id";
    private static final String COL_BORROWER_NAME = "borrower_name";
    private static final String COL_BORROWER_PHONE = "borrower_phone";
    private static final String COL_BORROW_DATE = "borrow_date";
    private static final String COL_DUE_DATE = "due_date";
    private static final String COL_IS_RETURNED = "is_returned"; // 0 = Hiệu lực, 1 = Đã trả

    public LibraryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createBooksTable = "CREATE TABLE " + TABLE_BOOKS + " (" +
                COL_BOOK_ID + " TEXT PRIMARY KEY, " +
                COL_TITLE + " TEXT NOT NULL, " +
                COL_AUTHOR + " TEXT NOT NULL, " +
                COL_CATEGORY + " TEXT NOT NULL, " +
                COL_STATUS + " INTEGER DEFAULT 0)";

        String createBorrowsTable = "CREATE TABLE " + TABLE_BORROWS + " (" +
                COL_RECORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_BOOK_ID + " TEXT NOT NULL, " +
                COL_BORROWER_NAME + " TEXT NOT NULL, " +
                COL_BORROWER_PHONE + " TEXT NOT NULL, " +
                COL_BORROW_DATE + " TEXT NOT NULL, " +
                COL_DUE_DATE + " TEXT NOT NULL, " +
                COL_IS_RETURNED + " INTEGER DEFAULT 0, " +
                "FOREIGN KEY(" + COL_BOOK_ID + ") REFERENCES " + TABLE_BOOKS + "(" + COL_BOOK_ID + "))";

        db.execSQL(createBooksTable);
        db.execSQL(createBorrowsTable);
        insertSampleData(db);
    }
    private void insertSampleData(SQLiteDatabase db) {
        // 1. Thêm 5 cuốn sách mẫu (4 cuốn Có sẵn [0], 1 cuốn Đang được mượn [1])
        db.execSQL("INSERT INTO " + TABLE_BOOKS + " VALUES ('S001', 'Lập trình Android cơ bản', 'Nguyễn Văn An', 'Công nghệ thông tin', 0)");
        db.execSQL("INSERT INTO " + TABLE_BOOKS + " VALUES ('S002', 'Cấu trúc dữ liệu và Giải thuật', 'Trần Minh Tuấn', 'Công nghệ thông tin', 1)");
        db.execSQL("INSERT INTO " + TABLE_BOOKS + " VALUES ('S003', 'Nhà Giả Kim', 'Paulo Coelho', 'Văn học', 0)");
        db.execSQL("INSERT INTO " + TABLE_BOOKS + " VALUES ('S004', 'Đắc Nhân Tâm', 'Dale Carnegie', 'Kỹ năng sống', 0)");
        db.execSQL("INSERT INTO " + TABLE_BOOKS + " VALUES ('S005', 'Clean Code - Mã sạch', 'Robert C. Martin', 'Công nghệ thông tin', 0)");

        // 2. Thêm 1 phiếu mượn đang hiệu lực tương ứng với cuốn S002
        db.execSQL("INSERT INTO " + TABLE_BORROWS + " (" +
                COL_BOOK_ID + ", " + COL_BORROWER_NAME + ", " + COL_BORROWER_PHONE + ", " +
                COL_BORROW_DATE + ", " + COL_DUE_DATE + ", " + COL_IS_RETURNED + ") " +
                "VALUES ('S002', 'Lê Hoàng Nam', '0912345678', '10/11/2026', '20/11/2026', 0)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BORROWS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        onCreate(db);
    }

    // --- CHỨC NĂNG SÁCH ---

    public boolean isBookIdExists(String bookId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKS, new String[]{COL_BOOK_ID},
                COL_BOOK_ID + "=?", new String[]{bookId}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean addBook(Book book) {
        if (isBookIdExists(book.getBookId())) return false; // Chặn trùng mã sách
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_BOOK_ID, book.getBookId());
        values.put(COL_TITLE, book.getTitle());
        values.put(COL_AUTHOR, book.getAuthor());
        values.put(COL_CATEGORY, book.getCategory());
        values.put(COL_STATUS, 0);
        long res = db.insert(TABLE_BOOKS, null, values);
        return res != -1;
    }

    public boolean updateBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, book.getTitle());
        values.put(COL_AUTHOR, book.getAuthor());
        values.put(COL_CATEGORY, book.getCategory());
        int rows = db.update(TABLE_BOOKS, values, COL_BOOK_ID + "=?", new String[]{book.getBookId()});
        return rows > 0;
    }

    public boolean deleteBook(String bookId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Chỉ cho xóa nếu sách đang Có sẵn (status = 0)
        int rows = db.delete(TABLE_BOOKS, COL_BOOK_ID + "=? AND " + COL_STATUS + "=0", new String[]{bookId});
        return rows > 0;
    }

    public List<Book> getAllBooks(String keyword) {
        List<Book> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        if (keyword == null || keyword.trim().isEmpty()) {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_BOOKS, null);
        } else {
            String query = "SELECT * FROM " + TABLE_BOOKS + " WHERE " +
                    COL_TITLE + " LIKE ? OR " + COL_AUTHOR + " LIKE ?";
            String param = "%" + keyword.trim() + "%";
            cursor = db.rawQuery(query, new String[]{param, param});
        }

        if (cursor.moveToFirst()) {
            do {
                list.add(new Book(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getInt(4)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public List<Book> getAvailableBooks() {
        List<Book> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BOOKS + " WHERE " + COL_STATUS + "=0", null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new Book(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getInt(4)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // --- CHỨC NĂNG MƯỢN & TRẢ SÁCH ---

    public boolean createBorrowRecord(String bookId, String name, String phone, String borrowDate, String dueDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            // 1. Kiểm tra sách có đang sẵn sàng không
            Cursor c = db.query(TABLE_BOOKS, new String[]{COL_STATUS}, COL_BOOK_ID + "=?", new String[]{bookId}, null, null, null);
            if (!c.moveToFirst() || c.getInt(0) != 0) {
                c.close();
                return false;
            }
            c.close();

            // 2. Lưu phiếu mượn
            ContentValues borrowValues = new ContentValues();
            borrowValues.put(COL_BOOK_ID, bookId);
            borrowValues.put(COL_BORROWER_NAME, name);
            borrowValues.put(COL_BORROWER_PHONE, phone);
            borrowValues.put(COL_BORROW_DATE, borrowDate);
            borrowValues.put(COL_DUE_DATE, dueDate);
            borrowValues.put(COL_IS_RETURNED, 0);
            long insertId = db.insert(TABLE_BORROWS, null, borrowValues);

            // 3. Cập nhật trạng thái sách thành Đang được mượn (1)
            ContentValues bookValues = new ContentValues();
            bookValues.put(COL_STATUS, 1);
            int updated = db.update(TABLE_BOOKS, bookValues, COL_BOOK_ID + "=?", new String[]{bookId});

            if (insertId != -1 && updated > 0) {
                db.setTransactionSuccessful();
                return true;
            }
            return false;
        } finally {
            db.endTransaction();
        }
    }

    public List<BorrowRecord> getActiveBorrowRecords() {
        List<BorrowRecord> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT r." + COL_RECORD_ID + ", r." + COL_BOOK_ID + ", b." + COL_TITLE + ", " +
                "r." + COL_BORROWER_NAME + ", r." + COL_BORROWER_PHONE + ", r." + COL_BORROW_DATE + ", " +
                "r." + COL_DUE_DATE + ", r." + COL_IS_RETURNED +
                " FROM " + TABLE_BORROWS + " r INNER JOIN " + TABLE_BOOKS + " b ON r." + COL_BOOK_ID + " = b." + COL_BOOK_ID +
                " WHERE r." + COL_IS_RETURNED + " = 0 ORDER BY r." + COL_RECORD_ID + " DESC";

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new BorrowRecord(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getInt(7)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public boolean returnBook(int recordId, String bookId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues borrowValues = new ContentValues();
            borrowValues.put(COL_IS_RETURNED, 1);
            int r1 = db.update(TABLE_BORROWS, borrowValues, COL_RECORD_ID + "=?", new String[]{String.valueOf(recordId)});

            ContentValues bookValues = new ContentValues();
            bookValues.put(COL_STATUS, 0); // Trả về Có sẵn
            int r2 = db.update(TABLE_BOOKS, bookValues, COL_BOOK_ID + "=?", new String[]{bookId});

            if (r1 > 0 && r2 > 0) {
                db.setTransactionSuccessful();
                return true;
            }
            return false;
        } finally {
            db.endTransaction();
        }
    }
}
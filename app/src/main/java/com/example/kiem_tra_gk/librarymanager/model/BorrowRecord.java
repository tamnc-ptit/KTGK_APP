package com.example.kiem_tra_gk.librarymanager.model;

public class BorrowRecord {
    private int recordId;
    private String bookId;
    private String bookTitle; // Dùng để hiển thị kèm tên sách
    private String borrowerName;
    private String borrowerPhone;
    private String borrowDate;
    private String dueDate;
    private int isReturned; // 0: Đang mượn, 1: Đã trả

    public BorrowRecord(int recordId, String bookId, String bookTitle, String borrowerName,
                        String borrowerPhone, String borrowDate, String dueDate, int isReturned) {
        this.recordId = recordId;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.borrowerName = borrowerName;
        this.borrowerPhone = borrowerPhone;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.isReturned = isReturned;
    }

    public int getRecordId() { return recordId; }
    public String getBookId() { return bookId; }
    public String getBookTitle() { return bookTitle; }
    public String getBorrowerName() { return borrowerName; }
    public String getBorrowerPhone() { return borrowerPhone; }
    public String getBorrowDate() { return borrowDate; }
    public String getDueDate() { return dueDate; }
    public int getIsReturned() { return isReturned; }
}
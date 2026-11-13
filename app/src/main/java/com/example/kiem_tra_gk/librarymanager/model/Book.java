package com.example.kiem_tra_gk.librarymanager.model;

import java.io.Serializable;
public class Book implements Serializable {
    private String bookId;
    private String title;
    private String author;
    private String category;
    private int status; // 0: Có sẵn, 1: Đang được mượn

    public Book(String bookId, String title, String author, String category, int status) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.status = status;
    }

    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getStatusText() {
        return status == 0 ? "Có sẵn" : "Đang được mượn";
    }

    @Override
    public String toString() {
        return bookId + " - " + title; // Hiển thị trên Spinner chọn sách mượn
    }
}
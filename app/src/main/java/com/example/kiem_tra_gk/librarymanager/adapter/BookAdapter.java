package com.example.kiem_tra_gk.librarymanager.adapter;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kiem_tra_gk.R;
import com.example.kiem_tra_gk.librarymanager.model.Book;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    public interface OnBookActionListener {
        void onEditBook(Book book);
        void onDeleteBook(Book book);
    }

    private List<Book> bookList;
    private final OnBookActionListener listener;

    public BookAdapter(List<Book> bookList, OnBookActionListener listener) {
        this.bookList = bookList;
        this.listener = listener;
    }

    public void updateData(List<Book> newList) {
        this.bookList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.tvTitle.setText(book.getTitle() + " (" + book.getBookId() + ")");
        holder.tvAuthorCategory.setText("Tác giả: " + book.getAuthor() + " | Thể loại: " + book.getCategory());
        holder.tvStatus.setText(book.getStatusText());

        if (book.getStatus() == 0) {
            holder.tvStatus.setTextColor(Color.parseColor("#2E7D32")); // Xanh lá: Có sẵn
        } else {
            holder.tvStatus.setTextColor(Color.parseColor("#C62828")); // Đỏ: Đang mượn
        }

        holder.btnEdit.setOnClickListener(v -> listener.onEditBook(book));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteBook(book));
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAuthorCategory, tvStatus;
        ImageButton btnEdit, btnDelete;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvBookTitle);
            tvAuthorCategory = itemView.findViewById(R.id.tvAuthorCategory);
            tvStatus = itemView.findViewById(R.id.tvBookStatus);
            btnEdit = itemView.findViewById(R.id.btnEditBook);
            btnDelete = itemView.findViewById(R.id.btnDeleteBook);
        }
    }
}
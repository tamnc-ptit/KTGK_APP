package com.example.kiem_tra_gk.librarymanager.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kiem_tra_gk.R;
import com.example.kiem_tra_gk.librarymanager.model.BorrowRecord;
import java.util.List;

public class BorrowAdapter extends RecyclerView.Adapter<BorrowAdapter.BorrowViewHolder> {

    public interface OnReturnClickListener {
        void onReturnBook(BorrowRecord record);
    }

    private List<BorrowRecord> recordList;
    private final OnReturnClickListener listener;

    public BorrowAdapter(List<BorrowRecord> recordList, OnReturnClickListener listener) {
        this.recordList = recordList;
        this.listener = listener;
    }

    public void updateData(List<BorrowRecord> newList) {
        this.recordList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BorrowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_borrow, parent, false);
        return new BorrowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BorrowViewHolder holder, int position) {
        BorrowRecord record = recordList.get(position);
        holder.tvBookInfo.setText("Sách: " + record.getBookTitle() + " [" + record.getBookId() + "]");
        holder.tvBorrower.setText("Người mượn: " + record.getBorrowerName() + " - SĐT: " + record.getBorrowerPhone());
        holder.tvDates.setText("Ngày mượn: " + record.getBorrowDate() + " | Hạn trả: " + record.getDueDate());
        holder.btnReturn.setOnClickListener(v -> listener.onReturnBook(record));
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    static class BorrowViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookInfo, tvBorrower, tvDates;
        Button btnReturn;

        public BorrowViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookInfo = itemView.findViewById(R.id.tvBorrowBookInfo);
            tvBorrower = itemView.findViewById(R.id.tvBorrowerInfo);
            tvDates = itemView.findViewById(R.id.tvBorrowDates);
            btnReturn = itemView.findViewById(R.id.btnReturnBook);
        }
    }
}
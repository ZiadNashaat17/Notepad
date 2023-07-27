package com.example.todo.rv;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.MainActivity;
import com.example.todo.NoteDetailsActivity;
import com.example.todo.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private Activity activity;
    private ArrayList<Model> notes;
    List<Model> data;

    public Adapter(Activity activity, List<Model> data) {
        this.activity = activity;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvTitle.setText(data.get(position).getTitle());
        holder.tvBody.setText(data.get(position).getDescription());
        holder.cardView.setOnClickListener(view -> {
            Intent intent = new Intent(activity, NoteDetailsActivity.class);
            intent.putExtra("id", data.get(position).getId());
            intent.putExtra("title", data.get(position).getTitle());
            intent.putExtra("description", data.get(position).getDescription());
            activity.startActivity(intent);
        });
        holder.iBtnEdit.setOnClickListener(view -> {
            Intent intent = new Intent(activity, NoteDetailsActivity.class);
            intent.putExtra("id", data.get(position).getId());
            intent.putExtra("title", data.get(position).getTitle());
            intent.putExtra("description", data.get(position).getDescription());
            activity.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvBody;
        ImageButton iBtnEdit, iBtnDelete;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvBody = itemView.findViewById(R.id.tv_body);
            iBtnEdit = itemView.findViewById(R.id.i_btn_edit);

        }
    }
}

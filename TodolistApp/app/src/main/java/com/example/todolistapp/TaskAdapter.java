package com.example.todolistapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final List<Task> taskList;
    private OnEditButtonClickListener onEditButtonClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public TaskAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    public interface OnEditButtonClickListener {
        void onEditButtonClick(int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    public void setOnEditButtonClickListener(OnEditButtonClickListener listener) {
        this.onEditButtonClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.taskDescriptionTextView.setText(task.getDescription());

        holder.checkBoxTask.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(holder.itemView.getContext(), "Task \"" + task.getDescription() + "\" completed", Toast.LENGTH_SHORT).show();
                // Perform any other actions when checkbox is checked
            }
        });

        holder.editButton.setOnClickListener(v -> {
            if (onEditButtonClickListener != null) {
                onEditButtonClickListener.onEditButtonClick(holder.getAdapterPosition());
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onItemLongClick(holder.getAdapterPosition());
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskDescriptionTextView;
        CheckBox checkBoxTask;
        ImageButton editButton;

        TaskViewHolder(View itemView) {
            super(itemView);
            taskDescriptionTextView = itemView.findViewById(R.id.task_description_text_view);
            checkBoxTask = itemView.findViewById(R.id.check_box_task);
            editButton = itemView.findViewById(R.id.button_edit_task);
        }
    }
}

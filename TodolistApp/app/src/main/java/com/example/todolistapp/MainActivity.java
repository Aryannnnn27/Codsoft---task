package com.example.todolistapp;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        FloatingActionButton buttonAddTask = findViewById(R.id.button_add_task);

        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(taskAdapter);

        sharedPreferences = getSharedPreferences("tasks", MODE_PRIVATE);
        loadTasks();

        buttonAddTask.setOnClickListener(v -> showAddTaskDialog());

        taskAdapter.setOnEditButtonClickListener(this::showEditTaskDialog);

        taskAdapter.setOnItemLongClickListener(this::showPinTaskDialog);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Task task = taskList.get(position);
                taskList.remove(position);
                taskAdapter.notifyItemRemoved(position);
                saveTasks();
                Toast.makeText(MainActivity.this, "Task \"" + task.getTitle() + "\" deleted", Toast.LENGTH_SHORT).show();
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Task");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null);
        EditText inputTaskTitle = view.findViewById(R.id.input_task_title);
        EditText inputTaskDescription = view.findViewById(R.id.input_task_description);

        builder.setView(view);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String taskTitle = inputTaskTitle.getText().toString().trim();
            String taskDescription = inputTaskDescription.getText().toString().trim();
            if (!taskTitle.isEmpty() && !taskDescription.isEmpty()) {
                Task task = new Task(taskTitle, taskDescription);
                taskList.add(task);
                taskAdapter.notifyItemInserted(taskList.size() - 1);
                saveTasks();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showEditTaskDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Task");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null);
        EditText inputTaskTitle = view.findViewById(R.id.input_task_title);
        EditText inputTaskDescription = view.findViewById(R.id.input_task_description);

        Task task = taskList.get(position);
        inputTaskTitle.setText(task.getTitle());
        inputTaskDescription.setText(task.getDescription());

        builder.setView(view);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String taskTitle = inputTaskTitle.getText().toString().trim();
            String taskDescription = inputTaskDescription.getText().toString().trim();
            if (!taskTitle.isEmpty() && !taskDescription.isEmpty()) {
                task.setTitle(taskTitle);
                task.setDescription(taskDescription);
                taskAdapter.notifyItemChanged(position);
                saveTasks();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showPinTaskDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pin Task");

        builder.setMessage("Do you want to pin this task?");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            Task task = taskList.get(position);
            taskList.remove(position);
            taskList.add(0, task);
            taskAdapter.notifyItemMoved(position, 0);
            saveTasks();
        });

        builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void saveTasks() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> taskSet = new HashSet<>();
        for (Task task : taskList) {
            taskSet.add(task.getTitle() + "::" + task.getDescription());
        }
        editor.putStringSet("tasks", taskSet);
        editor.apply();
    }

    private void loadTasks() {
        Set<String> taskSet = sharedPreferences.getStringSet("tasks", new HashSet<>());
        for (String taskString : taskSet) {
            String[] parts = taskString.split("::");
            if (parts.length == 2) {
                taskList.add(new Task(parts[0], parts[1]));
            }
        }
    }
}

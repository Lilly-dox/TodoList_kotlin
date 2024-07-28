package com.example.todolist1

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist1.databinding.ItemTodoBinding
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist1.Todo
import com.example.todolist1.TodoAdapter
import com.example.todolist1.databinding.ActivityMainBinding
import android.content.Intent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DiffUtil



import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG




import android.graphics.Paint


class TodoAdapter(
    private var todos: MutableList<Todo>
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    class TodoViewHolder(val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val curTodo = todos[position]
        holder.binding.apply {
            tvTodoTitle.text = curTodo.title
            cbDone.isChecked = curTodo.isChecked
            toggleStrikeThrough(tvTodoTitle, curTodo.isChecked)

            // Clear any existing listener to prevent multiple callbacks
            cbDone.setOnCheckedChangeListener(null)

            cbDone.setOnCheckedChangeListener { _, isChecked ->
                // Update the current todo's isChecked state
                val updatedTodo = curTodo.copy(isChecked = isChecked)
                // Update the list item with the new updated Todo
                todos[position] = updatedTodo
                toggleStrikeThrough(tvTodoTitle, isChecked)
            }
        }
    }

    override fun getItemCount(): Int = todos.size

    private fun toggleStrikeThrough(tvTodoTitle: TextView, isChecked: Boolean) {
        if (isChecked) {
            tvTodoTitle.paintFlags = tvTodoTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            tvTodoTitle.paintFlags = tvTodoTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    fun addTodo(todo: Todo) {
        todos.add(todo)
        notifyItemInserted(todos.size - 1)
    }

    fun deleteDoneTodos() {
        val iterator = todos.listIterator()
        while (iterator.hasNext()) {
            val todo = iterator.next()
            if (todo.isChecked) {
                val index = iterator.previousIndex()
                iterator.remove()
                notifyItemRemoved(index)
                notifyItemRangeChanged(index, todos.size)
            }
        }
    }
}
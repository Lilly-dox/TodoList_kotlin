package com.example.todolist1

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist1.databinding.ItemTodoBinding

class TodoAdapter(
    private val todos: MutableList<Todo>,
    private val onEditClicked: (Todo, Int) -> Unit
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    class TodoViewHolder(val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTodoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TodoViewHolder(binding)
    }

    fun addTodo(todo: Todo) {
        todos.add(todo)
        notifyItemInserted(todos.size - 1)
    }

    fun updateTodo(position: Int, updatedTodo: Todo) {
        if (position in 0 until todos.size) {
            todos[position] = updatedTodo
            notifyItemChanged(position)
        }
    }

    fun updateList(newList: List<Todo>) {
        todos.clear()
        todos.addAll(newList)
        notifyDataSetChanged()
    }

    fun deleteDoneTodos() {
        // Duyệt qua danh sách theo chiều ngược lại
        for (i in todos.size - 1 downTo 0) {
            if (todos[i].isChecked) {
                todos.removeAt(i)
                notifyItemRemoved(i)
            }
        }
    }

    private fun toggleStrikeThrough(tvTodoTitle: TextView, isChecked: Boolean) {
        if (isChecked) {
            tvTodoTitle.paintFlags = tvTodoTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
        } else {
            tvTodoTitle.paintFlags = tvTodoTitle.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        if (position < 0 || position >= todos.size) return  // Thêm kiểm tra này

        val curTodo = todos[position]
        holder.binding.apply {
            tvTodoTitle.text = curTodo.title
            cbDone.isChecked = curTodo.isChecked
            toggleStrikeThrough(tvTodoTitle, curTodo.isChecked)

            cbDone.setOnCheckedChangeListener(null)

            cbDone.setOnCheckedChangeListener { _, isChecked ->
                holder.itemView.post {
                    if (position < todos.size) {  // Thêm kiểm tra này
                        toggleStrikeThrough(tvTodoTitle, isChecked)
                        val updatedTodo = curTodo.copy(isChecked = isChecked)
                        todos[position] = updatedTodo
                        notifyItemChanged(position)
                    }
                }
            }

            btnEdit.setOnClickListener {
                if (position < todos.size) {  // Thêm kiểm tra này
                    onEditClicked(curTodo, position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return todos.size
    }
}

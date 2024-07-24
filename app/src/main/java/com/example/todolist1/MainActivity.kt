package com.example.todolist1

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var todoAdapter: TodoAdapter
    private lateinit var binding: ActivityMainBinding
    private val todoList = mutableListOf<Todo>()

    private val editTodoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val updatedDescription = data?.getStringExtra("TODO_DESCRIPTION")
            val position = data?.getIntExtra("TODO_POSITION", -1) ?: -1

            if (position in 0 until todoList.size && updatedDescription != null) {
                val updatedTodo = todoList[position].copy(description = updatedDescription)
                todoList[position] = updatedTodo

                runOnUiThread {
                    todoAdapter.notifyItemChanged(position)
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        todoAdapter = TodoAdapter(todoList) { todo, position ->
            val intent = Intent(this, TodoDetailsActivity::class.java).apply {
                putExtra("TODO_TITLE", todo.title)
                putExtra("TODO_DESCRIPTION", todo.description)
                putExtra("TODO_POSITION", position)
            }
            editTodoLauncher.launch(intent)
        }
        //todoAdapter = TodoAdapter(mutableListOf())

        binding.rvTodoItems.adapter = todoAdapter

        binding.rvTodoItems.layoutManager = LinearLayoutManager(this)
        binding.btnAddTodo.setOnClickListener {
            val todoTitle = binding.etTodoTitle.text.toString()
            if (todoTitle.isNotEmpty()) {
                val todo = Todo(todoTitle)
                todoAdapter.addTodo(todo)
                binding.etTodoTitle.text.clear()
            }
        }

        binding.btnDeleteDoneTodos.setOnClickListener {
            todoAdapter.deleteDoneTodos()

            binding.rvTodoItems.post { todoAdapter.notifyDataSetChanged() }
        }


    }
}

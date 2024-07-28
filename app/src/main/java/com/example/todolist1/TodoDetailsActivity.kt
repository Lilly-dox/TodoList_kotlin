package com.example.todolist1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist1.databinding.ActivityTodoDetailsBinding

class TodoDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTodoDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("TODO_TITLE")
        val description = intent.getStringExtra("TODO_DESCRIPTION")
        val position = intent.getIntExtra("TODO_POSITION", -1)

        binding.todoTitleEditText.setText(title)
        binding.todoDescriptionEditText.setText(description)

        binding.saveButton.setOnClickListener {
            val updatedDescription = binding.todoDescriptionEditText.text.toString()
            val resultIntent = Intent().apply {
                putExtra("TODO_DESCRIPTION", updatedDescription)
                putExtra("TODO_POSITION", position)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}

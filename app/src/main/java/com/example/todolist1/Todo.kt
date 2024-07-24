package com.example.todolist1

data class Todo(
    val title: String,
    val description: String = "",
    val isChecked: Boolean = false
)
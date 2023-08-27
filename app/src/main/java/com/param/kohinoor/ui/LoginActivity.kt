package com.param.kohinoor.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.param.kohinoor.MainActivity
import com.param.kohinoor.R
import com.param.kohinoor.databinding.ActivityLogin2Binding
import com.param.kohinoor.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLogin2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogin2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            submit.setOnClickListener {
                if (username.text.toString() == "admin" && password.text.toString() == "admin") {
                    Intent(this@LoginActivity, MainActivity::class.java).also {
                        startActivity(it)
                    }
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Incorrect Username/Password",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
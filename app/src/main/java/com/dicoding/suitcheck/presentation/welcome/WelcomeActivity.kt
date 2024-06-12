package com.dicoding.suitcheck.presentation.welcome

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.suitcheck.R
import com.dicoding.suitcheck.databinding.ActivityWelcomeBinding
import com.dicoding.suitcheck.presentation.userlist.UserListActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.welcome)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupView()
        setupAction()
    }

    @Suppress("DEPRECATION")
    private fun setupAction() {
        binding.toolbar.setOnClickListener {
            onBackPressed()
        }
        binding.chooseUserButton.setOnClickListener {
            val intent = Intent(this, UserListActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupView() {
        val name = intent.getStringExtra("NAME")
        binding.nameInput.text = name
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            val selectedUser = data?.getStringExtra("SELECTED_USER")
            findViewById<TextView>(R.id.selectedUserLabel).text = "Selected User: $selectedUser"
        }
    }

    companion object {
        const val REQUEST_CODE = 1
    }
}
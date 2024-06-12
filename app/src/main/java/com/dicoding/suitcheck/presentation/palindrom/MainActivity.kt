package com.dicoding.suitcheck.presentation.palindrom

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.suitcheck.R
import com.dicoding.suitcheck.databinding.ActivityMainBinding
import com.dicoding.suitcheck.presentation.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupAction()
    }

    private fun setupAction() {
        binding.checkButton.setOnClickListener {
            val sentence = binding.sentenceInput.text.toString()
            if (isPalindrome(sentence) && sentence.isNotEmpty()) {
                showDialog("Palindrome")
            } else if (!isPalindrome(sentence) && sentence.isNotEmpty()){
                showDialog("Not Palindrome")
            } else {
                showDialog("palindrome word must be filled")
            }
        }

        binding.nextButton.setOnClickListener {
            val name = binding.nameInput.text.toString()
            if (name.isEmpty()) {
                showDialog("Name must be filled")
                return@setOnClickListener
            }
            val intent = Intent(this, WelcomeActivity::class.java)
            intent.putExtra("NAME", name)
            startActivity(intent)
        }
    }

    private fun isPalindrome(sentence: String): Boolean {
        val cleanSentence = sentence.replace("\\s".toRegex(), "")
        val reverseSentence = cleanSentence.reversed()
        return cleanSentence.equals(reverseSentence, ignoreCase = true)
    }

    private fun showDialog(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}
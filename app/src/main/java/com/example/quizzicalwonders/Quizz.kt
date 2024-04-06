package com.example.quizzicalwonders

import android.app.AlertDialog
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Quizz : AppCompatActivity(), QuizAdapter.OnOptionSelectedListener {

    private lateinit var database: FirebaseDatabase
    private lateinit var quizRef: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var quizList: MutableList<QuizData>
    private var totalQuestions: Int = 0
    private var correctAnswers: Int = 0
    private var currentQuestionIndex = 0
    private lateinit var adapter: QuizAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quizz)

        database = FirebaseDatabase.getInstance()
        quizRef = database.getReference("quizzes")
        quizList = mutableListOf()

        recyclerView = findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        adapter = QuizAdapter(quizList,this)
        recyclerView.adapter = adapter

        quizRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                totalQuestions = dataSnapshot.child("quizzes").childrenCount.toInt()
                Log.d(TAG, "Total questions: $totalQuestions")

                for (quizSnapshot in dataSnapshot.child("quizzes").children) {
                    val question = quizSnapshot.child("question").getValue(String::class.java)
                    val options = mutableMapOf<String, String>()
                    quizSnapshot.child("options").children.forEach { optionSnapshot ->
                        val key = optionSnapshot.key
                        val value = optionSnapshot.getValue(String::class.java)
                        if (key != null && value != null) {
                            options[key] = value
                        }
                    }
                    val correctAnswer = quizSnapshot.child("correctAnswer").getValue(String::class.java)

                    // Check if question, options, and correctAnswer are not null or empty before processing
                    if (!question.isNullOrEmpty() && options.isNotEmpty() && !correctAnswer.isNullOrEmpty()) {
                        Log.d(TAG, "Question: $question, Options: $options, Correct Answer: $correctAnswer")
                        val quiz = QuizData(question, options, correctAnswer)
                        quizList.add(quiz)
                    } else {
                        Log.e(TAG, "Question, options, or correct answer is null or empty")
                    }
                }

                Log.d(TAG, "Quiz list size: ${quizList.size}")

                // Notify RecyclerView adapter after adding all quiz data
                val adapter = QuizAdapter(quizList,this@Quizz)
                recyclerView.adapter = adapter
                adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                    override fun onChanged() {
                        super.onChanged()
                        if (isQuizCompleted()) {
                            showScoreDialog()
                        }
                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Database error: ${error.message}")
            }
        })
    }

    fun incrementCorrectAnswers() {
        correctAnswers++
        Log.d(TAG, "Correct answers: $correctAnswers")
    }

    fun isQuizCompleted(): Boolean {
        val completed = correctAnswers == totalQuestions
        Log.d(TAG, "Quiz completed: $completed")
        return completed
    }

    fun showScoreDialog() {
        Log.d(TAG, "Showing score dialog")
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Quiz Completed")
        alertDialog.setMessage("Total Questions: $totalQuestions\nCorrect Answers: $correctAnswers\nIncorrect Answers: ${totalQuestions - correctAnswers}")
        alertDialog.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.show()
    }

    private fun navigateToPreviousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--
            recyclerView.scrollToPosition(currentQuestionIndex)
        }
    }

    private fun navigateToNextQuestion() {
        if (currentQuestionIndex < quizList.size - 1) {
            currentQuestionIndex++
            recyclerView.scrollToPosition(currentQuestionIndex)
        }
    }
    override fun onLeftArrowClicked() {
        navigateToPreviousQuestion()
    }

    override fun onRightArrowClicked() {
        navigateToNextQuestion()
    }
}

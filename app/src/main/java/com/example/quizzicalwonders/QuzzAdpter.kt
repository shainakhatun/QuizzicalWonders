package com.example.quizzicalwonders

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class QuizAdapter(private val quizzes: List<QuizData>, private val listener: OnOptionSelectedListener) : RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {
    interface OnOptionSelectedListener {
        fun onLeftArrowClicked()
        fun onRightArrowClicked()
    }

    interface ScoreUpdateListener {
        fun onScoreUpdated(correctResponses: Int, noResponse: Int)
    }

    private var correctResponses = 0
    private var noResponse = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_quiz, parent, false)
        return QuizViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val quiz = quizzes[position]
        holder.bind(quiz)
    }

    override fun getItemCount() = quizzes.size


    inner class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val optionA = itemView.findViewById<RadioButton>(R.id.optionA)
        private val optionB = itemView.findViewById<RadioButton>(R.id.optionB)
        private val optionC = itemView.findViewById<RadioButton>(R.id.optionC)
        private val optionD = itemView.findViewById<RadioButton>(R.id.optionD)

        init {
            val leftArrowImageView= itemView.findViewById<View>(R.id.left_arrow_image_view)
            leftArrowImageView.setOnClickListener {
                listener.onLeftArrowClicked()
            }
            val rightArrowImageView= itemView.findViewById<View>(R.id.right_arrow_image_view)
            rightArrowImageView.setOnClickListener {
                listener.onRightArrowClicked()
            }
        }
        fun bind(quiz: QuizData) {
            itemView.findViewById<TextView>(R.id.txtQuestion).text = quiz.question

            optionA.text = quiz.options["optionA"]
            optionB.text = quiz.options["optionB"]
            optionC.text = quiz.options["optionC"]
            optionD.text = quiz.options["optionD"]

            val button = itemView.findViewById<Button>(R.id.btnShow)
            button.setOnClickListener {
                if (optionA.isChecked) {
                    checkAnswer(optionA, quiz.correctAnswer)
                } else if (optionB.isChecked) {
                    checkAnswer(optionB, quiz.correctAnswer)
                } else if (optionC.isChecked) {
                    checkAnswer(optionC, quiz.correctAnswer)
                } else if (optionD.isChecked) {
                    checkAnswer(optionD, quiz.correctAnswer)
                }
            }
//            optionA.setOnClickListener {
//                checkAnswer(optionA, quiz.correctAnswer)
//            }
//            optionB.setOnClickListener {
//                checkAnswer(optionB, quiz.correctAnswer)
//            }
//            optionC.setOnClickListener {
//                checkAnswer(optionC, quiz.correctAnswer)
//            }
//            optionD.setOnClickListener {
//                checkAnswer(optionD, quiz.correctAnswer)
//            }

    }
        private fun checkAnswer(selectedOption: RadioButton, correctAnswer: String) {
            val selectedText = selectedOption.text.toString()
            val context = itemView.context
            val builder = AlertDialog.Builder(context)

            val color = if (selectedText == correctAnswer) {
                itemView.resources.getColor(android.R.color.holo_green_dark)
            } else {
                itemView.resources.getColor(android.R.color.holo_red_light)
            }
            selectedOption.buttonTintList = ColorStateList.valueOf(color)
            if (selectedText == correctAnswer) {
                val view = LayoutInflater.from(context).inflate(R.layout.dialog_correct, null)
                builder.setView(view)
                correctResponses++
            } else {
                val view = LayoutInflater.from(context).inflate(R.layout.dialog_incorrect, null)
                val correctAnswerTextView = view.findViewById<TextView>(R.id.textViewCorrectAnswer)
                correctAnswerTextView.text = "Correct Answer: $correctAnswer"
                builder.setView(view)
                val isNoneSelected = !optionA.isChecked && !optionB.isChecked && !optionC.isChecked && !optionD.isChecked
                if (isNoneSelected) {
                    noResponse++
                    Log.d("unattempted", "Unattempted response incremented: $noResponse") // Debug logging
                }
            }

                builder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()

            Log.d("correct", "Correct Responses: $correctResponses")
            Log.d("noresponse", "No Response: $noResponse")

            (itemView.context as? ScoreUpdateListener)?.onScoreUpdated(correctResponses, noResponse)
        }
    }
}
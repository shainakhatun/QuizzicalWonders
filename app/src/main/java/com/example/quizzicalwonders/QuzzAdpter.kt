package com.example.quizzicalwonders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class QuizAdapter(private val quizzes: List<QuizData>) : RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

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
        fun bind(quiz: QuizData) {
            itemView.findViewById<TextView>(R.id.txtQuestion).text = quiz.question

            val optionA = itemView.findViewById<RadioButton>(R.id.optionA)
            val optionB = itemView.findViewById<RadioButton>(R.id.optionB)
            val optionC = itemView.findViewById<RadioButton>(R.id.optionC)
            val optionD = itemView.findViewById<RadioButton>(R.id.optionD)
            val button = itemView.findViewById<Button>(R.id.btnShow)

            optionA.text = quiz.options["optionA"]
            optionB.text = quiz.options["optionB"]
            optionC.text = quiz.options["optionC"]
            optionD.text = quiz.options["optionD"]

            button.setOnClickListener(View.OnClickListener {
                if (optionA.isChecked) {
                    checkAnswer(optionA, quiz.correctAnswer)
                } else if (optionB.isChecked) {
                    checkAnswer(optionB, quiz.correctAnswer)
                } else if (optionC.isChecked) {
                    checkAnswer(optionC, quiz.correctAnswer)
                } else if (optionD.isChecked) {
                    checkAnswer(optionD, quiz.correctAnswer)
                }
            })
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
            if (selectedText == correctAnswer) {
                (itemView.context as? Quizz)?.incrementCorrectAnswers()
                Toast.makeText(itemView.context, "Correct!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(itemView.context, "Incorrect!", Toast.LENGTH_SHORT).show()
            }
            if ((itemView.context as? Quizz)?.isQuizCompleted() == true) {
                (itemView.context as? Quizz)?.showScoreDialog()
            }
        }
    }
}

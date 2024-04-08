package com.example.quizzicalwonders

import android.content.res.ColorStateList
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
            val context = itemView.context
            val color = if (selectedText == correctAnswer) {
                itemView.resources.getColor(android.R.color.holo_green_dark) // Green color for correct answer
            } else {
                itemView.resources.getColor(android.R.color.holo_red_light) // Red color for incorrect answer
            }
            selectedOption.buttonTintList = ColorStateList.valueOf(color)

            val toastMessage = if (selectedText == correctAnswer) {
                "Correct!"
            } else {
                "Incorrect!"
            }
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()

            if ((context as? Quizz)?.isQuizCompleted() == true) {
                context.showScoreDialog()
            }

//            if (selectedText == correctAnswer) {
//                (itemView.context as? Quizz)?.incrementCorrectAnswers()
//                Toast.makeText(itemView.context, "Correct!", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(itemView.context, "Incorrect!", Toast.LENGTH_SHORT).show()
//            }
//            if ((itemView.context as? Quizz)?.isQuizCompleted() == true) {
//                (itemView.context as? Quizz)?.showScoreDialog()
//            }
        }
    }
}

package com.example.he

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import java.lang.Exception
import kotlin.random.Random

class QuizRun : AppCompatActivity() {
    var QTimeTextView : TextView? =null
    var QQuesText : TextView? =null
    var QAlertTextView : TextView? =null
    var QScoreTextView : TextView? =null
    var QFinalScoreTextView : TextView? =null

    lateinit var Qbutton0: Button
    lateinit var Qbutton1: Button
    lateinit var Qbutton2: Button
    lateinit var Qbutton3: Button

    var countDownTimer: CountDownTimer?=null
    var random : Random = Random
    var a =0
    var b=0
    var correctans =0
    var ans = ArrayList<Int>()
    var pt =0
    var totalQuestion = 0
    var cals= ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_run)
        val calInt = intent.getStringExtra("cal")
        cals =calInt!!
        QTimeTextView = findViewById(R.id.QTimeTextView)
        QQuesText=findViewById(R.id.QQuesText)
        QAlertTextView=findViewById(R.id.QAlertTextView)
        QScoreTextView=findViewById(R.id.QScoreTextView)

        Qbutton0 = findViewById<Button>(R.id.Qbutton0)
        Qbutton1 = findViewById<Button>(R.id.Qbutton1)
        Qbutton2 = findViewById<Button>(R.id.Qbutton2)
        Qbutton3 = findViewById<Button>(R.id.Qbutton3)

        start()
    }

    fun NextQuestion(cal:String) {
        a = random.nextInt(10)
        b = random.nextInt(10)
        QQuesText!!.text = "$a $cal $b"
        correctans = random.nextInt(4)

        ans.clear()

        for (i in 0..3) {
            if (correctans == i) {
                when (cal) {
                    "+" -> {
                        ans.add(a + b)
                    }

                    "-" -> {
                        ans.add(a - b)
                    }

                    "x" -> {
                        ans.add(a * b)
                    }

                    "/" -> {
                        try {
                            ans.add(a / b)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            } else {
                var wrongAns = random.nextInt(20)
                try {
                    while (
                        wrongAns == a + b ||
                        wrongAns == a - b ||
                        wrongAns == a * b ||
                        wrongAns == a / b
                    ) {
                        wrongAns = random.nextInt(20)
                    }
                    ans.add(wrongAns)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        try {
            Qbutton0!!.text="${ans[0]}"
            Qbutton1!!.text="${ans[1]}"
            Qbutton2!!.text="${ans[2]}"
            Qbutton3!!.text="${ans[3]}"
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun QoptionSelct(view: View?){
        totalQuestion++
        if (correctans.toString() == view!!.tag.toString()){
            pt++
            QAlertTextView!!.text= "Correct"
        }else{
            QAlertTextView!!.text= "Wrong"
        }
        QScoreTextView!!.text = "$pt/$totalQuestion"
        NextQuestion(cals)
    }

    fun PlayAgain(view: View?){
        pt=0
        totalQuestion=0
        QScoreTextView!!.text="$pt/$totalQuestion"
        countDownTimer!!.start()
    }

    private fun start() {
        NextQuestion(cals)
        countDownTimer = object :CountDownTimer(30000,1000){
            override fun onTick(p0: Long) {
                val timeleft = p0/1000
                QTimeTextView!!.text = timeleft.toString()+"s"
            }

            override fun onFinish() {
                QTimeTextView!!.text = "Time Up!"
                openDilog()
            }

            private fun openDilog(){
                val  inflater = LayoutInflater.from(this@QuizRun)
                val winDialog = inflater.inflate(R.layout.qwin,null)
                QFinalScoreTextView =winDialog.findViewById(R.id.QFinalScoreTextView)
                val QbtnPlayagin = winDialog.findViewById<Button>(R.id.QPlayAgain)
                val Qback = winDialog.findViewById<Button>(R.id.QBack)
                val dialog = AlertDialog.Builder(this@QuizRun)
                dialog.setCancelable(false)
                dialog.setView(winDialog)
                QFinalScoreTextView!!.text = "$pt/$totalQuestion"
                Qback.setOnClickListener{ onBackPressed() }
                val showDialog = dialog.create()
                showDialog.show()
                QbtnPlayagin.setOnClickListener{
                    PlayAgain(it)
                    showDialog.dismiss() }

            }
        }

        (countDownTimer as CountDownTimer).start()
    }
}
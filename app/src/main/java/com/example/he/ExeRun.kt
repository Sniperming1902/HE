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

class ExeRun : AppCompatActivity() {
    var EQuesText : TextView? =null
    var EAlertTextView : TextView? =null
    var EScoreTextView : TextView? =null
    var EFinalScoreTextView : TextView? =null
    var ENumberQuestionTextView : TextView? =null

    lateinit var Ebutton0: Button
    lateinit var Ebutton1: Button
    lateinit var Ebutton2: Button
    lateinit var Ebutton3: Button

    var countDownTimer: CountDownTimer?=null
    var random : Random = Random
    var a =0
    var b=0
    var correctans =0
    var ans = ArrayList<Int>()
    var pt =0
    var doneQuestion = 1
    var totalQuestion = 20
    var cals= ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exe_run)

        val calInt = intent.getStringExtra("cal")
        cals =calInt!!
        EQuesText=findViewById(R.id.EQuesText)
        EAlertTextView=findViewById(R.id.EAlertTextView)
        EScoreTextView=findViewById(R.id.EScoreTextView)
        ENumberQuestionTextView=findViewById(R.id.NumberQuestion)

        Ebutton0 = findViewById<Button>(R.id.Ebutton0)
        Ebutton1 = findViewById<Button>(R.id.Ebutton1)
        Ebutton2 = findViewById<Button>(R.id.Ebutton2)
        Ebutton3 = findViewById<Button>(R.id.Ebutton3)
        ENumberQuestionTextView!!.text = doneQuestion.toString()
        start()
    }

    fun NextQuestion(cal:String) {
        a = random.nextInt(10)
        b = random.nextInt(10)
        EQuesText!!.text = "$a $cal $b"
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
            Ebutton0!!.text="${ans[0]}"
            Ebutton1!!.text="${ans[1]}"
            Ebutton2!!.text="${ans[2]}"
            Ebutton3!!.text="${ans[3]}"
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun QoptionSelct(view: View?){
        doneQuestion++
        ENumberQuestionTextView!!.text = doneQuestion.toString()
        if (doneQuestion == 21){
            openDilog()
        }else{
            if (correctans.toString() == view!!.tag.toString()){
                EAlertTextView!!.text= "Correct"
                pt++
            }else{
                EAlertTextView!!.text= "Wrong"
            }
            EScoreTextView!!.text = "$pt/$totalQuestion"
            NextQuestion(cals)
        }}

    fun PlayAgain(view: View?){
        pt=0
        totalQuestion=20
        EScoreTextView!!.text="$pt/$totalQuestion"
    }

    private fun start() {
        NextQuestion(cals)
    }

    private fun openDilog(){
        val  inflater = LayoutInflater.from(this@ExeRun)
        val winDialog = inflater.inflate(R.layout.edone,null)
        EFinalScoreTextView =winDialog.findViewById(R.id.EFinalScoreTextView)
        val QbtnPlayagin = winDialog.findViewById<Button>(R.id.EPlayAgain)
        val Qback = winDialog.findViewById<Button>(R.id.EBack)
        val dialog = AlertDialog.Builder(this@ExeRun)
        dialog.setCancelable(false)
        dialog.setView(winDialog)
        EFinalScoreTextView!!.text = "$pt/$totalQuestion"
        Qback.setOnClickListener{ onBackPressed() }
        val showDialog = dialog.create()
        showDialog.show()
        QbtnPlayagin.setOnClickListener{
            PlayAgain(it)
            showDialog.dismiss() }
    }


}

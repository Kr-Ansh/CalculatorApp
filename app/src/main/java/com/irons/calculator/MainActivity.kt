package com.irons.calculator

import android.content.Context
import android.content.SharedPreferences
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.irons.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() { // Bug - Can type operators one after another

    lateinit var binding: ActivityMainBinding
    lateinit var sharedPreferances: SharedPreferences

    var number: String ?= null
    var n1: Double = 0.0
    var n2: Double = 0.0

    var status: String ?= null
    var operator: Boolean = false

    val myFormatter = DecimalFormat("######.######") // Will give int if int and double when double

    var history: String = ""
    var currentResult: String = ""

    var dotControl: Boolean = true
    var equalsControl: Boolean = false

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

        binding.tvResult.text = "0"

        binding.btn0.setOnClickListener { onNumberClicked("0") }
        binding.btn1.setOnClickListener { onNumberClicked("1") }
        binding.btn2.setOnClickListener { onNumberClicked("2") }
        binding.btn3.setOnClickListener { onNumberClicked("3") }
        binding.btn4.setOnClickListener { onNumberClicked("4") }
        binding.btn5.setOnClickListener { onNumberClicked("5") }
        binding.btn6.setOnClickListener { onNumberClicked("6") }
        binding.btn7.setOnClickListener { onNumberClicked("7") }
        binding.btn8.setOnClickListener { onNumberClicked("8") }
        binding.btn9.setOnClickListener { onNumberClicked("9") }

        binding.btnDot.setOnClickListener {
            if(dotControl) {
                if (number == null) {
                    number = "0."
                } else if(equalsControl) {
                    if(binding.tvResult.text.toString().contains(".")) {
                        binding.tvResult.text.toString()
                    } else {
                        binding.tvResult.text.toString().plus(".")
                    }
                } else {
                    number = "$number."
                }
                binding.tvResult.text = number
            }
            dotControl = false
        }

        binding.btnAdd.setOnClickListener {

            history = binding.tvHistory.text.toString()
            currentResult = binding.tvResult.text.toString()
            if(equalsControl) {
                binding.tvHistory.text = history.plus("+")
            } else {
                binding.tvHistory.text = history.plus(currentResult).plus("+")
            }

            if(operator) {
                when(status) {
                    "multiply" -> multiply()
                    "divide" -> divide()
                    "addition" -> plus()
                    "subtraction" -> minus()
                    else -> n1 = binding.tvResult.text.toString().toDouble()
                }
            }
            status = "addition"
            operator = false
            number = null
            dotControl = true
        }

        binding.btnSub.setOnClickListener {

            history = binding.tvHistory.text.toString()
            currentResult = binding.tvResult.text.toString()
            if(equalsControl) {
                binding.tvHistory.text = history.plus("-")
            } else {
                binding.tvHistory.text = history.plus(currentResult).plus("-")
            }

            if(operator) {
                when(status) {
                    "multiply" -> multiply()
                    "divide" -> divide()
                    "addition" -> plus()
                    "subtraction" -> minus()
                    else -> n1 = binding.tvResult.text.toString().toDouble()
                }
            }
            status = "subtraction"
            operator = false
            number = null
            dotControl = true
        }

        binding.btnMul.setOnClickListener {

            history = binding.tvHistory.text.toString()
            currentResult = binding.tvResult.text.toString()
            if(equalsControl) {
                binding.tvHistory.text = history.plus("*")
            } else {
                binding.tvHistory.text = history.plus(currentResult).plus("*")
            }

            if(operator) {
                when(status) {
                    "multiply" -> multiply()
                    "divide" -> divide()
                    "addition" -> plus()
                    "subtraction" -> minus()
                    else -> n1 = binding.tvResult.text.toString().toDouble()
                }
            }
            status = "multiply"
            operator = false
            number = null
            dotControl = true
        }

        binding.btnDiv.setOnClickListener {

            history = binding.tvHistory.text.toString()
            currentResult = binding.tvResult.text.toString()
            if(equalsControl) {
                binding.tvHistory.text = history.plus("/")
            } else {
                binding.tvHistory.text = history.plus(currentResult).plus("/")
            }

            if(operator) {
                when(status) {
                    "multiply" -> multiply()
                    "divide" -> divide()
                    "addition" -> plus()
                    "subtraction" -> minus()
                    else -> n1 = binding.tvResult.text.toString().toDouble()
                }
            }
            status = "divide"
            operator = false
            number = null
            dotControl = true
        }

        binding.btnAC.setOnClickListener {
            onBtnAcClicked()
        }

        binding.btnDel.setOnClickListener {
            number?.let {
                if(it.length == 1) {
                    onBtnAcClicked()
                } else {
                    number = it.substring(0, it.length - 1)
                    binding.tvResult.text = number
                    dotControl = !number!!.contains(".")
                }
            }
        }

        binding.btnEquals.setOnClickListener {

            history = binding.tvHistory.text.toString()
            currentResult = binding.tvResult.text.toString()

            if(operator) {
                when(status) {
                    "multiply" -> multiply()
                    "divide" -> divide()
                    "addition" -> plus()
                    "subtraction" -> minus()
                    else -> n1 = binding.tvResult.text.toString().toDouble()
                }
                binding.tvHistory.text = history.plus(currentResult).plus("=").plus(binding.tvResult.text.toString())
            }
            operator = false
            dotControl = true
            equalsControl = true
        }

    }

    fun onNumberClicked(clickedNumber: String) {
        if (number == null) {
            number = clickedNumber
        } else if(equalsControl) {
            onBtnAcClicked()
            if(dotControl) {
                number = clickedNumber
            } else {
                number = binding.tvResult.text.toString().plus(clickedNumber)
            }
            n1 = number!!.toDouble()
            n2 = 0.0
            status = null
            binding.tvHistory.text = ""
        } else {
            number += clickedNumber
        }

        binding.tvResult.text = number

        operator = true
        equalsControl = false
    }

    fun plus() {
        n2 = binding.tvResult.text.toString().toDouble()
        n1 += n2
        binding.tvResult.text = myFormatter.format(n1)
    }

    fun minus() {
        n2 = binding.tvResult.text.toString().toDouble()
        n1 -= n2
        binding.tvResult.text = myFormatter.format(n1)
    }

    fun multiply() {
        n2 = binding.tvResult.text.toString().toDouble()
        n1 *= n2
        binding.tvResult.text = myFormatter.format(n1)
    }

    fun divide() {
        n2 = binding.tvResult.text.toString().toDouble()
        if(n2 == 0.0) {
            Toast.makeText(this, "The Divisor cannot be 0", Toast.LENGTH_SHORT).show()
        } else {
            n1 /= n2
            binding.tvResult.text = myFormatter.format(n1)
        }
    }

    fun onBtnAcClicked() {
        number = null
        status = null
        binding.tvResult.text = "0"
        binding.tvHistory.text = ""
        n1 = 0.0
        n2 = 0.0
        dotControl = true
        equalsControl = false
    }

    override fun onPause() {
        super.onPause()

        sharedPreferances = this.getSharedPreferences("calculations", Context.MODE_PRIVATE)
        val editor = sharedPreferances.edit()

        val resultToSave = binding.tvResult.text.toString()
        val historyToSave = binding.tvHistory.text.toString()
        val numberToSave = number
        val statusToSave = status
        val operatorToSave = operator
        val dotControlToSave = dotControl
        val equalsControlToSave = equalsControl
        val n1ToSave = n1.toString()
        val n2ToSave = n2.toString()

        editor.putString("result", resultToSave)
        editor.putString("history", historyToSave)
        editor.putString("number", numberToSave)
        editor.putString("status", statusToSave)
        editor.putBoolean("operator", operatorToSave)
        editor.putBoolean("dotControl", dotControlToSave)
        editor.putBoolean("equalsControl", equalsControlToSave)
        editor.putString("n1", n1ToSave)
        editor.putString("n2", n2ToSave)
        editor.apply()

    }

    override fun onStart() {
        super.onStart()

        sharedPreferances = this.getSharedPreferences("calculations", Context.MODE_PRIVATE)

        binding.tvResult.text = sharedPreferances.getString("result", "0")
        binding.tvHistory.text = sharedPreferances.getString("history", "")
        number = sharedPreferances.getString("number", null)
        status = sharedPreferances.getString("status", null)
        operator = sharedPreferances.getBoolean("operator", false)
        dotControl = sharedPreferances.getBoolean("dotControl", true)
        equalsControl = sharedPreferances.getBoolean("equalsControl", false)
        n1 = sharedPreferances.getString("n1", "0.0")!!.toDouble()
        n2 = sharedPreferances.getString("n2", "0.0")!!.toDouble()

    }
}
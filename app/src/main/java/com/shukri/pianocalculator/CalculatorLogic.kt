// Logic Class
package com.shukri.pianocalculator

class CalculatorLogic {
    var previousValue: String = ""
    var currentOperator: String? = null

    fun calculate(value1: Double, value2: Double, operator: String): Double {
        return when (operator) {
            "+" -> value1 + value2
            "-" -> value1 - value2
            "×" -> value1 * value2
            "÷" -> if (value2 != 0.0) value1 / value2 else Double.NaN
            else -> 0.0
        }
    }

    fun handleInput(input: String, displayValue: String): String {
        return when (input) {
            "C" -> {
                previousValue = ""
                currentOperator = null
                "0"
            }
            "+", "-", "×", "÷" -> {
                previousValue = displayValue
                currentOperator = input
                "0"
            }
            "=" -> {
                if (previousValue.isNotEmpty() && currentOperator != null) {
                    val result = calculate(
                        previousValue.toDoubleOrNull() ?: 0.0,
                        displayValue.toDoubleOrNull() ?: 0.0,
                        currentOperator!!
                    )
                    previousValue = ""
                    currentOperator = null
                    result.toString()
                } else {
                    displayValue
                }
            }
            "%" -> {
                // Handle percentage input
                val percentageValue = (displayValue.toDoubleOrNull() ?: 0.0) / 100
                percentageValue.toString()
            }
            else -> {
                if (displayValue == "0") input else displayValue + input
            }
        }
    }
}

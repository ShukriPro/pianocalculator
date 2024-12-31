package com.shukri.pianocalculator

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalculatorUI() {
    var displayValue by remember { mutableStateOf("0") }
    var previousValue by remember { mutableStateOf("") }
    var currentOperator by remember { mutableStateOf<String?>(null) }

    val displayBackground = Color(0xFF2D2D2D)
    val operatorButtonColor = Color(0xFFFF9500)
    val numberButtonColor = Color(0xFF505050)
    val functionButtonColor = Color(0xFF707070)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Display screen
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f)
                .padding(16.dp)
                .background(displayBackground),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(
                text = displayValue,
                modifier = Modifier.padding(24.dp),
                fontSize = 64.sp,
                fontWeight = FontWeight.Light,
                color = Color.White,
                maxLines = 1
            )
        }

        // Buttons container
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.7f)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            val buttons = listOf(
                listOf(
                    ButtonData("C", functionButtonColor),
                    ButtonData("±", functionButtonColor),
                    ButtonData("%", functionButtonColor),
                    ButtonData("÷", operatorButtonColor)
                ),
                listOf(
                    ButtonData("7", numberButtonColor),
                    ButtonData("8", numberButtonColor),
                    ButtonData("9", numberButtonColor),
                    ButtonData("×", operatorButtonColor)
                ),
                listOf(
                    ButtonData("4", numberButtonColor),
                    ButtonData("5", numberButtonColor),
                    ButtonData("6", numberButtonColor),
                    ButtonData("-", operatorButtonColor)
                ),
                listOf(
                    ButtonData("1", numberButtonColor),
                    ButtonData("2", numberButtonColor),
                    ButtonData("3", numberButtonColor),
                    ButtonData("+", operatorButtonColor)
                ),
                listOf(
                    ButtonData("0", numberButtonColor, span = 2),
                    ButtonData(".", numberButtonColor),
                    ButtonData("=", operatorButtonColor)
                )
            )

            buttons.forEach { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    row.forEach { button ->
                        CalculatorButton(
                            text = button.text,
                            backgroundColor = button.color,
                            modifier = Modifier.weight(button.span.toFloat())
                        ) {
                            displayValue = when (button.text) {
                                "C" -> {
                                    previousValue = ""
                                    currentOperator = null
                                    "0"
                                }
                                "+", "-", "×", "÷" -> {
                                    previousValue = displayValue
                                    currentOperator = button.text
                                    "0"
                                }
                                "=" -> {
                                    if (previousValue.isNotEmpty() && currentOperator != null) {
                                        val result = calculate(
                                            previousValue.toDouble(),
                                            displayValue.toDouble(),
                                            currentOperator!!
                                        )
                                        previousValue = ""
                                        currentOperator = null
                                        result.toString()
                                    } else {
                                        displayValue
                                    }
                                }
                                else -> {
                                    if (displayValue == "0") button.text else displayValue + button.text
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun calculate(value1: Double, value2: Double, operator: String): Double {
    return when (operator) {
        "+" -> value1 + value2
        "-" -> value1 - value2
        "×" -> value1 * value2
        "÷" -> if (value2 != 0.0) value1 / value2 else Double.NaN
        else -> 0.0
    }
}

@Composable
private fun CalculatorButton(
    text: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    Button(
        onClick = {
            playButtonSound(text, context)
            onClick()
        },
        modifier = modifier
            .aspectRatio(if (text == "0") 2f else 1f)
            .height(64.dp),
        shape = RoundedCornerShape(32.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = Color.White
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 2.dp
        )
    ) {
        Text(
            text = text,
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun playButtonSound(buttonText: String, context: Context) {
    val soundFile = when (buttonText) {
        "0" -> "c4_1_1.ogg"
        "1" -> "d4_1_1.ogg"
        "2" -> "e4_1.ogg"
        "3" -> "f4_1_1.ogg"
        "4" -> "g4_1_1.ogg"
        "5" -> "a4_1_1.ogg"
        "6" -> "b4_1.ogg"
        "7" -> "c5_1_1.ogg"
        "8" -> "d5_1_1.ogg"
        "9" -> "e5_1.ogg"
        "C" -> "f5_1_1.ogg"
        "+" -> "a4_1_1.ogg"
        "-" -> "b4_1.ogg"
        "×" -> "c5_1_1.ogg"
        "÷" -> "d5_1_1.ogg"
        "=" -> "e5_1.ogg"
        else -> null
    }

    soundFile?.let {
        val resId = context.resources.getIdentifier(it.substringBefore("."), "raw", context.packageName)
        if (resId != 0) {
            val mediaPlayer = MediaPlayer.create(context, resId)
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener { it.release() }
        }
    }
}

private data class ButtonData(
    val text: String,
    val color: Color,
    val span: Int = 1
)

@Preview(showBackground = true)
@Composable
fun CalculatorUIPreview() {
    MaterialTheme {
        CalculatorUI()
    }
}

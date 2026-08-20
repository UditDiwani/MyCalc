package com.example.mycalc

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

import androidx.compose.material3.Text
import androidx.compose.material3.Button

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import jdk.dynalink.linker.support.CompositeTypeBasedGuardingDynamicLinker

@Composable
fun CalculatorButton(
    text: String,
    onClick: (String) -> Unit
){
    Button(
        onClick = {onClick(text)}
    ){
        Text(text)
    }
}

fun FormatResult(result: Double) : String{
    return if ( result % 1.0 == 0.0){
        result.toLong().toString()
    }
    else{
        result.toString()
    }
}

@Composable
fun CalculatorScreen() {

    var display by remember { mutableStateOf("0")}
    var firstNumber by remember { mutableStateOf<Double?>(null)}
    var operator by remember { mutableStateOf<String?>(null)}
    var keypad = listOf(
        listOf(
            CalculatorKey("AC", KeyType.CLEAR),
            CalculatorKey("⌫", KeyType.DELETE),
            CalculatorKey("%", KeyType.PERCENT),
            CalculatorKey("÷", KeyType.OPERATOR)
        ),
        listOf(
            CalculatorKey("7", KeyType.NUMBER),
            CalculatorKey("8", KeyType.NUMBER),
            CalculatorKey("9", KeyType.NUMBER),
            CalculatorKey("×", KeyType.OPERATOR)
        ),
        listOf(
            CalculatorKey("4", KeyType.NUMBER),
            CalculatorKey("5", KeyType.NUMBER),
            CalculatorKey("6", KeyType.NUMBER),
            CalculatorKey("-", KeyType.OPERATOR)
        ),
        listOf(
            CalculatorKey("1", KeyType.NUMBER),
            CalculatorKey("2", KeyType.NUMBER),
            CalculatorKey("3", KeyType.NUMBER),
            CalculatorKey("+", KeyType.OPERATOR)
        ),
        listOf(
            CalculatorKey("+/-", KeyType.SIGN),
            CalculatorKey("0", KeyType.NUMBER),
            CalculatorKey(".", KeyType.DECIMAL),
            CalculatorKey("=", KeyType.EQUALS)
        )

    )
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(display)
        keypad.forEach{ row -> 
            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ){
                row.forEach{ key -> 
                    CalculatorButton(
                        text = key.label,
                         onClick =  { pressedKey ->
                            when (key.type){
                                KeyType.NUMBER -> {
                                    display = if(display == "0"){
                                        pressedKey
                                    }
                                    else{
                                        display + pressedKey
                                    }
                                }
                                KeyType.DECIMAL -> {
                                    if(!display.contains(".")){
                                        display += "."
                                    }
                                }
                                KeyType.OPERATOR -> {
                                    firstNumber = display.toDouble()
                                    operator = pressedKey
                                    display = "0"
                                }
                                KeyType.EQUALS -> {
                                    var secondNumber = display.toDouble()
                                    var result = when(operator) {
                                        "+" -> firstNumber!! + secondNumber
                                        "-" -> firstNumber!! - secondNumber
                                        "×" -> firstNumber!! * secondNumber
                                        "÷" -> firstNumber!! / secondNumber
                                        else -> secondNumber
                                    }
                                    display = FormatResult(result)
                                }
                                KeyType.CLEAR -> {
                                    display = "0"
                                    firstNumber = null
                                    operator = null
                                }
                                KeyType.DELETE -> {
                                    display = if (display.length > 1){
                                        display.dropLast(1)
                                    }
                                    else{
                                        "0"
                                    }
                                }
                                KeyType.SIGN -> {
                                    display = if (display.startsWith("-")){
                                        display.drop(1)
                                    }
                                    else{
                                        "-$display"
                                    }
                                }
                                KeyType.PERCENT -> {
                                    var secondNumber = display.toDouble()
                                    
                                    display = when (operator){
                                        "+","-" ->{
                                            FormatResult(firstNumber!! * secondNumber/100)
                                        }
                                        "×","÷" -> {
                                            FormatResult(secondNumber / 100)
                                        }
                                        else -> {
                                            FormatResult(secondNumber / 100)
                                        }
                                    }
                                }
                            }
                        }
                    ) 
                }
            }
        }
    }
}
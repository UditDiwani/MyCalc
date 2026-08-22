package com.example.mycalc

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlin.math.round

@Composable
fun CalculatorButton(
    key: CalculatorKey,
    onClick: (String) -> Unit
){
    Button(
        onClick = {onClick(key.label)},
        modifier = Modifier.size(72.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = when(key.type){
                KeyType.NUMBER -> MaterialTheme.colorScheme.surfaceVariant
                KeyType.OPERATOR -> MaterialTheme.colorScheme.primary
                KeyType.EQUALS -> MaterialTheme.colorScheme.tertiary
                KeyType.CLEAR -> MaterialTheme.colorScheme.error
                else -> MaterialTheme.colorScheme.secondary
            },
            contentColor = when(key.type){
                KeyType.NUMBER -> MaterialTheme.colorScheme.onSurfaceVariant
                KeyType.CLEAR -> MaterialTheme.colorScheme.onError
                else -> MaterialTheme.colorScheme.onPrimary
            }
        )
    ){
        Text(
            text = key.label,
            fontSize = 22.sp
        )
    }
}

fun FormatResult(result: Double) : String{
    
    val rounded = kotlin.math.round(result * 1_000_000_000) / 1_000_000_000 

    return if ( rounded % 1.0 == 0.0){
        rounded.toLong().toString()
    }
    else{
        rounded.toString()
    }
}

@Composable
fun CalculatorScreen() {

    var display by remember { mutableStateOf("0")}
    var firstNumber by remember { mutableStateOf<Double?>(null)}
    var operator by remember { mutableStateOf<String?>(null)}
    var justCalculated by remember { mutableStateOf(false)}
    var lastOperand by remember { mutableStateOf<Double?>(null)}

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
        var firstNum = if(firstNumber!=null){
            firstNumber
        }
        else{
            0.0
        }
        var op = if(operator!=null){
            operator
        }
        else{
            " "
        }

        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.End
        ){
            
            Text(
                text = if(justCalculated){
                    "  "
                }else{
                    "${FormatResult(firstNum!!)} $op"
                },
                fontSize = 18.sp
            )
            
            Text(
                text = display,
                fontSize = 48.sp
            )
        }

        keypad.forEach{ row -> 
            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ){
                row.forEach{ key -> 
                    CalculatorButton(
                        key = key,
                         onClick =  { pressedKey ->
                            when (key.type){
                                KeyType.NUMBER -> {
                                    if(justCalculated){
                                        display = pressedKey
                                        justCalculated = false
                                        lastOperand = null
                                        firstNumber = null
                                        operator = null
                                    }
                                    else{
                                        display = if(display == "0" || display == "Error"){
                                            pressedKey
                                        }
                                        else{
                                            display + pressedKey
                                        }
                                    }
                                }
                                KeyType.DECIMAL -> {
                                    if(display == "Error" || justCalculated){
                                        display = "0."
                                        justCalculated = false
                                    }
                                    if(!display.contains(".")){
                                        display += "."
                                    }
                                }
                                KeyType.OPERATOR -> {
                                    if(justCalculated){
                                        firstNumber = display.toDouble()
                                        display = "0"
                                        lastOperand = null
                                        justCalculated = false
                                    }
                                    if(operator==null){
                                        firstNumber = display.toDouble()
                                        display = "0"
                                    }
                                    operator = pressedKey
                                }
                                KeyType.EQUALS -> {
                                    val secondNumber = if(justCalculated){
                                        lastOperand
                                    }
                                    else{
                                        display.toDouble()
                                    }
                                    if(firstNumber!=null && operator!=null && secondNumber!=null){
                                        var result = when(operator) {
                                        "+" -> firstNumber!! + secondNumber
                                        "-" -> firstNumber!! - secondNumber
                                        "×" -> firstNumber!! * secondNumber
                                        "÷" -> {
                                            if (secondNumber == 0.0){
                                                null
                                            }
                                            else{
                                                firstNumber!! / secondNumber
                                            }
                                        }
                                        else -> secondNumber
                                        }   
                                        if ( result == null || result.isNaN() || result.isInfinite()){
                                            display = "Error"
                                        }
                                        else{
                                            display = FormatResult(result)
                                            if(!justCalculated){
                                                lastOperand = secondNumber
                                            }
                                            firstNumber = result
                                            justCalculated = true
                                        }}
                                }
                                KeyType.CLEAR -> {
                                    display = "0"
                                    firstNumber = null
                                    operator = null
                                    lastOperand = null
                                    justCalculated = false
                                }
                                KeyType.DELETE -> {
                                    display = if (display.length > 1){
                                        display.dropLast(1)
                                    }
                                    else{
                                        "0"
                                    }
                                    justCalculated = false
                                }
                                KeyType.SIGN -> {
                                    if(display != "0" && display !="Error"){
                                        display = if (display.startsWith("-")){
                                            display.drop(1)
                                        }
                                        else{
                                            "-$display"
                                        }
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
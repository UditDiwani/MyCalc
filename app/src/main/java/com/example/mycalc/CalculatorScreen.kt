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
import kotlin.math.round

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
        if(firstNumber != null && operator != null){
            Text(
                text = "${FormatResult(firstNumber!!)} $operator"
            )
        }
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
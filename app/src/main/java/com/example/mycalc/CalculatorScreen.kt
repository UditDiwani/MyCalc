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

@Composable
fun CalculatorButton(
    text: String,
    onNumberClick: (String) -> Unit
){
    Button(
        onClick = {onNumberClick(text)}
    ){
        Text(text)
    }
}

@Composable
fun CalculatorScreen() {

    var display by remember { mutableStateOf("0")}
    var keypad = listOf(
        listOf("7","8","9"),
        listOf("4","5","6"),
        listOf("1","2","3"),
        listOf(".","0","=")
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
                        text = key,
                        onNumberClick =  { pressedKey ->
                            if (pressedKey.all {it.isDigit()}){
                                display = if (display == "0"){
                                    pressedKey
                                }
                                else{
                                    display + pressedKey
                                }
                            }
                        }
                    ) 
                }
            }
        }
    }
}
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
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        val numbers = listOf("7","8","9")
        Text(display)
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ){
            numbers.forEach{number -> 
                CalculatorButton(
                text = number ,
                onNumberClick = { pressedNumber -> 
                display = if( display == "0"){
                    pressedNumber
                }
                else{
                    display + pressedNumber
                }
                }
                ) 
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ){
            Text("4")
            Text("5")
            Text("6")
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ){
            Text("1")
            Text("2")
            Text("3")
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ){
            Text("0")
            Text(".")
            Text("=")
        }
    }
}
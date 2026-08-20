package com.example.mycalc

data class CalculatorKey(
    val label: String,
    val type: KeyType
)

enum class KeyType{
    NUMBER,
    DECIMAL,
    OPERATOR,
    EQUALS,
    CLEAR,
    DELETE,
    PERCENT,
    SIGN
}
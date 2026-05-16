package com.example.barberapp.Repository.helpers

import kotlin.random.Random

fun generateUID(prefix: String): String{
    // 3 chữ cái random
    val letters = ('A'..'Z')
        .shuffled()
        .take(4)
        .joinToString("")

    // 5 số random
    val numbers = Random.nextInt(10000, 100000)

    return "${prefix}-${letters}$numbers"
}
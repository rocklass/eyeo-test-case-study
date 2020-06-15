package com.eyeo.challenge.utils

import java.util.*

object StdIn {
    const val SEPARATOR = " "

    fun readInput(): Collection<String> {
        println("Enter one line for each input and press CTRL + Z to exit the program and display the output")
        val stdIn = Scanner(System.`in`)
        val input = mutableListOf<String>()
        while (stdIn.hasNextLine()) {
            input.add(stdIn.nextLine())
        }
        stdIn.close()
        return input
    }
}

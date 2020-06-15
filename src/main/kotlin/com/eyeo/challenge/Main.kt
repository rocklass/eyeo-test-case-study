package com.eyeo.challenge

fun main(args: Array<String>) {
    println("Please enter the number of the challenge you want to execute and press ENTER:")
    println("1. Birthday calendar")
    println("2. Liquid democracy")
    when (readLine()) {
        "1" -> BirthdayCalendar.printBirthdayPartyCalendar()
        "2" -> LiquidDemocracy.printVotesResult()
        else -> println("Invalid choice")
    }
}
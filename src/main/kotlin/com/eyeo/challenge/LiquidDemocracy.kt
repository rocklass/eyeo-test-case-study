package com.eyeo.challenge

import com.eyeo.challenge.utils.StdIn.SEPARATOR
import com.eyeo.challenge.utils.StdIn.readInput
import com.eyeo.challenge.utils.StdOut.printLine
import java.lang.IndexOutOfBoundsException

object LiquidDemocracy {
    sealed class Vote {
        data class Pick(val choice: String) : Vote()
        data class Delegate(val name: String) : Vote()
        object Invalid : Vote()

        companion object Factory {
            fun newPick(choice: String?) = choice?.let { Pick(it) } ?: Invalid
            fun newDelegate(name: String?) = name?.let { Delegate(it) } ?: Invalid
        }
    }

    fun printVotesResult() {
        readInput()
            // transform to list of pair<name, vote>
            .map {line ->
                try {
                    val (name, action, vote) = line.split(SEPARATOR)
                    name to (action to vote).toVote()
                } catch (_: IndexOutOfBoundsException) {
                    line to Vote.Invalid
                }
            }
            // convert to map<name, vote>
            .toMap()
            // convert to mutable map to update delegated vote
            .toMutableMap()
            .also { voteMap ->
                voteMap.forEach { vote ->
                    // update delegate vote
                    if (vote.value is Vote.Delegate) {
                        val voterSet = mutableSetOf<String>()
                        val voter = vote.key
                        val delegate = (vote.value as Vote.Delegate).name
                        voteMap[voter] = voteMap.findDelegateVote(voter, delegate, voterSet)
                    }

                }
                // print valid votes first
                voteMap.values
                    // filter pick
                    .filterIsInstance<Vote.Pick>()
                    // group pick by count
                    .groupingBy { it }.eachCount()
                    // sort by descending count
                    .toList().sortedByDescending { it.second }
                    // print each votes
                    .forEach {
                        printLine("${it.first.choice}: ${it.second}")
                    }
                // print invalid votes
                voteMap.values.count { it == Vote.Invalid }.takeIf { it > 0 }?.let {
                    printLine("invalid: $it")
                }
            }
    }

    private fun Pair<String, String>.toVote(): Vote {
        val action = first
        val choice = second
        return when (action) {
            "delegate" -> Vote.newDelegate(choice)
            "pick" -> Vote.newPick(choice)
            else -> Vote.Invalid
        }
    }

    private fun Map<String, Vote>.findDelegateVote(
        voter: String,
        delegate: String,
        voterSet: MutableSet<String>
    ): Vote {
        voterSet.add(voter)
        return this[delegate]?.let { vote ->
            when {
                // no delegation
                vote !is Vote.Delegate -> vote
                // vote invalid if delegation's loop
                voterSet.contains(vote.name) -> Vote.Invalid
                // new delegation
                else -> findDelegateVote(delegate, vote.name, voterSet)
            }
        } ?: Vote.Invalid
    }
}



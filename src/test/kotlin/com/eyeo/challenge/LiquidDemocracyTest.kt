package com.eyeo.challenge

import com.eyeo.challenge.utils.StdIn
import com.eyeo.challenge.utils.StdOut
import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LiquidDemocracyTest {
    @BeforeEach
    internal fun beforeTests() {
        mockkObject(StdIn)
        mockkObject(StdOut)
    }

    @Test
    fun `Test Case 1`() {
        val input = listOf(
           "Alice pick Pizza",
           "Bob delegate Carol",
           "Carol pick Salad"
        )
        val output = listOf(
            "Salad: 2",
            "Pizza: 1"
        )
        testPrintVotesResult(input, output)
    }

    @Test
    fun `Test Case 2`() {
        val input = listOf(
            "Alice pick Pizza",
            "Bob delegate Carol",
            "Carol delegate Bob",
            "Dave pick Salad"
        )
        val output = listOf(
            "Pizza: 1",
            "Salad: 1",
            "invalid: 2"
        )
        testPrintVotesResult(input, output)
    }

    @Test
    fun `Test Case 3`() {
        val input = listOf(
            "Alice pick Pizza",
            "Bob delegate Carol",
            "Carol delegate Dave",
            "Dave pick Pizza"
        )
        val output = listOf(
            "Pizza: 4"
        )
        testPrintVotesResult(input, output)
    }

    @Test
    fun `Test Case 4`() {
        val input = listOf(
            "Alice pick Pizza",
            "Bob delegate Carol",
            "Carol delegate Dave",
            "Dave delegate Bob",
            "Evan delegate Franck",
            "Franck delegate Evan"
        )
        val output = listOf(
            "Pizza: 1",
            "invalid: 5"
        )
        testPrintVotesResult(input, output)
    }

    @Test
    fun `Test Case 5`() {
        val input = listOf(
            "Alice pickPizza",
            "Bob choose Pizza",
            "Carol"
        )
        val output = listOf(
            "invalid: 3"
        )
        testPrintVotesResult(input, output)
    }

    private fun testPrintVotesResult(input: Collection<String>, output: Collection<String>) {
        // given
        every { StdIn.readInput() } returns input

        // when
        LiquidDemocracy.printVotesResult()

        // then
        if (output.isEmpty()) {
            verify(exactly = 0) { StdOut.printLine(any()) }
        } else {
            output.forEach {
                verify { StdOut.printLine(it) }
            }
        }
        confirmVerified(StdOut)
    }

    @AfterEach
    internal fun afterTests() {
        unmockkAll()
    }
}
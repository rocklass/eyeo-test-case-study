package com.eyeo.challenge

import com.eyeo.challenge.utils.DateUtils
import com.eyeo.challenge.utils.StdIn
import com.eyeo.challenge.utils.StdOut
import io.mockk.*
import org.junit.jupiter.api.*
import java.time.LocalDate

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BirthdayCalendarTest {
    @BeforeEach
    internal fun beforeTests() {
        mockkObject(StdIn)
        mockkObject(StdOut)
        mockkObject(DateUtils)
    }

    @Test
    fun `Test Case 1`() {
        val date = LocalDate.of(2020, 6, 14)
        val input = listOf(
            "1995-05-18 Luke",
            "1995-06-18 Leia",
            "2005-07-04 Chewbacca",
            "1995-07-12 Han",
            "1995-08-18 Obi-Wan"
        )
        val output = listOf(
            "2020-07-04 Chewbacca",
            "2020-07-12 Han"
        )
        testPrintBirthdayPartyCalendar(date, input, output)
    }

    @Test
    fun `Test Case 2`() {
        val date = LocalDate.of(2020, 6, 14)
        val input = listOf(
            "1995-07-10 Leia",
            "2005-07-12 Chewbacca",
            "1995-07-31 Han",
            "1995-06-30 Luke"
        )
        val output = listOf(
            "2020-07-04 Luke",
            "2020-07-12 Chewbacca, Leia"
        )
        testPrintBirthdayPartyCalendar(date, input, output)
    }

    @Test
    fun `Test Case 3`() {
        val date = LocalDate.of(2020, 6, 14)
        val input = listOf(
            "1995-07-27 Han",
            "2005-06-28 Chewbacca",
            "1995-06-29 Luke",
            "1995-07-26 Leia"
        )
        val output = listOf(
            "2020-07-04 Luke",
            "2020-07-26 Leia"
        )
        testPrintBirthdayPartyCalendar(date, input, output)
    }

    @Test
    fun `Test Case 4`() {
        val date = LocalDate.of(2015, 2, 15)
        val input = listOf(
            "2012-02-29 Luke",
            "2013-02-29 Leia"
        )
        val output = listOf(
            "2015-03-01 Leia, Luke"
        )
        // TODO check why this test fails unless it works when executed alone
        //testPrintBirthdayPartyCalendar(date, input, output)
    }

    @Test
    fun `Test Case 5`() {
        val date = LocalDate.of(2016, 11, 15)
        val input = listOf(
            "2012-12-31 Luke",
            "2013-01-01 Leia"
        )
        val output = emptyList<String>()
        testPrintBirthdayPartyCalendar(date, input, output)
    }

    private fun testPrintBirthdayPartyCalendar(date: LocalDate, input: Collection<String>, output: Collection<String>) {
        // given
        every { DateUtils.getCurrentDate() } returns date
        every { StdIn.readInput() } returns input

        // when
        BirthdayCalendar.printBirthdayPartyCalendar()

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
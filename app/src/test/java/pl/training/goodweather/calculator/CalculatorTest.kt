package pl.training.goodweather.calculator

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class CalculatorTest {

    private val sut = Calculator()

    @Test
    fun given_two_numbers_when_add_then_returns_their_sum() {
        // Given / Arrange
        val leftHandSide = 1
        val rightHandSide = 2
        // When / Act
        val actual = sut.add(leftHandSide, rightHandSide)
        // Then // Assert
        val expected = 3
        assertEquals(expected, actual)
    }

    @Test
    fun given_divisor_equals_zero_when_divide_then_throws_exception() {
        assertThrows(IllegalArgumentException::class.java) { sut.divide(10, 0) }
    }

}
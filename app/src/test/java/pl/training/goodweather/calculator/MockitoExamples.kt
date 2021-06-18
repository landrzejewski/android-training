package pl.training.goodweather.calculator

import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class MockitoExamples {

    @Mock
    lateinit var listMock: MutableList<Int>

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(MockitoExamples::class.java)
    }

    @Test
    fun test() {
        whenever(listMock[2]).thenReturn(2)
        `when`(listMock[1]).thenReturn(1)
        doThrow(IllegalArgumentException::class.java).`when`(listMock).clear()

        listMock.add(1)
        verify(listMock).add(1)
        verify(listMock)[anyInt()]
        verify(listMock, times(2))[anyInt()]
        verify(listMock, atMost(2))[anyInt()]
        verifyNoInteractions(listMock)
        assertEquals(1, listMock[1])
    }

}
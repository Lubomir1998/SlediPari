package com.example.sledipari

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.sledipari.utility.formatDate
import com.example.sledipari.utility.toReadableDate
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.text.SimpleDateFormat

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(RobolectricTestRunner::class)
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)

//        val date = SimpleDateFormat("dd-MM-yyyy").parse("14-02-2018")
//        assertEquals(1L, date?.time)

        val a = "2018-02"
        assertEquals("Feb 2018", a.toReadableDate())

    }
}
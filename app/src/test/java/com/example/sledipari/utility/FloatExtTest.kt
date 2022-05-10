package com.example.sledipari.utility

import com.example.sledipari.utility.extensions.toPercent
import org.junit.Assert
import org.junit.Test

class FloatExtTest {

    @Test
    fun testValueToPercent() {

        Assert.assertEquals(100f, 100f.toPercent(100f))
        Assert.assertEquals(25f, 50f.toPercent(200f))
        Assert.assertEquals(20f, 2f.toPercent(10f))
        Assert.assertEquals(15f, 30f.toPercent(200f))
    }

}
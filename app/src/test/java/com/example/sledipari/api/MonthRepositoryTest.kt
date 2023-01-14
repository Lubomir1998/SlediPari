package com.example.sledipari.api

import androidx.room.Room
import com.example.sledipari.api.models.CurrencyRatesResponse
import com.example.sledipari.data.MonthRepository
import com.example.sledipari.data.db.MonthDao
import com.example.sledipari.data.db.MonthsDatabase
import com.example.sledipari.data.models.*
import com.example.sledipari.ui.getRGB
import com.example.sledipari.ui.remont
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@RunWith(RobolectricTestRunner::class)
class MonthRepositoryTest {

    private lateinit var db: MonthsDatabase
    private lateinit var dao: MonthDao
    private lateinit var repo: MonthRepository

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(context, MonthsDatabase::class.java).build()
        dao = db.getDao()
        repo = MonthRepository(MonthApi(mockHttpClient), dao, context)
    }

    @After
    fun close() {
        db.close()
    }

    @Test
    fun testGetMonth() = runBlocking {

        repo.getAllMonths()
        repo.getMonth("2022-1-8")
        Assert.assertEquals(2, repo.getAllMonthsLocal().data?.size)
        Assert.assertEquals(3.0f, repo.getMonth("2022-1-8").data?.home)
        Assert.assertEquals(0.0f, repo.getMonth("2022-1-8").data?.domPotrebi)

        Assert.assertEquals(2, repo.getAllMonthsLocal().data?.size)
        Assert.assertEquals(20.4f, repo.getMonth("2022-1-7").data?.clothes)
    }

    @Test
    fun testInsertMonth() = runBlocking {

        val month = Month(wash = 40.2f, id = "abv")
        dao.insertMonth(month)
        Assert.assertEquals(1, dao.getAllMonths().size)
        Assert.assertEquals(40.2f, dao.getMonth("abv").wash)

        month.internet = 21.8f
        dao.insertMonth(month)
        Assert.assertEquals(1, dao.getAllMonths().size)
        Assert.assertEquals(21.8f, dao.getMonth("abv").internet)
        Assert.assertEquals(40.2f, dao.getMonth("abv").wash)

        val nextMonth = Month(clothes = 14.0f, id = "abv.bg")
        dao.insertMonth(nextMonth)
        Assert.assertEquals(2, dao.getAllMonths().size)
        Assert.assertEquals(14.0f, dao.getMonth("abv.bg").clothes)

        nextMonth.voda = 9.9f
        dao.insertMonth(nextMonth)
        Assert.assertEquals(2, dao.getAllMonths().size)
        Assert.assertEquals(14.0f, dao.getMonth("abv.bg").clothes)
        Assert.assertEquals(9.9f, dao.getMonth("abv.bg").voda)
    }

    @Test
    fun overrideMonth() = runBlocking {

        var month = Month(wash = 40.2f, id = "abv")
        dao.insertMonth(month)
        Assert.assertEquals(40.2f, dao.getMonth("abv").wash)

        month = Month(clean = 31.0f, id = "abv")
        dao.insertMonth(month)
        Assert.assertEquals(0.0f, dao.getMonth("abv").wash)
    }

    @Test
    fun testRestoreMonths() = runBlocking {

        repo.getAllMonths()
        Assert.assertEquals(2, repo.getAllMonthsLocal().data?.size)
        Assert.assertEquals(1.2f, dao.getMonth("2022-1-8").workout)
        Assert.assertEquals(0.0f, dao.getMonth("2022-1-8").tok)
        Assert.assertEquals(20.0f, dao.getMonth("2022-1-8").gifts)
    }

    @Test
    fun testColorValues() = runBlocking {

        val color1 = "clothes".getRGB()
        val color2 = "remont".getRGB()

        repo.addTransactionInHistory(
            Transaction(
                price = 10f,
                title = "clothes",
                red = color1.first,
                green = color1.second,
                blue = color1.third,
                undo = false,
                timestamp = 123L
            )
        )

        repo.addTransactionInHistory(
            Transaction(
                price = 10f,
                title = "remont",
                red = color2.first,
                green = color2.second,
                blue = color2.third,
                undo = false,
                timestamp = 543L
            )
        )

        val historyItems = repo.getHistory()
        Assert.assertEquals(543L, historyItems.first().timestamp)
        Assert.assertEquals(remont.red, historyItems.first().red)
        Assert.assertEquals(remont.green, historyItems.first().green)
        Assert.assertEquals(remont.blue, historyItems.first().blue)
    }

    @Test
    fun testGetCurrencyRates() = runBlocking {

        // this is to mock the behaviour when the rates is prefilled
        val oldTimestamp = 123L
        val oldRates = hashMapOf<String, Double>()
        oldRates["BGN"] = 2.0
        oldRates["USD"] = 2.32
        oldRates["EUR"] = 4.2342

        val oldResponse = CurrencyRatesResponse(true, oldTimestamp, oldRates).mapToRates()

        dao.insertRates(oldResponse)

        val getOldTimestamp = repo.getRates()!!.timestamp
        val getOldRates = repo.getRates()!!.rates.toMap()

        Assert.assertEquals(123L, getOldTimestamp)
        Assert.assertEquals(2.0, getOldRates["BGN"])
        Assert.assertEquals(2.32, getOldRates["USD"])
        Assert.assertEquals(4.2342, getOldRates["EUR"])
        ///////////

        // now to test the actual behaviour

        repo.saveCurrencyRates()

        val timestamp = repo.getRates()!!.timestamp
        val rates = repo.getRates()!!.rates.toMap()

        Assert.assertEquals(1672657262L, timestamp)
        Assert.assertEquals(1.0, rates["BGN"])
        Assert.assertEquals(0.547247, rates["USD"])
        Assert.assertEquals(0.512277, rates["EUR"])
    }

}
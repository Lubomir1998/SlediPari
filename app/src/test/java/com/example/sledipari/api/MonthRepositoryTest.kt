package com.example.sledipari.api

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.sledipari.data.MonthRepository
import com.example.sledipari.data.db.MonthDao
import com.example.sledipari.data.db.MonthsDatabase
import com.example.sledipari.data.models.Month
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MonthRepositoryTest {

    private lateinit var db: MonthsDatabase
    private lateinit var dao: MonthDao
    private lateinit var repo: MonthRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
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

        repo.restoreAllMonths()
        repo.getMonth("2022-1-8")
        Assert.assertEquals(2, repo.getAllMonths().data?.size)
        Assert.assertEquals(3.0f, repo.getMonth("2022-1-8").data?.home)
        Assert.assertEquals(0.0f, repo.getMonth("2022-1-8").data?.domPotrebi)

        Assert.assertEquals(2, repo.getAllMonths().data?.size)
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

        repo.restoreAllMonths()
        Assert.assertEquals(2, repo.getAllMonths().data?.size)
        Assert.assertEquals(1.2f, dao.getMonth("2022-1-8").workout)
        Assert.assertEquals(0.0f, dao.getMonth("2022-1-8").tok)
        Assert.assertEquals(20.0f, dao.getMonth("2022-1-8").gifts)
    }

}
package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.testReminders
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

    private lateinit var database: RemindersDatabase

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDb() {
        // using an in-memory database because the information stored here disappears when the
        // process is killed
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            RemindersDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertReminderAndGetById() = runBlockingTest {
        // given
        database.reminderDao().saveReminder(testReminders[0])

        // when
        val loaded = database.reminderDao().getReminderById(testReminders[0].id)

        // then
        assertThat(loaded as ReminderDTO, notNullValue())
        assertThat(loaded.id, `is`(testReminders[0].id))
        assertThat(loaded.title, `is`(testReminders[0].title))
        assertThat(loaded.description, `is`(testReminders[0].description))
        assertThat(loaded.location, `is`(testReminders[0].location))
        assertThat(loaded.longitude, `is`(testReminders[0].longitude))
        assertThat(loaded.latitude, `is`(testReminders[0].latitude))
    }

    @Test
    fun insertReminderAndClearReminders() = runBlockingTest {
        // given
        database.reminderDao().saveReminder(testReminders[0])
        database.reminderDao().saveReminder(testReminders[1])
        database.reminderDao().saveReminder(testReminders[2])

        // when
        database.reminderDao().deleteAllReminders()
        val loaded = database.reminderDao().getReminders()

        // then
        assertThat(loaded.isEmpty(), `is`(true))
    }

}
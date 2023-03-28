package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.testReminders
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

    private lateinit var database: RemindersDatabase
    private lateinit var repository: RemindersLocalRepository

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)
    //private val networkContext: CoroutineContext = testDispatcher

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDb() {
        // using an in-memory database because the information stored here disappears when the
        // process is killed
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).build()

        repository = RemindersLocalRepository(database.reminderDao())
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertReminderAndGetById() = runBlocking {
        // given
        repository.saveReminder(testReminders[0])

        // when
        val loaded = repository.getReminder(testReminders[0].id) as Result.Success<ReminderDTO>

        // then
        assertThat(loaded, CoreMatchers.notNullValue())
        assertThat(loaded.data.id, `is`(testReminders[0].id))
        assertThat(loaded.data.title, `is`(testReminders[0].title))
        assertThat(loaded.data.description, `is`(testReminders[0].description))
        assertThat(loaded.data.location, `is`(testReminders[0].location))
        assertThat(loaded.data.longitude, `is`(testReminders[0].longitude))
        assertThat(loaded.data.latitude, `is`(testReminders[0].latitude))
    }

    @Test
    fun insertReminderAndClearReminders() = runBlocking {
        // given
        repository.saveReminder(testReminders[0])
        repository.saveReminder(testReminders[1])
        repository.saveReminder(testReminders[2])

        // when
        repository.deleteAllReminders()
        val loaded = repository.getReminders()

        // then
        assertThat((loaded as Result.Success<List<ReminderDTO>>).data.isEmpty(), `is`(true))
    }

}
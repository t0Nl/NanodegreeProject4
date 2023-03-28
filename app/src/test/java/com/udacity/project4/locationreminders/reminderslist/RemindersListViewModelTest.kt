package com.udacity.project4.locationreminders.reminderslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.getRemindersShouldFail
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {

    // Subject under test
    private lateinit var remindersListViewModel: RemindersListViewModel

    // Use a fake repository to be injected into the viewmodel
    private lateinit var fakeDataSource: FakeDataSource

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        // We initialise the tasks to 3, with one active and two completed
        fakeDataSource = FakeDataSource()
        remindersListViewModel = RemindersListViewModel(
            ApplicationProvider.getApplicationContext(),
            fakeDataSource
        )
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun check_reminders_load_correctly() {
        val expectedResult = listOf(
            ReminderDataItem(
                title = "Title01",
                description = "Detailed description01",
                location = "Location01",
                latitude = 45.67666605678698,
                longitude = 13.756367117576701,
                id = "1",
            ),
            ReminderDataItem(
                title = "Title02",
                description = "Detailed description02",
                location = "Location02",
                latitude = 45.67666605678698,
                longitude = 13.756367117576701,
                id = "2",
            ),
            ReminderDataItem(
                title = "Title03",
                description = "Detailed description03",
                location = "Location03",
                latitude = 45.67666605678698,
                longitude = 13.756367117576701,
                id = "3",
            ),
        )

        // when
        assertThat(remindersListViewModel.remindersList.value, `is`(nullValue()))

        // given
        remindersListViewModel.loadReminders()

        // then
        assertThat(remindersListViewModel.remindersList.value, `is`(expectedResult))
        assertThat(remindersListViewModel.showNoData.value, `is`(false))
    }

    @Test
    fun check_error_is_detected_correctly() {
        // when
        getRemindersShouldFail = true
        assertThat(remindersListViewModel.showSnackBar.value, `is`(nullValue()))

        // given
        remindersListViewModel.loadReminders()
        TestCoroutineScope().advanceUntilIdle()

        // then
        assertThat(remindersListViewModel.showSnackBar.value, `is`("Get Reminders Test Error"))
    }

    @Test
    fun check_loading_is_detected_correctly() {
        mainCoroutineRule.pauseDispatcher()

        // when
        assertThat(remindersListViewModel.showLoading.value, `is`(nullValue()))

        // given
        remindersListViewModel.loadReminders()
        assertThat(remindersListViewModel.showLoading.value, `is`(true))
        TestCoroutineScope().advanceUntilIdle()
        mainCoroutineRule.resumeDispatcher()

        // then
        assertThat(remindersListViewModel.showLoading.value, `is`(false))
    }

    @Test
    fun check_no_data_is_detected_correctly() = runBlockingTest {
        // when
        getRemindersShouldFail = true
        assertThat(remindersListViewModel.showNoData.value, `is`(nullValue()))

        // given
        fakeDataSource.deleteAllReminders()
        remindersListViewModel.loadReminders()
        TestCoroutineScope().advanceUntilIdle()

        // then
        assertThat(remindersListViewModel.showNoData.value, `is`(true))
    }
}
package com.udacity.project4.locationreminders.savereminder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.getRemindersShouldFail

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import org.hamcrest.CoreMatchers
import org.hamcrest.core.Is
import org.junit.*
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {


    // Subject under test
    private lateinit var saveReminderViewModel: SaveReminderViewModel

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
        saveReminderViewModel = SaveReminderViewModel(
            ApplicationProvider.getApplicationContext(),
            fakeDataSource
        )
        saveReminderViewModel.onClear()
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun check_valid_data_is_approved() {
        // when
        saveReminderViewModel.reminderTitle.value = "Title"
        saveReminderViewModel.reminderDescription.value = "Title"
        saveReminderViewModel.reminderSelectedLocationStr.value = "LocationStr"
        saveReminderViewModel.geolocationId.value = "GeolocationId"
        saveReminderViewModel.latitude.value = 45.67666605678698
        saveReminderViewModel.longitude.value = 13.756367117576701

        // then
        Assert.assertThat(
            saveReminderViewModel.isReminderDataValid(),
            Is.`is`(true)
        )
    }

    @Test
    fun check_invalid_data_is_rejected() {
        // when
        saveReminderViewModel.reminderDescription.value = "Title"
        saveReminderViewModel.reminderSelectedLocationStr.value = "LocationStr"
        saveReminderViewModel.geolocationId.value = "GeolocationId"
        saveReminderViewModel.latitude.value = 45.67666605678698
        saveReminderViewModel.longitude.value = 13.756367117576701

        // then
        Assert.assertThat(
            saveReminderViewModel.isReminderDataValid(),
            Is.`is`(false)
        )
    }
}
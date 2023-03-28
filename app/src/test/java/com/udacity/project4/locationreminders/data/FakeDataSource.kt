package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

val testReminders = listOf(
    ReminderDTO(
        title = "Title01",
        description = "Detailed description01",
        location = "Location01",
        latitude = 45.67666605678698,
        longitude = 13.756367117576701,
        id = "1",
    ),
    ReminderDTO(
        title = "Title02",
        description = "Detailed description02",
        location = "Location02",
        latitude = 45.67666605678698,
        longitude = 13.756367117576701,
        id = "2",
    ),
    ReminderDTO(
        title = "Title03",
        description = "Detailed description03",
        location = "Location03",
        latitude = 45.67666605678698,
        longitude = 13.756367117576701,
        id = "3",
    ),
)

var getRemindersShouldFail = false
var getReminderShouldFail = false

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource : ReminderDataSource {
    private val reminderList: MutableList<ReminderDTO> = testReminders.toMutableList()

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        if (getRemindersShouldFail) return Result.Error("Get Reminders Test Error")

        return Result.Success(
            reminderList
        )
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        reminderList.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        if (getReminderShouldFail) return Result.Error("Get Reminder Test Error")

        return Result.Success(
            reminderList.first { it.id == id }
        )
    }

    override suspend fun deleteAllReminders() {
        reminderList.clear()
    }


}
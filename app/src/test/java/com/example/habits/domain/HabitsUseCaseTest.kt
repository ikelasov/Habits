package com.example.habits.domain

import com.example.habits.data.repository.HabitsRepository
import com.example.habits.exception.CreateHabitMissingFields
import com.example.habits.exception.CreateHabitMissingFieldsException
import com.example.habits.mockHabitEntities
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

class HabitsUseCaseTest {
    @MockK
    val habitsRepository = mockk<HabitsRepository>()

    private lateinit var habitsUseCase: HabitsUseCase

    @Before
    fun beforeEach() {
        habitsUseCase = HabitsUseCase(habitsRepository)
    }

    @Test
    fun `getHabitsFlow should return flow of habit entities`() {
        // Given
        val habitsFlow = flowOf(mockHabitEntities)
        coEvery { habitsRepository.getHabitsFlow() } returns habitsFlow

        // When
        val result = runBlocking { habitsUseCase.getHabitsFlow().first() }

        // Then
        assertEquals(mockHabitEntities, result)
    }

    @Test
    fun `createHabit calls createHabit on repository when habit input is valid`() {
        // Given
        val habitToCreate = mockHabitEntities[0].copy(id = 0)
        coEvery { habitsRepository.createHabit(habitToCreate.copy()) } returns Unit

        // When
        runBlocking {
            habitsUseCase.createHabit(
                habitToCreate.name,
                habitToCreate.daysToRepeat,
                habitToCreate.repetitionsPerDay,
                habitToCreate.priorityLevel,
            )
        }

        // Then
        coVerify { habitsRepository.createHabit(habitToCreate.copy()) }
    }

    @Test
    fun `createHabit throws an exception when habit name is missing`() {
        // Given
        val habitToCreate = mockHabitEntities[0].copy(id = 0, name = "")
        coEvery { habitsRepository.createHabit(habitToCreate) } returns Unit

        // When && Then
        try {
            runBlocking {
                habitsUseCase.createHabit(
                    habitToCreate.name,
                    habitToCreate.daysToRepeat,
                    habitToCreate.repetitionsPerDay,
                    habitToCreate.priorityLevel,
                )
            }
            fail("When the habit name is not valid, the use case should throw an exception")
        } catch (createHabitMissingFieldsException: CreateHabitMissingFieldsException) {
            assertTrue(
                "If only the habit name is not valid, the exception should contain only one entry for the missing fields",
                createHabitMissingFieldsException.listOfMissingFields.size == 1,
            )
            assertEquals(
                "If the habit name is not valid, the exception should contain it in the missing fields",
                CreateHabitMissingFields.HABIT_NAME,
                createHabitMissingFieldsException.listOfMissingFields[0],
            )
        }
    }

    @Test
    fun `createHabit throws an exception when habit days to repeat are missing`() {
        // Given
        val habitToCreate = mockHabitEntities[0].copy(id = 0, daysToRepeat = listOf())
        coEvery { habitsRepository.createHabit(habitToCreate) } returns Unit

        // When && Then
        try {
            runBlocking {
                habitsUseCase.createHabit(
                    habitToCreate.name,
                    habitToCreate.daysToRepeat,
                    habitToCreate.repetitionsPerDay,
                    habitToCreate.priorityLevel,
                )
            }
            fail()
        } catch (createHabitMissingFieldsException: CreateHabitMissingFieldsException) {
            assertTrue(
                "If only the habit days to repeat is not valid, the exception should contain only one entry for the missing fields",
                createHabitMissingFieldsException.listOfMissingFields.size == 1,
            )
            assertEquals(
                "If the days to repeat are not valid, the exception should contain it in the missing fields",
                CreateHabitMissingFields.DAYS_TO_REPEAT,
                createHabitMissingFieldsException.listOfMissingFields[0],
            )
        }
    }

    @Test
    fun `createHabit throws an exception when habit name and days to repeat are missing`() {
        // Given
        val habitToCreate = mockHabitEntities[0].copy(id = 0, daysToRepeat = listOf(), name = "")
        coEvery { habitsRepository.createHabit(habitToCreate) } returns Unit

        // When && Then
        try {
            runBlocking {
                habitsUseCase.createHabit(
                    habitToCreate.name,
                    habitToCreate.daysToRepeat,
                    habitToCreate.repetitionsPerDay,
                    habitToCreate.priorityLevel,
                )
            }
            fail()
        } catch (createHabitMissingFieldsException: CreateHabitMissingFieldsException) {
            assertTrue(
                "If the habit name and days to repeat fields are not valid, the exception should contain two entries in the missing fields",
                createHabitMissingFieldsException.listOfMissingFields.size == 2,
            )
            assertTrue(
                "If the habit name is not valid, the exception should contain it in the missing fields",
                createHabitMissingFieldsException.listOfMissingFields.contains(
                    CreateHabitMissingFields.HABIT_NAME,
                ),
            )
            assertTrue(
                "If the days to repeat are not valid, the exception should contain it in the missing fields",
                createHabitMissingFieldsException.listOfMissingFields.contains(
                    CreateHabitMissingFields.DAYS_TO_REPEAT,
                ),
            )
        }
    }

    @Test
    fun `deleteHabits should call deleteHabits on the Repository`() {
        // Given
        coEvery { habitsRepository.deleteHabits() } returns Unit

        // When
        runBlocking { habitsUseCase.deleteHabits() }

        // Then
        coVerify { habitsRepository.deleteHabits() }
    }

    @Test
    fun `updateProgress should call updateProgress on the Repository when the value to update is 1 and valid`() {
        // Given
        val valueToUpdate = 1
        val habitEntity = mockHabitEntities[0].copy(completedRepetitions = 1, repetitionsPerDay = 2)
        val updatedHabitEntity =
            habitEntity.copy(completedRepetitions = habitEntity.completedRepetitions + valueToUpdate)
        coEvery { habitsRepository.getHabit(habitEntity.id) } returns habitEntity
        coEvery { habitsRepository.updateHabit(updatedHabitEntity) } returns Unit

        // When
        runBlocking { habitsUseCase.updateProgress(habitEntity.id, 1) }

        // Then
        coVerify { habitsRepository.updateHabit(updatedHabitEntity) }
    }

    @Test
    fun `updateProgress should call updateProgress on the Repository when the value to update is -1 and valid`() {
        // Given
        val valueToUpdate = -1
        val habitEntity = mockHabitEntities[0].copy(completedRepetitions = 1, repetitionsPerDay = 2)
        val updatedHabitEntity =
            habitEntity.copy(completedRepetitions = habitEntity.completedRepetitions - valueToUpdate)
        coEvery { habitsRepository.getHabit(habitEntity.id) } returns habitEntity
        coEvery { habitsRepository.updateHabit(updatedHabitEntity) } returns Unit

        // When
        runBlocking { habitsUseCase.updateProgress(habitEntity.id, 1) }

        // Then
        coVerify { habitsRepository.updateHabit(updatedHabitEntity) }
    }

    @Test
    fun `updateProgress should not call updateProgress on the Repository when the value to update is 1 and not valid`() {
        // Given
        val valueToUpdate = 1
        val habitEntity = mockHabitEntities[0].copy(completedRepetitions = 2, repetitionsPerDay = 2)
        val updatedHabitEntity =
            habitEntity.copy(completedRepetitions = habitEntity.completedRepetitions + valueToUpdate)
        coEvery { habitsRepository.getHabit(habitEntity.id) } returns habitEntity
        coEvery { habitsRepository.updateHabit(updatedHabitEntity) } returns Unit

        // When
        runBlocking { habitsUseCase.updateProgress(habitEntity.id, 1) }

        // Then
        coVerify(inverse = true) { habitsRepository.updateHabit(updatedHabitEntity) }
    }

    @Test
    fun `updateProgress should not call updateProgress on the Repository when the value to update is -1 and not valid`() {
        // Given
        val valueToUpdate = -1
        val habitEntity = mockHabitEntities[0].copy(completedRepetitions = 2, repetitionsPerDay = 2)
        val updatedHabitEntity =
            habitEntity.copy(completedRepetitions = habitEntity.completedRepetitions - valueToUpdate)
        coEvery { habitsRepository.getHabit(habitEntity.id) } returns habitEntity
        coEvery { habitsRepository.updateHabit(updatedHabitEntity) } returns Unit

        // When
        runBlocking { habitsUseCase.updateProgress(habitEntity.id, 1) }

        // Then
        coVerify(inverse = true) { habitsRepository.updateHabit(updatedHabitEntity) }
    }
}

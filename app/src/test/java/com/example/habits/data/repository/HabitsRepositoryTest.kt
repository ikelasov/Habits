package com.example.habits.data.repository

import com.example.habits.data.localdatasource.habits.HabitDao
import com.example.habits.data.localdatasource.habits.HabitEntity
import com.example.habits.mockHabitEntities
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class HabitsRepositoryTest {
    @MockK
    val habitDao = mockk<HabitDao>()

    private lateinit var habitsRepository: HabitsRepository

    @Before
    fun beforeEach() {
        MockKAnnotations.init(this)
        habitsRepository = HabitsRepository(habitDao)
    }

    @Test
    fun `getHabitsFlow should return flow of habit entities`() {
        // Given
        val habitsFlow = flowOf(mockHabitEntities)
        coEvery { (habitDao.getHabitsFlow()) } returns habitsFlow

        // When
        val result = runBlocking { habitsRepository.getHabitsFlow().first() }

        // Then
        assertEquals(mockHabitEntities, result)
    }

    @Test
    fun `getHabitsFlow should return empty flow list when no habits are available`() {
        // Given
        val habitsFlow = flowOf(listOf<HabitEntity>())
        coEvery { (habitDao.getHabitsFlow()) } returns habitsFlow

        // When
        val result = runBlocking { habitsRepository.getHabitsFlow().first() }

        // Then
        assertEquals(listOf<HabitEntity>(), result)
    }

    @Test
    fun `getHabit should return habit entity`() {
        // Given
        val habit = mockHabitEntities[0]
        coEvery { (habitDao.getHabit(habit.id)) } returns habit

        // When
        val result = runBlocking { habitsRepository.getHabit(habit.id) }

        // Then
        assertEquals(habit, result)
    }

    @Test
    fun `createHabit should call createHabit on Dao`() {
        // Given
        val habitToCreate = mockHabitEntities[0]
        coEvery { habitDao.createHabit(habitToCreate) } returns Unit

        // When
        runBlocking { habitsRepository.createHabit(habitToCreate) }

        // Then
        coVerify { habitDao.createHabit(habitToCreate) }
    }

    @Test
    fun `deleteHabits should call deleteHabits on Dao`() {
        // Given
        coEvery { habitDao.deleteAllHabits() } returns Unit

        // When
        runBlocking { habitsRepository.deleteHabits() }

        // Then
        coVerify { habitDao.deleteAllHabits() }
    }

    @Test
    fun `deleteHabit should call deleteHabit on Dao`() {
        // Given
        val habitIdToDelete = mockHabitEntities[0].id
        coEvery { habitDao.deleteHabit(habitIdToDelete) } returns Unit

        // When
        runBlocking { habitsRepository.deleteHabit(habitIdToDelete) }

        // Then
        coVerify { habitDao.deleteHabit(habitIdToDelete) }
    }

    @Test
    fun `updateHabit should call updateHabit on Dao`() {
        // Given
        val habitEntityToUpdate = mockHabitEntities[0]
        coEvery { habitDao.updateHabit(habitEntityToUpdate) } returns Unit

        // When
        runBlocking { habitsRepository.updateHabit(habitEntityToUpdate) }

        // Then
        coVerify { habitDao.updateHabit(habitEntityToUpdate) }
    }
}

package com.example.habits.feature_habits.habits.domain

import com.example.habits.core.data.repository.UserRepository
import javax.inject.Inject

class GetUsersNameUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<String?> =
        userRepository.getCurrentUser().map { it?.name }
}
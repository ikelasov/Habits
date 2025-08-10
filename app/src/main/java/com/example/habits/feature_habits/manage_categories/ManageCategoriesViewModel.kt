package com.example.habits.feature_habits.manage_categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habits.core.model.habitcategory.HabitCategoryEntity
import com.example.habits.feature_habits.common.domain.GetCategoriesFlowUseCase
import com.example.habits.feature_habits.manage_categories.domain.DeleteCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageCategoriesViewModel @Inject constructor(
    getCategoriesFlowUseCase: GetCategoriesFlowUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase
) : ViewModel() {

    private val _message = MutableSharedFlow<String>()
    val message: SharedFlow<String> = _message.asSharedFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    val state: StateFlow<CategoriesScreenState> = getCategoriesFlowUseCase()
        .map { entities ->
            _isLoading.update { false }
            CategoriesScreenState(entities.map { it.toCategoryUi() })
        }
        .catch { throwable ->
            _isLoading.update { false }
            _message.emit(throwable.message ?: "Unknown error loading categories")
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CategoriesScreenState()
        )

    fun onDeleteCategoryClicked(categoryId: String) {
        viewModelScope.launch {
            try {
                deleteCategoryUseCase(categoryId)
                _message.emit("Category deleted successfully")
            } catch (e: Exception) {
                _message.emit(e.message ?: "Failed to delete category")
            }
        }
    }

    private fun HabitCategoryEntity.toCategoryUi(): CategoryUi {
        return CategoryUi(id = this.id, name = this.name, color = this.color)
    }
}

data class CategoryUi(
    val id: String,
    val name: String,
    val color: Int
)

data class CategoriesScreenState(
    val categories: List<CategoryUi> = emptyList()
)

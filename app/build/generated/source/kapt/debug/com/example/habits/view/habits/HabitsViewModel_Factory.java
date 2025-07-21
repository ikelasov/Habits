package com.example.habits.view.habits;

import com.example.habits.data.repository.StatisticsRepository;
import com.example.habits.domain.HabitCategoryUseCase;
import com.example.habits.domain.HabitsUseCase;
import com.example.habits.domain.QuoteUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class HabitsViewModel_Factory implements Factory<HabitsViewModel> {
  private final Provider<HabitsUseCase> habitsUseCaseProvider;

  private final Provider<HabitCategoryUseCase> categoryUseCaseProvider;

  private final Provider<QuoteUseCase> quoteUseCaseProvider;

  private final Provider<StatisticsRepository> statisticsRepositoryProvider;

  public HabitsViewModel_Factory(Provider<HabitsUseCase> habitsUseCaseProvider,
      Provider<HabitCategoryUseCase> categoryUseCaseProvider,
      Provider<QuoteUseCase> quoteUseCaseProvider,
      Provider<StatisticsRepository> statisticsRepositoryProvider) {
    this.habitsUseCaseProvider = habitsUseCaseProvider;
    this.categoryUseCaseProvider = categoryUseCaseProvider;
    this.quoteUseCaseProvider = quoteUseCaseProvider;
    this.statisticsRepositoryProvider = statisticsRepositoryProvider;
  }

  @Override
  public HabitsViewModel get() {
    return newInstance(habitsUseCaseProvider.get(), categoryUseCaseProvider.get(), quoteUseCaseProvider.get(), statisticsRepositoryProvider.get());
  }

  public static HabitsViewModel_Factory create(Provider<HabitsUseCase> habitsUseCaseProvider,
      Provider<HabitCategoryUseCase> categoryUseCaseProvider,
      Provider<QuoteUseCase> quoteUseCaseProvider,
      Provider<StatisticsRepository> statisticsRepositoryProvider) {
    return new HabitsViewModel_Factory(habitsUseCaseProvider, categoryUseCaseProvider, quoteUseCaseProvider, statisticsRepositoryProvider);
  }

  public static HabitsViewModel newInstance(HabitsUseCase habitsUseCase,
      HabitCategoryUseCase categoryUseCase, QuoteUseCase quoteUseCase,
      StatisticsRepository statisticsRepository) {
    return new HabitsViewModel(habitsUseCase, categoryUseCase, quoteUseCase, statisticsRepository);
  }
}

package com.example.habits.view.habits;

import com.example.habits.data.repository.HabitsRepository;
import com.example.habits.data.repository.StatisticsRepository;
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
  private final Provider<HabitsRepository> habitsRepositoryProvider;

  private final Provider<StatisticsRepository> statisticsRepositoryProvider;

  public HabitsViewModel_Factory(Provider<HabitsRepository> habitsRepositoryProvider,
      Provider<StatisticsRepository> statisticsRepositoryProvider) {
    this.habitsRepositoryProvider = habitsRepositoryProvider;
    this.statisticsRepositoryProvider = statisticsRepositoryProvider;
  }

  @Override
  public HabitsViewModel get() {
    return newInstance(habitsRepositoryProvider.get(), statisticsRepositoryProvider.get());
  }

  public static HabitsViewModel_Factory create(Provider<HabitsRepository> habitsRepositoryProvider,
      Provider<StatisticsRepository> statisticsRepositoryProvider) {
    return new HabitsViewModel_Factory(habitsRepositoryProvider, statisticsRepositoryProvider);
  }

  public static HabitsViewModel newInstance(HabitsRepository habitsRepository,
      StatisticsRepository statisticsRepository) {
    return new HabitsViewModel(habitsRepository, statisticsRepository);
  }
}

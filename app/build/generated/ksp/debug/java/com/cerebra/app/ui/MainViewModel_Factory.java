package com.cerebra.app.ui;

import com.cerebra.app.data.repository.UserPreferencesRepository;
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
public final class MainViewModel_Factory implements Factory<MainViewModel> {
  private final Provider<UserPreferencesRepository> userPreferencesRepositoryProvider;

  public MainViewModel_Factory(
      Provider<UserPreferencesRepository> userPreferencesRepositoryProvider) {
    this.userPreferencesRepositoryProvider = userPreferencesRepositoryProvider;
  }

  @Override
  public MainViewModel get() {
    return newInstance(userPreferencesRepositoryProvider.get());
  }

  public static MainViewModel_Factory create(
      Provider<UserPreferencesRepository> userPreferencesRepositoryProvider) {
    return new MainViewModel_Factory(userPreferencesRepositoryProvider);
  }

  public static MainViewModel newInstance(UserPreferencesRepository userPreferencesRepository) {
    return new MainViewModel(userPreferencesRepository);
  }
}

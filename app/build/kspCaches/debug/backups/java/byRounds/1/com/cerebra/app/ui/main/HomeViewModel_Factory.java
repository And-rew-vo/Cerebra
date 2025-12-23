package com.cerebra.app.ui.main;

import com.cerebra.app.data.repository.UserPreferencesRepository;
import com.cerebra.app.domain.repository.TextRepository;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<TextRepository> repositoryProvider;

  private final Provider<UserPreferencesRepository> userPreferencesRepositoryProvider;

  public HomeViewModel_Factory(Provider<TextRepository> repositoryProvider,
      Provider<UserPreferencesRepository> userPreferencesRepositoryProvider) {
    this.repositoryProvider = repositoryProvider;
    this.userPreferencesRepositoryProvider = userPreferencesRepositoryProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(repositoryProvider.get(), userPreferencesRepositoryProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<TextRepository> repositoryProvider,
      Provider<UserPreferencesRepository> userPreferencesRepositoryProvider) {
    return new HomeViewModel_Factory(repositoryProvider, userPreferencesRepositoryProvider);
  }

  public static HomeViewModel newInstance(TextRepository repository,
      UserPreferencesRepository userPreferencesRepository) {
    return new HomeViewModel(repository, userPreferencesRepository);
  }
}

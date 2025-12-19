package com.cerebra.app.ui.main;

import com.cerebra.app.data.repository.UserPreferencesRepository;
import com.cerebra.app.domain.repository.PoetryRepository;
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
public final class LibraryViewModel_Factory implements Factory<LibraryViewModel> {
  private final Provider<TextRepository> repositoryProvider;

  private final Provider<PoetryRepository> poetryRepositoryProvider;

  private final Provider<UserPreferencesRepository> userPreferencesRepositoryProvider;

  public LibraryViewModel_Factory(Provider<TextRepository> repositoryProvider,
      Provider<PoetryRepository> poetryRepositoryProvider,
      Provider<UserPreferencesRepository> userPreferencesRepositoryProvider) {
    this.repositoryProvider = repositoryProvider;
    this.poetryRepositoryProvider = poetryRepositoryProvider;
    this.userPreferencesRepositoryProvider = userPreferencesRepositoryProvider;
  }

  @Override
  public LibraryViewModel get() {
    return newInstance(repositoryProvider.get(), poetryRepositoryProvider.get(), userPreferencesRepositoryProvider.get());
  }

  public static LibraryViewModel_Factory create(Provider<TextRepository> repositoryProvider,
      Provider<PoetryRepository> poetryRepositoryProvider,
      Provider<UserPreferencesRepository> userPreferencesRepositoryProvider) {
    return new LibraryViewModel_Factory(repositoryProvider, poetryRepositoryProvider, userPreferencesRepositoryProvider);
  }

  public static LibraryViewModel newInstance(TextRepository repository,
      PoetryRepository poetryRepository, UserPreferencesRepository userPreferencesRepository) {
    return new LibraryViewModel(repository, poetryRepository, userPreferencesRepository);
  }
}

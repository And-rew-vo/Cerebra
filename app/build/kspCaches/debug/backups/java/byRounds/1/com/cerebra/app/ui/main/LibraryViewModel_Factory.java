package com.cerebra.app.ui.main;

import com.cerebra.app.data.repository.CerebraRepository;
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
  private final Provider<CerebraRepository> repositoryProvider;

  public LibraryViewModel_Factory(Provider<CerebraRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public LibraryViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static LibraryViewModel_Factory create(Provider<CerebraRepository> repositoryProvider) {
    return new LibraryViewModel_Factory(repositoryProvider);
  }

  public static LibraryViewModel newInstance(CerebraRepository repository) {
    return new LibraryViewModel(repository);
  }
}

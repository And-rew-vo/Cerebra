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
public final class ProfileViewModel_Factory implements Factory<ProfileViewModel> {
  private final Provider<CerebraRepository> repositoryProvider;

  public ProfileViewModel_Factory(Provider<CerebraRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ProfileViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static ProfileViewModel_Factory create(Provider<CerebraRepository> repositoryProvider) {
    return new ProfileViewModel_Factory(repositoryProvider);
  }

  public static ProfileViewModel newInstance(CerebraRepository repository) {
    return new ProfileViewModel(repository);
  }
}

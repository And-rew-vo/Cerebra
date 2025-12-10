package com.cerebra.app.ui.auth;

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
public final class AuthViewModel_Factory implements Factory<AuthViewModel> {
  private final Provider<CerebraRepository> repositoryProvider;

  public AuthViewModel_Factory(Provider<CerebraRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public AuthViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static AuthViewModel_Factory create(Provider<CerebraRepository> repositoryProvider) {
    return new AuthViewModel_Factory(repositoryProvider);
  }

  public static AuthViewModel newInstance(CerebraRepository repository) {
    return new AuthViewModel(repository);
  }
}

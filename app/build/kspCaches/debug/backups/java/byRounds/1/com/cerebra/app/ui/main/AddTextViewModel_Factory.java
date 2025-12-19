package com.cerebra.app.ui.main;

import androidx.lifecycle.SavedStateHandle;
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
public final class AddTextViewModel_Factory implements Factory<AddTextViewModel> {
  private final Provider<TextRepository> textRepositoryProvider;

  private final Provider<UserPreferencesRepository> userPreferencesRepositoryProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public AddTextViewModel_Factory(Provider<TextRepository> textRepositoryProvider,
      Provider<UserPreferencesRepository> userPreferencesRepositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.textRepositoryProvider = textRepositoryProvider;
    this.userPreferencesRepositoryProvider = userPreferencesRepositoryProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public AddTextViewModel get() {
    return newInstance(textRepositoryProvider.get(), userPreferencesRepositoryProvider.get(), savedStateHandleProvider.get());
  }

  public static AddTextViewModel_Factory create(Provider<TextRepository> textRepositoryProvider,
      Provider<UserPreferencesRepository> userPreferencesRepositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new AddTextViewModel_Factory(textRepositoryProvider, userPreferencesRepositoryProvider, savedStateHandleProvider);
  }

  public static AddTextViewModel newInstance(TextRepository textRepository,
      UserPreferencesRepository userPreferencesRepository, SavedStateHandle savedStateHandle) {
    return new AddTextViewModel(textRepository, userPreferencesRepository, savedStateHandle);
  }
}

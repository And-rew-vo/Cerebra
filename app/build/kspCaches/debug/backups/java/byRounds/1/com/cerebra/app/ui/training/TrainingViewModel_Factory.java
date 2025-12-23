package com.cerebra.app.ui.training;

import androidx.lifecycle.SavedStateHandle;
import com.cerebra.app.domain.TextProcessor;
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
public final class TrainingViewModel_Factory implements Factory<TrainingViewModel> {
  private final Provider<TextRepository> repositoryProvider;

  private final Provider<TextProcessor> textProcessorProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public TrainingViewModel_Factory(Provider<TextRepository> repositoryProvider,
      Provider<TextProcessor> textProcessorProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.repositoryProvider = repositoryProvider;
    this.textProcessorProvider = textProcessorProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public TrainingViewModel get() {
    return newInstance(repositoryProvider.get(), textProcessorProvider.get(), savedStateHandleProvider.get());
  }

  public static TrainingViewModel_Factory create(Provider<TextRepository> repositoryProvider,
      Provider<TextProcessor> textProcessorProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new TrainingViewModel_Factory(repositoryProvider, textProcessorProvider, savedStateHandleProvider);
  }

  public static TrainingViewModel newInstance(TextRepository repository,
      TextProcessor textProcessor, SavedStateHandle savedStateHandle) {
    return new TrainingViewModel(repository, textProcessor, savedStateHandle);
  }
}

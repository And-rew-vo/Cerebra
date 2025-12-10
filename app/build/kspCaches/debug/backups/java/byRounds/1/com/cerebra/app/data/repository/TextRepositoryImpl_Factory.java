package com.cerebra.app.data.repository;

import com.cerebra.app.data.local.dao.TextDao;
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
public final class TextRepositoryImpl_Factory implements Factory<TextRepositoryImpl> {
  private final Provider<TextDao> textDaoProvider;

  public TextRepositoryImpl_Factory(Provider<TextDao> textDaoProvider) {
    this.textDaoProvider = textDaoProvider;
  }

  @Override
  public TextRepositoryImpl get() {
    return newInstance(textDaoProvider.get());
  }

  public static TextRepositoryImpl_Factory create(Provider<TextDao> textDaoProvider) {
    return new TextRepositoryImpl_Factory(textDaoProvider);
  }

  public static TextRepositoryImpl newInstance(TextDao textDao) {
    return new TextRepositoryImpl(textDao);
  }
}

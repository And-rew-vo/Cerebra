package com.cerebra.app.di;

import com.cerebra.app.data.local.AppDatabase;
import com.cerebra.app.data.local.dao.TextDocumentDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideTextDocumentDaoFactory implements Factory<TextDocumentDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideTextDocumentDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public TextDocumentDao get() {
    return provideTextDocumentDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideTextDocumentDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideTextDocumentDaoFactory(databaseProvider);
  }

  public static TextDocumentDao provideTextDocumentDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideTextDocumentDao(database));
  }
}

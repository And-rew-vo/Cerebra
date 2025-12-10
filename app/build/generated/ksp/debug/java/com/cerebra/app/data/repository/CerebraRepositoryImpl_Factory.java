package com.cerebra.app.data.repository;

import com.cerebra.app.data.local.dao.TextDocumentDao;
import com.cerebra.app.data.local.dao.UserDao;
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
public final class CerebraRepositoryImpl_Factory implements Factory<CerebraRepositoryImpl> {
  private final Provider<UserDao> userDaoProvider;

  private final Provider<TextDocumentDao> textDocumentDaoProvider;

  public CerebraRepositoryImpl_Factory(Provider<UserDao> userDaoProvider,
      Provider<TextDocumentDao> textDocumentDaoProvider) {
    this.userDaoProvider = userDaoProvider;
    this.textDocumentDaoProvider = textDocumentDaoProvider;
  }

  @Override
  public CerebraRepositoryImpl get() {
    return newInstance(userDaoProvider.get(), textDocumentDaoProvider.get());
  }

  public static CerebraRepositoryImpl_Factory create(Provider<UserDao> userDaoProvider,
      Provider<TextDocumentDao> textDocumentDaoProvider) {
    return new CerebraRepositoryImpl_Factory(userDaoProvider, textDocumentDaoProvider);
  }

  public static CerebraRepositoryImpl newInstance(UserDao userDao,
      TextDocumentDao textDocumentDao) {
    return new CerebraRepositoryImpl(userDao, textDocumentDao);
  }
}

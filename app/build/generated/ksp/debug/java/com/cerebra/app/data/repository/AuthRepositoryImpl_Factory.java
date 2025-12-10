package com.cerebra.app.data.repository;

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
public final class AuthRepositoryImpl_Factory implements Factory<AuthRepositoryImpl> {
  private final Provider<UserDao> userDaoProvider;

  public AuthRepositoryImpl_Factory(Provider<UserDao> userDaoProvider) {
    this.userDaoProvider = userDaoProvider;
  }

  @Override
  public AuthRepositoryImpl get() {
    return newInstance(userDaoProvider.get());
  }

  public static AuthRepositoryImpl_Factory create(Provider<UserDao> userDaoProvider) {
    return new AuthRepositoryImpl_Factory(userDaoProvider);
  }

  public static AuthRepositoryImpl newInstance(UserDao userDao) {
    return new AuthRepositoryImpl(userDao);
  }
}

package com.cerebra.app.domain;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class TextProcessor_Factory implements Factory<TextProcessor> {
  @Override
  public TextProcessor get() {
    return newInstance();
  }

  public static TextProcessor_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static TextProcessor newInstance() {
    return new TextProcessor();
  }

  private static final class InstanceHolder {
    private static final TextProcessor_Factory INSTANCE = new TextProcessor_Factory();
  }
}

package com.example.telephases.data.local.database;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class DatabaseInitializer_Factory implements Factory<DatabaseInitializer> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseInitializer_Factory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public DatabaseInitializer get() {
    return newInstance(databaseProvider.get());
  }

  public static DatabaseInitializer_Factory create(Provider<AppDatabase> databaseProvider) {
    return new DatabaseInitializer_Factory(databaseProvider);
  }

  public static DatabaseInitializer newInstance(AppDatabase database) {
    return new DatabaseInitializer(database);
  }
}

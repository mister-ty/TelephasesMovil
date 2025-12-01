package com.example.telephases.di;

import com.example.telephases.data.local.dao.EntidadSaludDao;
import com.example.telephases.data.local.database.AppDatabase;
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
public final class DatabaseModule_ProvideEntidadSaludDaoFactory implements Factory<EntidadSaludDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideEntidadSaludDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public EntidadSaludDao get() {
    return provideEntidadSaludDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideEntidadSaludDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideEntidadSaludDaoFactory(databaseProvider);
  }

  public static EntidadSaludDao provideEntidadSaludDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideEntidadSaludDao(database));
  }
}

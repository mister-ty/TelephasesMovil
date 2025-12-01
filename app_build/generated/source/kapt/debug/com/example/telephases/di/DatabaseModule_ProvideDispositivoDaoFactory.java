package com.example.telephases.di;

import com.example.telephases.data.local.dao.DispositivoDao;
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
public final class DatabaseModule_ProvideDispositivoDaoFactory implements Factory<DispositivoDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideDispositivoDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public DispositivoDao get() {
    return provideDispositivoDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideDispositivoDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideDispositivoDaoFactory(databaseProvider);
  }

  public static DispositivoDao provideDispositivoDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideDispositivoDao(database));
  }
}

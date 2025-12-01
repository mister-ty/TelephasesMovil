package com.example.telephases.di;

import com.example.telephases.data.local.dao.TipoExamenDao;
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
public final class DatabaseModule_ProvideTipoExamenDaoFactory implements Factory<TipoExamenDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideTipoExamenDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public TipoExamenDao get() {
    return provideTipoExamenDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideTipoExamenDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideTipoExamenDaoFactory(databaseProvider);
  }

  public static TipoExamenDao provideTipoExamenDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideTipoExamenDao(database));
  }
}

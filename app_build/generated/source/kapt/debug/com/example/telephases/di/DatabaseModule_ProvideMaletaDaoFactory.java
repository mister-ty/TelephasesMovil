package com.example.telephases.di;

import com.example.telephases.data.local.dao.MaletaDao;
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
public final class DatabaseModule_ProvideMaletaDaoFactory implements Factory<MaletaDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideMaletaDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public MaletaDao get() {
    return provideMaletaDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideMaletaDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideMaletaDaoFactory(databaseProvider);
  }

  public static MaletaDao provideMaletaDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideMaletaDao(database));
  }
}

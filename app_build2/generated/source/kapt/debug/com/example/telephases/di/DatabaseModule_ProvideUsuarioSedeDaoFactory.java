package com.example.telephases.di;

import com.example.telephases.data.local.dao.UsuarioSedeDao;
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
public final class DatabaseModule_ProvideUsuarioSedeDaoFactory implements Factory<UsuarioSedeDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideUsuarioSedeDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public UsuarioSedeDao get() {
    return provideUsuarioSedeDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideUsuarioSedeDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideUsuarioSedeDaoFactory(databaseProvider);
  }

  public static UsuarioSedeDao provideUsuarioSedeDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideUsuarioSedeDao(database));
  }
}

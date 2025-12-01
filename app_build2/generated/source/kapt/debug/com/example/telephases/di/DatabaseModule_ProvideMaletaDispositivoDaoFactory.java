package com.example.telephases.di;

import com.example.telephases.data.local.dao.MaletaDispositivoDao;
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
public final class DatabaseModule_ProvideMaletaDispositivoDaoFactory implements Factory<MaletaDispositivoDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideMaletaDispositivoDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public MaletaDispositivoDao get() {
    return provideMaletaDispositivoDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideMaletaDispositivoDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideMaletaDispositivoDaoFactory(databaseProvider);
  }

  public static MaletaDispositivoDao provideMaletaDispositivoDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideMaletaDispositivoDao(database));
  }
}

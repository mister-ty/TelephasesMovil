package com.example.telephases.di;

import com.example.telephases.data.local.dao.SyncMetadataDao;
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
public final class DatabaseModule_ProvideSyncMetadataDaoFactory implements Factory<SyncMetadataDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideSyncMetadataDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public SyncMetadataDao get() {
    return provideSyncMetadataDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideSyncMetadataDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideSyncMetadataDaoFactory(databaseProvider);
  }

  public static SyncMetadataDao provideSyncMetadataDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideSyncMetadataDao(database));
  }
}

package com.example.telephases.workers;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.example.telephases.data.local.database.AppDatabase;
import com.example.telephases.data.repository.SyncManager;
import dagger.internal.DaggerGenerated;
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
public final class MaintenanceWorker_Factory {
  private final Provider<SyncManager> syncManagerProvider;

  private final Provider<AppDatabase> databaseProvider;

  public MaintenanceWorker_Factory(Provider<SyncManager> syncManagerProvider,
      Provider<AppDatabase> databaseProvider) {
    this.syncManagerProvider = syncManagerProvider;
    this.databaseProvider = databaseProvider;
  }

  public MaintenanceWorker get(Context context, WorkerParameters workerParams) {
    return newInstance(context, workerParams, syncManagerProvider.get(), databaseProvider.get());
  }

  public static MaintenanceWorker_Factory create(Provider<SyncManager> syncManagerProvider,
      Provider<AppDatabase> databaseProvider) {
    return new MaintenanceWorker_Factory(syncManagerProvider, databaseProvider);
  }

  public static MaintenanceWorker newInstance(Context context, WorkerParameters workerParams,
      SyncManager syncManager, AppDatabase database) {
    return new MaintenanceWorker(context, workerParams, syncManager, database);
  }
}

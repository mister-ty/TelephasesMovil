package com.example.telephases.workers;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.example.telephases.data.repository.AuthRepository;
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
public final class SyncWorker_Factory {
  private final Provider<SyncManager> syncManagerProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  public SyncWorker_Factory(Provider<SyncManager> syncManagerProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    this.syncManagerProvider = syncManagerProvider;
    this.authRepositoryProvider = authRepositoryProvider;
  }

  public SyncWorker get(Context context, WorkerParameters workerParams) {
    return newInstance(context, workerParams, syncManagerProvider.get(), authRepositoryProvider.get());
  }

  public static SyncWorker_Factory create(Provider<SyncManager> syncManagerProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    return new SyncWorker_Factory(syncManagerProvider, authRepositoryProvider);
  }

  public static SyncWorker newInstance(Context context, WorkerParameters workerParams,
      SyncManager syncManager, AuthRepository authRepository) {
    return new SyncWorker(context, workerParams, syncManager, authRepository);
  }
}

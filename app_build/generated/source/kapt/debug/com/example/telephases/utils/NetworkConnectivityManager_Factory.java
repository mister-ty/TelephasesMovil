package com.example.telephases.utils;

import android.content.Context;
import com.example.telephases.data.repository.SyncManager;
import com.example.telephases.workers.WorkScheduler;
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
public final class NetworkConnectivityManager_Factory implements Factory<NetworkConnectivityManager> {
  private final Provider<Context> contextProvider;

  private final Provider<SyncManager> syncManagerProvider;

  private final Provider<WorkScheduler> workSchedulerProvider;

  public NetworkConnectivityManager_Factory(Provider<Context> contextProvider,
      Provider<SyncManager> syncManagerProvider, Provider<WorkScheduler> workSchedulerProvider) {
    this.contextProvider = contextProvider;
    this.syncManagerProvider = syncManagerProvider;
    this.workSchedulerProvider = workSchedulerProvider;
  }

  @Override
  public NetworkConnectivityManager get() {
    return newInstance(contextProvider.get(), syncManagerProvider.get(), workSchedulerProvider.get());
  }

  public static NetworkConnectivityManager_Factory create(Provider<Context> contextProvider,
      Provider<SyncManager> syncManagerProvider, Provider<WorkScheduler> workSchedulerProvider) {
    return new NetworkConnectivityManager_Factory(contextProvider, syncManagerProvider, workSchedulerProvider);
  }

  public static NetworkConnectivityManager newInstance(Context context, SyncManager syncManager,
      WorkScheduler workScheduler) {
    return new NetworkConnectivityManager(context, syncManager, workScheduler);
  }
}

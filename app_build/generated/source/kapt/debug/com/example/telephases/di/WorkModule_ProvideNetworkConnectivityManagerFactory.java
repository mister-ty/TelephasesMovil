package com.example.telephases.di;

import android.content.Context;
import com.example.telephases.data.repository.SyncManager;
import com.example.telephases.utils.NetworkConnectivityManager;
import com.example.telephases.workers.WorkScheduler;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class WorkModule_ProvideNetworkConnectivityManagerFactory implements Factory<NetworkConnectivityManager> {
  private final Provider<Context> contextProvider;

  private final Provider<SyncManager> syncManagerProvider;

  private final Provider<WorkScheduler> workSchedulerProvider;

  public WorkModule_ProvideNetworkConnectivityManagerFactory(Provider<Context> contextProvider,
      Provider<SyncManager> syncManagerProvider, Provider<WorkScheduler> workSchedulerProvider) {
    this.contextProvider = contextProvider;
    this.syncManagerProvider = syncManagerProvider;
    this.workSchedulerProvider = workSchedulerProvider;
  }

  @Override
  public NetworkConnectivityManager get() {
    return provideNetworkConnectivityManager(contextProvider.get(), syncManagerProvider.get(), workSchedulerProvider.get());
  }

  public static WorkModule_ProvideNetworkConnectivityManagerFactory create(
      Provider<Context> contextProvider, Provider<SyncManager> syncManagerProvider,
      Provider<WorkScheduler> workSchedulerProvider) {
    return new WorkModule_ProvideNetworkConnectivityManagerFactory(contextProvider, syncManagerProvider, workSchedulerProvider);
  }

  public static NetworkConnectivityManager provideNetworkConnectivityManager(Context context,
      SyncManager syncManager, WorkScheduler workScheduler) {
    return Preconditions.checkNotNullFromProvides(WorkModule.INSTANCE.provideNetworkConnectivityManager(context, syncManager, workScheduler));
  }
}

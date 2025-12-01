package com.example.telephases;

import androidx.hilt.work.HiltWorkerFactory;
import com.example.telephases.data.local.database.DatabaseInitializer;
import com.example.telephases.utils.NetworkConnectivityManager;
import com.example.telephases.workers.WorkScheduler;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class TelephaseApplication_MembersInjector implements MembersInjector<TelephaseApplication> {
  private final Provider<HiltWorkerFactory> workerFactoryProvider;

  private final Provider<DatabaseInitializer> databaseInitializerProvider;

  private final Provider<NetworkConnectivityManager> networkConnectivityManagerProvider;

  private final Provider<WorkScheduler> workSchedulerProvider;

  public TelephaseApplication_MembersInjector(Provider<HiltWorkerFactory> workerFactoryProvider,
      Provider<DatabaseInitializer> databaseInitializerProvider,
      Provider<NetworkConnectivityManager> networkConnectivityManagerProvider,
      Provider<WorkScheduler> workSchedulerProvider) {
    this.workerFactoryProvider = workerFactoryProvider;
    this.databaseInitializerProvider = databaseInitializerProvider;
    this.networkConnectivityManagerProvider = networkConnectivityManagerProvider;
    this.workSchedulerProvider = workSchedulerProvider;
  }

  public static MembersInjector<TelephaseApplication> create(
      Provider<HiltWorkerFactory> workerFactoryProvider,
      Provider<DatabaseInitializer> databaseInitializerProvider,
      Provider<NetworkConnectivityManager> networkConnectivityManagerProvider,
      Provider<WorkScheduler> workSchedulerProvider) {
    return new TelephaseApplication_MembersInjector(workerFactoryProvider, databaseInitializerProvider, networkConnectivityManagerProvider, workSchedulerProvider);
  }

  @Override
  public void injectMembers(TelephaseApplication instance) {
    injectWorkerFactory(instance, workerFactoryProvider.get());
    injectDatabaseInitializer(instance, databaseInitializerProvider.get());
    injectNetworkConnectivityManager(instance, networkConnectivityManagerProvider.get());
    injectWorkScheduler(instance, workSchedulerProvider.get());
  }

  @InjectedFieldSignature("com.example.telephases.TelephaseApplication.workerFactory")
  public static void injectWorkerFactory(TelephaseApplication instance,
      HiltWorkerFactory workerFactory) {
    instance.workerFactory = workerFactory;
  }

  @InjectedFieldSignature("com.example.telephases.TelephaseApplication.databaseInitializer")
  public static void injectDatabaseInitializer(TelephaseApplication instance,
      DatabaseInitializer databaseInitializer) {
    instance.databaseInitializer = databaseInitializer;
  }

  @InjectedFieldSignature("com.example.telephases.TelephaseApplication.networkConnectivityManager")
  public static void injectNetworkConnectivityManager(TelephaseApplication instance,
      NetworkConnectivityManager networkConnectivityManager) {
    instance.networkConnectivityManager = networkConnectivityManager;
  }

  @InjectedFieldSignature("com.example.telephases.TelephaseApplication.workScheduler")
  public static void injectWorkScheduler(TelephaseApplication instance,
      WorkScheduler workScheduler) {
    instance.workScheduler = workScheduler;
  }
}

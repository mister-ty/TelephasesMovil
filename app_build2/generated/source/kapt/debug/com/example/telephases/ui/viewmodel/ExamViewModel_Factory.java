package com.example.telephases.ui.viewmodel;

import com.example.telephases.data.repository.AuthRepository;
import com.example.telephases.data.repository.ExamRepository;
import com.example.telephases.utils.NetworkConnectivityManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class ExamViewModel_Factory implements Factory<ExamViewModel> {
  private final Provider<ExamRepository> examRepositoryProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<NetworkConnectivityManager> networkConnectivityManagerProvider;

  public ExamViewModel_Factory(Provider<ExamRepository> examRepositoryProvider,
      Provider<AuthRepository> authRepositoryProvider,
      Provider<NetworkConnectivityManager> networkConnectivityManagerProvider) {
    this.examRepositoryProvider = examRepositoryProvider;
    this.authRepositoryProvider = authRepositoryProvider;
    this.networkConnectivityManagerProvider = networkConnectivityManagerProvider;
  }

  @Override
  public ExamViewModel get() {
    return newInstance(examRepositoryProvider.get(), authRepositoryProvider.get(), networkConnectivityManagerProvider.get());
  }

  public static ExamViewModel_Factory create(Provider<ExamRepository> examRepositoryProvider,
      Provider<AuthRepository> authRepositoryProvider,
      Provider<NetworkConnectivityManager> networkConnectivityManagerProvider) {
    return new ExamViewModel_Factory(examRepositoryProvider, authRepositoryProvider, networkConnectivityManagerProvider);
  }

  public static ExamViewModel newInstance(ExamRepository examRepository,
      AuthRepository authRepository, NetworkConnectivityManager networkConnectivityManager) {
    return new ExamViewModel(examRepository, authRepository, networkConnectivityManager);
  }
}

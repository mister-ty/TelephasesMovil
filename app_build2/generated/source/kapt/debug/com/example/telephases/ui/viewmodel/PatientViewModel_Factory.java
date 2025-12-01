package com.example.telephases.ui.viewmodel;

import com.example.telephases.data.repository.AuthRepository;
import com.example.telephases.data.repository.PatientRepository;
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
public final class PatientViewModel_Factory implements Factory<PatientViewModel> {
  private final Provider<PatientRepository> patientRepositoryProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<NetworkConnectivityManager> networkConnectivityManagerProvider;

  public PatientViewModel_Factory(Provider<PatientRepository> patientRepositoryProvider,
      Provider<AuthRepository> authRepositoryProvider,
      Provider<NetworkConnectivityManager> networkConnectivityManagerProvider) {
    this.patientRepositoryProvider = patientRepositoryProvider;
    this.authRepositoryProvider = authRepositoryProvider;
    this.networkConnectivityManagerProvider = networkConnectivityManagerProvider;
  }

  @Override
  public PatientViewModel get() {
    return newInstance(patientRepositoryProvider.get(), authRepositoryProvider.get(), networkConnectivityManagerProvider.get());
  }

  public static PatientViewModel_Factory create(
      Provider<PatientRepository> patientRepositoryProvider,
      Provider<AuthRepository> authRepositoryProvider,
      Provider<NetworkConnectivityManager> networkConnectivityManagerProvider) {
    return new PatientViewModel_Factory(patientRepositoryProvider, authRepositoryProvider, networkConnectivityManagerProvider);
  }

  public static PatientViewModel newInstance(PatientRepository patientRepository,
      AuthRepository authRepository, NetworkConnectivityManager networkConnectivityManager) {
    return new PatientViewModel(patientRepository, authRepository, networkConnectivityManager);
  }
}

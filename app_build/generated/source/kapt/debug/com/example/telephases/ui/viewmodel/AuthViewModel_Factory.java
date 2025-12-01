package com.example.telephases.ui.viewmodel;

import com.example.telephases.data.repository.AuthRepository;
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
public final class AuthViewModel_Factory implements Factory<AuthViewModel> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<NetworkConnectivityManager> networkConnectivityManagerProvider;

  public AuthViewModel_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<NetworkConnectivityManager> networkConnectivityManagerProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.networkConnectivityManagerProvider = networkConnectivityManagerProvider;
  }

  @Override
  public AuthViewModel get() {
    return newInstance(authRepositoryProvider.get(), networkConnectivityManagerProvider.get());
  }

  public static AuthViewModel_Factory create(Provider<AuthRepository> authRepositoryProvider,
      Provider<NetworkConnectivityManager> networkConnectivityManagerProvider) {
    return new AuthViewModel_Factory(authRepositoryProvider, networkConnectivityManagerProvider);
  }

  public static AuthViewModel newInstance(AuthRepository authRepository,
      NetworkConnectivityManager networkConnectivityManager) {
    return new AuthViewModel(authRepository, networkConnectivityManager);
  }
}

package com.example.telephases.data.repository;

import com.example.telephases.data.local.dao.CitaDao;
import com.example.telephases.network.ApiInterface;
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
public final class CitaRepositoryImpl_Factory implements Factory<CitaRepositoryImpl> {
  private final Provider<CitaDao> citaDaoProvider;

  private final Provider<ApiInterface> apiInterfaceProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  public CitaRepositoryImpl_Factory(Provider<CitaDao> citaDaoProvider,
      Provider<ApiInterface> apiInterfaceProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    this.citaDaoProvider = citaDaoProvider;
    this.apiInterfaceProvider = apiInterfaceProvider;
    this.authRepositoryProvider = authRepositoryProvider;
  }

  @Override
  public CitaRepositoryImpl get() {
    return newInstance(citaDaoProvider.get(), apiInterfaceProvider.get(), authRepositoryProvider.get());
  }

  public static CitaRepositoryImpl_Factory create(Provider<CitaDao> citaDaoProvider,
      Provider<ApiInterface> apiInterfaceProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    return new CitaRepositoryImpl_Factory(citaDaoProvider, apiInterfaceProvider, authRepositoryProvider);
  }

  public static CitaRepositoryImpl newInstance(CitaDao citaDao, ApiInterface apiInterface,
      AuthRepository authRepository) {
    return new CitaRepositoryImpl(citaDao, apiInterface, authRepository);
  }
}

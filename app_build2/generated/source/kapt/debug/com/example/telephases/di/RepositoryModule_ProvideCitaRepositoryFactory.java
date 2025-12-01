package com.example.telephases.di;

import com.example.telephases.data.local.dao.CitaDao;
import com.example.telephases.data.repository.AuthRepository;
import com.example.telephases.data.repository.CitaRepository;
import com.example.telephases.network.ApiInterface;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class RepositoryModule_ProvideCitaRepositoryFactory implements Factory<CitaRepository> {
  private final Provider<CitaDao> citaDaoProvider;

  private final Provider<ApiInterface> apiServiceProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  public RepositoryModule_ProvideCitaRepositoryFactory(Provider<CitaDao> citaDaoProvider,
      Provider<ApiInterface> apiServiceProvider, Provider<AuthRepository> authRepositoryProvider) {
    this.citaDaoProvider = citaDaoProvider;
    this.apiServiceProvider = apiServiceProvider;
    this.authRepositoryProvider = authRepositoryProvider;
  }

  @Override
  public CitaRepository get() {
    return provideCitaRepository(citaDaoProvider.get(), apiServiceProvider.get(), authRepositoryProvider.get());
  }

  public static RepositoryModule_ProvideCitaRepositoryFactory create(
      Provider<CitaDao> citaDaoProvider, Provider<ApiInterface> apiServiceProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    return new RepositoryModule_ProvideCitaRepositoryFactory(citaDaoProvider, apiServiceProvider, authRepositoryProvider);
  }

  public static CitaRepository provideCitaRepository(CitaDao citaDao, ApiInterface apiService,
      AuthRepository authRepository) {
    return Preconditions.checkNotNullFromProvides(RepositoryModule.INSTANCE.provideCitaRepository(citaDao, apiService, authRepository));
  }
}

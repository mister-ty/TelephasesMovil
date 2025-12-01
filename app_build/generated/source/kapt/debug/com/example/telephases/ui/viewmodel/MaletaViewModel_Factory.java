package com.example.telephases.ui.viewmodel;

import com.example.telephases.data.local.dao.MaletaDao;
import com.example.telephases.data.repository.AuthRepository;
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
public final class MaletaViewModel_Factory implements Factory<MaletaViewModel> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<MaletaDao> maletaDaoProvider;

  public MaletaViewModel_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<MaletaDao> maletaDaoProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.maletaDaoProvider = maletaDaoProvider;
  }

  @Override
  public MaletaViewModel get() {
    return newInstance(authRepositoryProvider.get(), maletaDaoProvider.get());
  }

  public static MaletaViewModel_Factory create(Provider<AuthRepository> authRepositoryProvider,
      Provider<MaletaDao> maletaDaoProvider) {
    return new MaletaViewModel_Factory(authRepositoryProvider, maletaDaoProvider);
  }

  public static MaletaViewModel newInstance(AuthRepository authRepository, MaletaDao maletaDao) {
    return new MaletaViewModel(authRepository, maletaDao);
  }
}

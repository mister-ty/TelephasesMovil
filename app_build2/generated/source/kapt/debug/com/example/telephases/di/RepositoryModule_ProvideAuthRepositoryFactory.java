package com.example.telephases.di;

import android.content.Context;
import com.example.telephases.data.local.dao.SyncMetadataDao;
import com.example.telephases.data.local.dao.UserDao;
import com.example.telephases.data.repository.AuthRepository;
import com.example.telephases.network.ApiInterface;
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
public final class RepositoryModule_ProvideAuthRepositoryFactory implements Factory<AuthRepository> {
  private final Provider<UserDao> userDaoProvider;

  private final Provider<SyncMetadataDao> syncMetadataDaoProvider;

  private final Provider<ApiInterface> apiServiceProvider;

  private final Provider<Context> contextProvider;

  public RepositoryModule_ProvideAuthRepositoryFactory(Provider<UserDao> userDaoProvider,
      Provider<SyncMetadataDao> syncMetadataDaoProvider, Provider<ApiInterface> apiServiceProvider,
      Provider<Context> contextProvider) {
    this.userDaoProvider = userDaoProvider;
    this.syncMetadataDaoProvider = syncMetadataDaoProvider;
    this.apiServiceProvider = apiServiceProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public AuthRepository get() {
    return provideAuthRepository(userDaoProvider.get(), syncMetadataDaoProvider.get(), apiServiceProvider.get(), contextProvider.get());
  }

  public static RepositoryModule_ProvideAuthRepositoryFactory create(
      Provider<UserDao> userDaoProvider, Provider<SyncMetadataDao> syncMetadataDaoProvider,
      Provider<ApiInterface> apiServiceProvider, Provider<Context> contextProvider) {
    return new RepositoryModule_ProvideAuthRepositoryFactory(userDaoProvider, syncMetadataDaoProvider, apiServiceProvider, contextProvider);
  }

  public static AuthRepository provideAuthRepository(UserDao userDao,
      SyncMetadataDao syncMetadataDao, ApiInterface apiService, Context context) {
    return Preconditions.checkNotNullFromProvides(RepositoryModule.INSTANCE.provideAuthRepository(userDao, syncMetadataDao, apiService, context));
  }
}

package com.example.telephases.data.repository;

import android.content.Context;
import com.example.telephases.data.local.dao.SyncMetadataDao;
import com.example.telephases.data.local.dao.UserDao;
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
public final class AuthRepositoryImpl_Factory implements Factory<AuthRepositoryImpl> {
  private final Provider<UserDao> userDaoProvider;

  private final Provider<SyncMetadataDao> syncMetadataDaoProvider;

  private final Provider<ApiInterface> apiServiceProvider;

  private final Provider<Context> contextProvider;

  public AuthRepositoryImpl_Factory(Provider<UserDao> userDaoProvider,
      Provider<SyncMetadataDao> syncMetadataDaoProvider, Provider<ApiInterface> apiServiceProvider,
      Provider<Context> contextProvider) {
    this.userDaoProvider = userDaoProvider;
    this.syncMetadataDaoProvider = syncMetadataDaoProvider;
    this.apiServiceProvider = apiServiceProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public AuthRepositoryImpl get() {
    return newInstance(userDaoProvider.get(), syncMetadataDaoProvider.get(), apiServiceProvider.get(), contextProvider.get());
  }

  public static AuthRepositoryImpl_Factory create(Provider<UserDao> userDaoProvider,
      Provider<SyncMetadataDao> syncMetadataDaoProvider, Provider<ApiInterface> apiServiceProvider,
      Provider<Context> contextProvider) {
    return new AuthRepositoryImpl_Factory(userDaoProvider, syncMetadataDaoProvider, apiServiceProvider, contextProvider);
  }

  public static AuthRepositoryImpl newInstance(UserDao userDao, SyncMetadataDao syncMetadataDao,
      ApiInterface apiService, Context context) {
    return new AuthRepositoryImpl(userDao, syncMetadataDao, apiService, context);
  }
}

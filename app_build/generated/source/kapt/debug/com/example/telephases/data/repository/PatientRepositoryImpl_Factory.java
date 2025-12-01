package com.example.telephases.data.repository;

import android.content.Context;
import com.example.telephases.data.local.dao.PatientDao;
import com.example.telephases.data.local.dao.SyncMetadataDao;
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
public final class PatientRepositoryImpl_Factory implements Factory<PatientRepositoryImpl> {
  private final Provider<PatientDao> patientDaoProvider;

  private final Provider<SyncMetadataDao> syncMetadataDaoProvider;

  private final Provider<ApiInterface> apiServiceProvider;

  private final Provider<Context> contextProvider;

  public PatientRepositoryImpl_Factory(Provider<PatientDao> patientDaoProvider,
      Provider<SyncMetadataDao> syncMetadataDaoProvider, Provider<ApiInterface> apiServiceProvider,
      Provider<Context> contextProvider) {
    this.patientDaoProvider = patientDaoProvider;
    this.syncMetadataDaoProvider = syncMetadataDaoProvider;
    this.apiServiceProvider = apiServiceProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public PatientRepositoryImpl get() {
    return newInstance(patientDaoProvider.get(), syncMetadataDaoProvider.get(), apiServiceProvider.get(), contextProvider.get());
  }

  public static PatientRepositoryImpl_Factory create(Provider<PatientDao> patientDaoProvider,
      Provider<SyncMetadataDao> syncMetadataDaoProvider, Provider<ApiInterface> apiServiceProvider,
      Provider<Context> contextProvider) {
    return new PatientRepositoryImpl_Factory(patientDaoProvider, syncMetadataDaoProvider, apiServiceProvider, contextProvider);
  }

  public static PatientRepositoryImpl newInstance(PatientDao patientDao,
      SyncMetadataDao syncMetadataDao, ApiInterface apiService, Context context) {
    return new PatientRepositoryImpl(patientDao, syncMetadataDao, apiService, context);
  }
}

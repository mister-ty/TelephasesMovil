package com.example.telephases.di;

import android.content.Context;
import com.example.telephases.data.local.dao.PatientDao;
import com.example.telephases.data.local.dao.SyncMetadataDao;
import com.example.telephases.data.repository.PatientRepository;
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
public final class RepositoryModule_ProvidePatientRepositoryFactory implements Factory<PatientRepository> {
  private final Provider<PatientDao> patientDaoProvider;

  private final Provider<SyncMetadataDao> syncMetadataDaoProvider;

  private final Provider<ApiInterface> apiServiceProvider;

  private final Provider<Context> contextProvider;

  public RepositoryModule_ProvidePatientRepositoryFactory(Provider<PatientDao> patientDaoProvider,
      Provider<SyncMetadataDao> syncMetadataDaoProvider, Provider<ApiInterface> apiServiceProvider,
      Provider<Context> contextProvider) {
    this.patientDaoProvider = patientDaoProvider;
    this.syncMetadataDaoProvider = syncMetadataDaoProvider;
    this.apiServiceProvider = apiServiceProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public PatientRepository get() {
    return providePatientRepository(patientDaoProvider.get(), syncMetadataDaoProvider.get(), apiServiceProvider.get(), contextProvider.get());
  }

  public static RepositoryModule_ProvidePatientRepositoryFactory create(
      Provider<PatientDao> patientDaoProvider, Provider<SyncMetadataDao> syncMetadataDaoProvider,
      Provider<ApiInterface> apiServiceProvider, Provider<Context> contextProvider) {
    return new RepositoryModule_ProvidePatientRepositoryFactory(patientDaoProvider, syncMetadataDaoProvider, apiServiceProvider, contextProvider);
  }

  public static PatientRepository providePatientRepository(PatientDao patientDao,
      SyncMetadataDao syncMetadataDao, ApiInterface apiService, Context context) {
    return Preconditions.checkNotNullFromProvides(RepositoryModule.INSTANCE.providePatientRepository(patientDao, syncMetadataDao, apiService, context));
  }
}

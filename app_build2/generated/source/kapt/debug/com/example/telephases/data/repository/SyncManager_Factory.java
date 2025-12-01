package com.example.telephases.data.repository;

import android.content.Context;
import com.example.telephases.data.local.dao.SyncMetadataDao;
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
public final class SyncManager_Factory implements Factory<SyncManager> {
  private final Provider<PatientRepository> patientRepositoryProvider;

  private final Provider<ExamRepository> examRepositoryProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<SyncMetadataDao> syncMetadataDaoProvider;

  private final Provider<Context> contextProvider;

  public SyncManager_Factory(Provider<PatientRepository> patientRepositoryProvider,
      Provider<ExamRepository> examRepositoryProvider,
      Provider<AuthRepository> authRepositoryProvider,
      Provider<SyncMetadataDao> syncMetadataDaoProvider, Provider<Context> contextProvider) {
    this.patientRepositoryProvider = patientRepositoryProvider;
    this.examRepositoryProvider = examRepositoryProvider;
    this.authRepositoryProvider = authRepositoryProvider;
    this.syncMetadataDaoProvider = syncMetadataDaoProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public SyncManager get() {
    return newInstance(patientRepositoryProvider.get(), examRepositoryProvider.get(), authRepositoryProvider.get(), syncMetadataDaoProvider.get(), contextProvider.get());
  }

  public static SyncManager_Factory create(Provider<PatientRepository> patientRepositoryProvider,
      Provider<ExamRepository> examRepositoryProvider,
      Provider<AuthRepository> authRepositoryProvider,
      Provider<SyncMetadataDao> syncMetadataDaoProvider, Provider<Context> contextProvider) {
    return new SyncManager_Factory(patientRepositoryProvider, examRepositoryProvider, authRepositoryProvider, syncMetadataDaoProvider, contextProvider);
  }

  public static SyncManager newInstance(PatientRepository patientRepository,
      ExamRepository examRepository, AuthRepository authRepository, SyncMetadataDao syncMetadataDao,
      Context context) {
    return new SyncManager(patientRepository, examRepository, authRepository, syncMetadataDao, context);
  }
}

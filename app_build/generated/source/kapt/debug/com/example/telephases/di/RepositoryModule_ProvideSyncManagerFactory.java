package com.example.telephases.di;

import android.content.Context;
import com.example.telephases.data.local.dao.SyncMetadataDao;
import com.example.telephases.data.repository.AuthRepository;
import com.example.telephases.data.repository.ExamRepository;
import com.example.telephases.data.repository.PatientRepository;
import com.example.telephases.data.repository.SyncManager;
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
public final class RepositoryModule_ProvideSyncManagerFactory implements Factory<SyncManager> {
  private final Provider<PatientRepository> patientRepositoryProvider;

  private final Provider<ExamRepository> examRepositoryProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<SyncMetadataDao> syncMetadataDaoProvider;

  private final Provider<Context> contextProvider;

  public RepositoryModule_ProvideSyncManagerFactory(
      Provider<PatientRepository> patientRepositoryProvider,
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
    return provideSyncManager(patientRepositoryProvider.get(), examRepositoryProvider.get(), authRepositoryProvider.get(), syncMetadataDaoProvider.get(), contextProvider.get());
  }

  public static RepositoryModule_ProvideSyncManagerFactory create(
      Provider<PatientRepository> patientRepositoryProvider,
      Provider<ExamRepository> examRepositoryProvider,
      Provider<AuthRepository> authRepositoryProvider,
      Provider<SyncMetadataDao> syncMetadataDaoProvider, Provider<Context> contextProvider) {
    return new RepositoryModule_ProvideSyncManagerFactory(patientRepositoryProvider, examRepositoryProvider, authRepositoryProvider, syncMetadataDaoProvider, contextProvider);
  }

  public static SyncManager provideSyncManager(PatientRepository patientRepository,
      ExamRepository examRepository, AuthRepository authRepository, SyncMetadataDao syncMetadataDao,
      Context context) {
    return Preconditions.checkNotNullFromProvides(RepositoryModule.INSTANCE.provideSyncManager(patientRepository, examRepository, authRepository, syncMetadataDao, context));
  }
}

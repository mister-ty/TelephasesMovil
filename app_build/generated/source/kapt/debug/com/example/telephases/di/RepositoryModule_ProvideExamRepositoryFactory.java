package com.example.telephases.di;

import android.content.Context;
import com.example.telephases.data.local.dao.ExamDao;
import com.example.telephases.data.local.dao.PatientDao;
import com.example.telephases.data.local.dao.SyncMetadataDao;
import com.example.telephases.data.local.dao.TipoExamenDao;
import com.example.telephases.data.repository.ExamRepository;
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
public final class RepositoryModule_ProvideExamRepositoryFactory implements Factory<ExamRepository> {
  private final Provider<ExamDao> examDaoProvider;

  private final Provider<PatientDao> patientDaoProvider;

  private final Provider<SyncMetadataDao> syncMetadataDaoProvider;

  private final Provider<TipoExamenDao> tipoExamenDaoProvider;

  private final Provider<ApiInterface> apiServiceProvider;

  private final Provider<Context> contextProvider;

  public RepositoryModule_ProvideExamRepositoryFactory(Provider<ExamDao> examDaoProvider,
      Provider<PatientDao> patientDaoProvider, Provider<SyncMetadataDao> syncMetadataDaoProvider,
      Provider<TipoExamenDao> tipoExamenDaoProvider, Provider<ApiInterface> apiServiceProvider,
      Provider<Context> contextProvider) {
    this.examDaoProvider = examDaoProvider;
    this.patientDaoProvider = patientDaoProvider;
    this.syncMetadataDaoProvider = syncMetadataDaoProvider;
    this.tipoExamenDaoProvider = tipoExamenDaoProvider;
    this.apiServiceProvider = apiServiceProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public ExamRepository get() {
    return provideExamRepository(examDaoProvider.get(), patientDaoProvider.get(), syncMetadataDaoProvider.get(), tipoExamenDaoProvider.get(), apiServiceProvider.get(), contextProvider.get());
  }

  public static RepositoryModule_ProvideExamRepositoryFactory create(
      Provider<ExamDao> examDaoProvider, Provider<PatientDao> patientDaoProvider,
      Provider<SyncMetadataDao> syncMetadataDaoProvider,
      Provider<TipoExamenDao> tipoExamenDaoProvider, Provider<ApiInterface> apiServiceProvider,
      Provider<Context> contextProvider) {
    return new RepositoryModule_ProvideExamRepositoryFactory(examDaoProvider, patientDaoProvider, syncMetadataDaoProvider, tipoExamenDaoProvider, apiServiceProvider, contextProvider);
  }

  public static ExamRepository provideExamRepository(ExamDao examDao, PatientDao patientDao,
      SyncMetadataDao syncMetadataDao, TipoExamenDao tipoExamenDao, ApiInterface apiService,
      Context context) {
    return Preconditions.checkNotNullFromProvides(RepositoryModule.INSTANCE.provideExamRepository(examDao, patientDao, syncMetadataDao, tipoExamenDao, apiService, context));
  }
}

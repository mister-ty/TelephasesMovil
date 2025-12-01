package com.example.telephases.di;

import android.content.Context;
import com.example.telephases.data.repository.SyncManager;
import com.example.telephases.workers.WorkScheduler;
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
public final class WorkModule_ProvideWorkSchedulerFactory implements Factory<WorkScheduler> {
  private final Provider<Context> contextProvider;

  private final Provider<SyncManager> syncManagerProvider;

  public WorkModule_ProvideWorkSchedulerFactory(Provider<Context> contextProvider,
      Provider<SyncManager> syncManagerProvider) {
    this.contextProvider = contextProvider;
    this.syncManagerProvider = syncManagerProvider;
  }

  @Override
  public WorkScheduler get() {
    return provideWorkScheduler(contextProvider.get(), syncManagerProvider.get());
  }

  public static WorkModule_ProvideWorkSchedulerFactory create(Provider<Context> contextProvider,
      Provider<SyncManager> syncManagerProvider) {
    return new WorkModule_ProvideWorkSchedulerFactory(contextProvider, syncManagerProvider);
  }

  public static WorkScheduler provideWorkScheduler(Context context, SyncManager syncManager) {
    return Preconditions.checkNotNullFromProvides(WorkModule.INSTANCE.provideWorkScheduler(context, syncManager));
  }
}

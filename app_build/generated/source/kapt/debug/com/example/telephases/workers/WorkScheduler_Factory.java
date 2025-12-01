package com.example.telephases.workers;

import android.content.Context;
import com.example.telephases.data.repository.SyncManager;
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
public final class WorkScheduler_Factory implements Factory<WorkScheduler> {
  private final Provider<Context> contextProvider;

  private final Provider<SyncManager> syncManagerProvider;

  public WorkScheduler_Factory(Provider<Context> contextProvider,
      Provider<SyncManager> syncManagerProvider) {
    this.contextProvider = contextProvider;
    this.syncManagerProvider = syncManagerProvider;
  }

  @Override
  public WorkScheduler get() {
    return newInstance(contextProvider.get(), syncManagerProvider.get());
  }

  public static WorkScheduler_Factory create(Provider<Context> contextProvider,
      Provider<SyncManager> syncManagerProvider) {
    return new WorkScheduler_Factory(contextProvider, syncManagerProvider);
  }

  public static WorkScheduler newInstance(Context context, SyncManager syncManager) {
    return new WorkScheduler(context, syncManager);
  }
}

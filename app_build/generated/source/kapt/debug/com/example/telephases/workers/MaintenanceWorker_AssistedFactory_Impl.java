package com.example.telephases.workers;

import android.content.Context;
import androidx.work.WorkerParameters;
import dagger.internal.DaggerGenerated;
import dagger.internal.InstanceFactory;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class MaintenanceWorker_AssistedFactory_Impl implements MaintenanceWorker_AssistedFactory {
  private final MaintenanceWorker_Factory delegateFactory;

  MaintenanceWorker_AssistedFactory_Impl(MaintenanceWorker_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public MaintenanceWorker create(Context arg0, WorkerParameters arg1) {
    return delegateFactory.get(arg0, arg1);
  }

  public static Provider<MaintenanceWorker_AssistedFactory> create(
      MaintenanceWorker_Factory delegateFactory) {
    return InstanceFactory.create(new MaintenanceWorker_AssistedFactory_Impl(delegateFactory));
  }
}

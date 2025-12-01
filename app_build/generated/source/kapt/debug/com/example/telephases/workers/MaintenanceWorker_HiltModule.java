package com.example.telephases.workers;

import androidx.hilt.work.WorkerAssistedFactory;
import androidx.work.ListenableWorker;
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.codegen.OriginatingElement;
import dagger.hilt.components.SingletonComponent;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import javax.annotation.processing.Generated;

@Generated("androidx.hilt.AndroidXHiltProcessor")
@Module
@InstallIn(SingletonComponent.class)
@OriginatingElement(
    topLevelClass = MaintenanceWorker.class
)
public interface MaintenanceWorker_HiltModule {
  @Binds
  @IntoMap
  @StringKey("com.example.telephases.workers.MaintenanceWorker")
  WorkerAssistedFactory<? extends ListenableWorker> bind(MaintenanceWorker_AssistedFactory factory);
}

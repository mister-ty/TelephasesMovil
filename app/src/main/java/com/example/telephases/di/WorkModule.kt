package com.example.telephases.di

import android.content.Context
import com.example.telephases.data.repository.SyncManager
import com.example.telephases.utils.NetworkConnectivityManager
import com.example.telephases.workers.WorkScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo Hilt para WorkManager y utilidades
 */
@Module
@InstallIn(SingletonComponent::class)
object WorkModule {

    /**
     * Proporciona WorkScheduler
     */
    @Provides
    @Singleton
    fun provideWorkScheduler(
        @ApplicationContext context: Context,
        syncManager: SyncManager
    ): WorkScheduler {
        return WorkScheduler(context, syncManager)
    }

    /**
     * Proporciona NetworkConnectivityManager
     */
    @Provides
    @Singleton
    fun provideNetworkConnectivityManager(
        @ApplicationContext context: Context,
        syncManager: SyncManager,
        workScheduler: WorkScheduler
    ): NetworkConnectivityManager {
        return NetworkConnectivityManager(context, syncManager, workScheduler)
    }
}



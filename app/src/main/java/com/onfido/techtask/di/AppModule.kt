package com.onfido.techtask.di

import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun provideResources(@ApplicationContext context: Context): Resources = context.resources

    @IoScheduler
    @Provides
    fun provideIoScheduler(): Scheduler = Schedulers.io()

    @MainScheduler
    @Provides
    fun provideMainScheduler(): Scheduler = AndroidSchedulers.mainThread()
}
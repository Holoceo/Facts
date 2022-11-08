package com.onfido.techtask.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class IoScheduler

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MainScheduler
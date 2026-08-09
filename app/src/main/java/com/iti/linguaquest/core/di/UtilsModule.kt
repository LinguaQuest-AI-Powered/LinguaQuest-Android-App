package com.iti.linguaquest.core.di

import com.iti.linguaquest.core.utils.FileHelper
import com.iti.linguaquest.core.utils.FileHelperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UtilsModule {

    @Binds
    abstract fun bindFileHelper(
        fileHelperImpl: FileHelperImpl
    ): FileHelper
}

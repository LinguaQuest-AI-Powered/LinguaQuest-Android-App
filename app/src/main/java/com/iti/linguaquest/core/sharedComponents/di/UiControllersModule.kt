package com.iti.linguaquest.core.sharedComponents.di



import com.iti.linguaquest.core.sharedComponents.dialog.DialogController
import com.iti.linguaquest.core.sharedComponents.dialog.DialogControllerImpl
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarControllerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UiControllersModule {

    @Binds
    @Singleton
    abstract fun bindSnackbarController(impl: SnackbarControllerImpl): SnackbarController

    @Binds
    @Singleton
    abstract fun bindDialogController(impl: DialogControllerImpl): DialogController
}
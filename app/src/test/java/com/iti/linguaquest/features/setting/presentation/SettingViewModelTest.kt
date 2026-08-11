package com.iti.linguaquest.features.setting.presentation

import app.cash.turbine.test
import com.iti.linguaquest.R
import com.iti.linguaquest.core.connectivity.domain.ObserveNetworkStatusUseCase
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.core.language.domain.usecase.GetSupportedLanguagesUseCase
import com.iti.linguaquest.features.auth.domain.usecase.LogoutUserUseCase
import com.iti.linguaquest.features.home.domain.model.LanguageOption
import com.iti.linguaquest.features.setting.domain.usecase.CancelReminderUseCase
import com.iti.linguaquest.features.setting.domain.usecase.ChangeAppLanguageUseCase
import com.iti.linguaquest.features.setting.domain.usecase.ChangeAppThemeUseCase
import com.iti.linguaquest.features.setting.domain.usecase.GetAppLanguageUseCase
import com.iti.linguaquest.features.setting.domain.usecase.GetAppThemeUseCase
import com.iti.linguaquest.features.setting.domain.usecase.GetNotificationsEnabledUseCase
import com.iti.linguaquest.features.setting.domain.usecase.GetReminderDaysUseCase
import com.iti.linguaquest.features.setting.domain.usecase.GetReminderEnabledUseCase
import com.iti.linguaquest.features.setting.domain.usecase.GetReminderTimeUseCase
import com.iti.linguaquest.features.setting.domain.usecase.GetSoundEnabledUseCase
import com.iti.linguaquest.features.setting.domain.usecase.SaveReminderDaysUseCase
import com.iti.linguaquest.features.setting.domain.usecase.SaveReminderEnabledUseCase
import com.iti.linguaquest.features.setting.domain.usecase.SaveReminderTimeUseCase
import com.iti.linguaquest.features.setting.domain.usecase.ScheduleReminderUseCase
import com.iti.linguaquest.features.setting.domain.usecase.ToggleNotificationsUseCase
import com.iti.linguaquest.features.setting.domain.usecase.ToggleSoundUseCase
import com.iti.linguaquest.features.setting.presentation.contract.ReminderEffect
import com.iti.linguaquest.features.setting.presentation.contract.ReminderIntent
import com.iti.linguaquest.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.DayOfWeek

class SettingViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getAppLanguageUseCase: GetAppLanguageUseCase = mockk()
    private val getAppThemeUseCase: GetAppThemeUseCase = mockk()
    private val getSoundEnabledUseCase: GetSoundEnabledUseCase = mockk()
    private val getNotificationsEnabledUseCase: GetNotificationsEnabledUseCase = mockk()
    private val changeAppLanguageUseCase: ChangeAppLanguageUseCase = mockk(relaxed = true)
    private val changeAppThemeUseCase: ChangeAppThemeUseCase = mockk(relaxed = true)
    private val toggleSoundUseCase: ToggleSoundUseCase = mockk(relaxed = true)
    private val toggleNotificationsUseCase: ToggleNotificationsUseCase = mockk(relaxed = true)
    private val logoutUserUseCase: LogoutUserUseCase = mockk(relaxed = true)
    private val getSupportedLanguagesUseCase: GetSupportedLanguagesUseCase = mockk()
    private val getReminderEnabledUseCase: GetReminderEnabledUseCase = mockk()
    private val getReminderTimeUseCase: GetReminderTimeUseCase = mockk()
    private val getReminderDaysUseCase: GetReminderDaysUseCase = mockk()
    private val saveReminderEnabledUseCase: SaveReminderEnabledUseCase = mockk(relaxed = true)
    private val saveReminderTimeUseCase: SaveReminderTimeUseCase = mockk(relaxed = true)
    private val saveReminderDaysUseCase: SaveReminderDaysUseCase = mockk(relaxed = true)
    private val scheduleReminderUseCase: ScheduleReminderUseCase = mockk(relaxed = true)
    private val cancelReminderUseCase: CancelReminderUseCase = mockk(relaxed = true)
    private val observeNetworkStatusUseCase: ObserveNetworkStatusUseCase = mockk()
    private val snackbarController: SnackbarController = mockk(relaxed = true)

    private lateinit var viewModel: SettingViewModel

    @Before
    fun setUp() {
        // Given
        every { observeNetworkStatusUseCase() } returns flowOf(true)
        coEvery { getSupportedLanguagesUseCase() } returns LinguaQuestResult.Success(emptyList())
        every { getAppLanguageUseCase() } returns flowOf("en")
        every { getAppThemeUseCase() } returns flowOf("system")
        every { getSoundEnabledUseCase() } returns flowOf(true)
        every { getNotificationsEnabledUseCase() } returns flowOf(true)
        every { getReminderEnabledUseCase() } returns flowOf(false)
        every { getReminderTimeUseCase() } returns flowOf("10:00")
        every { getReminderDaysUseCase() } returns flowOf("1,2,3,4,5")

        viewModel = SettingViewModel(
            getAppLanguageUseCase,
            getAppThemeUseCase,
            getSoundEnabledUseCase,
            getNotificationsEnabledUseCase,
            changeAppLanguageUseCase,
            changeAppThemeUseCase,
            toggleSoundUseCase,
            toggleNotificationsUseCase,
            logoutUserUseCase,
            getSupportedLanguagesUseCase,
            getReminderEnabledUseCase,
            getReminderTimeUseCase,
            getReminderDaysUseCase,
            saveReminderEnabledUseCase,
            saveReminderTimeUseCase,
            saveReminderDaysUseCase,
            scheduleReminderUseCase,
            cancelReminderUseCase,
            observeNetworkStatusUseCase,
            snackbarController
        )
    }

    @Test
    fun onReminderIntent_emitsReminderEnabledAndSuccessSnackbar_whenToggledOn() = runTest {
        // Given
        val intent = ReminderIntent.ToggleReminder(enabled = true)

        viewModel.reminderEffect.test {
            // When
            viewModel.onReminderIntent(intent)

            // Then
            val effect = awaitItem()
            assertEquals(ReminderEffect.ReminderEnabled, effect)
            assertTrue(viewModel.reminderState.value.enabled)

            coVerify { saveReminderEnabledUseCase(true) }
            coVerify {
                snackbarController.sendEvent(
                    match {
                        it.type == SnackbarType.SUCCESS &&
                        (it.message as UiText.StringResource).resId == R.string.reminder_turned_on
                    }
                )
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onReminderIntent_emitsReminderDisabledAndInfoSnackbar_whenToggledOff() = runTest {
        // Given
        val intent = ReminderIntent.ToggleReminder(enabled = false)

        viewModel.reminderEffect.test {
            // When
            viewModel.onReminderIntent(intent)

            // Then
            val effect = awaitItem()
            assertEquals(ReminderEffect.ReminderDisabled, effect)
            assertFalse(viewModel.reminderState.value.enabled)

            coVerify { saveReminderEnabledUseCase(false) }
            coVerify {
                snackbarController.sendEvent(
                    match {
                        it.type == SnackbarType.INFO &&
                        (it.message as UiText.StringResource).resId == R.string.reminder_turned_off
                    }
                )
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onReminderIntent_updatesTimeStateAndShowsSnackbar_whenTimeSelected() = runTest {
        // Given
        val intent = ReminderIntent.SelectTime(hour = 8, minute = 30)

        viewModel.reminderEffect.test {
            // When
            viewModel.onReminderIntent(intent)

            // Then
            val effect = awaitItem()
            assertEquals(ReminderEffect.ReminderUpdated, effect)
            assertEquals(8, viewModel.reminderState.value.hour)
            assertEquals(30, viewModel.reminderState.value.minute)
            assertFalse(viewModel.reminderState.value.showTimePicker)

            coVerify { saveReminderTimeUseCase("08:30") }
            coVerify {
                snackbarController.sendEvent(
                    match {
                        it.type == SnackbarType.SUCCESS &&
                        (it.message as UiText.StringResource).resId == R.string.reminder_time_updated
                    }
                )
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun toggleNotifications_cancelsReminderAndShowsInfoSnackbar_whenDisabled() = runTest {
        // Given
        val notificationsEnabled = false

        // When
        viewModel.toggleNotifications(notificationsEnabled)

        // Then
        coVerify { toggleNotificationsUseCase(false) }
        coVerify { cancelReminderUseCase() }
        coVerify {
            snackbarController.sendEvent(
                match {
                    it.type == SnackbarType.INFO &&
                    (it.message as UiText.StringResource).resId == R.string.notifications_off_msg
                }
            )
        }
    }

    @Test
    fun logout_callsLogoutUseCase_andExecutesCallback() = runTest {
        // Given
        var callbackExecuted = false
        val onSuccess = { callbackExecuted = true }

        // When
        viewModel.logout(onSuccess)

        // Then
        coVerify { logoutUserUseCase() }
        assertTrue(callbackExecuted)
    }

    @Test
    fun onReminderIntent_updatesState_whenShowTimePicker() = runTest {
        // Given
        val intent = ReminderIntent.ShowTimePicker

        // When
        viewModel.onReminderIntent(intent)

        // Then
        assertTrue(viewModel.reminderState.value.showTimePicker)
    }

    @Test
    fun onReminderIntent_updatesState_whenDismissTimePicker() = runTest {
        // Given
        val intent = ReminderIntent.DismissTimePicker

        // When
        viewModel.onReminderIntent(intent)

        // Then
        assertFalse(viewModel.reminderState.value.showTimePicker)
    }

    @Test
    fun onReminderIntent_updatesState_whenShowRepeatSheet() = runTest {
        // Given
        val intent = ReminderIntent.ShowRepeatSheet

        // When
        viewModel.onReminderIntent(intent)

        // Then
        assertTrue(viewModel.reminderState.value.showRepeatSheet)
    }

    @Test
    fun onReminderIntent_updatesState_whenDismissRepeatSheet() = runTest {
        // Given
        val intent = ReminderIntent.DismissRepeatSheet

        // When
        viewModel.onReminderIntent(intent)

        // Then
        assertFalse(viewModel.reminderState.value.showRepeatSheet)
    }

    @Test
    fun onReminderIntent_togglesDay_whenToggleDay() = runTest {
        // Given
        val initialDays = viewModel.reminderState.value.selectedDays
        val dayToToggle = DayOfWeek.MONDAY
        val intent = ReminderIntent.ToggleDay(dayToToggle)
        val initiallyContains = initialDays.contains(dayToToggle)

        // When
        viewModel.onReminderIntent(intent)

        // Then
        val currentDays = viewModel.reminderState.value.selectedDays
        if (initiallyContains) {
            assertFalse(currentDays.contains(dayToToggle))
        } else {
            assertTrue(currentDays.contains(dayToToggle))
        }
    }

    @Test
    fun changeAppLanguage_callsUseCaseAndEmitsEvent() = runTest {
        // Given
        val language = LanguageOption(id = 1, code = "fr", name = "French", imageUrl = "", isAdded = false)

        viewModel.languageChanged.test {
            // When
            viewModel.changeAppLanguage(language)

            // Then
            coVerify { changeAppLanguageUseCase(language.id, language.code, language.name) }
            val effect = awaitItem()
            assertEquals(Unit, effect)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun changeAppTheme_callsUseCase() = runTest {
        // Given
        val theme = "dark"

        // When
        viewModel.changeAppTheme(theme)

        // Then
        coVerify { changeAppThemeUseCase(theme) }
    }

    @Test
    fun toggleSound_callsUseCase() = runTest {
        // Given
        val soundEnabled = false

        // When
        viewModel.toggleSound(soundEnabled)

        // Then
        coVerify { toggleSoundUseCase(false) }
    }
}

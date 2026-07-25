package com.iti.linguaquest.features.game.presentation.level.contract

sealed interface LevelIntent {
    data object BackClicked : LevelIntent
    data object MascotTapped : LevelIntent
    data object DismissBottomSheet : LevelIntent
    data object OpenCameraClicked : LevelIntent
    data object ChangeWordClicked : LevelIntent
    data object ConfirmChangeWordClicked : LevelIntent
    data object CancelChangeWordClicked : LevelIntent
    data object SkipClicked : LevelIntent
    data object RevealFirstLetterClicked : LevelIntent
    data object ShowCategoryClueClicked : LevelIntent
    data object SoundClicked : LevelIntent
}

# Authentication Unit Testing Documentation

## Overview
This document describes the unit testing suite created for the **Authentication** feature (login, registration, Google OAuth, session management, OTP verification, forget password, and new password setup). The tests cover the Data layer (`AuthRemoteDataSourceImpl`, `AuthRepositoryImpl`), Domain layer (all auth UseCases), and Presentation layer (`LoginViewModel`, `SignUpViewModel`, `OTPViewModel`, `ForgetPasswordViewModel`, `NewPasswordViewModel`).

## Architecture & Testing Coverage

### 1. Data Layer (`AuthRemoteDataSourceImplTest` & `AuthRepositoryImplTest`)
- **Remote Data Source**: Verifies that calling `AuthRemoteDataSource` maps to the Retrofit `AuthApiService` endpoints and propagates successes or exceptions (e.g., `UnknownHostException` mapping to `LinguaQuestDataError.Remote.UNKNOWN`).
- **Repository Token & Preference Handling**: Tests storing tokens dynamically in `TokensLocalDataSource` and checking user ID changes to clear local database caching/onboarding dependencies when a new user logs in.
- **Session Events & Logout Lifecycle**: Verifies clearing tokens and Datastore preferences upon `logout()`, notifying the application via `SessionEventBus` with `SessionEvent.LoggedOut`.

### 2. Domain Layer (Use Cases)
- **Login / Register / OAuth**: Verifies credentials delegation and result mapping (`LoginUserUseCaseTest`, `RegisterUserUseCaseTest`, `SignInWithGoogleUseCaseTest`, `CompleteOAuthProfileUseCaseTest`).
- **OTP Operations**: Tests OTP request sending and code validation responses (`SendRegistrationOtpUseCaseTest`, `SendPasswordResetOtpUseCaseTest`, `VerifyEmailOtpUseCaseTest`, `VerifyPasswordResetOtpUseCaseTest`).
- **Logout & Session Checks**: Verifies device token unregistration timeout safety and session state flows (`LogoutUserUseCaseTest`, `CheckUserLoggedInUseCaseTest`, `SyncUserNativeLanguageUseCaseTest`).

### 3. Presentation Layer (ViewModels)
- **Login & SignUp**: Mock-tests `ValidationUtils` validations on the JVM, credential login/signup lifecycles, MVI states, Google Sign-In paths, and error animations (`LoginViewModelTest`, `SignUpViewModelTest`).
- **OTP Verification**: Tests verification initialization, OTP timer ticks, resend constraints, and screen flows (`OTPViewModelTest`).
- **Forget & Reset Password**: Verifies email/password structures, match checking, token resets, and error snackbars (`ForgetPasswordViewModelTest`, `NewPasswordViewModelTest`).

## Verification
All authentication tests compile and run successfully via Gradle:
```bash
./gradlew testDebugUnitTest --tests "com.iti.linguaquest.features.auth.*"
```

# FCM Device Registration & Unregistration Feature

## High-Level Overview
The FCM Device Registration & Unregistration service manages Firebase Cloud Messaging push notification tokens across authenticated user lifecycle events. The architecture is cleanly partitioned within the core layer (`com.iti.linguaquest.core.notification`) as a shared, reusable cross-cutting feature across auth and settings modules.

To ensure strict Clean Architecture isolation:
- Token retrieval (`FirebaseMessaging.getInstance().token.await()`) is encapsulated strictly within the Data layer (`FcmTokenManager`), with potential runtime exceptions caught and logged cleanly using **Timber** (`Timber.e`).
- Single-shot token registration and unregistration operations utilize lightweight standard `suspend` functions (`RegisterDeviceTokenUseCase` and `UnregisterDeviceTokenUseCase`) rather than continuous reactive Flows, avoiding unnecessary reactive scaffolding for single-shot RPC calls.

## Domain-Layer Orchestration (UseCases)
Device registration and unregistration occur seamlessly during user authentication and session termination without presentation-layer (ViewModel) intervention or coroutine scope leaks:
- **Login Orchestration**: Triggered inside domain use cases (`LoginUserUseCase`, `SignInWithGoogleUseCase`, and `CompleteOAuthProfileUseCase`) immediately upon successful authentication or OAuth language profile completion.
- **Logout Orchestration**: Triggered inside `LogoutUserUseCase` prior to clearing credentials from `AuthRepository`.
- **Timeout & Failure Protection**: Every invocation of token registration or unregistration within Auth UseCases is safeguarded with a 3,000ms timeout (`withTimeoutOrNull(3_000L)`) and wrapped in a try-catch block logging via Timber. Whether network operations succeed, stall, or fail, local session transitions are guaranteed to execute safely without blocking the user.

## State Management (MVI / ViewModel Integration)
ViewModels are completely decoupled from FCM token dependencies and long-lived coroutine scopes:
- **`LoginViewModel`**: Remains purely focused on user intent processing and UI state management without injecting `applicationScope` or `RegisterDeviceTokenUseCase`.
- **`SettingViewModel`**: Simplifies logout processing by directly calling `logoutUserUseCase()`, delegating all session and device token cleanup to the domain layer.

## Navigation 3 Keys Used
This core service does not directly alter back stack state or register new standalone destination `NavKey` entries in `Screens.kt`. It reacts seamlessly to standard authentication transitions handled by:
- `AuthScreen.Login`
- `AuthScreen.OTP`
- `AuthScreen.OAuthLanguageSelection`
- `SettingScreen`

## Shared Components & Extensions Created
- **`FcmTokenProvider` & `FcmTokenManager`**: Abstracted asynchronous wrapper around Firebase SDK token extraction utilizing coroutines (`await()`) and Timber exception logging.
- **`NotificationRepository` & `NotificationRepositoryImpl`**: Suspend-backed implementation invoking remote API endpoints (`POST /devices` and `DELETE /devices`) wrapped inside standard `safeApiCall` execution blocks.
- **`NotificationApiService`**: Retrofit contract utilizing explicit HTTP method declarations (`@POST` and `@HTTP(method = "DELETE", path = "devices", hasBody = true)`).

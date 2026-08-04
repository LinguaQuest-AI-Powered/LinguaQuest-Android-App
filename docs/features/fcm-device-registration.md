# FCM Device Registration & Unregistration Feature

## High-Level Overview
The FCM Device Registration & Unregistration service manages Firebase Cloud Messaging push notification tokens across authenticated user lifecycle events. The architecture is cleanly partitioned within the core layer (`com.iti.linguaquest.core.notification`) as a shared, reusable cross-cutting feature across auth and settings modules.

To ensure strict Clean Architecture isolation:
- Token retrieval (`FirebaseMessaging.getInstance().token.await()`) is encapsulated strictly within the Data layer (`FcmTokenManager`).
- UseCases operate completely independent of SDK specifics and token delivery mechanisms, relying purely on domain abstractions (`NotificationRepository`).

## State Management (MVI / ViewModel Integration)
Device registration status operates asynchronously without intruding on UI rendering or interrupting primary screen navigation states:
- **Login Flow (`LoginViewModel`)**: Triggered immediately upon successful user authentication (Email/Password login, Google Sign-In with complete profile, or completion of OAuth language selection). Calls `RegisterDeviceTokenUseCase` asynchronously inside `applicationScope` (fire-and-forget), preventing cancellation on navigation away from the screen.
- **Logout Flow (`SettingViewModel`)**: Triggered prior to clearing user credentials during logout. Invocation of `UnregisterDeviceTokenUseCase` is safeguarded with a 3,000ms timeout (`withTimeoutOrNull`) and coroutine exception handling (`catch` block). Whether network operations succeed, fail, or stall, local user session termination (`logoutUserUseCase()`) always executes safely without blocking the user.

## Navigation 3 Keys Used
This core service does not directly alter back stack state or register new standalone destination `NavKey` entries in `Screens.kt`. It reacts seamlessly to standard authentication transitions handled by:
- `AuthScreen.Login`
- `AuthScreen.OTP`
- `AuthScreen.OAuthLanguageSelection`
- `SettingScreen`

## Shared Components & Extensions Created
- **`FcmTokenProvider` & `FcmTokenManager`**: Abstracted asynchronous wrapper around Firebase SDK token extraction utilizing coroutines (`await()`) and Timber exception logging.
- **`NotificationRepository` & `NotificationRepositoryImpl`**: Flow-backed reactive implementation invoking remote API endpoints (`POST /devices` and `DELETE /devices`) wrapped inside standard `safeApiCall` execution blocks.
- **`NotificationApiService`**: Retrofit contract utilizing explicit HTTP method declarations (`@POST` and `@HTTP(method = "DELETE", path = "devices", hasBody = true)`).

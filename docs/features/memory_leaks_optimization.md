# Memory Leaks & Memory Optimization

This document tracks identified memory leaks and their resolutions across the LinguaQuest project. By actively using tools like **LeakCanary** and adhering to Clean Architecture principles, we ensure the app remains performant and avoids OutOfMemory (OOM) errors.

---

## 1. TextToSpeech Context Leak
**Observation:**
*   LeakCanary detected that the `TextToSpeech` engine was holding onto a destroyed Activity context.
*   When a feature initialized the TTS engine using `this` (the Activity or Fragment context), the internal Android TTS service retained a strong reference to that screen even after the user navigated away.

**Solution: Global Application Context**
*   We modified the TTS initialization to pass the `applicationContext` instead. 
*   Since the `TextToSpeech` engine is a system service that lives across multiple screens, binding it to the global Application (which is never destroyed during the app lifecycle) entirely eliminated the leak.

---

## 2. CredentialManager Context Leak (`GoogleSignInLauncher.kt`)
**Observation:**
*   A memory leak trace flagged `android.credentials.CredentialManager$GetCredentialTransport` for leaking the `MainActivity` context.
*   In `GoogleSignInLauncher.kt`, the `CredentialManager` was initialized via `CredentialManager.create(this@launchGoogleSignIn)`, providing the Activity context.
*   This is a known quirk in early versions of the `androidx.credentials` library where the internal transport manager permanently caches the context it was created with, preventing the Activity from being garbage collected.

**Solution: Global Application Context**
*   We changed the initialization to:
    ```kotlin
    val credentialManager = CredentialManager.create(this@launchGoogleSignIn.applicationContext)
    ```
*   The `CredentialManager` only requires a base context to initialize its background services, so passing the `applicationContext` is perfectly safe. 
*   We still pass the Activity context dynamically into the actual `getCredential(...)` request when prompting the user (because it requires UI), but the core manager itself no longer retains a leaked reference.

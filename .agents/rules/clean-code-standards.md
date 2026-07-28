---
trigger: always_on
---

---

description: Enforces clean code standards, including a strict ban on boilerplate comments and an absolute prohibition of fully qualified inline package names.
mode: always
---

# Clean Code & Styling Standards

## 1. No Code Comments

* **Self-Documenting Code:** Write clear, descriptive variable and function names instead of relying on comments.
* **No Boilerplate Comments:** Do not generate unnecessary, explanatory, or boilerplate comments (e.g., `// Initialize view model`, `// Set up UI`) in the code.
* **Exceptions:** Only write comments if explicitly requested by the user or if explaining a highly complex, non-standard workaround (which should be rare).

## 2. Strict Import Conventions (ZERO Fully Qualified Inline Calls)

* **Absolute Prohibition:** You are strictly prohibited from using fully qualified package names inline for *any* class, function, or property. This applies universally to project components, intents, contracts, AndroidX, Compose, and standard library classes.
* **Explicit Imports Only:** Always add an explicit `import` statement at the top of the Kotlin file and reference the class directly.
* **Import Aliases:** If an import conflict occurs (e.g., two classes with the exact same name), use a Kotlin import alias (`import package.Name as OtherName`) rather than resorting to an inline fully qualified path.

### Import Example

❌ **Incorrect (Never do this):**

```kotlin
val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
androidx.compose.runtime.DisposableEffect(lifecycleOwner) {
    val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
        if (event == androidx.lifecycle.Lifecycle.Event.ON_RESUME) {
            viewModel.onIntent(com.iti.linguaquest.features.home.presentation.contract.HomeIntent.LoadHome)
        }
    }
    lifecycleOwner.lifecycle.addObserver(observer)
    onDispose {
        lifecycleOwner.lifecycle.removeObserver(observer)
    }
}
```

✅ **Correct (Always do this):**

```kotlin
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.Lifecycle
import com.iti.linguaquest.features.home.presentation.contract.HomeIntent

// ... inside composable ...
val lifecycleOwner = LocalLifecycleOwner.current
DisposableEffect(lifecycleOwner) {
    val observer = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            viewModel.onIntent(HomeIntent.LoadHome)
        }
    }
    lifecycleOwner.lifecycle.addObserver(observer)
    onDispose {
        lifecycleOwner.lifecycle.removeObserver(observer)
    }
}
```

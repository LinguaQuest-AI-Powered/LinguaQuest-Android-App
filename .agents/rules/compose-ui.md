---
trigger: always_on
---

---
description: This rule enforces LinguaQuest's Jetpack Compose UI standards, including state hoisting, strict component naming, theme enforcement, and string resource management. It must be applied whenever creating or modifying Compose UI components.
mode: always
---

# LinguaQuest Jetpack Compose UI Rules

## 1. State Hoisting & Component Naming (Strict)
- **Stateful Components**: Top-level wrappers that instantiate Hilt ViewModels, collect state, and manage effects must always end in **`Screen`** (e.g., `LoginScreen`, `HomeScreen`)[cite: 1, 2].
- **Stateless Components**: Pure UI functions that only accept raw data, modifiers, and lambda callbacks must always end in **`Content`**, **`Card`**, or **`View`** (e.g., `LoginContent`, `LanguageProgressCard`)[cite: 1, 2].
- **Previews**: `@Preview` annotations are heavily utilized but must strictly be restricted to stateless components to prevent mocking complex ViewModels[cite: 1].

## 2. Modifiers
- Every Composable must accept `modifier: Modifier = Modifier` as its first optional parameter[cite: 1].

## 3. Theming & Design System
- **No Hardcoded Colors**: Never use hardcoded hex values or raw `Color()` objects.
- **Base Material Tokens**: Always use `MaterialTheme.colorScheme` and `MaterialTheme.typography` for standard design tokens[cite: 3].
- **Custom Tokens**: For highly specific app colors (e.g., `LeaderboardGold`), you must access the custom local composition via `LinguaQuestTheme.colors`[cite: 3].

## 4. UI Strings & Resources
- **No String Literals**: String literals are strictly prohibited in Compose UI code for user-facing text[cite: 2].
- **Compose Layer**: Always resolve text via `stringResource(id = R.string.x)`[cite: 2].
- **ViewModel Layer**: When the ViewModel needs to pass text or error messages to the UI, it must use the custom wrapper class `UiText` (e.g., `UiText.StringResource(R.string.x)`)[cite: 2]. ViewModels must remain pure and never use the Android `Context` to resolve strings[cite: 2].
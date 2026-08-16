# Button Design Unification

## Overview
This document outlines the project-wide button design unification in LinguaQuest. To maintain consistent aesthetics and tactile user interaction across all screens, all action buttons have been unified under the shared 3D button system provided in `com.iti.linguaquest.core.sharedComponents`.

## Design Specifications

All standard buttons in LinguaQuest conform to the following specifications:
- **Corner Radius**: `16.dp` (`RoundedCornerShape(16.dp)`)
- **Button Height**: `52.dp`
- **Ledge Height (3D Shadow)**: `6.dp`
- **Tactile Animation**: Smooth translation down `6.dp` when pressed with animated physics (`animateDpAsState`).
- **Core Components**:
  - `AppButton3D`: Single unified tactile action button supporting variants:
    - `ButtonVariant.PRIMARY`: Solid Brand Orange (`#FF9F29`) face with deep Shadow Orange (`#C77800`) 3D base and white text.
    - `ButtonVariant.SECONDARY`: Outlined 3D button with surface background, Brand Teal (`MaterialTheme.colorScheme.tertiary` / `#006B5C`) border and text, and soft teal 3D ledge (`#006B5C.copy(0.35f)`).
    - `ButtonVariant.SOCIAL`: Clean surface button with social border for third-party auth.
  - `AppDialog`: Unified modal dialog featuring `AppButton3D` for primary and `ButtonVariant.SECONDARY` for dismiss/cancel actions.
  - *(Note: `AppOutlinedButton` has been completely deprecated and removed in favor of `AppButton3D(variant = ButtonVariant.SECONDARY)`).*

## Refactored Components

### 1. Home Feature
- **`ClaimRewardButton.kt`**:
  - Replaced legacy Material3 `Button` (which used a non-standard 28.dp pill shape) with `AppButton3D`.
  - Retains the gift icon and integrated `LingoSpinningIcon` loading state.

### 2. Lock Screen Feature
- **`LockScreenSettingsScreen.kt`**:
  - Replaced standard `AlertDialog` and raw `Button` / `OutlinedButton` with `AppDialog`.
  - Replaced feature action buttons (Enable/Retry, Disable, Test Notification) with `AppButton3D` and `AppOutlinedButton`.
- **`FullScreenIntentPermissionCard.kt`**:
  - Replaced raw 12.dp `Button` with Amber-themed `AppButton3D`.

### 3. Game Feature
- **`CameraContent.kt`**:
  - Replaced raw Material3 `Button` in permission-denied fallback with `AppButton3D`.
- **`GameWhackView.kt`**:
  - Replaced raw Material3 `Button` with `AppButton3D`.

### 4. Mind Reader Feature
- **`MindReaderAnswerButton.kt`**:
  - Replaced ad-hoc pill `RoundedCornerShape(50)` box button with `AppButton3D`, using thematic beige/brown overrides while inheriting the standard 16.dp 3D tactile button behavior.

### 5. Auth & Gallery Cleanliness
- **`AuthFooter.kt`**:
  - Cleaned up fully qualified inline package calls (`androidx.compose.material3.TextButton`, `androidx.compose.foundation.layout.PaddingValues`) with explicit top-level imports.
- **`EmptyGalleryView.kt`**:
  - Cleaned up unused legacy `Button` and `ButtonDefaults` imports.

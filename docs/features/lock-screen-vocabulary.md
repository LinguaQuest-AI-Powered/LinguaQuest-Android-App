# Lock Screen Vocabulary Notification — Screen-Off Fix

## Overview

The Lock Screen Vocabulary feature shows a notification on the device lock screen
when the user locks their phone. The notification displays a vocabulary word with
its translation, example sentence, and a "Got It" action button.

---

## Root Cause of the Bug

`SCREEN_OFF` fires the moment the display turns off, but `isKeyguardLocked` may
still return `false` for several seconds (or minutes, depending on the device's
"Lock after screen off" setting). The previous flow scheduled an alarm 5 seconds
after screen-off and checked `isKeyguardLocked` at that point — which was nearly
always `false`, causing the notification to be silently discarded and the system
to fall back to the 15-minute periodic alarm.

---

## Notification Flow

### Screen-Off Path (primary trigger)

```
SCREEN_OFF broadcast
  → VocabularyScreenOffReceiver
  → scheduleScreenOffNotification()          [20 s, isScreenOff=true, retry=0]

  VocabularyNotificationReceiver fires
  ├── isKeyguardLocked = true  → ✅ show notification (normal word flow)
  └── isKeyguardLocked = false
        ├── retry < 3 → scheduleScreenOffRetry(retry+1) [15 s]
        │     (repeats up to 3 times → max 20+15+15+15 = 65 s before giving up)
        └── retry ≥ 3 → scheduleNotificationWork()  [falls back to 15-min periodic]
```

### Periodic Path (background reminder, unchanged)

```
scheduleNotificationWork()  [15-min AlarmManager]
  → VocabularyNotificationReceiver
  → isKeyguardLocked = true  → show notification
  → isKeyguardLocked = false → reschedule 15 min
```

### Test Notification Path (unchanged)

```
TestNotificationClicked
  → testNotification(delaySeconds=5)         [forceShow=true]
  → VocabularyNotificationReceiver
  → forceShow=true → bypass isKeyguardLocked check → show notification with fallback word
```

---

## MVI / State Management

The feature uses `LockScreenSettingsViewModel` with:

- **State** — `LockScreenState`: `featureState`, `isNotificationPermissionGranted`,
  `isFullScreenIntentGranted`, `pendingCount`, etc.
- **Intent** — `LockScreenIntent`: `ToggleFeatureClicked`, `SyncNotificationPermission`,
  `SyncFullScreenIntentPermission`, `OpenFullScreenIntentSettingsClicked`, etc.
- **Effect** — `LockScreenEffect`: `RequestNotificationPermission`,
  `OpenFullScreenIntentSettings`, `PlayCoinDeductedSound`

---

## Navigation Keys

`LockScreenSettingsScreen` is reached via the Settings bottom nav tab.
No dedicated `NavKey` — it is embedded inside `SettingScreen`.

---

## Key Files

| File | Role |
|------|------|
| `VocabularyScreenOffReceiver` | Listens to `ACTION_SCREEN_OFF` (dynamic registration in `MainActivity`) |
| `VocabularyNotificationReceiver` | AlarmManager broadcast receiver — decides whether to show or retry |
| `VocabularyWorkSchedulerImpl` | Schedules AlarmManager alarms (exact or inexact depending on permission) |
| `VocabularyNotificationManager` | Builds and posts the `NotificationCompat` with `VISIBILITY_PUBLIC` and `setFullScreenIntent` |
| `LockScreenSettingsScreen` | UI — checks `USE_FULL_SCREEN_INTENT` on every `RESUMED` lifecycle event |
| `FullScreenIntentPermissionCard` | Warning card shown when `USE_FULL_SCREEN_INTENT` is not granted (Android 14+) |

---

## AlarmManager Request Codes

| Constant | Code | Purpose |
|----------|------|---------|
| `ALARM_REQUEST_CODE` | 3000 | 15-min periodic reminder |
| `IMMEDIATE_ALARM_REQUEST_CODE` | 3001 | Post-generation immediate attempt |
| `TEST_ALARM_REQUEST_CODE` | 3002 | Test notification button |
| `SCREEN_OFF_ALARM_REQUEST_CODE` | 3003 | Initial screen-off attempt (20 s delay) |
| `SCREEN_OFF_RETRY_ALARM_REQUEST_CODE` | 3004 | Screen-off retries (15 s each, max 3) |

---

## Intent Extras

| Extra | Type | Source |
|-------|------|--------|
| `EXTRA_FORCE_SHOW` | Boolean | Test notification path — bypasses `isKeyguardLocked` |
| `EXTRA_IS_SCREEN_OFF` | Boolean | Set by `scheduleScreenOffAlarm()` — triggers retry logic |
| `EXTRA_SCREEN_OFF_RETRY_COUNT` | Int | Tracks how many retries have been attempted (max 3) |

---

## Android 14+ Permission (USE_FULL_SCREEN_INTENT)

Without this permission the notification is sent but never renders on top of the
lock screen — it only appears in the notification shade after the user unlocks.

`LockScreenSettingsScreen` checks this permission on every `Lifecycle.State.RESUMED`
event and shows `FullScreenIntentPermissionCard` if not granted. Tapping "Open
Settings" fires `OpenFullScreenIntentSettingsClicked` → `LockScreenEffect.OpenFullScreenIntentSettings`
→ `Settings.ACTION_MANAGE_APP_USE_FULL_SCREEN_INTENT`.

---

## Shared Components Created

- `FullScreenIntentPermissionCard` — generic warning card in
  `lockscreen/presentation/view/component/`. Shows animated warning with a
  direct link to app settings. Disappears automatically when permission is granted.

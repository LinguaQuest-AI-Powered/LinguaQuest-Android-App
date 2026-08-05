# Feature Documentation: Notification System & Home Badge Integration

## 1. High-Level Overview
The Notification System provides LinguaQuest users with a central inbox for real-time alerts, daily vocabulary reminders, game updates, and achievement announcements. The feature is seamlessly integrated across the entire application:
- **Top App Bar Integration:** Displays a live unread badge overlay indicator on the bell icon whenever unread notifications exist.
- **Paginated Inbox:** Utilizes Jetpack Paging 3 to efficiently stream paginated notifications from the server with infinite scrolling.
- **Optimistic Interactions:** Tapping unread items immediately marks them as read both locally and on the backend. Deleting items instantly animates them out of the list without triggering jumpy list refetches.
- **Clean Architecture & MVI:** Strictly adheres to domain layer separation, custom `LinguaQuestResult` error handling, and unidirectional MVI state flow.

---

## 2. Architecture & Layer Separation

### Remote Data Layer (`core/notification/data/`)
- **`NotificationApiService`**: Retrofit service interfacing with backend endpoints:
  - `GET /notifications`: Paginated retrieval (`NotificationsPaginatedResponseDto`)
  - `GET /notifications/unread-count`: Returns `UnreadCountResponseDto`
  - `DELETE /notifications`: Clears all user notifications
  - `DELETE /notifications/{id}`: Deletes a specific notification
  - `PATCH /notifications/{id}/read`: Marks a single item as read
- **`NotificationPagingSource`**: Standard Paging 3 source extending `PagingSource<Int, Notification>`. Handles page indices, bounds checking, and converts remote network errors into domain exceptions (`NotificationPagingException`).
- **`NotificationRepositoryImpl`**: Implements domain layer repository interface, managing remote interactions and exposing paginated streams via `Pager`.

### Domain Layer (`core/notification/domain/`)
All presentation interaction is strictly brokered via atomic Use Cases:
1. `GetNotificationsUseCase`: Exposes `Flow<PagingData<Notification>>`
2. `GetUnreadNotificationCountUseCase`: Retrieves integer unread count
3. `MarkNotificationAsReadUseCase`: Updates read status
4. `DeleteNotificationUseCase`: Removes individual notification by ID
5. `DeleteAllNotificationsUseCase`: Clears all notifications

---

## 3. Presentation Layer & MVI Contract (`features/notification/`)

### MVI Contract (`presentation/contract/`)
- **`NotificationState`**: Tracks optimistic mutations and active confirmation dialogs:
  ```kotlin
  data class NotificationState(
      val deletedNotificationIds: Set<Long> = emptySet(),
      val readNotificationIds: Set<Long> = emptySet(),
      val showDeleteAllDialog: Boolean = false,
      val notificationToDelete: Long? = null,
      val isDeleting: Boolean = false
  )
  ```
- **`NotificationIntent`**: Expresses user intent cleanly (`NotificationClicked`, `DeleteNotificationClicked`, `ConfirmDeleteNotification`, `DeleteAllClicked`, `ConfirmDeleteAll`, etc.).
- **`NotificationEffect`**: Exposes one-off events such as `RefreshNotifications`. Toasts and snackbars are handled directly by injecting `SnackbarController` into the ViewModel.

### Reactive Optimistic Paging Filtering
To prevent visual jumps and eliminate redundant network refetches during single-item deletion or mark-as-read actions, `NotificationViewModel` combines the cached `PagingData` stream with local state mutations:
```kotlin
val notifications: Flow<PagingData<Notification>> = getNotificationsUseCase()
    .cachedIn(viewModelScope)
    .combine(_state.map { it.deletedNotificationIds }.distinctUntilChanged()) { pagingData, deletedIds ->
        pagingData.filter { it.id !in deletedIds }
    }
    .combine(_state.map { it.readNotificationIds }.distinctUntilChanged()) { pagingData, readIds ->
        pagingData.map { item ->
            if (item.id in readIds) item.copy(isRead = true) else item
        }
    }
```
If an API operation fails, local state mutations are seamlessly reverted and an error snackbar is dispatched via `SnackbarController`.

---

## 4. UI & Components (`presentation/view/`)

- **Stateful Wrapper (`NotificationScreen`)**: Instantiates `NotificationViewModel` via Hilt, observes state flows and lifecycle effects, and passes clean event callbacks down to child composables.
- **Stateless Content (`NotificationContent` & `NotificationCard`)**:
  - Implements infinite scrolling using `LazyColumn` and `LazyPagingItems`.
  - Integrates `ShareTopBar` with a custom trailing action button for "Delete All".
  - Displays polished empty states (`no_notifications_found`) and error recovery screens with retry capabilities.
  - Leverages reusable `AppDialog` components for delete confirmation dialogs.

---

## 5. Navigation 3 & Badge Synchronization

- **Route Definition**: Defined under `@Serializable data object Notification : RootScreen` in `Screens.kt`.
- **Top App Bar Integration**: `LinguaQuestTopAppBar` accepts `unreadCount: Int` and renders an intuitive red indicator overlay above the notifications bell icon whenever count exceeds zero.
- **Automatic Return Sync**: In `NestedNavigation.kt` (`MainScreen`), an explicit state observer monitors the developer-owned `rootBackStack`. When navigation returns to `RootScreen.Main` from the notification inbox, `MainViewModel.refreshUnreadCount()` is invoked automatically to immediately extinguish or update the red unread badge indicator.

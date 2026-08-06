# Feature Documentation: Offline-First Notification System & Badge Integration

## 1. High-Level Overview
The Notification System provides LinguaQuest users with a central inbox for real-time alerts, daily vocabulary reminders, game updates, and achievement announcements. The feature is implemented using a strict offline-first, reactive database-driven architecture:
- **Offline-First Room Cache:** Displays cached notifications immediately upon opening the screen via a reactive Room database `Flow<List<Notification>>`, ordered by timestamp (`createdAt DESC`). Background syncing updates the cache seamlessly without visual jumps.
- **Optimistic Local Mutations:** User interactions such as marking items as read or deleting notifications apply mutations directly to the Room local database first for instant UI responsiveness. Remote backend API requests are executed asynchronously with automatic database rollback if a network failure occurs.
- **Offline Network Guarding:** Integrates `ObserveNetworkStatusUseCase` to monitor connectivity. When offline (`isOnline == false`), remote-mutating operations are safely short-circuited with contextual feedback via error Snackbars and visual alpha adjustment on interactive controls.
- **Clean Architecture & MVI:** Strictly enforces separation of concerns across granular Data Sources (Local/Remote), domain UseCases, custom `LinguaQuestResult` wrappers, and unidirectional MVI state management without inline comments or qualified package names.

---

## 2. Architecture & Layer Separation

### Core Database Layer (`core/database/notification/`)
- **`NotificationEntity`**: Room table `@Entity(tableName = "notifications")` storing notification records with timestamp ordering field `createdAt: Long`.
- **`NotificationDao`**: Exposes reactive database stream `fun getNotifications(): Flow<List<NotificationEntity>>` ordered by `createdAt DESC`, along with helpers for atomic upserts, read-status mutations, and rollbacks.
- **`AppDatabase` & `DatabaseModule`**: Integrated into database schema version 10 with Hilt singleton bindings.

### Data Layer & Data Sources (`features/notification/data/`)
- **`NotificationLocalDataSource` & `NotificationLocalDataSourceImpl`**: Wraps `NotificationDao` operations and converts Room database entities into domain models.
- **`NotificationRemoteDataSource` & `NotificationRemoteDataSourceImpl`**: Wraps `NotificationApiService` using `safeApiCall` to ensure type-safe domain error resolution (`LinguaQuestResult<T, LinguaQuestDataError>`).
- **`NotificationRepositoryImpl`**: Implements the repository interface by combining local and remote data sources:
  - Exposes `val notifications: Flow<List<Notification>> = localDataSource.getNotifications()`.
  - Implements `refreshNotifications()`, which retrieves server notifications and syncs them directly into Room using non-destructive `upsertNotifications()`.
  - Implements optimistic UI pattern with transaction backup/restore rollback on failure for deletion and read status changes.

### Domain Layer (`features/notification/domain/`)
All business logic is brokered exclusively through single-responsibility Use Cases:
1. `GetNotificationsUseCase`: Exposes reactive `Flow<List<Notification>>` from local cache.
2. `RefreshNotificationsUseCase`: Triggers asynchronous remote fetch and local database upsert.
3. `GetUnreadNotificationCountUseCase`: Retrieves integer unread count.
4. `MarkNotificationAsReadUseCase`: Executes optimistic read status mutation.
5. `DeleteNotificationUseCase`: Removes individual notification with fallback rollback.
6. `DeleteAllNotificationsUseCase`: Clears all notification records with backup restoration on network error.

---

## 3. Presentation Layer & MVI Contract (`features/notification/presentation/`)

### MVI Contract (`contract/`)
- **`NotificationState`**: Holds screen state and connectivity status:
  ```kotlin
  data class NotificationState(
      val showDeleteAllDialog: Boolean = false,
      val notificationToDelete: Long? = null,
      val isDeleting: Boolean = false,
      val isOnline: Boolean = true
  )
  ```
- **`NotificationIntent`**: Expresses user actions cleanly (`NotificationClicked`, `DeleteNotificationClicked`, `ConfirmDeleteNotification`, `DeleteAllClicked`, `ConfirmDeleteAll`, etc.).
- **`NotificationEffect`**: Handles one-time events such as `RefreshNotifications`.

### Reactive Network Awareness in `NotificationViewModel`
`NotificationViewModel` observes device network connectivity via `ObserveNetworkStatusUseCase`. When the user is offline, remote-dependent mutation intents are intercepted and blocked before initiating UseCase operations, emitting localized Snackbar feedback:
```kotlin
val isOnline: StateFlow<Boolean> = observeNetworkStatusUseCase()
    .stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(5_000), initialValue = true)
```
Upon reconnection, the ViewModel automatically initiates `refreshNotificationsUseCase()` to ensure cache integrity and data consistency.

---

## 4. UI & Components (`presentation/view/`)

- **Stateful Wrapper (`NotificationScreen`)**: Instantiates `NotificationViewModel` via Hilt and observes reactive `notifications` and `state` streams using `collectAsStateWithLifecycle()`.
- **Stateless Content (`NotificationContent` & `NotificationListContainer`)**:
  - Renders standard Compose list structures (`LazyColumn`) fed directly by Room cache without legacy Paging overhead.
  - Dynamically updates action button styles (e.g. "Delete All") based on network availability (`state.isOnline`).
  - Employs reusable shared design components (`ShareTopBar`, `AppDialog`, `SnackbarController`).

---

## 5. Navigation 3 & Badge Synchronization

- **Route Definition**: Defined under `@Serializable data object Notification : RootScreen` in `Screens.kt`.
- **Top App Bar Integration**: `LinguaQuestTopAppBar` renders a red indicator overlay above the notifications bell icon whenever unread notifications exist.
- **Automatic Return Sync**: In `NestedNavigation.kt` (`MainScreen`), an explicit observer monitors the developer-owned `rootBackStack`. When returning to `RootScreen.Main` from the notification inbox, `MainViewModel.refreshUnreadCount()` is invoked automatically to reflect the latest unread indicator status.

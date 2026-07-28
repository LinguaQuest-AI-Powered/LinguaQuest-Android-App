---
trigger: always_on
---

---

description: Enforces that the agent automatically runs Gradle builds to verify code integrity and fix errors after making changes.
mode: always
---

# Gradle Build and Validation Rules

## 1. Post-Modification Verification

* After completing a code modification, refactor, or feature implementation, you must verify the build integrity before asking the user for review.
* Execute `./gradlew assembleDebug` in the terminal to ensure the project compiles successfully without errors.

## 2. Autonomous Error Resolution

* If any Gradle command fails, you are strictly prohibited from stopping and asking the user for help immediately.
* You must read the terminal error output, identify the cause (e.g., missing imports, syntax errors, type mismatches), and apply a fix.
* Re-run the failing Gradle command recursively until the build completes successfully.

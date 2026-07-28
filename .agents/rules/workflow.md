---
trigger: always_on
---

---
description: This rule defines the strict workflow and standard operating procedure (SOP) that the agent must follow for all major tasks, features, and refactors.
mode: always
---

# Antigravity Development Workflow

## 1. Phase 1: Implementation Planning
*   **Mandatory Plan:** Before writing any code, running any builds, or modifying any files for a new feature or major task, you must output a clear, step-by-step Implementation Plan.
*   **User Approval:** After presenting the plan, you must pause and explicitly ask the user for approval. Do not proceed to Phase 2 until the user types "approved", "yes", or "go ahead".

## 2. Phase 2: Execution
*   Execute the approved plan step-by-step.
*   Strictly adhere to all other architectural, UI, logging, and navigation rules present in the `.agents/rules/` directory.

## 3. Phase 3: Documentation
*   **Mandatory Documentation:** Once a feature or major task is completed and verified, you must generate documentation for it.
*   **Location:** Create or update a markdown file in the project's `docs/features/` directory (e.g., `docs/features/game-flow.md`). 
*   **Content:** The documentation must include a high-level overview of the feature, the state management (MVI/ViewModel), the Navigation 3 keys used, and any new shared components created.
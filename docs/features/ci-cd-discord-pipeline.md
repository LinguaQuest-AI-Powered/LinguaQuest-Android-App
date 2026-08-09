# CI/CD Pipeline: Discord Webhook Notifications

## Overview
This document outlines the architecture and journey of integrating Discord notifications into our GitHub Actions CI/CD pipeline for the LinguaQuest Android app. The goal was to automatically send built Debug APKs to a Discord channel for QA and testing.

## The Journey & Attempted Approaches

During the implementation, several file hosting strategies were explored to bypass Discord's 25MB file upload limit for standard webhooks, as our Debug APK is approximately 50MB.

1. **Direct Webhook Upload (Failed)**
   *   **Approach:** Send the APK directly to Discord via `multipart/form-data`.
   *   **Result:** Failed due to Discord's hard 25MB file upload limit on free servers.

2. **Temporary File Hosts - `transfer.sh` & `catbox.moe` (Failed)**
   *   **Approach:** Upload the APK to a free file hosting service in the workflow, extract the download URL, and send that URL to Discord.
   *   **Result:** While `catbox.moe` successfully worked locally, it failed on GitHub Actions runners with an `Invalid uploader` error. These services actively block cloud IPs (AWS/GitHub Actions) to prevent automated abuse.

3. **Google Drive Integration (Failed)**
   *   **Approach:** Use the `adityak74/google-drive-upload-git-action` to upload the APK to a shared Google Drive folder using a Service Account JSON key.
   *   **Result:** Failed due to a Google Drive API restriction (`storageQuotaExceeded`). Service Accounts on free `@gmail.com` accounts possess 0 bytes of storage quota and cannot own uploaded files without a paid Google Workspace setup.

## The Final (and Best) Solution: GitHub Artifacts

To ensure 100% reliability, security, and permanence (up to 90 days), we reverted to using **GitHub's native Artifacts**.

### How it Works
1.  **Build Phase:** The workflow builds the APK using `./gradlew assembleDebug`.
2.  **Artifact Upload:** The `actions/upload-artifact@v4` action securely stores the APK on GitHub's servers attached to that specific workflow run.
3.  **Discord Notification:** A simple bash script runs at the very end. It constructs a direct URL to the GitHub Actions Run page (`https://github.com/${{ github.repository }}/actions/runs/${{ github.run_id }}`) and sends it to the Discord channel via the webhook.

### Benefits of this Architecture
*   **Zero IP Bans:** Relies entirely on GitHub's native infrastructure.
*   **Security:** Only authenticated team members with repository access can download the APK.
*   **No Quotas:** Bypasses Google Drive limits and third-party file size caps completely.

## Environment Variables & Secrets
The pipeline relies on the following GitHub Repository Secrets:
*   `DISCORD_WEBHOOK`: The Webhook URL provided by the Discord channel integration settings. (The workflow safely skips the notification step if this secret is missing).

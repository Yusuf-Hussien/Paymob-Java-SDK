# Publishing Guide for Paymob Java SDK

This guide explains how to publish your SDK to **Maven Central** using the configured GitHub Actions workflow.

## 1. Prerequisites (Maven Central Access)

To publish to Maven Central, you need an account with **Sonatype OSSRH** (Open Source Software Repository Hosting).

1.  **Create an Account**: Sign up at [issues.sonatype.org](https://issues.sonatype.org/secure/Signup!default.jspa).
2.  **Create a JIRA Ticket**: Create a "New Project" ticket to claim your Group ID.
    *   If using your GitHub username, your Group ID will be `io.github.yusuf-hussien`.
    *   *Note*: If claiming `com.paymob`, you must verify domain ownership (DNS record).
3.  **Wait for Approval**: Sonatype bots/admins will approve your request (usually takes a few hours).

## 2. Generate GPG Keys

Maven Central requires all artifacts to be signed with GPG.

1.  **Install GPG**: verify with `gpg --version`.
2.  **Generate Key**:
    ```bash
    gpg --gen-key
    # Select ECC or RSA (RSA 4096 is good)
    # Enter Name and Email
    # Set a Passphrase (remember this!)
    ```
3.  **List Keys**: `gpg --list-keys` (Find your Key ID, e.g., `ABCD1234`).
4.  **Publish Public Key**:
    ```bash
    gpg --keyserver keyserver.ubuntu.com --send-keys YOUR_KEY_ID
    ```
5.  **Export Secret Key for GitHub**:
    ```bash
    gpg --export-secret-keys --armor YOUR_KEY_ID | base64 -w 0
    # Copy this long base64 string
    ```

## 3. Configure GitHub Secrets

Go to your GitHub Repository -> **Settings** -> **Secrets and variables** -> **Actions** -> **New repository secret**.

Add the following secrets:

| Secret Name | Value |
| :--- | :--- |
| `OSSRH_USERNAME` | Your Sonatype JIRA Username |
| `OSSRH_PASSWORD` | Your Sonatype JIRA Password |
| `GPG_PASSPHRASE` | The passphrase you set for your GPG key |
| `GPG_SECRET_KEY_BASE64` | The base64 encoded secret key string from Step 2.5 |

## 4. Usage

The workflow is configured in `.github/workflows/maven-publish.yml`.

*   **Continuous Integration**: Pushes to `main` or `develop` will trigger a **Build & Test** run.
*   **Publishing**:
    1.  Ensure all tests pass.
    2.  Update the version in `pom.xml` (remove `-SNAPSHOT` for a release).
    3.  Push to `main`.
    4.  The `publish` job will run, signing artifacts and deploying them to **OSSRH Staging**.
    5.  (If configured) It will automatically release to Maven Central (sync takes ~30 mins).

### Release Process
1.  **Prepare Release**:
    ```bash
    # Update version in pom.xml to 1.0.0 (remove -SNAPSHOT)
    mvn versions:set -DnewVersion=1.0.0
    
    # Commit
    git add pom.xml
    git commit -m "chore: release 1.0.0"
    git push origin main
    ```
2.  **Check Actions**: Go to GitHub Actions tab and watch the "Publish" job.
3.  **Verify on Internal Staging**: Log in to [s01.oss.sonatype.org](https://s01.oss.sonatype.org) to see your staged repository (if auto-release is disabled). If `autoReleaseAfterClose` is true in `pom.xml`, it will happen automatically.

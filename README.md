# BanglaPro Keyboard

BanglaPro is an offline, high-performance Android Input Method Editor (IME) for Bangla phonetic typing and English keyboard layouts.

## GitHub Actions CI/CD Build Pipeline

This repository is configured with a GitHub Actions workflow (`.github/workflows/android-build.yml`) that automatically builds and verifies the Android APK on every push or pull request to `main` / `master`.

### Artifacts
Upon workflow completion, the output APK is available for download under Actions artifacts as **`BanglaPro-Keyboard-APK`**.

### Optional Release Signing Configuration
To sign release builds with your custom keystore in GitHub Actions, add the following secrets in your repository settings (**Settings > Secrets and variables > Actions**):

- `KEYSTORE_BASE64`: Base64-encoded string of your `.jks` or `.keystore` file (`base64 -w 0 my-upload-key.jks`)
- `STORE_PASSWORD`: Keystore password
- `KEY_ALIAS`: Key alias
- `KEY_PASSWORD`: Key password

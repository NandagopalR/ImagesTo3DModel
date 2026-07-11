# ImagesTo3DModel

ImagesTo3DModel is a native Android app that captures object photos and submits them to Meshy AI's Multi-Image to 3D API to generate downloadable 3D models.

## What Was Built

- Kotlin Android application using Jetpack Compose, Material 3, MVVM, Coroutines, Retrofit, OkHttp, CameraX, Coil, and SharedPreferences.
- Full-screen camera scan flow with CameraX preview, runtime camera permission handling, capture button, selected-image thumbnails, upload progress, and snackbar feedback.
- Image preprocessing helper that crops captured images into a centered object frame before submission.
- Meshy API integration for creating multi-image 3D generation tasks, checking task status, and downloading generated GLB models.
- Local task history using SharedPreferences and Gson so submitted Meshy task IDs remain available between app launches.
- History screen that lists submitted tasks by creation time and lets the user download the generated GLB file when Meshy has finished processing.
- Compose navigation flow for splash, home, camera scan, and history screens.
- Gradle/Android project setup with Java 17, Android Gradle Plugin 8.9.1, Kotlin 2.0.21, Compose compiler plugin, and Android SDK 35.

## Project Structure

```text
app/src/main/java/com/example/meshyeam3d/
  data/local/          SharedPreferences-backed task history
  data/model/          Meshy request/response and history models
  data/remote/         Retrofit API definitions and client setup
  data/repository/     Meshy task creation, polling, and GLB download logic
  ui/camera/           Camera capture screen and upload ViewModel
  ui/history/          Task history screen and download ViewModel
  ui/home/             App entry screen
  ui/navigation/       Compose route definitions and navigation host
  ui/splash/           Splash screen
  ui/theme/            Material theme
  util/                Captured image processing
```

## Meshy API Key

Provide the API key at build time. Do not commit real API keys to the repository.

```properties
MESHY_API_KEY=your_meshy_api_key
```

Recommended options:

- Add it to user-level `~/.gradle/gradle.properties`.
- Export it as an environment variable before building.
- Add it to local project `gradle.properties` only for local development.

The Gradle build reads `MESHY_API_KEY` from a Gradle property first, then from the environment, and injects it into `BuildConfig.MESHY_API_KEY`.

## Build

Use Android Studio's JBR or another Java 17 runtime:

```sh
./gradlew :app:assembleDebug
```

The debug APK is generated at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Meshy Multi-Image Note

Meshy's current Multi-Image to 3D endpoint accepts `image_urls` as public URLs or base64 data URIs. The repository converts captured images to JPEG base64 data URIs and sends the first 4 processed images to Meshy for task creation.

The camera flow currently requires 5 captured pictures before submission, while the repository caps Meshy upload payloads to 4 images to match the API limit.

## Generated Files

Gradle build output, Android Studio metadata, APK artifacts, local SDK configuration, keystores, logs, and local API-key configuration are ignored through `.gitignore`.

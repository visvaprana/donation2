# Volunteer Form Android App

Native Android app written in Kotlin with Gradle Kotlin DSL support.

## Open and run

Open the `native-android` folder in Android Studio. Android Studio will sync the
Gradle project and download the Android and Retrofit dependencies. Run the
`app` configuration on an emulator or connected Android device.

## Behavior

- Collects Name, Mobile, and Address.
- Requires all three fields before submitting.
- Sends a JSON `POST` request to:
  `https://volunteer.matchlessgiftikd.com/api/android-test`
- Reads the JSON `message` field from a successful response.
- Shows the server message in the form and as a toast.
- Disables the submit action and shows progress while the request is in flight.
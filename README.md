# Name: Md Mahi Al Jubair Talukder
# student id: 001166325


\## Step-by-Step System Setup



1\.  \*\*Clone the Repo\*\*:

&#x20;   ```bash

&#x20;   git clone https://github.com/LordMahi19/ravintola-android.git

&#x20;   ```

2\.  \*\*Open Project\*\*:

&#x20;   Launch \*\*Android Studio\*\* and click \*\*Open\*\*, selecting the `RavintolaApp` directory as the root.

3\.  \*\*Sync Gradle\*\*:

&#x20;   Wait for Android Studio to resolve the dependencies configured in `build.gradle.kts` (Retrofit, Glide, Gson, and Material UI packages).

4\.  \*\*Verify API URL\*\*:

&#x20;   Verify that `com.example.ravintolaapp.network.ApiClient` is pointing to the cloud server:

&#x20;   `"https://ravintola-server.onrender.com/"`. If running a local server on your computer, configure Retrofit to use your machine's local IP or standard emulator loopback `10.0.2.2:5000`.

5\.  \*\*Deploy\*\*:

&#x20;   Run the application on an Android Virtual Device (AVD Emulator) or connect a physical device via USB debugging. Requires \*\*API Level 24+ (Android 7.0 Nougat)\*\*.



\---


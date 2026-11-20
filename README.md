# My Ruijie Android — Auto Detect prototype

Android starter app using Kotlin and Jetpack Compose.

## What works in this prototype
- Detects the active Wi-Fi network's IPv4 default gateway using Android ConnectivityManager.
- Displays the detected gateway IP.
- Opens `http://<gateway-ip>` in the phone's browser when tapped.

## Important limitations
- The detected gateway is not guaranteed to be a Ruijie router; it may be another router or access point.
- Android may not expose an IPv4 gateway in some network configurations.
- This prototype does not bypass admin login.
- Device listing, Wi-Fi changes, block/unblock, speed limits, QR generation and traffic monitoring are placeholders. They require an authorized, supported integration for the exact Ruijie firmware/cloud management setup.
- Do not put your router administrator password into an untrusted app or share it in chat.

## Build
Open this folder in Android Studio, sync Gradle, then use Build > Build Bundle(s) / APK(s) > Build APK(s).

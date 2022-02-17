# GetOut 
An Android application made for the MobServ challenge project.

## Prerequisites
Android Studio with device emulator that supports Google Maps API.

## Installation instructions
**Step 1: Upload SHA-1 certificate fingerprint.**
To run the application, your SHA-1 certificate fingerprint needs to be uploaded to our Google Maps API. The easiest way to do so is:
1. Open the LocationServices app made during the Android Location API lab.
2. Open res->values->google_maps_api.xml
3. Copy your SHA-1 certificate fingerprint.
4. Send it to us by mail.

Alternatively create a new Android project using the Google Maps API and follow steps 2-4.

**Step 2: Build project**
Use git clone or manually download the source code, and open with Android Studio. Make sure to build the project before running the application. It should be connected to Firebase automatically.

**Step 3: Run the application**
If everything went well, you should be presented with the sign-in screen! Create a user and check out the application. You can change your location in the emulator control to move around.
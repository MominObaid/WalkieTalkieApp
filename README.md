📡 **WalkieTalkie** _Android App_



A real-time Walkie-Talkie Android application built with Kotlin that allows two Android devices to communicate using Bluetooth Classic (RFCOMM) without requiring an internet connection.


This app records audio from one device and streams it to another device over Bluetooth, enabling short-range voice communication similar to a walkie-talkie.





🚀 **Features**


✅ Device-to-device communication using Bluetooth Classic


✅ Real-time audio streaming between connected devices


✅ Works without internet connection


✅ Server / Client connection model


✅ Built using Kotlin and Android native APIs


✅ Uses AudioRecord for recording and AudioTrack for playback


✅ Handles Bluetooth permissions and connection lifecycle


**🏗️ App Architecture
**


_The application works using a Bluetooth server-client architecture.
_


1️⃣ Server Device
Opens a Bluetooth Server Socket
Waits for incoming connections
Accepts connection from client device
2️⃣ Client Device
Searches paired devices
Connects to the server using BluetoothSocket
3️⃣ Audio Streaming

Once connection is established:

Microphone captures audio using AudioRecord
Audio data is converted to byte stream
Data is sent through Bluetooth OutputStream
Receiving device reads data using InputStream
Audio is played using AudioTrack

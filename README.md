📡 **WalkieTalkie** _Android App_


<img src = "https://github.com/user-attachments/assets/50e4e145-5e0d-49c5-aab9-a1390902a15e" width="30" >



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


1️⃣ _Server Device_

Opens a Bluetooth Server Socket

Waits for incoming connections

Accepts connection from client device


2️⃣ _Client Device_

Searches paired devices

Connects to the server using BluetoothSocket

3️⃣ _Audio Streaming_

_Once connection is established:_


Microphone captures audio using AudioRecord

Audio data is converted to byte stream

Data is sent through Bluetooth OutputStream

Receiving device reads data using InputStream

Audio is played using AudioTrack

**Key Components**

_MainActivity_

_Handles_:

UI interactions

Bluetooth initialization

Starting server/client threads

ServerThread

Opens BluetoothServerSocket

Waits for incoming connections

ClientThread

Connects to server device using UUID

AudioRecorder

Records microphone input

AudioPlayer

Plays received audio

ConnectedThread

Handles continuous input/output streaming


**🧪 Future Improvements**

Push-to-Talk button
Better audio compression
Multi-device communication
Noise reduction
Modern UI
Background service support
WiFi Direct support

**🧑‍💻 Developer**

_Developed using Kotlin for Android as a learning project for:_

Bluetooth networking

Audio streaming

Real-time communication apps


📄 **License**

This project is for educational and learning purposes.
















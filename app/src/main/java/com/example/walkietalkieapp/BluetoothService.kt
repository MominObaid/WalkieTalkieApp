package com.example.walkietalkieapp

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.*
import android.media.*
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.annotation.RestrictTo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class BluetoothService {

    private val adapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var socket: BluetoothSocket? = null
    private var serverSocket: BluetoothServerSocket? = null

    private var recorder: AudioRecord? = null
    private var isSending = false

    private val uuid: UUID =
        UUID.fromString("12345678-1234-1234-1234-123456789abc")

    @SuppressLint("MissingPermission")
    fun startServer(onConnected: () -> Unit) {
        if (adapter == null || !adapter.isEnabled) return

        Thread {
            try {
                serverSocket =
                    adapter.listenUsingRfcommWithServiceRecord("WalkieTalkie", uuid)

                socket = serverSocket?.accept()

                if (socket != null) {
                    onConnected()
                }
            } catch (e: Exception) {
                Log.e("BT_ERROR", "Server error: ${e.message}")
            }
        }.start()
    }

//    @SuppressLint("MissingPermission")
    fun connect(device: BluetoothDevice, onConnected: () -> Unit) {
        if (adapter == null || !adapter.isEnabled) return

        CoroutineScope(Dispatchers.IO).launch{
            try {
                socket = device.createRfcommSocketToServiceRecord(uuid)
                socket?.connect()

                if (socket?.isConnected == true) {
                    onConnected()
                }
            } catch (e: Exception) {
                Log.e("BT_ERROR", "Connection error: ${e.message}")
            }
        }.start()
    }

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    fun startSending() {
        val outputStream: OutputStream = socket?.outputStream ?: return

        val bufferSize = AudioRecord.getMinBufferSize(
            44100,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        if (bufferSize <= 0) return

        recorder = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            44100,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize
        )

        val buffer = ByteArray(bufferSize)
        recorder?.startRecording()
        isSending = true

        Thread {
            try {
                while (isSending) {
                    val read = recorder?.read(buffer, 0, buffer.size) ?: 0
                    if (read > 0) {
                        outputStream.write(buffer, 0, read)
                    }
                }
            } catch (e: Exception) {
                Log.e("AUDIO_SEND", e.message ?: "")
            }
        }.start()
    }

    fun stopSending() {
        isSending = false
        recorder?.stop()
        recorder?.release()
        recorder = null
    }

    fun startReceiving() {
        val inputStream: InputStream = socket?.inputStream ?: return

        val bufferSize = AudioTrack.getMinBufferSize(
            44100,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        if (bufferSize <= 0) return

        val player = AudioTrack(
            AudioManager.STREAM_MUSIC,
            44100,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize,
            AudioTrack.MODE_STREAM
        )

        val buffer = ByteArray(bufferSize)
        player.play()

        Thread {
            try {
                while (true) {
                    val bytes = inputStream.read(buffer)
                    if (bytes > 0) {
                        player.write(buffer, 0, bytes)
                    }
                }
            } catch (e: Exception) {
                Log.e("AUDIO_RECEIVE", e.message ?: "")
            }
        }.start()
    }
}
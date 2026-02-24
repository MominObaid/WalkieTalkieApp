package com.example.walkietalkieapp

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private var bluetoothAdapter: BluetoothAdapter? = null
    private lateinit var bluetoothService: BluetoothService

    private lateinit var tvStatus: TextView
    private lateinit var btnServer: Button
    private lateinit var btnConnect: Button
    private lateinit var btnTalk: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bluetoothService = BluetoothService()
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        tvStatus = findViewById(R.id.tvStatus)
        btnServer = findViewById(R.id.btnServer)
        btnConnect = findViewById(R.id.btnConnect)
        btnTalk = findViewById(R.id.btnPushToTalk)

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        requestPermissions()

        btnServer.setOnClickListener {
            if (!hasBluetoothPermission()) return@setOnClickListener

            tvStatus.text = "Waiting..."
            bluetoothService.startServer {
                runOnUiThread {
                    tvStatus.text = "Connected"
                }
                bluetoothService.startReceiving()
            }
        }

        btnConnect.setOnClickListener {
            if (!hasBluetoothPermission()) return@setOnClickListener

            val device: BluetoothDevice? =
                bluetoothAdapter?.bondedDevices?.firstOrNull()

            if (device == null) {
                Toast.makeText(this, "No paired device", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            tvStatus.text = "Connecting..."

            bluetoothService.connect(device) {
                runOnUiThread {
                    tvStatus.text = "Connected"
                }
                bluetoothService.startReceiving()
            }
        }

        btnTalk.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> bluetoothService.startSending()
                MotionEvent.ACTION_UP -> bluetoothService.stopSending()
            }
            true
        }
    }

    private fun requestPermissions() {
        val permissions = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
            permissions.add(Manifest.permission.BLUETOOTH_SCAN)
        }

        permissions.add(Manifest.permission.RECORD_AUDIO)

        ActivityCompat.requestPermissions(
            this,
            permissions.toTypedArray(),
            101
        )
    }

    private fun hasBluetoothPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        } else true
    }
}
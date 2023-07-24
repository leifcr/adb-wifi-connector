// WifiConnectActivity.kt
package no.normedia.adbwificonnector

import android.content.Context
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.activity.ComponentActivity

class WiFiConnectActivity : ComponentActivity() {
    private lateinit var wifiManager: WifiManager
    private var netId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wifi_connect)

        wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager

        val action = intent.action
        when (action) {
            "connect" -> {
                val ssid = intent.getStringExtra("ssid")
                val password = intent.getStringExtra("password")
                if (ssid != null && password != null) {
                    connect(ssid, password)
                    displayNetworkInfo(ssid, password)
                    exitAfterDelay()
                }
            }
            "disconnect" -> {
                val ssid = intent.getStringExtra("ssid")
                if (ssid != null) {
                    disconnect(ssid)
                    displayDisconnectedInfo(ssid)
                    exitAfterDelay()
                }
            }
            else -> {
                // Invalid action, finish the activity
                finish()
            }
        }
    }

    private fun connect(ssid: String, password: String) {
        // Use ssid and password to connect to the network...
        val wifiConfig = WifiConfiguration()
        wifiConfig.SSID = String.format("\"%s\"", ssid)
        wifiConfig.preSharedKey = String.format("\"%s\"", password)

        // Set the allowed key management scheme
        if (password.isEmpty()) {
            wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE)
        } else {
            wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK)
        }

        // Add new network to the list of configured networks
        netId = wifiManager.addNetwork(wifiConfig)

        // Save the network configuration to the device's Wifi settings
        if (netId >= 0) {
            wifiManager.disconnect()
            wifiManager.enableNetwork(netId, true)
            wifiManager.saveConfiguration()
            wifiManager.reconnect()
        }
    }

    private fun disconnect(ssid: String) {
      // Disconnect from the network
      val wifiInfo = wifiManager.connectionInfo
      if (wifiInfo.ssid == String.format("\"%s\"", ssid)) {
          wifiManager.disableNetwork(wifiInfo.networkId)
          wifiManager.disconnect()
      }
  
      // Remove the network from the device if it is saved
      val networks = wifiManager.configuredNetworks
      for (network in networks) {
          if (network.SSID == String.format("\"%s\"", ssid)) {
              wifiManager.removeNetwork(network.networkId)
              wifiManager.saveConfiguration()
              break
          }
      }
  }
    private fun displayNetworkInfo(ssid: String, password: String) {
        // Display the SSID and password on the screen
        val ssidTextView = findViewById<TextView>(R.id.ssidTextView)
        ssidTextView.text = "SSID: $ssid"

        val passwordTextView = findViewById<TextView>(R.id.passwordTextView)
        passwordTextView.text = "Password: $password"

        // Display some information from the WifiManager on the screen
        val wifiInfo = wifiManager.connectionInfo
        val wifiInfoTextView = findViewById<TextView>(R.id.wifiInfoTextView)
        wifiInfoTextView.text = "SSID: ${wifiInfo.ssid}, BSSID: ${wifiInfo.bssid}, RSSI: ${wifiInfo.rssi}"
    }

    private fun displayDisconnectedInfo(ssid: String) {
        // Display the SSID on the screen
        val ssidTextView = findViewById<TextView>(R.id.ssidTextView)
        ssidTextView.text = "SSID: $ssid"

        // Display a message on the screen
        val passwordTextView = findViewById<TextView>(R.id.passwordTextView)
        passwordTextView.text = "Disconnected"

        // Display some information from the WifiManager on the screen
        val wifiInfoTextView = findViewById<TextView>(R.id.wifiInfoTextView)
        wifiInfoTextView.text = ""
    }

    private fun exitAfterDelay() {
        // Exit the app after 3 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            finish()
        }, 3000)
    }
}
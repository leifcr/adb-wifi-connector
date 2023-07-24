// MainActivity.kt
package no.normedia.adbwificonnector

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import no.normedia.adbwificonnector.ui.theme.ADBwificonnectorTheme

class MainActivity : ComponentActivity() {
    private lateinit var wifiStateReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ADBwificonnectorTheme {
                ADBwificonnectorContent()
            }
        }
    }

    @Composable
    fun ADBwificonnectorContent() {
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        val ssid = wifiInfo.ssid
        val isConnected by remember { mutableStateOf(ssid != null && ssid != "0x" && ssid.isNotEmpty()) }

        LaunchedEffect(isConnected) {
            if (isConnected) {
                delay(3000)
                finish()
            }
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ADBwificonnectorTitle()
                if (isConnected) {
                    Greeting("Connected to $ssid")
                } else {
                    Greeting("Not connected to wifi")
                }
                Spacer(modifier = Modifier.height(16.dp))
                ADBwificonnectorExitButton()
            }
        }
    }

    @Composable
    fun ADBwificonnectorTitle() {
        Text(
            text = "ADB Wifi Connector",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }

    @Composable
    fun ADBwificonnectorExitButton() {
        Button(onClick = { finish() }) {
            Text(text = "Exit")
        }
    }

    override fun onResume() {
        super.onResume()
        // Ensure content is freeeeeeeeeesh
        setContent {
            ADBwificonnectorTheme {
                ADBwificonnectorContent()
            }
        }
        // Register a broadcast receiver to listen for wifi state changes
        wifiStateReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                setContent {
                    ADBwificonnectorTheme {
                        ADBwificonnectorContent()
                    }
                }
            }
        }
        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
        registerReceiver(wifiStateReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        // Unregister the broadcast receiver when the activity is paused
        unregisterReceiver(wifiStateReceiver)
    }
}

@Composable
fun Greeting(message: String, modifier: Modifier = Modifier) {
    Text(
        text = message,
        modifier = modifier
    )
}

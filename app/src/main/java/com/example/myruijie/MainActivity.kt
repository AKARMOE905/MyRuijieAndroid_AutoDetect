package com.example.myruijie

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.net.Inet4Address

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MyRuijieAutoDetectApp() }
    }
}

private fun findDefaultGateway(context: Context): String? {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = cm.activeNetwork ?: return null
    val caps = cm.getNetworkCapabilities(network) ?: return null
    if (!caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) return null
    val links: LinkProperties = cm.getLinkProperties(network) ?: return null
    return links.routes.firstOrNull { it.isDefaultRoute && it.gateway is Inet4Address }
        ?.gateway?.hostAddress
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyRuijieAutoDetectApp() {
    val context = LocalContext.current
    var gateway by remember { mutableStateOf<String?>(null) }
    var status by remember { mutableStateOf("Tap Auto Detect to find your Wi-Fi router.") }
    var scanned by remember { mutableStateOf(false) }

    MaterialTheme(colorScheme = lightColorScheme(
        primary = androidx.compose.ui.graphics.Color(0xFF176B55),
        secondary = androidx.compose.ui.graphics.Color(0xFF4C7C70)
    )) {
        Scaffold(topBar = {
            TopAppBar(title = {
                Column {
                    Text("My Ruijie", fontWeight = FontWeight.Bold)
                    Text("Automatic Router Discovery", style = MaterialTheme.typography.labelSmall)
                }
            })
        }) { padding ->
            Column(
                Modifier.fillMaxSize().padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Router, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("ROUTER DISCOVERY", style = MaterialTheme.typography.labelLarge)
                            Spacer(Modifier.weight(1f))
                            AssistChip(onClick = {}, label = { Text("Auto") })
                        }
                        Text(
                            gateway ?: "Router not detected yet",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(status, style = MaterialTheme.typography.bodyMedium)
                        Button(
                            onClick = {
                                scanned = true
                                gateway = findDefaultGateway(context)
                                status = if (gateway != null) {
                                    "Default Wi-Fi gateway found. It may be your Ruijie router."
                                } else {
                                    "No Wi-Fi gateway found. Connect your phone to the Ruijie Wi-Fi and try again."
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Search, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Auto Detect Router")
                        }
                        Button(
                            onClick = {
                                val ip = gateway
                                if (ip != null) {
                                    try {
                                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://$ip")))
                                        status = "Router admin page opened in your browser."
                                    } catch (_: Exception) {
                                        status = "Could not open the router page."
                                    }
                                } else {
                                    status = "Detect the router first."
                                }
                            },
                            enabled = gateway != null,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Icon(Icons.Default.OpenInBrowser, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Open Router Admin Page")
                        }
                    }
                }

                Text("MANAGEMENT FEATURES", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.secondary)
                FeatureRow("Device Manager", Icons.Default.Devices, "Connected device list — integration pending")
                FeatureRow("Wi-Fi Settings", Icons.Default.Wifi, "Change network name and password — integration pending")
                FeatureRow("Speed & Access Control", Icons.Default.Speed, "Speed limits and block/unblock — integration pending")
                FeatureRow("QR Connect & Monitor", Icons.Default.QrCode2, "Wi-Fi QR and traffic monitor — integration pending")

                Text(
                    "Note: Auto Detect finds the active Wi-Fi network's default gateway. It cannot guarantee that the gateway is Ruijie, identify every model, or bypass router login. Live management needs a supported, authorized Ruijie interface.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun FeatureRow(title: String, icon: ImageVector, detail: String) {
    OutlinedCard(shape = RoundedCornerShape(14.dp)) {
        Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(12.dp))
            Column {
                Text(title, fontWeight = FontWeight.SemiBold)
                Text(detail, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

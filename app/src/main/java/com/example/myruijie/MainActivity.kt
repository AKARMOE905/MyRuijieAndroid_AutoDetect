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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.net.Inet4Address

private val Black = Color(0xFF070B14)
private val Panel = Color(0xFF101A2B)
private val NeonBlue = Color(0xFF35C8FF)
private val SoftBlue = Color(0xFF1676B8)
private val WhiteText = Color(0xFFEAF7FF)

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
    return links.routes.firstOrNull {
        it.isDefaultRoute && it.gateway is Inet4Address
    }?.gateway?.hostAddress
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyRuijieAutoDetectApp() {
    val context = LocalContext.current
    var gateway by remember { mutableStateOf<String?>(null) }
    var status by remember { mutableStateOf("Router ကိုရှာဖွေရန် Auto Detect ကိုနှိပ်ပါ။") }
    var selectedFeature by remember { mutableStateOf<String?>(null) }

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = NeonBlue,
            secondary = SoftBlue,
            background = Black,
            surface = Panel,
            onBackground = WhiteText,
            onSurface = WhiteText
        )
    ) {
        Scaffold(
            containerColor = Black,
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                "AKAR MOE",
                                color = NeonBlue,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                "MY RUIJIE CONTROL",
                                color = WhiteText.copy(alpha = 0.65f),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    },
                    navigationIcon = {
                        Image(
                            painter = painterResource(id = R.mipmap.ic_launcher),
                            contentDescription = "AKAR MOE Hacker Logo",
                            modifier = Modifier
                                .padding(start = 12.dp, end = 8.dp)
                                .size(48.dp)
                                .clip(CircleShape)
                                .border(2.dp, NeonBlue, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Black
                    )
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(
                        Brush.verticalGradient(
                            listOf(Black, Color(0xFF0B1830), Black)
                        )
                    )
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Panel),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonBlue.copy(alpha = 0.65f))
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Router, null, tint = NeonBlue)
                            Spacer(Modifier.width(9.dp))
                            Text(
                                "ROUTER DISCOVERY",
                                color = NeonBlue,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.weight(1f))
                            Text(
                                "AUTO",
                                color = NeonBlue,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }

                        Text(
                            gateway ?: "ROUTER NOT DETECTED",
                            color = WhiteText,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            status,
                            color = WhiteText.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.bodySmall
                        )

                        Button(
                            onClick = {
                                gateway = findDefaultGateway(context)
                                status = if (gateway != null) {
                                    "Wi-Fi Gateway တွေ့ပါပြီ။ Ruijie Router ဖြစ်နိုင်ပါတယ်။"
                                } else {
                                    "Wi-Fi ချိတ်ဆက်ပြီးမှ ထပ်စမ်းကြည့်ပါ။"
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SoftBlue,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Search, null)
                            Spacer(Modifier.width(8.dp))
                            Text("AUTO DETECT ROUTER")
                        }

                        OutlinedButton(
                            onClick = {
                                val ip = gateway
                                if (ip != null) {
                                    try {
                                        context.startActivity(
                                            Intent(Intent.ACTION_VIEW, Uri.parse("http://$ip"))
                                        )
                                        status = "Router Admin Page ကို ဖွင့်လိုက်ပါပြီ။"
                                    } catch (_: Exception) {
                                        status = "Browser ဖွင့်မရပါ။"
                                    }
                                } else {
                                    status = "အရင်ဆုံး Router ကို Detect လုပ်ပါ။"
                                }
                            },
                            enabled = gateway != null,
                            modifier = Modifier.fillMaxWidth(),
                            border = androidx.compose.foundation.BorderStroke(1.dp, NeonBlue),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.OpenInBrowser, null, tint = NeonBlue)
                            Spacer(Modifier.width(8.dp))
                            Text("OPEN ROUTER ADMIN", color = NeonBlue)
                        }
                    }
                }

                Text(
                    "SYSTEM FEATURES",
                    color = NeonBlue,
                    fontWeight = FontWeight.Bold
                )

                FeatureRow(
                    "DEVICE MANAGER",
                    Icons.Default.Devices,
                    "ချိတ်ဆက်ထားသော Device များ",
                    { selectedFeature = "DEVICE MANAGER" }
                )
                FeatureRow(
                    "ONLINE / OFFLINE",
                    Icons.Default.Wifi,
                    "Device အခြေအနေ စစ်ဆေးရန်",
                    { selectedFeature = "ONLINE / OFFLINE" }
                )
                FeatureRow(
                    "BLOCK / UNBLOCK",
                    Icons.Default.Security,
                    "Device အသုံးပြုခွင့် ထိန်းချုပ်ရန်",
                    { selectedFeature = "BLOCK / UNBLOCK" }
                )
                FeatureRow(
                    "SPEED MONITOR",
                    Icons.Default.Speed,
                    "Internet Speed စောင့်ကြည့်ရန်",
                    { selectedFeature = "SPEED MONITOR" }
                )
                FeatureRow(
                    "SPEED LIMIT",
                    Icons.Default.Tune,
                    "အသုံးပြုနှုန်း ကန့်သတ်ရန်",
                    { selectedFeature = "SPEED LIMIT" }
                )
                FeatureRow(
                    "WI-FI QR CONNECT",
                    Icons.Default.QrCode2,
                    "Wi-Fi QR Code",
                    { selectedFeature = "WI-FI QR CONNECT" }
                )

                Text(
                    "SYSTEM NOTE: အမှန်တကယ် Router ထိန်းချုပ်ရန် Router ဖွင့်ထားပြီး ခွင့်ပြုထားသော Ruijie API ချိတ်ဆက်မှု လိုအပ်ပါသည်။",
                    color = WhiteText.copy(alpha = 0.55f),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }

        if (selectedFeature != null) {
            AlertDialog(
                onDismissRequest = { selectedFeature = null },
                containerColor = Panel,
                title = {
                    Text(selectedFeature ?: "", color = NeonBlue)
                },
                text = {
                    Text(
                        "ဒီလုပ်ဆောင်ချက်ကို အမှန်တကယ် အသုံးပြုနိုင်ရန် Ruijie Router နှင့် API ချိတ်ဆက်မှု လိုအပ်ပါသေးသည်။",
                        color = WhiteText
                    )
                },
                confirmButton = {
                    TextButton(onClick = { selectedFeature = null }) {
                        Text("OK", color = NeonBlue)
                    }
                }
            )
        }
    }
}

@Composable
fun FeatureRow(
    title: String,
    icon: ImageVector,
    detail: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = Panel),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            NeonBlue.copy(alpha = 0.35f)
        )
    ) {
        Row(
            modifier = Modifier.padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = NeonBlue,
                modifier = Modifier.size(27.dp)
            )
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    title,
                    color = WhiteText,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    detail,
                    color = WhiteText.copy(alpha = 0.55f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                "API",
                color = NeonBlue,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

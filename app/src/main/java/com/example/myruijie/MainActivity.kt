package com.example.myruijie

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

private val Black = Color(0xFF070B14)
private val Panel = Color(0xFF101A2B)
private val Blue = Color(0xFF35C8FF)
private val WhiteText = Color(0xFFEAF7FF)

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MyRuijieAutoDetectApp() }
    }
}

private fun openTelegram(context: Context) {
    try {
        context.startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/akarmoe11"))
        )
    } catch (_: Exception) { }
}

@Composable
fun MyRuijieAutoDetectApp() {
    val context = LocalContext.current
    var loggedIn by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginMessage by remember { mutableStateOf("") }
    var selectedTab by remember { mutableIntStateOf(0) }
    var routerStatus by remember { mutableStateOf("Router မရှာရသေးပါ။") }

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Blue,
            secondary = Color(0xFF1676B8),
            background = Black,
            surface = Panel,
            onSurface = WhiteText
        )
    ) {
        if (!loggedIn) {
            LoginScreen(
                email = email,
                onEmail = { email = it },
                password = password,
                onPassword = { password = it },
                message = loginMessage,
                onLogin = {
                    if (email.isBlank() || password.isBlank()) {
                        loginMessage = "Email နဲ့ Password ဖြည့်ပေးပါ။"
                    } else {
                        loggedIn = true
                        loginMessage = ""
                    }
                },
                onTelegram = { openTelegram(context) }
            )
        } else {
            MainDashboard(
                selectedTab = selectedTab,
                onSelectTab = { selectedTab = it },
                email = email,
                routerStatus = routerStatus,
                onDetect = {
                    routerStatus = "Ruijie Cloud API ချိတ်ဆက်မှု မရသေးပါ။"
                },
                onTelegram = { openTelegram(context) },
                onLogout = {
                    loggedIn = false
                    password = ""
                    selectedTab = 0
                }
            )
        }
    }
}

@Composable
private fun LoginScreen(
    email: String,
    onEmail: (String) -> Unit,
    password: String,
    onPassword: (String) -> Unit,
    message: String,
    onLogin: () -> Unit,
    onTelegram: () -> Unit
) {
    Box(
        Modifier.fillMaxSize().background(
            Brush.verticalGradient(listOf(Black, Color(0xFF0B1D36), Black))
        ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Image(
                painter = painterResource(R.mipmap.ic_launcher),
                contentDescription = "AKAR MOE Logo",
                modifier = Modifier.size(120.dp).clip(CircleShape)
                    .border(2.dp, Blue, CircleShape)
            )
            Text("AKAR MOE", color = Blue, style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black)
            Text("MY RUIJIE CONTROL", color = WhiteText)
            Text("UNIVERSAL RUIJIE ACCOUNT", color = Blue,
                style = MaterialTheme.typography.labelSmall)

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Panel),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text("ACCOUNT LOGIN", color = Blue, fontWeight = FontWeight.Bold)

                    OutlinedTextField(
                        value = email,
                        onValueChange = onEmail,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text("Gmail / Email") },
                        leadingIcon = { Icon(Icons.Default.Email, null) }
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = onPassword,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text("Password") },
                        leadingIcon = { Icon(Icons.Default.Lock, null) },
                        visualTransformation = PasswordVisualTransformation()
                    )

                    Button(
                        onClick = onLogin,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1676B8))
                    ) {
                        Text("LOGIN")
                    }

                    Text(
                        "DEMO LOGIN ONLY — Ruijie API မချိတ်ဆက်ရသေးပါ။",
                        color = Blue,
                        style = MaterialTheme.typography.labelSmall
                    )

                    if (message.isNotEmpty()) {
                        Text(message, color = Color(0xFFFF7777))
                    }

                    HorizontalDivider(color = Blue.copy(alpha = 0.4f))

                    Row(
                        Modifier.fillMaxWidth().clickable { onTelegram() }.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Send, null, tint = Blue)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text("CONTACT DEVELOPER", color = WhiteText)
                            Text("@akarmoe11", color = Blue, fontWeight = FontWeight.Bold)
                        }
                        Text("TELEGRAM", color = Blue)
                    }
                }
            }
        }
    }
}

private val menuNames = listOf(
    "Home", "Discover", "Product", "Toolkit",
    "My Account", "Support", "More"
)

private val menuIcons = listOf(
    Icons.Default.Home,
    Icons.Default.Explore,
    Icons.Default.Devices,
    Icons.Default.Build,
    Icons.Default.Person,
    Icons.Default.Help,
    Icons.Default.MoreHoriz
)

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MainDashboard(
    selectedTab: Int,
    onSelectTab: (Int) -> Unit,
    email: String,
    routerStatus: String,
    onDetect: () -> Unit,
    onTelegram: () -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        containerColor = Black,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("AKAR MOE", color = Blue, fontWeight = FontWeight.Black)
                        Text("MY RUIJIE CONTROL", color = WhiteText,
                            style = MaterialTheme.typography.labelSmall)
                    }
                },
                navigationIcon = {
                    Image(
                        painter = painterResource(R.mipmap.ic_launcher),
                        contentDescription = "App Logo",
                        modifier = Modifier.padding(start = 10.dp).size(42.dp)
                            .clip(CircleShape).border(1.dp, Blue, CircleShape)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Black)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Panel) {
                menuNames.forEachIndexed { index, name ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { onSelectTab(index) },
                        icon = { Icon(menuIcons[index], contentDescription = name) },
                        label = { Text(name, style = MaterialTheme.typography.labelSmall) },
                        alwaysShowLabel = true
                    )
                }
            }
        }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding)
                .background(Brush.verticalGradient(listOf(Black, Color(0xFF0B1830))))
                .verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            when (selectedTab) {
                0 -> {
                    Text("WELCOME TO AKAR MOE", color = Blue,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold)
                    RouterCard(routerStatus, onDetect)
                    Text("SYSTEM FEATURES", color = Blue, fontWeight = FontWeight.Bold)
                    FeatureCard("Device Manager", "Connected device list — API လိုအပ်", Icons.Default.Devices)
                    FeatureCard("Online / Offline", "Router status — API လိုအပ်", Icons.Default.Wifi)
                    FeatureCard("Block / Unblock", "Access control — API လိုအပ်", Icons.Default.Security)
                    FeatureCard("Speed Monitor", "Traffic monitoring — API လိုအပ်", Icons.Default.Speed)
                }
                1 -> {
                    PageTitle("DISCOVER", "Ruijie Router ရှာဖွေရန်")
                    RouterCard(routerStatus, onDetect)
                    FeatureCard("Cloud Auto Detect", "Ruijie Cloud Account ထဲက Router များကို ရှာဖွေရန် — API လိုအပ်", Icons.Default.Search)
                }
                2 -> {
                    PageTitle("PRODUCT", "Ruijie Router Families")
                    listOf("RG-EG Series", "RG-NBR Series", "RG-EW Series",
                        "RG-Enterprise Series", "Other Ruijie Models").forEach {
                        FeatureCard(it, "Model အလိုက် Support စစ်ဆေးရန် လိုအပ်", Icons.Default.Router)
                    }
                }
                3 -> {
                    PageTitle("TOOLKIT", "Network Tools")
                    FeatureCard("Speed Test", "Internet speed test", Icons.Default.Speed)
                    FeatureCard("Wi-Fi QR", "Wi-Fi QR Code", Icons.Default.QrCode2)
                    FeatureCard("Cloud Router Info", "Ruijie Cloud Router အချက်အလက် — API လိုအပ်", Icons.Default.Info)
                }
                4 -> {
                    PageTitle("MY ACCOUNT", "ကိုယ်ပိုင် Account")
                    FeatureCard("Email", email, Icons.Default.Email)
                    FeatureCard("Login Type", "Demo Login — Ruijie API မချိတ်ရသေးပါ", Icons.Default.Lock)
                    Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.Logout, null)
                        Spacer(Modifier.width(8.dp))
                        Text("LOG OUT")
                    }
                }
                5 -> {
                    PageTitle("SUPPORT", "Developer Contact")
                    FeatureCard("Telegram", "@akarmoe11", Icons.Default.Send)
                    Button(onClick = onTelegram, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.Send, null)
                        Spacer(Modifier.width(8.dp))
                        Text("CONTACT ON TELEGRAM")
                    }
                }
                6 -> {
                    PageTitle("MORE", "About AKAR MOE MyRuijie")
                    FeatureCard("App Version", "Version 1.0", Icons.Default.Info)
                    FeatureCard("Developer", "AKAR MOE", Icons.Default.Person)
                    Button(onClick = onTelegram, modifier = Modifier.fillMaxWidth()) {
                        Text("Telegram @akarmoe11")
                    }
                }
            }
            Text(
                "မှတ်ချက် - Live Router Management အတွက် Ruijie ရဲ့ တရားဝင် API ချိတ်ဆက်မှု လိုအပ်ပါတယ်။",
                color = WhiteText.copy(alpha = 0.6f),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
private fun RouterCard(
    status: String,
    onDetect: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Panel),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("RUIJIE CLOUD DISCOVERY", color = Blue, fontWeight = FontWeight.Bold)
            Text("ROUTER LIST", color = WhiteText,
                style = MaterialTheme.typography.titleLarge)
            Text(status, color = WhiteText.copy(alpha = 0.7f))
            Button(onClick = onDetect, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Search, null)
                Spacer(Modifier.width(8.dp))
                Text("AUTO DETECT ROUTERS")
            }
        }
    }
}

@Composable
private fun PageTitle(title: String, subtitle: String) {
    Text(title, color = Blue, style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold)
    Text(subtitle, color = WhiteText.copy(alpha = 0.7f))
}

@Composable
private fun FeatureCard(title: String, detail: String, icon: ImageVector) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Panel),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Blue.copy(alpha = 0.3f))
    ) {
        Row(
            Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = Blue, modifier = Modifier.size(26.dp))
            Spacer(Modifier.width(12.dp))
            Column {
                Text(title, color = WhiteText, fontWeight = FontWeight.Bold)
                Text(detail, color = WhiteText.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

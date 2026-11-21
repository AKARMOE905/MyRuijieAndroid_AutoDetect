package com.example.myruijie

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.nativeCanvas

@Composable
fun HackerCodeRain() {
    val transition = rememberInfiniteTransition(label = "hackerTerminal")

    val progress = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(18000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "terminalScroll"
    )

    val codeLines = remember {
        listOf(
            "AKARMOE // SYSTEM INITIALIZED",
            "import android.os.Bundle",
            "class NetworkManager {",
            "fun connectGateway(ip: String) {",
            "val status = checkConnection()",
            "if (status == ONLINE) {",
            "println(\"GATEWAY CONNECTED\")",
            "}",
            "}",
            "import requests",
            "response = requests.get(api_url)",
            "device_status = response.json()",
            "print(\"NETWORK SCANNING...\")",
            "192.168.1.1  GATEWAY ONLINE",
            "PING 192.168.1.1 -t",
            "PACKET RECEIVED [OK]",
            "DNS LOOKUP SUCCESS",
            "PORT 80  HTTP OPEN",
            "PORT 443 HTTPS OPEN",
            "PORT 22  SSH CHECK",
            "MAC ADDRESS DETECTED",
            "DEVICE DISCOVERY ACTIVE",
            "JSON { \"status\": \"online\" }",
            "GET /api/device/list",
            "POST /api/network/status",
            "ACCESS TOKEN VALID",
            "AUTHENTICATION SUCCESS",
            "ROUTER INTERFACE ACTIVE",
            "WAN LINK CONNECTED",
            "LAN PORT SCANNING...",
            "TRAFFIC MONITOR RUNNING",
            "DOWNLOAD FLOW DETECTED",
            "UPLOAD FLOW DETECTED",
            "ENCRYPTION: AES-256",
            "TCP CONNECTION ESTABLISHED",
            "UDP PACKET RECEIVED",
            "SYSTEM SECURITY ACTIVE",
            "while (network.isActive) {",
            "  monitorTraffic()",
            "  checkDeviceStatus()",
            "}",
            "fun scanNetwork() {",
            "  println(\"SCANNING...\")",
            "}",
            "DEVICE STATUS: ONLINE",
            "GATEWAY STATUS: ACTIVE",
            "API RESPONSE: SUCCESS",
            "AKARMOE TERMINAL v1.0",
            ">>> SYSTEM RUNNING...",
            ">>> WAITING FOR DATA..."
        )
    }

    Canvas(Modifier.fillMaxSize()) {
        val paint = android.graphics.Paint().apply {
            color = android.graphics.Color.rgb(0, 255, 65)
            textSize = 32f
            alpha = 255
            typeface = android.graphics.Typeface.create(
                android.graphics.Typeface.MONOSPACE,
                android.graphics.Typeface.BOLD
            )
            isAntiAlias = true
            setShadowLayer(
                12f, 0f, 0f,
                android.graphics.Color.rgb(0, 255, 65)
            )
        }

        val bluePaint = android.graphics.Paint(paint).apply {
            color = android.graphics.Color.rgb(0, 220, 255)
            setShadowLayer(
                10f, 0f, 0f,
                android.graphics.Color.rgb(0, 220, 255)
            )
        }

        val canvas = drawContext.canvas.nativeCanvas
        val lineHeight = 44f
        val totalHeight = codeLines.size * lineHeight
        val scroll = progress.value * totalHeight

        codeLines.forEachIndexed { index, line ->
            val y = ((index * lineHeight + scroll) % totalHeight)

            if (y > 0f && y < size.height + lineHeight) {
                canvas.drawText(
                    line,
                    8f,
                    y,
                    if (index % 9 == 0) bluePaint else paint
                )
            }
        }
    }
}

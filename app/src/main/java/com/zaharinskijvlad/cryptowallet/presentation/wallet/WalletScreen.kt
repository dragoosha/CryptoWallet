package com.zaharinskijvlad.cryptowallet.presentation.wallet

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

private val BrandBlue = Color(0xFF3B82F6)
private val LightBlueBg = Color(0xFFEFF6FF)
private val TextGray = Color(0xFF6B7280)
private val CardBg = Color(0xFFF9FAFB)
private val DangerRed = Color(0xFFEF4444)

@Composable
fun WalletScreen(
    onNavigateToSend: () -> Unit,
    onLogout: () -> Unit,
    viewModel: WalletViewModel = koinViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    WalletScreenContent(
        state = state,
        onNavigateToSend = onNavigateToSend,
        onLogout = { viewModel.logout(onLogout) },
        onCopyAddress = {
            clipboardManager.setText(AnnotatedString(state.address))
            Toast.makeText(context, "Address copied", Toast.LENGTH_SHORT).show()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreenContent(
    state: WalletState,
    onNavigateToSend: () -> Unit,
    onLogout: () -> Unit,
    onCopyAddress: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Wallet Details",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.padding(start = 16.dp)
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- MAIN INFO CARD ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    // EVM Badge
                    Surface(
                        color = LightBlueBg,
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Text(
                            text = "EVM",
                            color = BrandBlue,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }

                    // Address Label
                    Text(
                        text = "Address",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextGray
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Address Value Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CardBg, RoundedCornerShape(12.dp))
                            .padding(16.dp)
                            .clickable { onCopyAddress() }
                    ) {
                        Text(
                            text = state.address,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = FontFamily.Monospace
                            ),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider(color = Color(0xFFF3F4F6))
                    Spacer(modifier = Modifier.height(24.dp))

                    // Network
                    Text(
                        text = "Current Network",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextGray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = state.networkName,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Balance
                    Text(
                        text = "Balance",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextGray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = state.balance,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = BrandBlue
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- ACTIONS ---

            // 1. Copy Address
            ActionItem(
                text = "Copy Address",
                icon = Icons.Default.ContentCopy,
                onClick = onCopyAddress
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 2. Send Transaction
            Button(
                onClick = onNavigateToSend,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandBlue)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Send Transaction",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 3. Logout
            ActionItem(
                text = "Logout",
                icon = Icons.AutoMirrored.Filled.ExitToApp,
                textColor = DangerRed,
                iconColor = DangerRed,
                onClick = onLogout
            )
        }
    }
}

@Composable
fun ActionItem(
    text: String,
    icon: ImageVector,
    textColor: Color = Color.Black,
    iconColor: Color = Color.Gray,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
        color = Color.White
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    color = textColor
                ),
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}

// --- PREVIEWS ---

@Preview(showBackground = true, name = "Wallet Details - Success")
@Composable
private fun WalletScreenPreview() {
    MaterialTheme {
        WalletScreenContent(
            state = WalletState(
                address = "0x6E6F148c27fB49c9760371A9723d08C4d5Af8b4",
                balance = "1.18769409 ETH",
                networkName = "Sepolia - 11155111"
            ),
            onNavigateToSend = {},
            onLogout = {},
            onCopyAddress = {}
        )
    }
}

@Preview(showBackground = true, name = "Wallet Details - Loading")
@Composable
private fun WalletScreenLoadingPreview() {
    MaterialTheme {
        WalletScreenContent(
            state = WalletState(
                address = "...",
                balance = "Loading...",
                networkName = "Checking network...",
                isLoading = true
            ),
            onNavigateToSend = {},
            onLogout = {},
            onCopyAddress = {}
        )
    }
}
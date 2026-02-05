package com.zaharinskijvlad.cryptowallet.presentation.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview

private val BrandBlue = Color(0xFF3B82F6)
private val SuccessGreen = Color(0xFF10B981)
private val SuccessBg = Color(0xFFECFDF5)
private val TextGray = Color(0xFF6B7280)

private const val ETHERSCAN_BASE_URL = "https://sepolia.etherscan.io/tx/"
@Composable
fun SendTransactionScreen(
    onBackClick: () -> Unit,
    viewModel: SendTransactionViewModel = koinViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    SendTransactionContent(
        state = state,
        onBackClick = {
            onBackClick()
            viewModel.clearState()
        },
        onRecipientChange = viewModel::onRecipientChange,
        onAmountChange = viewModel::onAmountChange,
        onSendClick = viewModel::sendTransaction,
        onCopyHash = { hash ->
            clipboardManager.setText(AnnotatedString(hash))
            Toast.makeText(context, "Hash copied!", Toast.LENGTH_SHORT).show()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendTransactionContent(
    state: SendTransactionState,
    onBackClick: () -> Unit,
    onRecipientChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onCopyHash: (String) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Send Transaction", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = state.recipientAddress,
                    onValueChange = onRecipientChange,
                    label = { Text("Recipient Address") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    enabled = state.transactionHash == null,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrandBlue,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color(0xFFF9FAFB),
                        disabledBorderColor = Color(0xFFE5E7EB)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = state.amount,
                    onValueChange = onAmountChange,
                    label = { Text("Amount (ETH)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    enabled = state.transactionHash == null,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrandBlue,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color(0xFFF9FAFB),
                        disabledBorderColor = Color(0xFFE5E7EB)
                    )
                )

                if (state.error != null && state.transactionHash == null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = state.error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                if (state.transactionHash != null) {
                    SuccessView(
                        txHash = state.transactionHash,
                        onCopyHash = { onCopyHash(state.transactionHash) }
                    )
                } else {
                    Button(
                        onClick = onSendClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = !state.isLoading && state.recipientAddress.isNotBlank() && state.amount.isNotBlank(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandBlue)
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("Send Transaction", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SuccessView(
    txHash: String,
    onCopyHash: () -> Unit
) {
    val context = LocalContext.current
    val etherscanUrl = "$ETHERSCAN_BASE_URL$txHash"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, SuccessGreen.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SuccessBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(SuccessGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Transaction Success!",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = SuccessGreen
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = SuccessGreen.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Transaction Hash:",
                style = MaterialTheme.typography.bodyMedium,
                color = TextGray
            )
            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
                    .clickable { onCopyHash() }
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = txHash,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                            color = TextGray
                        ),
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.ContentCopy, null, tint = TextGray, modifier = Modifier.size(20.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(etherscanUrl))
                        context.startActivity(intent)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "View on Etherscan",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = BrandBlue,
                        fontWeight = FontWeight.Medium
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.OpenInNew,
                    contentDescription = null,
                    tint = BrandBlue,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "1. Form State")
@Composable
private fun SendTransactionFormPreview() {
    MaterialTheme {
        SendTransactionContent(
            state = SendTransactionState(
                recipientAddress = "0x742d355...",
                amount = "0.001"
            ),
            onBackClick = {},
            onRecipientChange = {},
            onAmountChange = {},
            onSendClick = {},
            onCopyHash = {}
        )
    }
}

@Preview(showBackground = true, name = "2. Success State")
@Composable
private fun SendTransactionSuccessPreview() {
    MaterialTheme {
        SendTransactionContent(
            state = SendTransactionState(
                recipientAddress = "0x742d355cc634C0032925a3b844Bc9e755595f0bDd7",
                amount = "0.001",
                transactionHash = "0xabc123...def456"
            ),
            onBackClick = {},
            onRecipientChange = {},
            onAmountChange = {},
            onSendClick = {},
            onCopyHash = {}
        )
    }
}
package com.zaharinskijvlad.cryptowallet.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaharinskijvlad.cryptowallet.R
import org.koin.androidx.compose.koinViewModel

private val BrandBlue = Color(0xFF3B82F6)
private val BrandLightBlue = Color(0xFFEFF6FF)
private val TextGray = Color(0xFF6B7280)
private val InputBorder = Color(0xFFE5E7EB)

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = koinViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(state.isSuccessfullyAuthenticated) {
        if (state.isSuccessfullyAuthenticated) {
            onLoginSuccess()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(color = BrandBlue)
            } else {
                if (!state.isOtpSent) {
                    EmailInputContent(
                        email = state.email,
                        onEmailChange = viewModel::onEmailChange,
                        onSendClick = viewModel::sendOtp,
                        error = state.error
                    )
                } else {
                    OtpVerificationContent(
                        email = state.email,
                        otpCode = state.otpCode,
                        onOtpChange = viewModel::onOtpCodeChange,
                        onVerifyClick = viewModel::verifyOtp,
                        onResendClick = { },
                        onBackClick = { },
                        error = state.error
                    )
                }
            }
        }
    }
}

@Composable
fun EmailInputContent(
    email: String,
    onEmailChange: (String) -> Unit,
    onSendClick: () -> Unit,
    error: String?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    color = BrandBlue,
                    shape = RoundedCornerShape(20.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Wallet,
                contentDescription = "Logo",
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Crypto Wallet",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Please sign in to continue",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = TextGray
            )
        )

        Spacer(modifier = Modifier.height(48.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            if (email.isNotEmpty()) {
                Text(
                    text = "Email",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextGray,
                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                )
            }

            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                placeholder = { Text("you@example.com", color = Color.Gray.copy(alpha = 0.5f)) },
                singleLine = true,
                isError = error != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandBlue,
                    unfocusedBorderColor = InputBorder,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            if (error != null) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onSendClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = email.isNotBlank(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BrandBlue,
                disabledContainerColor = BrandBlue.copy(alpha = 0.5f)
            )
        ) {
            Text(
                text = "Send Email OTP",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Color.White
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpVerificationContent(
    email: String,
    otpCode: String,
    onOtpChange: (String) -> Unit,
    onVerifyClick: () -> Unit,
    onResendClick: () -> Unit,
    onBackClick: () -> Unit,
    error: String?
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    "Verify Email",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(BrandLightBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email",
                    tint = BrandBlue,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Check your email",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "We sent a verification code to",
                style = MaterialTheme.typography.bodyMedium.copy(color = TextGray)
            )
            Text(
                text = email,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            OtpInputField(
                otpCode = otpCode,
                onOtpChange = onOtpChange,
                isError = error != null
            )

            if (error != null) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onVerifyClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = otpCode.length >= 6,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandBlue,
                    disabledContainerColor = BrandBlue.copy(alpha = 0.5f)
                )
            ) {
                Text(
                    text = "Verify Code",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Didn't receive the code?",
                style = MaterialTheme.typography.bodyMedium.copy(color = TextGray)
            )

            Text(
                text = "Resend Code",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = BrandBlue,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .clickable { onResendClick() }
                    .padding(8.dp)
            )
        }
    }
}

@Composable
fun OtpInputField(
    otpCode: String,
    onOtpChange: (String) -> Unit,
    isError: Boolean
) {
    BasicTextField(
        value = otpCode,
        onValueChange = {
            if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                onOtpChange(it)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        decorationBox = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(5) { index ->
                    val char = when {
                        index < otpCode.length -> otpCode[index].toString()
                        else -> ""
                    }
                    val isFocused = otpCode.length == index

                    Box(
                        modifier = Modifier
                            .width(56.dp)
                            .height(56.dp)
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(
                                width = if (isFocused || char.isNotEmpty()) 2.dp else 1.dp,
                                color = when {
                                    isError -> MaterialTheme.colorScheme.error
                                    isFocused -> BrandBlue
                                    char.isNotEmpty() -> BrandBlue
                                    else -> InputBorder
                                },
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true, name = "1. Email Input - Default")
@Composable
private fun EmailInputPreview() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Box(contentAlignment = Alignment.Center) {
                EmailInputContent(
                    email = "",
                    onEmailChange = {},
                    onSendClick = {},
                    error = null
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "2. Email Input - Error")
@Composable
private fun EmailInputErrorPreview() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Box(contentAlignment = Alignment.Center) {
                EmailInputContent(
                    email = "invalid-email",
                    onEmailChange = {},
                    onSendClick = {},
                    error = "Invalid email address format"
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "3. OTP Verification - Default")
@Composable
private fun OtpVerificationPreview() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Box(contentAlignment = Alignment.Center) {
                OtpVerificationContent(
                    email = "test@example.com",
                    otpCode = "123",
                    onOtpChange = {},
                    onVerifyClick = {},
                    error = null,
                    onResendClick = {  },
                    onBackClick = { TODO() }
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "4. OTP Verification - Error")
@Composable
private fun OtpVerificationErrorPreview() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Box(contentAlignment = Alignment.Center) {
                OtpVerificationContent(
                    email = "test@example.com",
                    otpCode = "12345",
                    onOtpChange = {},
                    onVerifyClick = {},
                    error = "Incorrect code",
                    onResendClick = {  },
                    onBackClick = { TODO() }
                )
            }
        }
    }
}
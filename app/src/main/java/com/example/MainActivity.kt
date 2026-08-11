package com.example

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.keyboard.settings.PreferencesManager
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color(0xFF1C1B1F)
                ) { innerPadding ->
                    MainKeyboardSetupScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainKeyboardSetupScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val prefsManager = remember { PreferencesManager(context) }

    var testInputText by remember { mutableStateOf("") }
    var hapticsEnabled by remember { mutableStateOf(prefsManager.hapticFeedbackEnabled) }
    var selectedLanguage by remember { mutableStateOf(prefsManager.currentLanguage) }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFD0BCFF),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "ব",
                            color = Color(0xFF381E72),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Column {
                    Text(
                        text = "BanglaPro",
                        color = Color(0xFFE6E1E5),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Keyboard Setup • IME Engine",
                        color = Color(0xFF938F99),
                        fontSize = 13.sp
                    )
                }
            }
        }

        // Activation Banner Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF4A4458)),
            shape = RoundedCornerShape(24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFE8DEF8),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "⚡",
                            fontSize = 22.sp
                        )
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Activation Required",
                        color = Color(0xFFE8DEF8),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "To use BanglaPro as your primary keyboard, enable it in Android System Settings and select it as your active input method.",
                        color = Color(0xFFE6E1E5),
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Setup Steps
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "SETUP STEPS",
                color = Color(0xFFD0BCFF),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp,
                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2B2930)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column {
                    // Step 1
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = Color.Transparent,
                            border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFD0BCFF)),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("1", color = Color(0xFFD0BCFF), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Enable BanglaPro Keyboard", color = Color(0xFFE6E1E5), fontSize = 15.sp, fontWeight = FontWeight.Medium)
                            Text("Add to available on-screen input methods", color = Color(0xFF938F99), fontSize = 13.sp)
                        }
                        Button(
                            onClick = {
                                val intent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A4458)),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("Open", color = Color(0xFFD0BCFF), fontSize = 13.sp)
                        }
                    }

                    HorizontalDivider(color = Color(0xFF4A4458).copy(alpha = 0.5f))

                    // Step 2
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = Color.Transparent,
                            border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFD0BCFF)),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("2", color = Color(0xFFD0BCFF), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Select Input Method", color = Color(0xFFE6E1E5), fontSize = 15.sp, fontWeight = FontWeight.Medium)
                            Text("Set BanglaPro as your current keyboard", color = Color(0xFF938F99), fontSize = 13.sp)
                        }
                        Button(
                            onClick = {
                                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                                imm?.showInputMethodPicker()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD0BCFF)),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("Select", color = Color(0xFF381E72), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Live Typing Field
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "TEST KEYBOARD LIVE",
                color = Color(0xFFD0BCFF),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp,
                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2B2930)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = testInputText,
                        onValueChange = { testInputText = it },
                        placeholder = { Text("Tap here to test typing (e.g. ami -> আমি)...", color = Color(0xFF938F99)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color(0xFFE6E1E5),
                            unfocusedTextColor = Color(0xFFE6E1E5),
                            focusedContainerColor = Color(0xFF1C1B1F),
                            unfocusedContainerColor = Color(0xFF1C1B1F),
                            focusedBorderColor = Color(0xFFD0BCFF),
                            unfocusedBorderColor = Color(0xFF4A4458)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }

        // Configuration / Settings
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "CONFIGURATION",
                color = Color(0xFFD0BCFF),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp,
                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2B2930)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🌐", fontSize = 20.sp)
                            Column {
                                Text("Language Mode", color = Color(0xFFE6E1E5), fontSize = 15.sp, fontWeight = FontWeight.Medium)
                                Text("Bangla Phonetic & English", color = Color(0xFF938F99), fontSize = 13.sp)
                            }
                        }
                        FilterChip(
                            selected = selectedLanguage == PreferencesManager.LANG_BANGLA,
                            onClick = {
                                selectedLanguage = if (selectedLanguage == PreferencesManager.LANG_BANGLA) {
                                    PreferencesManager.LANG_ENGLISH
                                } else {
                                    PreferencesManager.LANG_BANGLA
                                }
                                prefsManager.currentLanguage = selectedLanguage
                            },
                            label = {
                                Text(
                                    if (selectedLanguage == PreferencesManager.LANG_BANGLA) "বাংলা" else "English",
                                    color = Color(0xFFE6E1E5)
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF4A4458),
                                selectedLabelColor = Color(0xFFD0BCFF)
                            )
                        )
                    }

                    HorizontalDivider(color = Color(0xFF4A4458).copy(alpha = 0.5f))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("📳", fontSize = 20.sp)
                            Column {
                                Text("Haptic Feedback", color = Color(0xFFE6E1E5), fontSize = 15.sp, fontWeight = FontWeight.Medium)
                                Text("Keypress vibration response", color = Color(0xFF938F99), fontSize = 13.sp)
                            }
                        }
                        Switch(
                            checked = hapticsEnabled,
                            onCheckedChange = {
                                hapticsEnabled = it
                                prefsManager.hapticFeedbackEnabled = it
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color(0xFF381E72),
                                checkedTrackColor = Color(0xFFD0BCFF)
                            )
                        )
                    }

                    HorizontalDivider(color = Color(0xFF4A4458).copy(alpha = 0.5f))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🎨", fontSize = 20.sp)
                            Column {
                                Text("Design Theme", color = Color(0xFFE6E1E5), fontSize = 15.sp, fontWeight = FontWeight.Medium)
                                Text("Elegant Dark (Applied)", color = Color(0xFF938F99), fontSize = 13.sp)
                            }
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF4A4458)
                        ) {
                            Text(
                                "Active",
                                color = Color(0xFFD0BCFF),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // Privacy Statement
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2B2930)),
            shape = RoundedCornerShape(20.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("🛡️", fontSize = 22.sp)
                Text(
                    text = stringResource(R.string.privacy_note),
                    color = Color(0xFF938F99),
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}


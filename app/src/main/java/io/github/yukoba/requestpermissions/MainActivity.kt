package io.github.yukoba.requestpermissions

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.yukoba.requestpermissions.ui.theme.RequestPermissionsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val requestPermissions = RequestPermissions(this)

        val requiredPermissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
            .apply {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    add(Manifest.permission.NEARBY_WIFI_DEVICES)
                }
            }

        var whyNeedPermissionsOnConfirm = {}

        setContent {
            var whyNeedPermissionsDialogIsShown by remember { mutableStateOf(false) }

            RequestPermissionsTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Button(
                            onClick = {
                                requestPermissions.request(requiredPermissions) {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Permissions granted!",
                                        Toast.LENGTH_LONG,
                                    )
                                        .show()
                                }
                            },
                        ) {
                            Text(
                                text = "Request permissions",
                            )
                        }

                        Button(
                            modifier = Modifier.padding(top = 32.dp),
                            onClick = {
                                requestPermissions.request(
                                    requiredPermissions,
                                    onDenied = { deniedPermissions ->
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Denied permissions: $deniedPermissions",
                                            Toast.LENGTH_LONG,
                                        )
                                            .show()
                                    },
                                ) {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Permissions granted!",
                                        Toast.LENGTH_LONG,
                                    )
                                        .show()
                                }
                            },
                        ) {
                            Text(text = "Request permissions with onDenied")
                        }

                        Button(
                            modifier = Modifier.padding(top = 32.dp),
                            onClick = {
                                requestPermissions.request(
                                    requiredPermissions,
                                    showWhyNeedPermissionsDialog = {
                                        whyNeedPermissionsOnConfirm = it

                                        whyNeedPermissionsDialogIsShown = true
                                    },
                                ) {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Permissions requested in dialog has been granted!",
                                        Toast.LENGTH_LONG,
                                    )
                                        .show()
                                }
                            },
                        ) {
                            Text(text = "Request permissions with why need dialog")
                        }
                    }

                    if (whyNeedPermissionsDialogIsShown) {
                        AlertDialog(
                            onDismissRequest = { whyNeedPermissionsDialogIsShown = false },
                            dismissButton = {
                                Button(
                                    onClick = { whyNeedPermissionsDialogIsShown = false }
                                ) {
                                    Text(text = "Cancel")
                                }
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        whyNeedPermissionsDialogIsShown = false

                                        whyNeedPermissionsOnConfirm()
                                    },
                                ) {
                                    Text(text = "OK")
                                }
                            },
                            text = { Text(text = "Reason of requesting permissions") },
                        )
                    }
                }
            }
        }
    }
}
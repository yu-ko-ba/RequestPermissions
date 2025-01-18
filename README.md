# RequestPermissions

## Installation
settings.gradle.kts
```settings.gradle.kts
dependencyResolutionManagement {
    ...
    repositories {
        ...
+       maven { setUrl("https://jitpack.io") }
    }
}
```

build.gradle.kts
```app/build.gradle.kts
dependencies {
    ...
+   implementation("com.github.yu-ko-ba:RequestPermissions:0.0.1")
}
```

## Example
[MainActivity.kt](https://github.com/yu-ko-ba/RequestPermissions/blob/main/app/src/main/java/io/github/yukoba/requestpermissions/MainActivity.kt)
```MainActivity.kt
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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (whyNeedPermissionsDialogIsShown) {
                        AlertDialog(
                            onDismissRequest = { whyNeedPermissionsDialogIsShown = false },
                            dismissButton = {
                                Button(
                                    onClick = { whyNeedPermissionsDialogIsShown = false }
                                ) { Text(text = "Cancel") }
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        whyNeedPermissionsDialogIsShown = false
                                        whyNeedPermissionsOnConfirm()
                                    },
                                ) { Text(text = "OK") }
                            },
                            text = { Text(text = "Need permissions to work.") },
                        )
                    }

                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Button(
                            onClick = {
                                requestPermissions.request(
                                    permissions = requiredPermissions,
                                    showWhyNeedPermissionsDialog = {
                                        whyNeedPermissionsOnConfirm = it
                                        whyNeedPermissionsDialogIsShown = true
                                    },
                                    onGranted = {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Permissions granted!",
                                            Toast.LENGTH_LONG,
                                        ).show()
                                    },
                                    onDenied = {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Permissions denied.",
                                            Toast.LENGTH_LONG,
                                        ).show()
                                    },
                                )
                            },
                        ) { Text(text = "Request permissions") }
                    }
                }
            }
        }
    }
}
```

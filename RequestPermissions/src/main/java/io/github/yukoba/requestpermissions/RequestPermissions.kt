package io.github.yukoba.requestpermissions

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class RequestPermissions(private val componentActivity: ComponentActivity) {
    private var _onDenied: (permissions: List<String>) -> Unit = {}
    private var _onGranted: () -> Unit = {}

    private val requestPermissionsLauncher =
        componentActivity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { results ->
            val deniedPermissions = mutableListOf<String>()
            for ((permission, granted) in results) {
                if (!granted) {
                    deniedPermissions.add(permission)
                }
            }

            if (deniedPermissions.isEmpty()) {
                // Permission granted
                _onGranted()
            } else {
                // Permission denied
                _onDenied(deniedPermissions)
            }
        }

    fun request(
        permissions: List<String>,
        onDenied: (permissions: List<String>) -> Unit = {},
        showWhyNeedPermissionsDialog: ((onConfirm: () -> Unit) -> Unit)? = null,
        onGranted: () -> Unit,
    ) {
        if (showWhyNeedPermissionsDialog == null) {
            requestPermissions(permissions, onDenied, onGranted)

            return
        }

        showWhyNeedPermissionsDialog {
            requestPermissions(permissions, onDenied, onGranted)
        }
    }

    private fun requestPermissions(
        permissions: List<String>,
        onDenied: (permissions: List<String>) -> Unit = {},
        onGranted: () -> Unit,
    ) {
        _onDenied = onDenied
        _onGranted = onGranted

        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    componentActivity,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionsLauncher.launch(permissions.toTypedArray())

                return
            }
        }

        // Permissions already granted
        _onGranted()
    }
}
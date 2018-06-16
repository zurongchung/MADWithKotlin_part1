package journaler.ollie.study.journaler.permission

import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

abstract class PermissionCompactActivity: AppCompatActivity() {
    private val tag = "Permission extension"
    private val latestPermissionRequest = AtomicInteger()
    private val permissionRequests = ConcurrentHashMap<Int, List<String>>()
    private val permissionCallback = ConcurrentHashMap<List<String>, PermissionRequestCallback>()

    private val defaultPermissionCallback = object : PermissionRequestCallback {
        override fun onPermissionGranted(permissions: List<String>) {
            Log.i(tag, "Permission granted [ $permissions ]")
        }

        override fun onPermissionDenied(permissions: List<String>) {
            Log.e(tag, "Permission denied [ $permissions ]")
        }

    }

    fun requestPermissions(
        vararg permissions: String,
        callback: PermissionRequestCallback = defaultPermissionCallback) {
        val id = latestPermissionRequest.incrementAndGet()
        val items = mutableListOf<String>()
        items.addAll(permissions)

        permissionRequests[id] = items
        permissionCallback[items] = callback

        ActivityCompat.requestPermissions(this, permissions, id)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        val items = permissionRequests[requestCode]
        items?.let {
            val callback = permissionCallback[items]
            callback?.let {
                var success = true
                for (x in 0..grantResults.lastIndex) {
                    val result = grantResults[x]
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        success = false
                        break
                    }
                }
                if (success) {
                    callback.onPermissionGranted(items)
                } else {
                    callback.onPermissionDenied(items)
                }
            }
        }
    }
}
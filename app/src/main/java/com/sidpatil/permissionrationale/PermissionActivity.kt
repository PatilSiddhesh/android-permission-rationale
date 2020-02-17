package com.sidpatil.permissionrationale

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

/*
    Created by SidPatil
 */
private const val TAG: String = "PermissionActivity"

class PermissionActivity : AppCompatActivity() {

    private val REQUEST_CONTACTS_STATE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)
        checkContactsPermission()
    }

    /**
     * Checks permission, if permission has been denied shows rationale for the permission
     */
    private fun checkContactsPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Check if permission is not granted
            Log.d(TAG, "Permission for contacts is not granted")

            // This condition only becomes true if the user has denied the permission previously
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_CONTACTS)) {
                showRationaleDialog(
                    getString(R.string.rationale_title),
                    getString(R.string.rationale_desc),
                    android.Manifest.permission.READ_CONTACTS,
                    REQUEST_CONTACTS_STATE
                )
            } else {
                // Perform a permission check
                Log.d(TAG, "Checking permission")
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_CONTACTS),
                    REQUEST_CONTACTS_STATE
                )
            }
        } else {
            // Permission is already granted, do your magic here!
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CONTACTS_STATE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * Shows rationale dialog for displaying why the app needs permission
     * Only shown if the user has denied the permission request previously
     */
    private fun showRationaleDialog(
        title: String,
        message: String,
        permission: String,
        requestCode: Int
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Ok", { dialog, which ->
                requestPermissions(arrayOf(permission), requestCode)
            })
        builder.create().show()
    }
}

package com.shinedev.digitalent.view.upload

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.shinedev.digitalent.ViewModelWithPrefFactory
import com.shinedev.digitalent.common.reduceFileImage
import com.shinedev.digitalent.common.rotateBitmap
import com.shinedev.digitalent.common.uriToFile
import com.shinedev.digitalent.databinding.ActivityUploadStoryBinding
import com.shinedev.digitalent.pref.AuthPreference
import com.shinedev.digitalent.view.camera.CameraActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = AuthPreference.AUTH_PREFERENCE)

class UploadStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadStoryBinding
    private lateinit var currentPhotoPath: String

    private var getFile: File? = null

    private lateinit var viewModel: UploadStoryViewModel

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        val pref = AuthPreference.getInstance(dataStore)
        viewModel =
            ViewModelProvider(
                this,
                ViewModelWithPrefFactory(pref)
            )[UploadStoryViewModel::class.java]
        setupAction()
        observeData()
    }

    private fun observeData() = with(binding) {
        val owner = this@UploadStoryActivity
        viewModel.apply {
            bitmap.observe(owner) {
                it?.let { bitmap ->
                    ivPreview.setImageBitmap(bitmap)
                }
            }
            uri.observe(owner) {
                it?.let { uri ->
                    ivPreview.setImageURI(uri)
                }
            }
            enableUpload.observe(owner) {
                it?.let { enable ->
                    Log.d("xxx", "isEnable = $enable")
                    uploadButton.isEnabled = enable
                }
            }
            result.observe(owner) {
                it?.let { result ->
                    if (!result.error) {
                        Toast.makeText(
                            this@UploadStoryActivity,
                            "Story berhasil dibuat",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    } else {
                        uploadButton.setLoading(false)
                        val snackBar = Snackbar.make(
                            binding.root, "Gagal mengupload data",
                            Snackbar.LENGTH_LONG
                        ).setAction("Action", null)
                        snackBar.setActionTextColor(Color.RED)

                        val snackBarView = snackBar.view
                        snackBarView.setBackgroundColor(Color.CYAN)
                        val textView =
                            snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                        textView.setTextColor(Color.BLUE)
                        snackBar.show()
                    }
                }
            }
        }
    }

    private fun setupAction() = with(binding) {
        cameraXButton.setOnClickListener { startCameraX() }
        galleryButton.setOnClickListener { startGallery() }
        uploadButton.apply {
            setOnClickListener {
                setLoading(true)
                uploadImage()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(getFile?.path),
                isBackCamera
            )
            viewModel.setBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = uriToFile(selectedImg, this@UploadStoryActivity)

            getFile = myFile
            viewModel.setUri(selectedImg)
        }
    }

    private fun uploadImage() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val description =
                binding.edAddDescription.getStringText().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            viewModel.addNewStory(imageMultipart, description, 0.0, 0.0)
        }
    }
}


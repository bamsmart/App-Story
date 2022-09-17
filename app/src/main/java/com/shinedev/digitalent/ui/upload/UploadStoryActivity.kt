package com.shinedev.digitalent.ui.upload

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.shinedev.digitalent.R
import com.shinedev.digitalent.ViewModelFactory
import com.shinedev.digitalent.common.reduceFileImage
import com.shinedev.digitalent.common.rotateBitmap
import com.shinedev.digitalent.common.uriToFile
import com.shinedev.digitalent.data.DataResult
import com.shinedev.digitalent.databinding.ActivityUploadStoryBinding
import com.shinedev.digitalent.ui.camera.CameraActivity
import com.shinedev.digitalent.ui.camera.CameraActivity.Companion.BACK_CAMERA
import com.shinedev.digitalent.ui.camera.CameraActivity.Companion.PICTURE
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException
import java.util.*


class UploadStoryActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityUploadStoryBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap
    private lateinit var mLatLng: LatLng

    private lateinit var viewModel: UploadStoryViewModel

    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        setupViewModel()
        setupAction()
        observeData()
    }

    private fun setupViewModel() {
        viewModel =
            ViewModelProvider(
                this,
                ViewModelFactory(this@UploadStoryActivity)
            )[UploadStoryViewModel::class.java]
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
            latLng.observe(owner) {
                it?.let {
                    mLatLng = it
                }
            }

            enableUpload.observe(owner) {
                it?.let { enable ->
                    uploadButton.isEnabled = enable
                }
            }
            result.observe(owner) { result ->
                if (result != null) {
                    when (result) {
                        is DataResult.Loading -> {
                            uploadButton.setLoading(true)
                        }
                        is DataResult.Success -> {
                            uploadButton.setLoading(false)
                            Toast.makeText(
                                this@UploadStoryActivity,
                                getString(R.string.text_upload_succeed),
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        }
                        is DataResult.Error -> {
                            uploadButton.setLoading(false)
                            val snackBar = Snackbar.make(
                                binding.root, result.message,
                                Snackbar.LENGTH_LONG
                            ).setAction(getString(R.string.text_try_again), null)
                            snackBar.setActionTextColor(Color.WHITE)
                            val snackBarView = snackBar.view
                            snackBarView.setBackgroundColor(Color.RED)
                            val textView =
                                snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                            textView.setTextColor(Color.WHITE)
                            snackBar.show()
                        }
                    }
                }
            }
        }
    }

    private fun setupAction() = with(binding) {
        cameraXButton.setOnClickListener { startCameraX() }
        galleryButton.setOnClickListener { startGallery() }
        edAddDescription.apply {
            textChangedListener(lifecycleScope) {
                setErrorMessage("")
            }
        }
        uploadButton.apply {
            setOnClickListener {
                if (edAddDescription.getStringText().isNotEmpty()) {
                    setLoading(true)
                    uploadImage()
                } else {
                    edAddDescription.setErrorMessage(getString(R.string.empty_description))
                }
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
                    getString(R.string.text_no_permission),
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
        val chooser = Intent.createChooser(intent, getString(R.string.choose_image))
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra(PICTURE) as File
            val isBackCamera = it.data?.getBooleanExtra(BACK_CAMERA, true) as Boolean

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
            viewModel.addNewStory(
                imageMultipart,
                description,
                mLatLng.latitude.toFloat(),
                mLatLng.longitude.toFloat()
            )
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        getMyLastLocation()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLastLocation()
            }
        }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            mMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    showStartMarker(location)
                } else {
                    Toast.makeText(
                        this@UploadStoryActivity,
                        getString(R.string.location_not_found),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    private fun showStartMarker(location: Location) {
        mLatLng = LatLng(location.latitude, location.longitude)
        viewModel.setLatLng(mLatLng)

        mMap.addMarker(
            MarkerOptions()
                .position(mLatLng)
                .title(getAddressName(location.latitude, location.longitude))
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 17f))
        mMap.setOnPoiClickListener {
            viewModel.setLatLng(mLatLng)
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getAddressName(lat: Double, lon: Double): String? {
        var addressName: String? = null
        val geocoder = Geocoder(this@UploadStoryActivity, Locale.getDefault())
        try {
            val list = geocoder.getFromLocation(lat, lon, 1)
            if (list != null && list.size != 0) {
                addressName = list[0].getAddressLine(0)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return addressName
    }

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}


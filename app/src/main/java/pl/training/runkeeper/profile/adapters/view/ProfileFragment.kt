package pl.training.runkeeper.profile.adapters.view

import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.provider.MediaStore.ACTION_PICK_IMAGES
import android.provider.MediaStore.EXTRA_OUTPUT
import android.provider.MediaStore.Images.Media.DATA
import android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
import android.provider.MediaStore.MediaColumns.TITLE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import pl.training.runkeeper.common.view.RoundedTransformation
import pl.training.runkeeper.databinding.FragmentProfileBinding

class ProfileFragment: Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private var profilePhotoUri: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView();
    }

    private fun initView() {
        loadPhoto(Uri.parse(PHOTO_URL))
        binding.profileImage.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Select")
            .setItems(arrayOf("Take photo", "Use gallery", "Cancel")) { dialog, sourceIdx ->
                when (sourceIdx) {
                    0 -> startCamera()
                    1 -> showGallery()
                    else -> dialog.cancel()
                }
            }
            .show()
    }

    private fun startCamera() {
        profilePhotoUri = null
        val values = ContentValues()
        values.put(TITLE, PROFILE_PHOTO_TITLE)
        profilePhotoUri = requireContext().contentResolver.insert(EXTERNAL_CONTENT_URI, values)
        val intent = Intent(ACTION_IMAGE_CAPTURE)
        intent.putExtra(EXTRA_OUTPUT, profilePhotoUri)
        requestCamera.launch(intent)
    }

    private val requestCamera = registerForActivityResult(StartActivityForResult()) { result ->
        /*if (result.resultCode == RESULT_OK && result.data != null) {
            val image = result.data?.extras?.get("data").let { imageData ->
                val bitmap = imageData as Bitmap?
                binding.profileImage.setImageBitmap(bitmap)
            }
        }*/
        if (result.resultCode == RESULT_OK) {
            profilePhotoUri?.let { loadPhoto(it) }
        }
    }

    private fun loadPhoto(uri: Uri) {
        Picasso.get()
            .load(uri)
            .resize(100, 100)
            .transform(RoundedTransformation(100, 0))
            .into(binding.profileImage)
    }

    private fun showGallery() {
        val intent = Intent(ACTION_PICK_IMAGES)
        requestGallery.launch(intent)
    }

    private val requestGallery = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.data?.let { loadPhoto(it) }
            //result.data?.data?.let { binding.profileImage.setImageURI(it) }
            //result.data?.data?.let { setBitmap(it) }
        }
    }

    private fun setBitmap(uri: Uri) {
        requireContext().contentResolver
            .query(uri, arrayOf(DATA), null, null, null)
            ?.let { cursor ->
                cursor.moveToNext()
                val columnIdx = cursor.getColumnIndex(DATA)
                val path = cursor.getString(columnIdx)
                val bitmap = BitmapFactory.decodeFile(path)
                cursor.close()
                binding.profileImage.setImageBitmap(bitmap)
            }
    }

    companion object {

        const val PHOTO_URL = "https://www.kindpng.com/picc/m/3-36825_and-art-default-profile-picture-png-transparent-png.png"
        const val PROFILE_PHOTO_TITLE = "Profile photo"

    }


}
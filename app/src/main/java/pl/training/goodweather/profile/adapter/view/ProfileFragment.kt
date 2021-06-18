package pl.training.goodweather.profile.adapter.view

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.jakewharton.rxbinding4.widget.textChanges
import com.squareup.picasso.Picasso
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import pl.training.goodweather.R
import pl.training.goodweather.common.view.DialogBox
import pl.training.goodweather.common.view.RoundedTransformation
import pl.training.goodweather.databinding.FragmentProfileBinding
import java.util.*
import java.util.Calendar.*

class ProfileFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: FragmentProfileBinding
    private val disposables = CompositeDisposable()
    private val viewModel: ProfileViewModel by activityViewModels()
    private val cameraRequestCode = 1_000

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        bindView()
        DialogBox().show(requireContext(), "Fill profile data", view)
    }

    private fun initView() {
        Picasso.get()
            .load("https://st.depositphotos.com/2101611/3925/v/600/depositphotos_39258143-stock-illustration-businessman-avatar-profile-picture.jpg")
            .transform(RoundedTransformation(150, 0))
            .into(binding.photoImageView)
    }

    private fun bindView() {
        val fullNameChanges = binding.fullNameEditText.textChanges()
            .map { it.length > 3 }
        val emailChanges = binding.emailEditText.textChanges()
            .map { it.toString() }
            .map(viewModel::isEmailValid)
        fullNameChanges
            .map { getColor(requireContext(), if (it) R.color.text else R.color.invalid ) }
            .subscribe { binding.fullNameEditText.setTextColor(it) }
            .addTo(disposables)
        emailChanges
            .map { getColor(requireContext(), if (it) R.color.text else R.color.invalid ) }
            .subscribe { binding.emailEditText.setTextColor(it) }
            .addTo(disposables)
        Observable.combineLatest(fullNameChanges, emailChanges, { fullName, email -> fullName && email })
            .subscribe {  binding.errorsTextView.text = if (it) "" else getString(R.string.invalid_form) }
            .addTo(disposables)
        binding.birthDateEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                createDialog(requireContext()).show()
                binding.birthDateEditText.clearFocus()
            }
        }
        binding.promoImageBox.setOnClickListener { showAlert() }
        binding.photoImageView.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, cameraRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == cameraRequestCode && resultCode == RESULT_OK) {
            val image = data?.extras?.get("data") as Bitmap
            binding.photoImageView.setImageBitmap(image)
        }
    }

    private fun showAlert() {
        AlertDialog.Builder(requireContext())
            .setMessage("Buy license")
            .setPositiveButton(R.string.ok) { dialog, id ->  }
            .setNegativeButton(getString(R.string.cancel)) { dialog, id ->  }
            .create()
            .show()
    }

    private fun createDialog(context: Context): DatePickerDialog {
        val calendar = Calendar.getInstance()
        val day = calendar.get(DAY_OF_MONTH)
        val month = calendar.get(MONTH)
        val year = calendar.get(YEAR)
        return DatePickerDialog(context, this, year, month, day)
    }

    override fun onDetach() {
        super.onDetach()
        disposables.clear()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        binding.birthDateEditText.setText("$dayOfMonth/$month/$year")
    }

}
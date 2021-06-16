package pl.training.goodweather.profile.adapter.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.kotlin.addTo
import pl.training.goodweather.R
import pl.training.goodweather.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val disposables = CompositeDisposable()
    private val viewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView()
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
    }

    override fun onDetach() {
        super.onDetach()
        disposables.clear()
    }


}
package pl.training.runkeeper.profile.adapters.view

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import pl.training.runkeeper.R
import pl.training.runkeeper.common.view.RoundedTransformation
import pl.training.runkeeper.databinding.FragmentProfileBinding
import java.net.URI

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        loadPhoto(Uri.parse(PHOTO_URL))
    }

    private fun loadPhoto(uri: Uri) {
        Picasso.get()
            .load(uri)
            //.placeholder(R.drawable.ic_weather)
            .resize(100,100)
            .transform(RoundedTransformation(100, 0))
            .into(binding.profileImage)
    }

    companion object {

        const val PHOTO_URL = "https://placehold.co/100x100"
        const val PROFILE_PHOTO_TITLE = "Profile photo"

    }

}
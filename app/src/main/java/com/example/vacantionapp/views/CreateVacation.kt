package com.example.vacantionapp.views

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.vacantionapp.databinding.FragmentCreateVacationBinding
import com.example.vacantionapp.model.DesiredVacation
import com.example.vacantionapp.viewModel.VacationViewModel
import kotlinx.coroutines.launch


class CreateVacation : Fragment() {

    private lateinit var binding: FragmentCreateVacationBinding
    private lateinit var myVacationViewModel: VacationViewModel
    private var myUri: Uri? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myVacationViewModel = ViewModelProvider(this).get(VacationViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateVacationBinding.inflate(inflater, container, false)
        val root = binding.root

        val backBtn = binding.backToVacation
        val addImage = binding.imageViewInput
        val createBtn = binding.userCreateBtn


        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.

                myUri = uri
                if (uri != null) {
                    addImage.setImageURI(uri)
                    Log.d("PhotoPicker", "Selected URI: $uri")
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }

            }

        addImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }

        createBtn.setOnClickListener {

            val hotelName = binding.hotelNameInput.text.toString()
            val vacationLocation =
                binding.vacationLocationInput.text.toString()
            val costAmount = binding.costAmountInput.text.toString()
            val vacationDescription =
                binding.vacationDescriptionInput.text.toString()


            lifecycleScope.launch {
                if (inputCheck(hotelName, vacationLocation, costAmount, vacationDescription) && myUri !=null) {
                    val vacation = DesiredVacation(
                        0,
                        hotelName,
                        vacationLocation,
                        costAmount,
                        vacationDescription,
                        getBitmap(myUri!!)
                    )

                    myVacationViewModel.addVacation(vacation)
                    Toast.makeText(requireContext(), "Successfully created!", Toast.LENGTH_SHORT)
                        .show()

                    val action = CreateVacationDirections.actionCreateVacationToVacationsFragment()
                    findNavController().navigate(action)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "All fields are mandatory!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

        }

        backBtn.setOnClickListener {
            val action = CreateVacationDirections.actionCreateVacationToVacationsFragment()
            findNavController().navigate(action)
        }
        return root
    }


    private suspend fun getBitmap(uri: Uri): Bitmap {
        val loading = ImageLoader(requireContext())
        val request = ImageRequest.Builder(requireContext()).data(uri).build()

        val result = (loading.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }

    private fun inputCheck(
        vacationName: String,
        location: String,
        amount: String,
        description: String,
    ): Boolean {
        return !(TextUtils.isEmpty(vacationName) && TextUtils.isEmpty(location) && TextUtils.isEmpty(
            amount
        ) && TextUtils.isEmpty(description))
    }

}
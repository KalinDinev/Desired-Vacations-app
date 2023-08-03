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
import androidx.navigation.fragment.navArgs
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.vacantionapp.model.DesiredVacation
import com.example.vacantionapp.viewModel.VacationViewModel
import com.example.vacantionapp.databinding.FragmentDetailBinding
import kotlinx.coroutines.launch


class DetailFragment() : Fragment() {

    private val args by navArgs<DetailFragmentArgs>()
    private lateinit var binding: FragmentDetailBinding
    private lateinit var myViewModel: VacationViewModel
    private var myUri: Uri? = null
    private var bitmap: Bitmap? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        val root = binding.root

        val currentId = args.currentId

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.

                myUri = uri
                if (uri != null) {
                    binding.detailImageView.setImageURI(uri)
                    Log.d("PhotoPicker", "Selected URI: $uri")
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }

            }

        myViewModel = ViewModelProvider(this)[VacationViewModel::class.java]
        myViewModel.findViewById(currentId).observe(viewLifecycleOwner) { currentVacation ->
            bitmap = currentVacation.photo

            binding.detailImageView.setImageBitmap(bitmap)
            binding.hotelNameValue.text = currentVacation.hotelName
            binding.detailLocationValue.text = currentVacation.location
            binding.detailAmountValue.text = currentVacation.necessaryMoneyAmount
            binding.detailDescriptionValue.text = currentVacation.description
        }

        val editBtn = binding.editBtn
        editBtn.setOnClickListener {
            editBtn.visibility = View.GONE

            binding.detailImageView.setOnClickListener {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
            }

            val vacationNameInput = binding.detailEditName
            vacationNameInput.visibility = View.VISIBLE
            val vacationEditLocation = binding.detailEditLocation
            vacationEditLocation.visibility = View.VISIBLE
            val vacationEditAmount = binding.detailEditAmount
            vacationEditAmount.visibility = View.VISIBLE
            val vacationEditDescription = binding.detailEditDescription
            vacationEditDescription.visibility = View.VISIBLE
            val vacationEditButton = binding.editVacationBtn
            vacationEditButton.visibility = View.VISIBLE


            vacationEditButton.setOnClickListener {
                val vacationInput = vacationNameInput.text.toString()
                val locationInput = vacationEditLocation.text.toString()
                val amountInput = vacationEditAmount.text.toString()
                val descriptionInput = vacationEditDescription.text.toString()

                lifecycleScope.launch {

                    val bitmapToUpdate = if (myUri != null) {
                        getBitmap(myUri!!)
                    } else {
                        bitmap
                    }

                    if(inputCheck(vacationInput,locationInput,amountInput,descriptionInput)) {

                        val editVacation = DesiredVacation(
                            currentId,
                            vacationInput,
                            locationInput,
                            amountInput,
                            descriptionInput,
                            bitmapToUpdate!!
                        )
                        myViewModel.updateVacation(editVacation)
                        Toast.makeText(requireContext(), "Successfully edited!", Toast.LENGTH_SHORT).show()

                        val action = DetailFragmentDirections.actionDetailFragmentToVacationsFragment()
                        findNavController().navigate(action)
                    }else{
                        Toast.makeText(requireContext(), "Make edit!", Toast.LENGTH_SHORT).show()

                    }

                }

            }
        }

        val backBtn = binding.detailBackBtn
        backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        return root
    }

    private suspend fun getBitmap(uri: Uri): Bitmap {
        val loading = ImageLoader(requireContext())
        val request = ImageRequest.Builder(requireContext()).data(uri).build()

        val result = (loading.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }

    private fun inputCheck(vacationName:String,location:String,amount:String,description:String): Boolean {
        return !(TextUtils.isEmpty(vacationName) && TextUtils.isEmpty(location) &&TextUtils.isEmpty(amount) && TextUtils.isEmpty(description)  )
    }

}

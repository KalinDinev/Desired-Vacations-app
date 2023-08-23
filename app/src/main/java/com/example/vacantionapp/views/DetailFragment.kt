package com.example.vacantionapp.views

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.text.InputType
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
import com.example.vacantionapp.widget.CustomLayout
import kotlinx.coroutines.launch


class DetailFragment() : Fragment() {

    private val args by navArgs<DetailFragmentArgs>()
    private lateinit var binding: FragmentDetailBinding
    private lateinit var myViewModel: VacationViewModel
    private lateinit var customLayout: CustomLayout
    private var myUri: Uri? = null
    private var bitmap: Bitmap? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(layoutInflater)
        val root = binding.root
        customLayout = CustomLayout(requireContext())
        setInitialHint()


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


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



        val showSocials =binding.socialsButton
        showSocials.setOnClickListener {
            val action =DetailFragmentDirections.actionDetailFragmentToModalBottomSheet()
            findNavController().navigate(action)
        }


        val editBtn = binding.editBtn
        editBtn.setOnClickListener {
            editBtn.visibility = View.GONE

            binding.detailImageView.setOnClickListener {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
            }


            val vacationNameInput = binding.editVacationInput
            vacationNameInput.visibility = View.VISIBLE


            val vacationEditLocation = binding.editLocationInput
            vacationEditLocation.visibility = View.VISIBLE

            val vacationEditAmount = binding.editAmountInput
            vacationEditAmount.visibility = View.VISIBLE
            //Setting editText in cost input to be of type NUMBER by default is text!More user friendly!
            vacationEditAmount.editText.inputType = InputType.TYPE_CLASS_NUMBER

            val vacationEditDescription = binding.editDescriptionInput
            vacationEditDescription.visibility = View.VISIBLE

            val vacationEditButton = binding.editVacationBtn
            vacationEditButton.visibility = View.VISIBLE

            var vacationInputValue = vacationNameInput.getUserInputText()
            var locationInputValue = vacationEditLocation.getUserInputText()
            var amountInputValue = vacationEditAmount.getUserInputText()
            var descriptionInputValue = vacationEditDescription.getUserInputText()


            vacationEditButton.setOnClickListener {


                vacationInputValue = vacationNameInput.getUserInputText()
                locationInputValue = vacationEditLocation.getUserInputText()
                amountInputValue = vacationEditAmount.getUserInputText()
                descriptionInputValue = vacationEditDescription.getUserInputText()

                vacationNameInput.editText.clearFocus()
                vacationEditAmount.editText.clearFocus()
                vacationEditLocation.editText.clearFocus()
                vacationEditDescription.editText.clearFocus()


                lifecycleScope.launch {

                    val bitmapToUpdate = if (myUri != null) {
                        getBitmap(myUri!!)
                    } else {
                        bitmap
                    }

                    if (vacationNameInput.editText.text.isEmpty()) {
                        vacationNameInput.errorFields()
                    }
                    if (vacationEditLocation.editText.text.isEmpty()) {
                        vacationEditLocation.errorFields()
                    }


                    if (vacationNameInput.editText.text.isNotEmpty() && vacationEditLocation.editText.text.isNotEmpty()) {


                        val editVacation = DesiredVacation(
                            currentId,
                            vacationInputValue,
                            locationInputValue,
                            amountInputValue,
                            descriptionInputValue,
                            bitmapToUpdate!!
                        )
                        myViewModel.updateVacation(editVacation)
                        Toast.makeText(requireContext(), "Successfully edited!", Toast.LENGTH_SHORT)
                            .show()

                        val action =
                            DetailFragmentDirections.actionDetailFragmentToVacationsFragment()
                        findNavController().navigate(action)

                    } else {
                        Toast.makeText(
                            requireContext(), "Name and location requared!", Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }

        }


        val backBtn = binding.detailBackBtn
        backBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private suspend fun getBitmap(uri: Uri): Bitmap {
        val loading = ImageLoader(requireContext())
        val request = ImageRequest.Builder(requireContext()).data(uri).build()

        val result = (loading.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }

//    private fun inputCheck(
//        vacationName: String,
//        location: String,
//        amount: String,
//        description: String
//    ): Boolean {
//        return !(TextUtils.isEmpty(vacationName) && TextUtils.isEmpty(location) && TextUtils.isEmpty(
//            amount
//        ) && TextUtils.isEmpty(description))
//    }


    private fun setInitialHint() {

        binding.editVacationInput.hintTextView.hint = "Edit Vacation"
        binding.editVacationInput.editText.hint = "Edit Vacation Name"

        binding.editLocationInput.hintTextView.hint = "Edit Location"
        binding.editLocationInput.editText.hint = "Edit Vacation Location"

        binding.editAmountInput.hintTextView.hint = "Edit Amount"
        binding.editAmountInput.editText.hint = "Edit Amount"

        binding.editDescriptionInput.hintTextView.hint = "Edit Description "
        binding.editDescriptionInput.editText.hint = "Edit Description"
    }
}

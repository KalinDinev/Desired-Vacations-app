package com.example.vacantionapp.views

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.text.InputType
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
import com.example.vacantionapp.widget.CustomLayout
import kotlinx.coroutines.launch


class CreateVacation : Fragment() {

    private lateinit var binding: FragmentCreateVacationBinding
    private lateinit var myVacationViewModel: VacationViewModel
    private lateinit var customLayout: CustomLayout
    private var myUri: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        customLayout = CustomLayout(requireContext())
        binding = FragmentCreateVacationBinding.inflate(inflater, container, false)

        val root = binding.root

        setInitialHint()


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myVacationViewModel = ViewModelProvider(this).get(VacationViewModel::class.java)


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


        val vacationNameInput = binding.vacationInput
        val vacationLocationInput = binding.locationInput
        val costAmountInput = binding.costInput
        //Setting editText in cost input to be of type NUMBER by default is text!More user friendly!
        costAmountInput.editText.inputType =InputType.TYPE_CLASS_NUMBER
        val vacationDescriptionInput = binding.descriptionInput


        var vacationInputValue = vacationNameInput.getUserInputText()
        var locationInputValue = vacationLocationInput.getUserInputText()
        var amountInputValue = costAmountInput.getUserInputText()
        var descriptionInputValue = vacationDescriptionInput.getUserInputText()


        createBtn.setOnClickListener {

            vacationInputValue = vacationNameInput.getUserInputText()
            locationInputValue = vacationLocationInput.getUserInputText()
            amountInputValue = costAmountInput.getUserInputText()
            descriptionInputValue = vacationDescriptionInput.getUserInputText()

            vacationNameInput.editText.clearFocus()
            vacationLocationInput.editText.clearFocus()
            costAmountInput.editText.clearFocus()
            vacationDescriptionInput.editText.clearFocus()



            lifecycleScope.launch {

                if (vacationNameInput.editText.text.isEmpty()) {
                    binding.vacationInput.errorFields()
                }

                if (vacationLocationInput.editText.text.isEmpty()) {
                    binding.locationInput.errorFields()
                }

                if (costAmountInput.editText.text.isEmpty()) {
                    binding.costInput.errorFields()
                }

                if (vacationDescriptionInput.editText.text.isEmpty()) {
                    binding.descriptionInput.errorFields()
                }


                if (myUri != null) {

                    if (vacationInputValue.isNotEmpty() && locationInputValue.isNotEmpty() && amountInputValue.isNotEmpty() && descriptionInputValue.isNotEmpty()) {


                        val vacation = DesiredVacation(
                            0,
                            vacationInputValue,
                            locationInputValue,
                            amountInputValue,
                            descriptionInputValue,
                            getBitmap(myUri!!)
                        )

                        myVacationViewModel.addVacation(vacation)
                        Toast.makeText(
                            requireContext(), "Successfully created!", Toast.LENGTH_SHORT
                        ).show()

                        val action =
                            CreateVacationDirections.actionCreateVacationToVacationsFragment()
                        findNavController().navigate(action)
                    }
                } else {

                    Toast.makeText(
                        requireContext(), "All fields are mandatory!", Toast.LENGTH_SHORT
                    ).show()
                }

            }

        }

        backBtn.setOnClickListener {
            val action = CreateVacationDirections.actionCreateVacationToVacationsFragment()
            findNavController().navigate(action)
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
//        description: String,
//    ): Boolean {
//        return !(TextUtils.isEmpty(vacationName) && TextUtils.isEmpty(location) && TextUtils.isEmpty(
//            amount
//        ) && TextUtils.isEmpty(description))
//    }


    private fun setInitialHint() {
        binding.vacationInput.hintTextView.hint = "Vacation Name"
        binding.vacationInput.editText.hint = "Vacation Name"
        binding.locationInput.hintTextView.hint = "Location Name"
        binding.locationInput.editText.hint = "Location Name"
        binding.costInput.hintTextView.hint = "Cost Amount"
        binding.costInput.editText.hint = "Cost"
        binding.descriptionInput.hintTextView.hint = "Description"
        binding.descriptionInput.editText.hint = "Description"


    }

}
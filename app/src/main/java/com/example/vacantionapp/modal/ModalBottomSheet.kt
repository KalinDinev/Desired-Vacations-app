package com.example.vacantionapp.modal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.vacantionapp.databinding.FragmentModalBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ModalBottomSheet : BottomSheetDialogFragment() {

    private val args by navArgs<ModalBottomSheetArgs>()
    private lateinit var binding: FragmentModalBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentModalBottomSheetBinding.inflate(layoutInflater)
        val root = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val homeImage = binding.homeImageModal
        val homeModalText = binding.homeModalText

        homeModalText.setOnClickListener {
            val action = ModalBottomSheetDirections.actionModalBottomSheetToVacationsFragment()
            findNavController().navigate(action)
        }


        homeImage.setOnClickListener {
            val action = ModalBottomSheetDirections.actionModalBottomSheetToVacationsFragment()
            findNavController().navigate(action)
        }


        val socialImage = binding.socialImageModal
        val socialModalText = binding.socialModalText

        socialImage.setOnClickListener {
            val action = ModalBottomSheetDirections.actionModalBottomSheetToSocialsFragment()
            findNavController().navigate(action)
        }

        socialModalText.setOnClickListener {
            val action = ModalBottomSheetDirections.actionModalBottomSheetToSocialsFragment()
            findNavController().navigate(action)
        }

        val editImage = binding.editImageModal
        val editModalText = binding.editModalText

        editImage.setOnClickListener {

            val isEditClicked = true
            val action = ModalBottomSheetDirections.actionModalBottomSheetToDetailFragment(
                args.currentId, isEditClicked
            )
            findNavController().navigate(action)
        }

        editModalText.setOnClickListener {
            val isEditClicked = true
            val action = ModalBottomSheetDirections.actionModalBottomSheetToDetailFragment(
                args.currentId, isEditClicked
            )
            findNavController().navigate(action)
        }


    }

}
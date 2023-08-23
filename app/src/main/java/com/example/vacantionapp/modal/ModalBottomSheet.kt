package com.example.vacantionapp.modal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.vacantionapp.databinding.FragmentModalBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ModalBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding:FragmentModalBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentModalBottomSheetBinding.inflate(layoutInflater)
        val root =binding.root


        return  root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        val homeImage =binding.homeImageModal
        val homeModalText =binding.homeModalText

        homeModalText.setOnClickListener {
            val action =ModalBottomSheetDirections.actionModalBottomSheetToVacationsFragment()
            findNavController().navigate(action)
        }


        homeImage.setOnClickListener {
            val action =ModalBottomSheetDirections.actionModalBottomSheetToVacationsFragment()
            findNavController().navigate(action)
        }
    }

}
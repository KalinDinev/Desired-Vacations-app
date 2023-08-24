package com.example.vacantionapp.modal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.vacantionapp.databinding.FragmentSocialsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ModalSocialSheet : BottomSheetDialogFragment() {

    private  lateinit var binding:FragmentSocialsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        binding = FragmentSocialsBinding.inflate(layoutInflater)
        val root =binding.root


        return  root
    }

}
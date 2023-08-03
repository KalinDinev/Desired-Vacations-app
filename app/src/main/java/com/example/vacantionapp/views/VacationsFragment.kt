package com.example.vacantionapp.views

import VacationAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vacantionapp.viewModel.VacationViewModel
import com.example.vacantionapp.databinding.FragmentVacationsBinding


class VacationsFragment : Fragment() {

    private lateinit var binding: FragmentVacationsBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var myVacationModel: VacationViewModel
    private lateinit var itemTouchHelper: ItemTouchHelper.SimpleCallback


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentVacationsBinding.inflate(inflater, container, false)
        val root = binding.root

        recyclerView = binding.vacationRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        myVacationModel = ViewModelProvider(this)[VacationViewModel::class.java]
        myVacationModel.readAllData.observe(viewLifecycleOwner, Observer { vacation ->
            val adapter = VacationAdapter(vacation)
//            adapter.setOnLongClickListener(this)
            recyclerView.adapter = adapter
            setSwipeToDelete()
        })

        val createBtn = binding.createVacationBtn
        createBtn.setOnClickListener {

            val action = VacationsFragmentDirections.actionVacationsFragmentToCreateVacation()
            findNavController().navigate(action)
        }
        return root

    }

    private fun setSwipeToDelete() {
        itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val vacationToDelete = myVacationModel.readAllData.value?.get(position)

                // Ensure the vacationToDelete is not null before proceeding
                vacationToDelete?.let {
                    myVacationModel.deleteVacation(it)
                    Toast.makeText(requireContext(), "Successfully deleted!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerView)
    }

}
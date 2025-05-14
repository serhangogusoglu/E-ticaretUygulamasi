package com.example.e_ticaretuygulamasi.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_ticaretuygulamasi.data.CartDatabase
import com.example.e_ticaretuygulamasi.adapter.FoodAdapter
import com.example.e_ticaretuygulamasi.R
import com.example.e_ticaretuygulamasi.data.RetrofitClient
import com.example.e_ticaretuygulamasi.databinding.FragmentHomeBinding
import com.example.e_ticaretuygulamasi.viewmodel.FoodViewModel
import com.example.e_ticaretuygulamasi.viewmodel.FoodViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FoodViewModel
    private lateinit var adapter: FoodAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val db = CartDatabase.getDatabase(requireContext())  // Singleton veritabanı
        val factory = FoodViewModelFactory(RetrofitClient.foodApi, db.cartDao())
        viewModel = ViewModelProvider(requireActivity(), factory)[FoodViewModel::class.java]

        adapter = FoodAdapter {
            viewModel.addToCart(it)
            Toast.makeText(requireContext(), "${it.yemekAdi} " + getString(R.string.addedToCart), Toast.LENGTH_SHORT).show()
        }

        binding.recyclerViewFood.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFood.adapter = adapter

        viewModel.foods.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.loadFoods()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

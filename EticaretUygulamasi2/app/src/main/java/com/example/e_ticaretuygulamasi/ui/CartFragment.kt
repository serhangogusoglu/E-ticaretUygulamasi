package com.example.e_ticaretuygulamasi.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.e_ticaretuygulamasi.adapter.CartAdapter
import com.example.e_ticaretuygulamasi.data.CartDatabase
import com.example.e_ticaretuygulamasi.data.CartItem
import com.example.e_ticaretuygulamasi.R
import com.example.e_ticaretuygulamasi.data.RetrofitClient
import com.example.e_ticaretuygulamasi.databinding.FragmentCartBinding
import com.example.e_ticaretuygulamasi.viewmodel.FoodViewModel
import com.example.e_ticaretuygulamasi.viewmodel.FoodViewModelFactory
import kotlinx.coroutines.launch


class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FoodViewModel
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val db = Room.databaseBuilder(requireContext(), CartDatabase::class.java, "cart.db").build()
        val factory = FoodViewModelFactory(RetrofitClient.foodApi, db.cartDao())
        viewModel = ViewModelProvider(requireActivity(), factory)[FoodViewModel::class.java]

        cartAdapter = CartAdapter { cartItem ->
            viewModel.deleteFromCart(cartItem)
        }
        binding.recyclerViewCart.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCart.adapter = cartAdapter

        // Sepet içeriği değiştiğinde toplam fiyatı güncelle
        viewModel.cartItems.observe(viewLifecycleOwner) { cartItems ->
            cartAdapter.submitList(cartItems)
            updateTotalPrice(cartItems)
            binding.buttonConfirm.visibility = if (cartItems.isNotEmpty()) View.VISIBLE else View.GONE
        }

        // ViewModel'dan sepete ürün eklenme olayını dinle ve toplamı güncelle
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.itemAddedToCart.collect { cartItem ->
                val currentList = cartAdapter.currentList.toMutableList()
                currentList.add(cartItem)
                cartAdapter.submitList(currentList)
                binding.recyclerViewCart.scrollToPosition(currentList.size - 1)
                updateTotalPrice(currentList)
                binding.buttonConfirm.visibility = if (currentList.isNotEmpty()) View.VISIBLE else View.GONE
            }
        }

        // ViewModel'dan sepetten ürün silinme olayını dinle ve toplamı güncelle
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.itemRemovedFromCart.collect { removedItem ->
                val currentList = cartAdapter.currentList.toMutableList()
                currentList.remove(removedItem)
                cartAdapter.submitList(currentList)
                updateTotalPrice(currentList)
                binding.buttonConfirm.visibility = if (currentList.isNotEmpty()) View.VISIBLE else View.GONE
                Toast.makeText(requireContext(), "${removedItem.name} " + getString(R.string.deletedFromCart), Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonConfirm.setOnClickListener {
            viewModel.clearCart()

            val intent = Intent(requireContext(), PaymentActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateTotalPrice(cartItems: List<CartItem>) {
        val totalPrice = cartItems.sumOf { it.price }
        binding.textViewTotal.text = "Toplam: ₺ ${totalPrice}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
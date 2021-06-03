package com.frankegan.pokedex.android.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.frankegan.pokedex.android.R
import com.frankegan.pokedex.android.databinding.HomeFragmentBinding
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment(R.layout.home_fragment) {

    private val viewModel by viewModels<HomeViewModel>()

    private val adapter = HomeAdapter()

    private var _binding: HomeFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        binding.pokemonList.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getPokemon().observe(viewLifecycleOwner) { pokemonResult ->
            pokemonResult.fold(
                onSuccess = { data -> adapter.addNextPage(data) },
                onFailure = { err ->
                    Log.e("HomeFragment", err.localizedMessage, err)
                    Snackbar.make(binding.root, "❌ ${err.localizedMessage}", Snackbar.LENGTH_LONG).show()
                }
            )
        }
    }
}
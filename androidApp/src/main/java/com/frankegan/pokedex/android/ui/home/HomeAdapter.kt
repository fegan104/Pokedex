package com.frankegan.pokedex.android.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.frankegan.pokedex.android.databinding.HomeRowItemBinding
import com.frankegan.pokedex.data.Pokemon

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private val pokemon = mutableListOf<Pokemon>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HomeRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pokemon[position])
    }

    override fun getItemCount(): Int = pokemon.size

    fun addNextPage(nextPage: List<Pokemon>) {
        pokemon += nextPage
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: HomeRowItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(pokemon: Pokemon) {
            binding.pokemonName.text = pokemon.name
            binding.pokemonNumber.text = pokemon.formattedNumber
            binding.pokemonImg.load(pokemon.sprites.frontDefault)
        }
    }
}
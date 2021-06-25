package com.frankegan.pokedex.android.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.frankegan.pokedex.android.databinding.HomeRowItemBinding
import com.frankegan.pokedex.data.Pokemon

class HomeAdapter : PagingDataAdapter<Pokemon, HomeAdapter.ViewHolder>(REPO_COMPARATOR) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HomeRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pokemon = getItem(position) ?: return
        holder.bind(pokemon)
    }

    class ViewHolder(private val binding: HomeRowItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(pokemon: Pokemon) {
            binding.pokemonName.text = pokemon.name//.replaceFirstChar { it.titlecase() }
            binding.pokemonNumber.text = pokemon.formattedNumber
            binding.pokemonImg.load(pokemon.sprites.frontDefault)
        }
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Pokemon>() {
            override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
                oldItem == newItem
        }
    }
}
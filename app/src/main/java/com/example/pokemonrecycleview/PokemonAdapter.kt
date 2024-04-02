package com.example.pokemonrecycleview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PokemonAdapter(private val pokemonList: List<Pokemon>) : RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    data class Pokemon(val name: String, val imageUrl: String, val abilities: List<String>)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val pokemonImage: ImageView = view.findViewById(R.id.pokemon_image)
        val pokemonName: TextView = view.findViewById(R.id.pokemon_name)
        val pokemonAbilities: TextView = view.findViewById(R.id.pokemon_abilities)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pokemon_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = pokemonList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pokemon = pokemonList[position]

        Glide.with(holder.itemView)
            .load(pokemon.imageUrl)
            .centerCrop()
            .into(holder.pokemonImage)

        holder.pokemonName.text = pokemon.name
        holder.pokemonAbilities.text = pokemon.abilities.joinToString(", ")

        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Pokemon ${pokemon.name} clicked", Toast.LENGTH_SHORT).show()
        }
    }

}

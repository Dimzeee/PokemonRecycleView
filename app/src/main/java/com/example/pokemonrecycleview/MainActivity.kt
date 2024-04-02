package com.example.pokemonrecycleview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers


class MainActivity : AppCompatActivity() {

    private lateinit var rvPokemon: RecyclerView
    private lateinit var adapter: PokemonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvPokemon = findViewById(R.id.pokemon_list)
        adapter = PokemonAdapter(emptyList())
        rvPokemon.adapter = adapter
        rvPokemon.layoutManager = LinearLayoutManager(this)
        rvPokemon.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        getPokemonData()
    }

    private fun getPokemonData() {
        val client = AsyncHttpClient()

        client.get("https://pokeapi.co/api/v2/pokemon/", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                val pokemonArray = json.jsonObject.getJSONArray("results")
                val pokemonList = mutableListOf<PokemonAdapter.Pokemon>()

                for (i in 0 until pokemonArray.length()) {
                    val pokemonObject = pokemonArray.getJSONObject(i)
                    val pokemonName = pokemonObject.getString("name")
                    val pokemonUrl = pokemonObject.getString("url")

                    fetchPokemonDetails(pokemonName, pokemonUrl, pokemonList)
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.e("Pokemon Error", "Failed to fetch data: $errorResponse", throwable)
            }
        })
    }

    private fun fetchPokemonDetails(name: String, url: String, pokemonList: MutableList<PokemonAdapter.Pokemon>) {
        val client = AsyncHttpClient()

        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                val abilitiesList = mutableListOf<String>()

                val abilitiesArray = json.jsonObject.getJSONArray("abilities")
                for (i in 0 until abilitiesArray.length()) {
                    val ability = abilitiesArray.getJSONObject(i).getJSONObject("ability")
                    val abilityName = ability.getString("name")
                    abilitiesList.add(abilityName)
                }

                val imageUrl = json.jsonObject.getJSONObject("sprites").getString("front_default")

                val pokemon = PokemonAdapter.Pokemon(name, imageUrl, abilitiesList)
                pokemonList.add(pokemon)
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.e("Pokemon Error", "Failed to fetch details for $name: $errorResponse", throwable)
            }
        })
    }
}


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
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var pokemonList: MutableList<PokemonAdapter.Pokemon>
    private lateinit var rvPokemon: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvPokemon = findViewById(R.id.pokemon_list)
        pokemonList = mutableListOf()
        getPokemonData()
    }

    private fun getPokemonData() {
        val randPokemonId = (1..807).random() // There are 807 Pokémon in total
        val url = "https://pokeapi.co/api/v2/pokemon/$randPokemonId"

        val client = AsyncHttpClient()

        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {

                Log.d("Pokemon Success", "$json")
                val jsonObject = json.jsonObject
                val name = jsonObject.getString("name")
                val sprites = jsonObject.getJSONObject("sprites")
                val imageUrl = sprites.getString("front_default")

                val abilitiesList = mutableListOf<String>()
                val abilitiesArray = jsonObject.getJSONArray("abilities")
                for (i in 0 until abilitiesArray.length()) {
                    val ability = abilitiesArray.getJSONObject(i).getJSONObject("ability")
                    val abilityName = ability.getString("name")
                    abilitiesList.add(abilityName)
                }

                val pokemon = PokemonAdapter.Pokemon(name.capitalize(), imageUrl, abilitiesList)
                pokemonList.add(pokemon)

                val adapter = PokemonAdapter(pokemonList)
                rvPokemon.adapter = adapter
                rvPokemon.layoutManager = LinearLayoutManager(this@MainActivity)
                rvPokemon.addItemDecoration(DividerItemDecoration(this@MainActivity, LinearLayoutManager.VERTICAL))
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.e("Pokemon Error", errorResponse, throwable)
            }
        })
    }
}

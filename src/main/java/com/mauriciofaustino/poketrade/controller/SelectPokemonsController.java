package com.mauriciofaustino.poketrade.controller;

import java.util.ArrayList;
import java.util.List;

import me.sargunvohra.lib.pokekotlin.client.PokeApi;
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient;
import me.sargunvohra.lib.pokekotlin.model.Pokemon;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SelectPokemonsController {

    public static final int FAIR_FACTOR = 10;

    @RequestMapping("/")
    public String index(Model model) {
        PokeApi pokeApi = new PokeApiClient();
        var pokemons = pokeApi.getPokemonList(0, 150);
        model.addAttribute("pokemons", pokemons);
        return "index.html";
    }

    @RequestMapping("/trade")
    public String trade(Model model, String[] player1, String[] player2) {
        PokeApi pokeApi = new PokeApiClient();
        List<Pokemon> pokemonsPlayer1 = getPokemons(player1, pokeApi);
        List<Pokemon> pokemonsPlayer2 = getPokemons(player2, pokeApi);
        int baseExpPlayer1 = getSumBaseExp(pokemonsPlayer1);
        int baseExpPlayer2 = getSumBaseExp(pokemonsPlayer2);
        model.addAttribute("player1", baseExpPlayer1);
        model.addAttribute("player2", baseExpPlayer2);
        model.addAttribute("justa", calculateFair(baseExpPlayer1, baseExpPlayer2));
        return "trade.html";
    }

    private boolean calculateFair(int baseExpPlayer1, int baseExpPlayer2) {
        return baseExpPlayer1 - baseExpPlayer2 <= FAIR_FACTOR &&
                baseExpPlayer2 - baseExpPlayer1 <= FAIR_FACTOR  ;
    }

    private int getSumBaseExp(List<Pokemon> pokemonsPlayer1) {
        return pokemonsPlayer1
                .stream()
                .mapToInt(Pokemon::getBaseExperience)
                .sum();
    }

    private List<Pokemon> getPokemons(String[] player, PokeApi pokeApi) {
        List<Pokemon> pokemonsPlayer = new ArrayList<>();
        for (String pokemonId : player) {
            pokemonsPlayer.add(pokeApi.getPokemon(Integer.parseInt(pokemonId)));
        }
        return pokemonsPlayer;
    }
}

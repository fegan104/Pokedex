import SwiftUI
import shared

struct ContentView: View {
    @StateObject var viewModel = HomeViewModel(pokemonRepo: PokemonRepository())
    
    var body: some View {
        NavigationView {
            listView()
                .navigationBarTitle("Pokédex")
        }
    }
    
    private func listView() -> AnyView {
        switch viewModel.pokemonResult {
        case .success(let pokemon):
            return AnyView(
                List {
                    ForEach(0..<pokemon.count) { index in
                        PokemonRow(pokemon: pokemon[index])
                    }
                }
            )
        case .failure(let error):
            return AnyView(Text(error.localizedDescription).multilineTextAlignment(.center))
        }
    }
}

extension ContentView {
    
    class HomeViewModel: ObservableObject {
        let pokemonRepo: PokemonRepository
        
        @Published var pokemonResult: Result<[Pokemon], Error> = .success([])
        
        init(pokemonRepo: PokemonRepository) {
            self.pokemonRepo = pokemonRepo
            self.loadPokemon()
        }
        
        func loadPokemon() {
            pokemonRepo.getPokemonPage(page: 0) { pokemon, error in
                
                if (pokemon != nil){
                    self.pokemonResult = .success(pokemon!)
                } else {
                    self.pokemonResult = .failure(error ?? NSError(domain: "No data found", code: 404, userInfo: nil))
                }
            }
        }
    }
}

extension Pokemon: Identifiable { }

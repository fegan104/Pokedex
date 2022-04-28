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
                List(pokemon) { item in
                    PokemonRow(pokemon: item)
                        .onAppear {
                            viewModel.loadMoreContentIfNeeded(currentItem: item)
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
        @Published var isLoadingPage = false
        private var currentPage = 0
        
        init(pokemonRepo: PokemonRepository) {
            self.pokemonRepo = pokemonRepo
            self.loadMoreContent()
        }
        
        private func loadMoreContent() {
            guard !isLoadingPage else {
                return
            }
            
            isLoadingPage = true
            
            pokemonRepo.getPokemonPage(page: Int32(currentPage)) { pokemon, error in
                DispatchQueue.main.async {
                    self.isLoadingPage = false
                }
                self.currentPage += 1
                
                if (pokemon != nil) {
                    DispatchQueue.main.async {
                        self.pokemonResult = self.pokemonResult.map { currentPokemon in
                            currentPokemon + pokemon!
                        }
                    }
                } else {
                    print(error?.localizedDescription ?? "error")
                    self.pokemonResult = .failure(error ?? NSError(domain: "No data found", code: 404, userInfo: nil))
                }
            }
        }
        
        func loadMoreContentIfNeeded(currentItem item: Pokemon?) {
            guard let item = item else {
                loadMoreContent()
                return
            }
            
            _ = pokemonResult.map { data  in
                let thresholdIndex = data.index(data.endIndex, offsetBy: -5)
                if data.firstIndex(where: { $0.id == item.id }) == thresholdIndex {
                    loadMoreContent()
                }
            }
        }
        
        private func loadPokemon(page: Int) {
            
        }
    }
}

extension Pokemon: Identifiable { }

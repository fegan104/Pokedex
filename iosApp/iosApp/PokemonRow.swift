//
//  PokemonRow.swift
//  iosApp
//
//  Created by Frank Egan on 6/11/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import shared

struct PokemonRow: View {
    var pokemon: Pokemon
    
    var body: some View {
        HStack {
            HStack(alignment: .center, spacing: 10.0) {
                Text(pokemon.formattedNumber)
                Text(pokemon.name)
                
                Spacer()
                
                if let spriteUrl = pokemon.sprites.frontDefault {
                    AsyncImageView(withURL: spriteUrl, width: 72, height: 72)
                }
            }
            Spacer()
        }
    }
}

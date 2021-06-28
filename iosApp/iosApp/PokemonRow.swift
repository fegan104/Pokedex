//
//  PokemonRow.swift
//  iosApp
//
//  Created by Frank Egan on 6/11/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import shared
import URLImage

struct PokemonRow: View {
    var pokemon: Pokemon
    let imageWidth = CGFloat(72.0)
    
    var body: some View {
        HStack {
            HStack(alignment: .center, spacing: 10.0) {
                Text(pokemon.formattedNumber)
                Text(pokemon.name)
                
                Spacer()
                
                if let spriteUrl = pokemon.sprites.frontDefault, let url = URL(string: spriteUrl) {
                    URLImage(url) { image in
                        image
                            .resizable()
                            .aspectRatio(contentMode: .fill)
        
                    }
                    .frame(width: imageWidth, height: imageWidth)
                    .clipped()
                    .cornerRadius(imageWidth / 2.0)
                }
            }
            Spacer()
        }
    }
}

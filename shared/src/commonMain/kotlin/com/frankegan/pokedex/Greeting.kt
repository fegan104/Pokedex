package com.frankegan.pokedex

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}
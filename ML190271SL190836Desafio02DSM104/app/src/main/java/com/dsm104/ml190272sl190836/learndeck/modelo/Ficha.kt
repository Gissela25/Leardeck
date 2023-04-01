package com.dsm104.ml190272sl190836.learndeck.modelo

class Ficha {
    fun key(key: String?) {
    }

    var enunciado: String? = null
        get() = field
        set(value) {
            field = value
        }
    var solucion: String? = null
        get() = field
        set(value) {
            field = value
        }
    var hashkeyFicha: String? = null
        get() = field
        set(value) {
            field = value
        }
    var imgUrl: String? = null
        get() = field
        set(value) {
            field = value
        }
    var key: String? = null
    var per: MutableMap<String, Boolean> = HashMap()

    constructor() {}

    constructor(nombre: String?, solucion: String?, hashkeyFicha: String?, imgUrl: String?) {
        this.enunciado = nombre
        this.solucion = solucion
        this.hashkeyFicha = hashkeyFicha
        this.imgUrl = imgUrl
    }

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "enunciado" to enunciado,
            "solucion" to solucion,
            "hashkeyFicha" to hashkeyFicha,
            "imgUrl" to imgUrl,
            "key" to key,
            "per" to per
        )
    }
}
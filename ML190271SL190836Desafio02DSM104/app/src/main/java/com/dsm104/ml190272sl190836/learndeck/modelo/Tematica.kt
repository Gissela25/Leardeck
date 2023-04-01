package com.dsm104.ml190272sl190836.learndeck.modelo

class Tematica {
    fun key(key: String?) {

    }

    var nombre: String? = null
        get() = field
        set(value) {
            field = value
        }
    var hashkey: String? = null
        get() = field
        set(value) {
            field = value
        }
    var key: String? = null
    var per: MutableMap<String, Boolean> = HashMap()

    constructor() {}
    constructor(nombre: String?, hashKey: String?) {
        this.nombre = nombre
        this.hashkey = hashKey
    }

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "nombre" to nombre, "hashkey" to hashkey, "key" to key, "per" to per
        )
    }
}
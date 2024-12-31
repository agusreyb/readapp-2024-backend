package ar.edu.unsam.algo3.domain

import ar.edu.unsam.algo3.repositorio.Entidad

class Libro(
    var titulo: String = "",
    var autor: Autor,
    var paginas: Int,
    var palabras: Int,
    var cant_ediciones: Int,
    var ventasSemanales: Int,
    var complejo: Boolean,
    val imagen: String
) : Entidad, Cloneable {

    override var id = -1
    val traducciones = mutableListOf<Lenguaje>()
    val idiomaOriginal = autor.lenguaNativa
    var paginasLargo = 600

    fun calculoIdiomasTotales() = traducciones.plus(idiomaOriginal).toMutableSet().size

    fun calculoIdiomasTotalesString(): List<String> {
        return traducciones.map { it.name }
            .plus(idiomaOriginal.name)
            .toMutableSet()
            .toList()
    }

    fun esLargo() = this.paginas >= paginasLargo

    fun esDesafiante() = complejo || this.esLargo()

    fun agregarIdioma(idiomaIngresado: Lenguaje) {
        traducciones.add(idiomaIngresado)
    }

    fun esBestSeller() = ventasSemanales > 10000 && (cant_ediciones >= 2 || traducciones.size >=5)

    fun copy() = this.clone()

}

class Autor(
    var nombre: String,
    var apellido: String,
    var seudonimo: String,
    var lenguaNativa: Lenguaje,
    private var edad: Int,
    private var cantPremios: Int
) : Entidad {

    override var id = -1
    fun esConsagrado()= edad >= 50 && cantPremios >= 1
}
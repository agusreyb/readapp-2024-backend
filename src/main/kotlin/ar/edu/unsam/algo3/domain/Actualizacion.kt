package ar.edu.unsam.algo3.domain

import ar.edu.unsam.algo3.repositorio.Repositorio
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

interface ServiceLibros {

    fun getLibros() : String
}

@Serializable
class SerializableLibro(
    val ID : Int,
    val ediciones : Int,
    val ventasSemanales: Int
){}

class ActualizacionLibros(private val repositorioLibros : Repositorio<Libro>, private val servicioLibros : ServiceLibros) {

    fun actualizarRepositorio() {
        deserializarLibrosJson(servicioLibros.getLibros()).forEach {
            libro ->
            var libroRepositorioClonado = (repositorioLibros.getByID(libro.ID))!!.copy() as Libro
            libroRepositorioClonado.cant_ediciones = libro.ediciones
            libroRepositorioClonado.ventasSemanales =  libro.ventasSemanales
            repositorioLibros.update(libroRepositorioClonado)
        }

    }

    fun deserializarLibrosJson(jsonString : String): List<SerializableLibro> =
        Json.decodeFromString<List<SerializableLibro>>(jsonString)
}
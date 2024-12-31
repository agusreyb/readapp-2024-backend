package ar.edu.unsam.algo3.repositorio

import ar.edu.unsam.algo2.readapp.NotFoundException
import ar.edu.unsam.algo3.domain.*

interface Entidad{
    var id : Int
}
open class Repositorio<T : Entidad>(val criterioBusqueda: TipoCriterio<T>) {
    var elementos: MutableList<T> = mutableListOf()
    var IDActual = 1

    fun getAllElements():List<T> = elementos.toList()

    fun create(elemento: T) {
        elemento.id = IDActual
        IDActual += 1
        elementos.add(elemento)
    }

    fun clear() {
        elementos.clear()
        IDActual = 1
    }

    fun delete(elemento: T) {
        elementos.remove(elemento)
    }

    fun update(elemento: T){
       val indiceAModificar = elementos.indexOfFirst {it.id == elemento.id}
        if (indiceAModificar == -1)
            throw IllegalArgumentException("No se encontró elemento con la misma ID")
        elementos.remove(elementos[indiceAModificar])
        elementos.add(elemento)

    }
    fun getByID(id: Int): T {
        return elementos.find { it.id == id }
            ?: throw NotFoundException("El elemento con id $id no existe")
    }

    fun findById(id: Int): T {
        return getByID(id) // Usa el método ya definido en Repositorio
    }

    fun search(valor: String?): List<T> {
        return if (valor === null) {
            this.elementos.toList()
        } else {
            return criterioBusqueda.filtrarBusqueda(valor, this)
        }
    }

    fun listarCentrosLectura(repositorio: Repositorio<CentroDeLectura>): List<CentroDeLectura> {
        return repositorio.elementos.toList()
    }

    fun contar(): Int {
        return elementos.size
    }
}

interface TipoCriterio<T : Entidad> {
    fun filtrarBusqueda(valor: String, repositorio: Repositorio<T>): List<T>
}

object CriterioLibros : TipoCriterio<Libro> {
    override fun filtrarBusqueda(valor: String, repositorio: Repositorio<Libro>): List<Libro> {
        return repositorio.elementos.filter { it.titulo.contains(valor, ignoreCase = true) ||
                it.autor.apellido.contains(valor, ignoreCase = true) }
    }
}

object CriterioUsuarios: TipoCriterio<Usuario> {
    override fun filtrarBusqueda(valor: String, repositorio: Repositorio<Usuario>): List<Usuario> {
        return repositorio.elementos.filter { it.nombre.contains(valor, ignoreCase = true) ||
                it.apellido.contains(valor, ignoreCase = true) ||
                it.username == valor ||
                it.mail == valor ||
                it.id.toString() == valor
        }
    }
}

object CriterioAutores: TipoCriterio<Autor> {
    override fun filtrarBusqueda(valor: String, repositorio: Repositorio<Autor>): List<Autor> {
        return repositorio.elementos.filter { it.nombre.contains(valor, ignoreCase = true) ||
                it.apellido.contains(valor, ignoreCase = true) ||
                it.seudonimo == valor
        }
    }
}

object CriterioRecomendaciones: TipoCriterio<Recomendacion> {
    override fun filtrarBusqueda(valor: String, repositorio: Repositorio<Recomendacion>): List<Recomendacion> {
        return repositorio.elementos.filter {it.creador.apellido.contains(valor, ignoreCase = true) ||
                it.libros.any{ it.titulo.contains(valor, ignoreCase = true) }
        }
    }
}

object CriterioCentrosDeLectura: TipoCriterio<CentroDeLectura> {
    override fun filtrarBusqueda(valor: String, repositorio: Repositorio<CentroDeLectura>): List<CentroDeLectura> {
        return repositorio.elementos.filter{ it.libro.titulo == valor }
    }
}







package ar.edu.unsam.algo3.service

import ar.edu.unsam.algo2.readapp.BusinessException
import ar.edu.unsam.algo3.domain.Autor
import ar.edu.unsam.algo3.dto.AutorResumenDTO
import ar.edu.unsam.algo3.repositorio.AutoresRepositorio
import ar.edu.unsam.algo3.repositorio.LibrosRepositorio
import org.springframework.stereotype.Service

@Service
class AutorService(val autoresRepositorio: AutoresRepositorio, val librosRepositorio: LibrosRepositorio) {
    fun search(busqueda: String?): List<Autor> = autoresRepositorio.search(busqueda)

    fun getByID(id: Int): Autor {
        return autoresRepositorio.getByID(id)
    }

    fun modificarDatosAutor(autorModificadoDTO: AutorResumenDTO) {
        val autorAModificar = autoresRepositorio.getByID(autorModificadoDTO.id)

        autorAModificar.apply {
            nombre = autorModificadoDTO.nombre
            apellido = autorModificadoDTO.apellido
            lenguaNativa = autorModificadoDTO.lenguaNativa
        }
    }

    fun eliminarAutor(id:Int){
        val autorAEliminar = autoresRepositorio.getByID(id)
        if(!librosRepositorio.elementos.any{libro -> libro.autor == autorAEliminar}){
            autoresRepositorio.delete(autorAEliminar)
        }
        else {
            throw BusinessException("No puede eliminar este autor")
        }

    }
}
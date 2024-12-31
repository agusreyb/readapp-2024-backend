package ar.edu.unsam.algo3.service

import ar.edu.unsam.algo3.repositorio.CentrosDeLecturaRepositorio
import org.springframework.stereotype.Service

@Service
class CentroService(private val centrosLecturaRepositorio: CentrosDeLecturaRepositorio) {

    fun obtenerTotalCentros(): Int {
        return centrosLecturaRepositorio.buscarTodos().size
    }
}
package ar.edu.unsam.algo3.repositorio

import ar.edu.unsam.algo3.domain.CentroDeLectura
import org.springframework.stereotype.Repository

@Repository
class CentrosDeLecturaRepositorio : Repositorio<CentroDeLectura>(CriterioCentrosDeLectura){
    fun crear(centro: CentroDeLectura) {
        create(centro)
    }
    fun buscarTodos(): List<CentroDeLectura> {
        return getAllElements()
    }
}

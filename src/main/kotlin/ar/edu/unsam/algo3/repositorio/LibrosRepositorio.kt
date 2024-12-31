package ar.edu.unsam.algo3.repositorio

import ar.edu.unsam.algo3.domain.Libro
import org.springframework.stereotype.Repository

@Repository
class LibrosRepositorio: Repositorio<Libro> (CriterioLibros)
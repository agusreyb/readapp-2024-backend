package ar.edu.unsam.algo3.repositorio

import ar.edu.unsam.algo3.domain.Autor
import org.springframework.stereotype.Repository

@Repository
class AutoresRepositorio: Repositorio<Autor>(CriterioAutores)
package ar.edu.unsam.algo3.repositorio

import ar.edu.unsam.algo3.domain.Recomendacion
import org.springframework.stereotype.Repository

@Repository
class RecomendacionesRepositorio: Repositorio<Recomendacion>(CriterioRecomendaciones)
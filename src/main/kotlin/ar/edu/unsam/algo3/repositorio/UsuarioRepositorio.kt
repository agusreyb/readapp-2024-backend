package ar.edu.unsam.algo3.repositorio

import ar.edu.unsam.algo3.domain.Usuario
import org.springframework.stereotype.Repository

@Repository
class UsuariosRepositorio: Repositorio<Usuario> (CriterioUsuarios)
package ar.edu.unsam.algo3.bootstrap

import ar.edu.unsam.algo3.domain.Autor
import ar.edu.unsam.algo3.domain.Lenguaje
import ar.edu.unsam.algo3.repositorio.AutoresRepositorio
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component

@Component
class AutoresBootstrap (
    val autoresRepositorio: AutoresRepositorio
): InitializingBean{
    private val autores = mapOf(
        "Nuevo_Autor" to Autor("","","", Lenguaje.Inglés, 0,0),
        "Hirsch" to Autor("Alex", "Hirsch","A.Hirsch" , Lenguaje.Inglés, 39, 1),
        "McCurdy" to Autor("Janette", "McCurdy","J.McCurdy", Lenguaje.Inglés, 32, 0),
        "Cervantes" to Autor("Miguel", "de Cervantes","Cervantes" , Lenguaje.Español, 68, 10),
        "Cinetto" to Autor("Liliana", "Cinetto","Cinetto", Lenguaje.Español, 57, 2),
        "Christie" to Autor("Agatha", "Christie","A.Christie" , Lenguaje.Inglés, 85, 15),
        "Bronte" to Autor("Charlotte", "Bronte","C.Bronte", Lenguaje.Inglés, 38, 8),
        "Exupery" to Autor("Antoine", "De Saint-Exupery","S.Exupery" , Lenguaje.Francés, 44, 4),
        "Austen" to Autor("Jane", "Austen","Austen", Lenguaje.Inglés, 41, 12),
        "Paolini" to Autor("Christopher", "Paolini","C.Paolini" , Lenguaje.Inglés, 40, 3),
        "Miller" to Autor("Madeline", "Miller","M.Miller", Lenguaje.Inglés, 46, 1),
        "Burton" to Autor("Jessie", "Burton","J.Burton", Lenguaje.Inglés, 42, 0),
        "Barker" to Autor("Pat", "Barker","P.Barker", Lenguaje.Inglés, 81, 2),
        "Riordan" to Autor("Rick", "Riordan","R.Riordan", Lenguaje.Inglés, 60, 5),
        "Rowling" to Autor("J.K.", "Rowling","J.K.", Lenguaje.Inglés, 59, 25),
        "Bla" to Autor("Bla", "Bla","R.Riordan", Lenguaje.Inglés, 60, 5),
    )

    fun crearAutores() {
        autores.values.forEach { autor -> autoresRepositorio.apply { create(autor) } }
    }

    override fun afterPropertiesSet() {
        this.crearAutores()
    }
}
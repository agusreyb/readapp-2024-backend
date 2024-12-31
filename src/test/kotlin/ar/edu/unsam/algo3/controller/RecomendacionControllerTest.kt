package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo2.readapp.GlobalExceptionHandler
import ar.edu.unsam.algo3.domain.*
import ar.edu.unsam.algo3.dto.RecomendacionUpdateDTO
import ar.edu.unsam.algo3.repositorio.RecomendacionesRepositorio
import ar.edu.unsam.algo3.repositorio.UsuariosRepositorio
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@SpringBootTest
@AutoConfigureMockMvc
@Import(GlobalExceptionHandler::class)
@DisplayName("Dado un controller de recomendaciones")
class RecomendacionControllerTest(@Autowired val mockMvc: MockMvc) {

    @Autowired
    lateinit var recomendacionesRepositorio: RecomendacionesRepositorio

    @Autowired
    lateinit var usuariosRepositorio: UsuariosRepositorio
    lateinit var usuario: Usuario
    lateinit var otroUsuario: Usuario


    @BeforeEach
    fun init() {
        usuariosRepositorio.clear()
        recomendacionesRepositorio.clear()
        usuario = Usuario("Juan", "Perez", "juancito", "juanperez@gmail.com", "password", 2002, 9, 12, Lenguaje.Español, 200.0, Leedor(), Promedio(), "")
        otroUsuario = Usuario("Maria", "Gomez", "mariagomez", "mariagomez@gmail.com", "password", 2006, 10, 7, Lenguaje.Inglés, 250.0, Leedor(), Ansioso(), "")
        usuariosRepositorio.apply {
            create(usuario)
            create(otroUsuario)
        }

        val recomendacion1 = Recomendacion(titulo = "Recomendacion Loca", usuario, "Es un conjunto de libros muy random. Disfrutalo!", false)
        val recomendacion2 = Recomendacion(titulo = "Recomendacion Misteriosa", usuario, "Espero que te guste este conjunto de libros de misterios de Agatha Christie")
        recomendacionesRepositorio.apply {
            create(recomendacion1)
            create(recomendacion2)
        }
    }

    @Test
    fun `se pueden obtener todas las recomendaciones aptas para el usuario`() {
        mockMvc
            .perform(MockMvcRequestBuilders.get("/recomendaciones/busqueda/1"))
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.length()").value(2))
    }

    @Test
    fun `se pueden buscar recomendaciones por el apellido del creador`() {
        val recomendacion3 = Recomendacion(titulo = "Clasicos!",otroUsuario, "Te dejo una lista de libros clásicos que TENES que leer" )
        recomendacionesRepositorio.create(recomendacion3)

        assertEquals(3, recomendacionesRepositorio.getAllElements().size)
        mockMvc
            .perform(MockMvcRequestBuilders.get("/recomendaciones/busqueda/1?busqueda=${usuario.apellido}"))
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.length()").value(2))
    }

    @Test
    fun `se puede borrar una recomendacion`() {
        val recomendacion = Recomendacion(titulo = "Clasicos!",usuario, "Te dejo una lista de libros clásicos que TENES que leer" )
        recomendacionesRepositorio.create(recomendacion)
        mockMvc.perform(MockMvcRequestBuilders.delete("/recomendaciones/eliminar-recomendacion/3"))
            .andExpect { status().isOk }
        assertEquals(2, recomendacionesRepositorio.getAllElements().size)
    }


    @Test
    fun `usuario puede editar una recomendacion`() {
        val idRecomendacion = 1
        val recomendacion = recomendacionesRepositorio.getByID(idRecomendacion)
        val listaLibros = listOf(1, 2)
        val recomendacionUpdateDTO = RecomendacionUpdateDTO(
            idRecomendacion,
            usuario.id,
            "Recomendacion fantasma",
            true,
            "Boo",
            listaLibros
            )
        val mapper = ObjectMapper()
        assertEquals("Recomendacion Loca",recomendacion.titulo)
        mockMvc.perform(MockMvcRequestBuilders
            .patch("/recomendaciones/editar/por/${usuario.id}")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(recomendacionUpdateDTO))
        ).andExpect(status().isOk)


        assertEquals("Recomendacion fantasma", recomendacion.titulo, "No se editó el título correctamente")
        assertEquals(true, recomendacion.esPublica, "La recomendación no se volvió pública")
        assertEquals("Boo", recomendacion.descripcion, "No se actualizó la descripción")
        assertEquals( listOf(1, 2) , recomendacion.libros.map { it.id }, "La lista de libros no fue modificada")
    }

    @Test
    fun `usuario no puede editar una recomendacion`() {
        val idRecomendacion = 1
        val recomendacion = recomendacionesRepositorio.getByID(idRecomendacion)
        val listaLibros = listOf(1, 2)
        val recomendacionUpdateDTO = RecomendacionUpdateDTO(
            idRecomendacion,
            usuario.id,
            "Recomendacion fantasma",
            true,
            "Boo",
            listaLibros
        )
        val mapper = ObjectMapper()
        assertEquals("Recomendacion Loca",recomendacion.titulo)
        mockMvc.perform(MockMvcRequestBuilders
            .patch("/recomendaciones/editar/por/${otroUsuario.id}")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(recomendacionUpdateDTO))
        )   .andExpect(status().isNotFound)
            .andExpect { assertTrue(it.resolvedException is IllegalArgumentException) }
            .andExpect { assertEquals("El usuario '$otroUsuario' no tiene permiso para editar esta recomendación.", it.resolvedException?.message) }
    }

}
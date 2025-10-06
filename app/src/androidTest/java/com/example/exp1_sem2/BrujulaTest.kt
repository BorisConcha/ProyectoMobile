package com.example.exp1_sem2

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.*
import com.example.exp1_sem2.viewmodel.UsuarioViewModel
import org.junit.Test
import org.junit.Rule
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4

@RunWith(AndroidJUnit4::class)
class BrujulaTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun testLogin_ElementosVisibles() {
        val usuarioViewModel = UsuarioViewModel()

        composeTestRule.setContent {
            Login(usuarioViewModel = usuarioViewModel)
        }
        
        composeTestRule
            .onNodeWithContentDescription("Titulo del login de la app")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription("Campo del ingresar nombre de usuario")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription("Campo para ingresar contraseña")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription("Boton para iniciar sesion")
            .assertIsDisplayed()
    }
    
    @Test
    fun testLogin_CompletarCampos() {
        val usuarioViewModel = UsuarioViewModel()

        composeTestRule.setContent {
            Login(usuarioViewModel = usuarioViewModel)
        }
        
        composeTestRule
            .onNodeWithContentDescription("Campo del ingresar nombre de usuario")
            .performTextInput("juanito555")
        
        composeTestRule
            .onNodeWithContentDescription("Campo para ingresar contraseña")
            .performTextInput("123456")
        
        composeTestRule
            .onNodeWithContentDescription("Campo del ingresar nombre de usuario")
            .assertTextContains("juanito555")
    }
    
    @Test
    fun testLogin_LoginExitoso() {
        val usuarioViewModel = UsuarioViewModel()

        composeTestRule.setContent {
            Login(usuarioViewModel = usuarioViewModel)
        }
        
        composeTestRule
            .onNodeWithContentDescription("Campo del ingresar nombre de usuario")
            .performTextInput("mari123")

        composeTestRule
            .onNodeWithContentDescription("Campo para ingresar contraseña")
            .performTextInput("maria18")
        
        composeTestRule
            .onNodeWithContentDescription("Boton para iniciar sesion")
            .performClick()
        
        composeTestRule.waitUntil(timeoutMillis = 3000) {
            composeTestRule
                .onAllNodesWithText("Credenciales incorrectas")
                .fetchSemanticsNodes().isEmpty()
        }
    }


    @Test
    fun testLogin_LoginFallido() {
        val usuarioViewModel = UsuarioViewModel()

        composeTestRule.setContent {
            Login(usuarioViewModel = usuarioViewModel)
        }
        
        composeTestRule
            .onNodeWithContentDescription("Campo del ingresar nombre de usuario")
            .performTextInput("usuarioInvalido")

        composeTestRule
            .onNodeWithContentDescription("Campo para ingresar contraseña")
            .performTextInput("passwordIncorrecta")
        
        composeTestRule
            .onNodeWithContentDescription("Boton para iniciar sesion")
            .performClick()
        
        composeTestRule.waitUntil(timeoutMillis = 3000) {
            composeTestRule
                .onAllNodesWithText("Credenciales incorrectas")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule
            .onNodeWithText("Credenciales incorrectas")
            .assertIsDisplayed()
    }

    @Test
    fun testLogin_VerificacionBotones() {
        val usuarioViewModel = UsuarioViewModel()

        composeTestRule.setContent {
            Login(usuarioViewModel = usuarioViewModel)
        }
        
        composeTestRule
            .onNodeWithContentDescription("Enlace para poder recuperar contraseña")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription("Enlace para crear una nueva cuenta")
            .assertIsDisplayed()
    }

}
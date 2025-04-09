import Entities.PrestamoEntity;
import Entities.UsuarioEntity;
import Exceptions.CampoObligatorioException;
import Exceptions.CampoUnicoException;
import Services.PrestamoService;
import Services.UsuarioService;

import java.util.NoSuchElementException;

public class Main {
    public static void main(String[] args) {

        UsuarioService usuarioService = UsuarioService.getInstance();
        PrestamoService prestamoService = PrestamoService.getInstance();

/// Cambiar valores para probar
        UsuarioEntity usuario = new UsuarioEntity("Manuel", "manuelpalaciosinza@gmail.com");
        UsuarioEntity usuario1 = new UsuarioEntity("Repetido", "manuelpalaciosinza@gmail.com");
        PrestamoEntity prestamo = new PrestamoEntity(1, 5);
        PrestamoEntity prestamo1 = new PrestamoEntity(214, 342);

        /// Agregar Usuario
        try {
            usuarioService.guardarUsuario(usuario);
            usuarioService.guardarUsuario(usuario1);
        } catch (CampoObligatorioException e) {
            System.out.println(e.getMessage());
        } catch (CampoUnicoException e) {
            System.out.println(e.getMessage());
        }

        /// Eliminar Usuario
        try {
            usuarioService.borrarUsuario(17);
            usuarioService.borrarUsuario(10000);
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
        /// Ver todos Usuarios
        usuarioService.listarUsuarios();

        /// Ver Usuarios con Prestamos
        System.out.println("Usuarios con prestamos: " + usuarioService.listarUsuariosConPrestamos());

        /// Cargar Prestamo
        try {
            prestamoService.cargarPrestamo(prestamo);
            prestamoService.cargarPrestamo(prestamo1);
        } catch (CampoObligatorioException e) {
            System.out.println(e.getMessage());
        }
    }
}
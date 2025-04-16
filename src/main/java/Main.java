import Entities.LibroEntity;
import Entities.PrestamoEntity;
import Entities.UsuarioEntity;
import Exceptions.CampoObligatorioException;
import Exceptions.CampoUnicoException;
import Repositories.impl.LibroRepository;
import Services.LibroService;
import Services.PrestamoService;
import Services.UsuarioService;

import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {

        UsuarioService usuarioService = UsuarioService.getInstance();
        PrestamoService prestamoService = PrestamoService.getInstance();
        LibroService libroService = LibroService.getInstance();
/// Cambiar valores para probar
        UsuarioEntity usuario = new UsuarioEntity("Manuel", "manuelpalaciosinza@gmail.com");
        UsuarioEntity usuario1 = new UsuarioEntity("Repetido", "manuelpalaciosinza@gmail.com");
        PrestamoEntity prestamo = new PrestamoEntity(7, 5);
        PrestamoEntity prestamo1 = new PrestamoEntity(214, 342);

        /*/// Agregar Usuario
        try {
            usuarioService.guardarUsuario(usuario);
            usuarioService.guardarUsuario(usuario1);
        } catch (CampoObligatorioException | CampoUnicoException e) {
            System.out.println(e.getMessage());
        }

        /// Eliminar Usuario
        try {
            usuarioService.borrarUsuario(17);
            usuarioService.borrarUsuario(10000);
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        } */
        /*
        /// Ver todos Usuarios
        usuarioService.listarUsuarios();

        /// Ver Usuarios con Prestamos
        System.out.println("Usuarios con prestamos: " + usuarioService.listarUsuariosConPrestamos());
         */

        /*
        /// Cargar Prestamo
        prestamoService.cargarPrestamo(prestamo);
         */


        ///Devolver Prestamo
        prestamoService.devolverPrestamo(2);

        /*
        ///Ver todos los prestamos
        System.out.println("Prestamos:" + prestamoService.verTodos());
        System.out.println("Prestamos Activos: "+prestamoService.verActivos());
        */
        /*///Libro mas prestado
        Optional<LibroEntity> libroMasPrestadoOp = libroService.libroMasPrestado();
        libroMasPrestadoOp.ifPresent(System.out::println);
         */

        ///System.out.println("Cantidad de libros disponibles: " + libroService.totalLibrosDisponibles());

        ///System.out.println("Libros: " + libroService.verTodos());

        /*
        ///Usuario con mas prestamos
        Optional<UsuarioEntity> usuarioEntityOptional = usuarioService.usuarioConMasPrestamos();
        usuarioEntityOptional.ifPresent(System.out::println);

         */
        /*
        System.out.println("Promedio de prestamos por usuarios: " + prestamoService.promedioPrestamos());
        */
    }
}
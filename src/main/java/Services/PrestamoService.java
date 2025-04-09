package Services;

import Entities.LibroEntity;
import Entities.PrestamoEntity;
import Entities.UsuarioEntity;
import Exceptions.CampoObligatorioException;
import Repositories.impl.LibroRepository;
import Repositories.impl.PrestamoRepository;
import Repositories.impl.UsuarioRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class PrestamoService {
    private static PrestamoService instance;
    private PrestamoRepository prestamoRepository;
    private UsuarioRepository usuarioRepository;
    private LibroRepository libroRepository;
    private PrestamoService(){
        prestamoRepository = PrestamoRepository.getInstance();
        usuarioRepository =  UsuarioRepository.getInstance();
        libroRepository = LibroRepository.getInstance();
    }
    public static PrestamoService getInstance(){
        if(instance == null){
            instance = new PrestamoService();
        }
        return instance;
    }

    /// No es necesario hacer verificaciones que hace automaticamente la base de datos
    public void cargarPrestamo(PrestamoEntity prestamo){
        try {
            if (prestamo.getUsuario_id() == null || prestamo.getLibro_id() == null) {
                throw new CampoObligatorioException("Error: Debe ingresar un id de libro y id de usuario");
            }
            if (!findAllIdLibros().contains(prestamo.getLibro_id()) || !findAllIdUsers().contains(prestamo.getUsuario_id())) {
                throw new NoSuchElementException("El usuario o libro ingresado no existen en el sistema");
            }
            prestamoRepository.save(prestamo);
        }catch (SQLException e){
            System.out.println("Error en la conexion a la DB: " + e.getMessage());
        }catch (CampoObligatorioException | NoSuchElementException e){
            System.out.println(e.getMessage());
        }
    }

    private List<Integer> findAllIdUsers (){
        List<Integer> listId = new ArrayList<>();
        try {
            listId = usuarioRepository.findAll().stream()
                    .map(UsuarioEntity::getId)
                    .toList();
        } catch (SQLException e) {
            System.out.println("Error en la conexion con la base de datos: " + e.getMessage());
        }
        return listId;
    }
    private List<Integer> findAllIdLibros (){
        List<Integer> listId = new ArrayList<>();
        try {
            listId = libroRepository.findAll().stream()
                    .map(LibroEntity::getId)
                    .toList();
        }catch (SQLException e){
            System.out.println("Error en la conexion con la base de datos: " + e.getMessage());
        }
        return listId;
    }
}

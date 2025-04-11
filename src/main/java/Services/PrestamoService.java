package Services;

import Entities.LibroEntity;
import Entities.PrestamoEntity;
import Entities.UsuarioEntity;
import Exceptions.ExcedePrestamosException;
import Exceptions.StockInsuficienteException;
import Repositories.impl.LibroRepository;
import Repositories.impl.PrestamoRepository;
import Repositories.impl.UsuarioRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public void cargarPrestamo(PrestamoEntity prestamo) {
        try {
            if(getPrestamosActivosUsuario(prestamo.getUsuario_id()) >= 5){
                throw new ExcedePrestamosException("El usuario tiene mas de 5 prestamos activos");
            }
            if (getStockLibro(prestamo.getLibro_id()) < 1){
                throw new StockInsuficienteException("El libro no tiene unidades en stock");
            }

            //Al comprobar que el usuario tiene menos de 5 prestamos y hay stock suficiente,
            //Resto el stock del libro y lo actualizo en la db y luego creo el prestamo
            restarStock(prestamo.getLibro_id());
            prestamoRepository.save(prestamo);
            System.out.println("Prestamo realizado con exito!");
        }catch (SQLException e){
            System.out.println("Error en la conexion a la DB: " + e.getMessage());
        }catch (ExcedePrestamosException | StockInsuficienteException e){
            System.out.println(e.getMessage());
        }
    }

    ///Esta roto por el tema de date null. Consultar en clase
    public void devolverPrestamo(PrestamoEntity prestamo){
        try {
            LocalDate fechaDevolucion = LocalDate.now();
            //Al devolver el libro, se suma el stock y se actualiza la fecha de devolucion del prestamo
            prestamo.setFecha_devolucion(fechaDevolucion);
            prestamoRepository.update(prestamo);
            sumarStock(prestamo.getLibro_id());
            System.out.println("Prestamo devuelto con exito");
        }catch (SQLException e){
            System.out.println("Error al devolver el prestamo: " + e.getMessage());
        }
    }

    public ArrayList<PrestamoEntity> verTodos (){
        ArrayList<PrestamoEntity> prestamoEntities = new ArrayList<>();
        try {
            prestamoEntities = prestamoRepository.findAll();
        }catch (SQLException e){
            System.out.println("Error al visualizar los prestamos: "+e.getMessage());
        }
        return prestamoEntities;
    }
    public List<PrestamoEntity> verActivos(){
        List<PrestamoEntity> prestamoEntities = new ArrayList<>();
        try {
            prestamoEntities = prestamoRepository.findAll().stream()
                    .filter(prestamo -> prestamo.getFecha_devolucion() == null)
                    .toList();
        }catch (SQLException e){
            System.out.println("Error al visualizar los prestamos: "+e.getMessage());
        }
        return prestamoEntities;
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

    private int getStockLibro (int id_libro){
        int stock = 0;
        try {
            Optional<LibroEntity> libroAPrestar = libroRepository.findById(id_libro);
            if (libroAPrestar.isPresent()){
                stock = libroAPrestar.get().getUnidades_disponibles();
            }
        } catch (SQLException e) {
            System.out.println("El libro buscado no se encontro en la base de datos: " + e.getMessage());
        }
        return stock;
    }
    private long getPrestamosActivosUsuario(int id_usuario){
        long cantidadPrestamos = 0;
        try {
            ArrayList<PrestamoEntity> prestamosUsuario = prestamoRepository.findAll();
            cantidadPrestamos = prestamosUsuario.stream()
                    .filter(prestamoEntity -> prestamoEntity.getUsuario_id().equals(id_usuario))
                    .filter(prestamoEntity -> prestamoEntity.getFecha_devolucion() == null)
                    .count();
        }catch (SQLException e){
            System.out.println("Error en la busqueda de prestamos en la base de datos: " + e.getMessage());
        }
        return cantidadPrestamos;
    }
    private void restarStock (int id_libro){
        try {
            Optional<LibroEntity> libro = libroRepository.findById(id_libro);
            if (libro.isPresent()){
                libro.get().setUnidades_disponibles(libro.get().getUnidades_disponibles() - 1);
                libroRepository.update(libro.get());
            }
        }catch (SQLException e){
            System.out.println("Error al actualizar el stock del libro: "+e.getMessage());
        }
    }

    private void sumarStock (int id_libro){
        try {
            Optional<LibroEntity> libro = libroRepository.findById(id_libro);
            if (libro.isPresent()){
                libro.get().setUnidades_disponibles(libro.get().getUnidades_disponibles() + 1);
                libroRepository.update(libro.get());
            }
        }catch (SQLException e){
            System.out.println("Error al actualizar el stock del libro: "+e.getMessage());
        }
    }

    private long getPrestamosTotalesUsuario(int id_usuario){
        long cantidadPrestamos = 0;
        try {
            ArrayList<PrestamoEntity> prestamosUsuario = prestamoRepository.findAll();
            cantidadPrestamos = prestamosUsuario.stream()
                    .filter(prestamoEntity -> prestamoEntity.getUsuario_id().equals(id_usuario))
                    .count();
        }catch (SQLException e){
            System.out.println("Error en la busqueda de prestamos en la base de datos: " + e.getMessage());
        }
        return cantidadPrestamos;
    }
    public Double promedioPrestamos(){
        double promedio = 0;
        try {
            long cantidadUsuarios = usuarioRepository.findAll().stream()
                    .filter(usuarioEntity -> getPrestamosTotalesUsuario(usuarioEntity.getId()) >= 1)
                    .count();
            int cantidadPrestamos = prestamoRepository.findAll().size();
            promedio = (double) cantidadPrestamos / cantidadUsuarios;

        } catch (SQLException e) {
            System.out.println("Error al calcular el promedio de prestamos por usuario");
        }
        return promedio;
    }
}

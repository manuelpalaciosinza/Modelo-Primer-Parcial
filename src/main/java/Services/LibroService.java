package Services;

import Entities.LibroEntity;
import Entities.PrestamoEntity;
import Entities.UsuarioEntity;
import Repositories.impl.LibroRepository;
import Repositories.impl.PrestamoRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class LibroService {
    private static LibroService instance;
    private LibroRepository libroRepository;
    private PrestamoRepository prestamoRepository;
    private LibroService(){
        libroRepository = LibroRepository.getInstance();
        prestamoRepository = PrestamoRepository.getInstance();
    }
    public static LibroService getInstance(){
        if (instance == null){
            instance = new LibroService();
        }
        return instance;
    }

    public Optional<LibroEntity> libroMasPrestado(){
        try {
            ///Creo un mapa con clave id libro y valor cantidad de prestamos de ese libro
            Map<Integer,Long> mapaPrestamos = prestamoRepository.findAll().stream()
                    .collect(Collectors.groupingBy(PrestamoEntity::getLibro_id,
                            Collectors.counting()));
            ///Filtro el mapa para conseguir la entrada con mayor valor, es decir el id libro con mas prestamos asociados
            Optional<Map.Entry<Integer,Long>> mayorEntry = mapaPrestamos.entrySet().stream()
                    .max(Map.Entry.<Integer,Long>comparingByValue());

            ///Abro esa entrada optional y busco el libro con ese id y lo retorno
            if (mayorEntry.isPresent()){
                int idLibroMasPrestado = mayorEntry.get().getKey();
                return libroRepository.findById(idLibroMasPrestado);
            }

        }catch (SQLException e){
            System.out.println("Error al buscar el libro mas prestado: " + e.getMessage());
        }
        return Optional.empty();
    }
    ///La consigna dice visualizar el total de libros disponibles.
    ///Yo lo interpreto como todas las unidades disponibles, pero
    ///Tambien podria ser la cantidad de libros distintos que existen
    public int totalLibrosDisponibles(){
        int cantidadDisponibles = 0;
        try {
             cantidadDisponibles = libroRepository.findAll().stream()
                    .map(LibroEntity::getUnidades_disponibles)
                    .reduce(0,Integer::sum);
        }catch (SQLException e){
            System.out.println("Error al mostrar todos los libros disponibles: " + e.getMessage());
        }
        return cantidadDisponibles;
    }
    public ArrayList<LibroEntity> verTodos(){
        ArrayList<LibroEntity> libros = new ArrayList<>();
        try {
            libros = libroRepository.findAll();
        }catch (SQLException e){
            System.out.println("Error al visualizar todos los libros: "+e.getMessage());
        }
        return libros;
    }
}

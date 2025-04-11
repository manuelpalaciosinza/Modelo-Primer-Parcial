package Services;

import Entities.PrestamoEntity;
import Entities.UsuarioEntity;
import Exceptions.CampoObligatorioException;
import Exceptions.CampoUnicoException;
import Repositories.impl.PrestamoRepository;
import Repositories.impl.UsuarioRepository;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class UsuarioService {
    private static UsuarioService instance;
    private static UsuarioRepository usuarioRepository;
    private static PrestamoRepository prestamoRepository;
    private UsuarioService(){
        usuarioRepository = UsuarioRepository.getInstance();
        prestamoRepository = PrestamoRepository.getInstance();
    };
    public static UsuarioService getInstance(){
        if (instance == null){
            instance = new UsuarioService();
        }
        return instance;
    }

    public void guardarUsuario(UsuarioEntity usuario) throws CampoObligatorioException,CampoUnicoException{
        if (usuario.getNombre() == null || usuario.getEmail() == null)
        {
            throw new CampoObligatorioException("El nombre no puede estar vacio");
        }
        try {
            ArrayList<UsuarioEntity> lista = usuarioRepository.findAll();
            lista = lista.stream().
                    filter(usuario1 -> usuario1.getEmail().equals(usuario.getEmail()))
                    .collect(Collectors.toCollection(ArrayList::new));
            if (!lista.isEmpty()){
                throw new CampoUnicoException("El email ingresado ya esta en uso");
            }
            usuarioRepository.save(usuario);
            System.out.println("Usuario guardado con exito");
        } catch (SQLException e) {
            System.out.println("Error en la conexion: "+e.getMessage());
        }
    }

    public void borrarUsuario(int id)throws NoSuchElementException{
        try {
            ArrayList<UsuarioEntity> usuarios = usuarioRepository.findAll();
            boolean existe = usuarios.stream().
                    anyMatch(usuario -> usuario.getId().equals(id));
            if (!existe){
                throw new NoSuchElementException("No existe usuario con ese id");
            }
            usuarioRepository.deleteById(id);
            System.out.println("Usuario eliminado con exito");
        } catch (SQLException e) {
            System.out.println("Error en el borrado de usuario: "+e.getMessage());
        }
    }

    public void listarUsuarios(){
        try {
            ArrayList<UsuarioEntity> lista = usuarioRepository.findAll();
            System.out.println(lista);
        } catch (SQLException e) {
            System.out.println("Error en la carga de usuarios: " + e.getMessage());
        }
    }
    public List<UsuarioEntity> listarUsuariosConPrestamos(){
        List<UsuarioEntity> listaUsuarios = new ArrayList<>();
        try {
            ArrayList<PrestamoEntity> prestamos = prestamoRepository.findAll();
            Map<Integer, List<PrestamoEntity>> mapa = new HashMap<>();
            mapa = prestamos.stream()
                    .filter(prestamoEntity -> prestamoEntity.getFecha_devolucion() == null)
                    .collect(Collectors.groupingBy(PrestamoEntity::getUsuario_id));
            List<Integer> lista_id = mapa.entrySet().stream()
                    .map(Map.Entry::getKey)
                    .toList();
            listaUsuarios = usuarioRepository.findAll().stream()
                    .filter(usuario -> lista_id.contains(usuario.getId()))
                    .toList();

        } catch (SQLException e) {
            System.out.println("Error en la conexion: " + e.getMessage());
        }
        return listaUsuarios;
    }

    public Optional<UsuarioEntity> usuarioConMasPrestamos(){
        try {
            ///Creo un mapa con clave id usuario y valor cantidad de prestamos de ese usuario
            Map<Integer,Long> mapaPrestamos = prestamoRepository.findAll().stream()
                    .collect(Collectors.groupingBy(PrestamoEntity::getUsuario_id,
                            Collectors.counting()));
            ///Filtro el mapa para conseguir la entrada con mayor valor, es decir el id usuario con mas prestamos asociados
            Optional<Map.Entry<Integer,Long>> mayorEntry = mapaPrestamos.entrySet().stream()
                    .max(Map.Entry.<Integer,Long>comparingByValue());

            ///Abro esa entrada optional y busco el libro con ese id y lo retorno
            if (mayorEntry.isPresent()){
                int idUsuarioMasPrestamos = mayorEntry.get().getKey();
                return usuarioRepository.findById(idUsuarioMasPrestamos);
            }

        }catch (SQLException e){
            System.out.println("Error al buscar el usuario con mas prestamos: " + e.getMessage());
        }
        return Optional.empty();
    }
}

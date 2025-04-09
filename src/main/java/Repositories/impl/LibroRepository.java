package Repositories.impl;

import Connections.DataBaseConnection;
import Entities.LibroEntity;
import Repositories.interfaces.IRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class LibroRepository implements IRepository<LibroEntity> {
    private static LibroRepository instance;
    private LibroRepository(){};
    public static LibroRepository getInstance(){
        if (instance == null){
            instance = new LibroRepository();
        }
        return instance;
    }

    private Optional<LibroEntity> resultToLibro(ResultSet rs) throws SQLException{
        return Optional.of(LibroEntity.builder()
                .id(rs.getInt("id"))
                .titulo(rs.getString("titulo"))
                .autor(rs.getString("autor"))
                .anio_publicacion(rs.getInt("anio_publicacion"))
                .unidades_disponibles(rs.getInt("unidades_disponibles"))
                .build());
    }

    @Override
    public void save(LibroEntity libroEntity) throws SQLException {
        String sql = "INSERT INTO libros (titulo,autor,anio_publicacion,unidades_disponibles) VALUES(?,?,?,?)";
        try (Connection connection = DataBaseConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1,libroEntity.getTitulo());
            ps.setString(2,libroEntity.getAutor());
            ps.setInt(3,libroEntity.getAnio_publicacion());
            ps.setInt(4,libroEntity.getUnidades_disponibles());
            ps.executeUpdate();
        }
    }

    @Override
    public void deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM libros WHERE id = ?";
        try (Connection connection = DataBaseConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1,id);
            ps.executeUpdate();
        }
    }

    @Override
    public void update(LibroEntity libroEntity) throws SQLException {
        String sql = "UPDATE libros SET titulo = ?, autor = ?, anio_publicacion = ?, unidades_disponibles = ? WHERE id = ?";
        try (Connection connection = DataBaseConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1,libroEntity.getTitulo());
            ps.setString(2,libroEntity.getAutor());
            ps.setInt(3,libroEntity.getAnio_publicacion());
            ps.setInt(4,libroEntity.getUnidades_disponibles());
            ps.setInt(5,libroEntity.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public ArrayList<LibroEntity> findAll() throws SQLException {
        ArrayList<LibroEntity> libros = new ArrayList<>();
        String sql = "SELECT * FROM libros";
        try (Connection connection = DataBaseConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql);){
            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()){
                    Optional<LibroEntity> libro = resultToLibro(rs);
                    libro.ifPresent(libros::add);
                }
            }
        }
        return libros;
    }

    @Override
    public Optional<LibroEntity> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM libros WHERE id = ?";
        try (Connection connection = DataBaseConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return resultToLibro(rs);
                }
                return Optional.empty();
            }
        }
    }
}

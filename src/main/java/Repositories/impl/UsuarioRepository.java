package Repositories.impl;

import Connections.DataBaseConnection;
import Entities.UsuarioEntity;
import Repositories.interfaces.IRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class UsuarioRepository implements IRepository<UsuarioEntity> {

    private static UsuarioRepository instance;
    private UsuarioRepository(){};
    public static UsuarioRepository getInstance(){
        if (instance == null){
            instance = new UsuarioRepository();
        }
        return instance;
    }

    public Optional<UsuarioEntity> resultToUsuario(ResultSet rs) throws SQLException{
        return Optional.of(UsuarioEntity.builder()
                .id(rs.getInt("id"))
                .nombre(rs.getString("nombre"))
                .email(rs.getString("email"))
                .build());
    }

    @Override
    public void save(UsuarioEntity usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre,email) VALUES(?,?)";
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1,usuario.getNombre());
            ps.setString(2,usuario.getEmail());
            ps.executeUpdate();
        }
    }

    @Override
    public void deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try(Connection connection = DataBaseConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1,id);
            ps.executeUpdate();
        }
    }

    @Override
    public void update(UsuarioEntity usuario) throws SQLException {
        String sql = "UPDATE usuarios SET nombre = ?, email = ? WHERE id = ?";
        try (Connection connection = DataBaseConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1,usuario.getNombre());
            ps.setString(2, usuario.getEmail());
            ps.setInt(3,usuario.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public ArrayList<UsuarioEntity> findAll() throws SQLException {
        String sql = "SELECT * FROM usuarios";
        ArrayList<UsuarioEntity> usuarios = new ArrayList<>();
        try (Connection connection = DataBaseConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            try(ResultSet rs = ps.executeQuery()){
                while (rs.next())
                {
                    Optional<UsuarioEntity> usuario = resultToUsuario(rs);
                    usuario.ifPresent(usuarios::add);
                }
            }
        }
        return usuarios;
    }

    @Override
    public Optional<UsuarioEntity> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try (Connection connection = DataBaseConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1,id);
            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()){
                    return resultToUsuario(rs);
                }
            }
        }
        return Optional.empty();
    }
}

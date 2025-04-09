package Repositories.impl;

import Connections.DataBaseConnection;
import Entities.PrestamoEntity;
import Repositories.interfaces.IRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class PrestamoRepository implements IRepository<PrestamoEntity> {
    private static PrestamoRepository instance;
    private PrestamoRepository(){};
    public static PrestamoRepository getInstance(){
        if (instance == null){
            instance = new PrestamoRepository();
        }
        return instance;
    }

    private Optional<PrestamoEntity> resultToPrestamo(ResultSet rs) throws SQLException{

        /// Verificar que la fecha sea nula
        LocalDate fecha = null;
        String fechaDevolucion = rs.getString("fecha_devolucion");
        if(fechaDevolucion != null) fecha = LocalDate.parse(fechaDevolucion);

        return Optional.of(PrestamoEntity.builder()
                .id(rs.getInt("id"))
                .libro_id(rs.getInt("libro_id"))
                .usuario_id(rs.getInt("usuario_id"))
                .fecha_prestamo(LocalDate.parse(rs.getString("fecha_prestamo")))
                .fecha_devolucion(fecha)
                .build());

    }
    @Override
    public void save(PrestamoEntity prestamo) throws SQLException {
        String sql = "INSERT INTO prestamos (usuario_id,libro_id) VALUES(?,?)";
        try (Connection connection = DataBaseConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1,prestamo.getUsuario_id());
            ps.setInt(2,prestamo.getLibro_id());
            ps.executeUpdate();
        }
    }

    @Override
    public void deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM prestamos WHERE id = ?";
        try (Connection connection = DataBaseConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1,id);
            ps.executeUpdate();
        }
    }

    @Override
    public void update(PrestamoEntity prestamo) throws SQLException {
        String sql = "UPDATE prestamos SET usuario_id = ?, libro_id = ?, fecha_prestamo = ?, fecha_devolucion = ? WHERE id = ?";
        try (Connection connection = DataBaseConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1,prestamo.getUsuario_id());
            ps.setInt(2,prestamo.getLibro_id());
            ps.setDate(3, Date.valueOf(prestamo.getFecha_prestamo()));
            ps.setDate(4, Date.valueOf(prestamo.getFecha_devolucion()));
            ps.setInt(5,prestamo.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public ArrayList<PrestamoEntity> findAll() throws SQLException {
        ArrayList<PrestamoEntity> prestamos = new ArrayList<>();
        String sql = "SELECT * FROM prestamos";
        try (Connection connection = DataBaseConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()){
                    Optional<PrestamoEntity> prestamo = resultToPrestamo(rs);
                    prestamo.ifPresent(prestamos::add);
                }
            }
        }
        return prestamos;
    }

    @Override
    public Optional<PrestamoEntity> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM prestamos WHERE id = ?";
        try (Connection connection = DataBaseConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1,id);
            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()){
                    return resultToPrestamo(rs);
                }
            }
        }
        return Optional.empty();
    }
}

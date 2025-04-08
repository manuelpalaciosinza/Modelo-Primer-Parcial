package Repositories.interfaces;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public interface IRepository<T> {
    public void save (T t) throws SQLException;
    public void deleteById (Integer id) throws SQLException;
    public void update (T t) throws SQLException;
    public ArrayList<T> findAll() throws SQLException;
    public Optional<T> findById (Integer id) throws SQLException;
}

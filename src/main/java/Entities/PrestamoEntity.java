package Entities;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder

public class PrestamoEntity {
    private Integer id; //PK AUto
    private Integer libro_id; //FK LIBRO
    private Integer usuario_id; //FK USUARIO
    private LocalDate fecha_prestamo; // se carga sola
    private LocalDate fecha_devolucion; // si es null no se devolvio

    public PrestamoEntity(Integer libro_id,Integer usuario_id){
        this.libro_id = libro_id;
        this.usuario_id = usuario_id;
    }
}

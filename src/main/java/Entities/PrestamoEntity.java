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
}

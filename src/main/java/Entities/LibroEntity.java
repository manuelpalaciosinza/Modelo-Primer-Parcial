package Entities;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LibroEntity {
    private Integer id; //PK AUTO
    private String titulo; //Not Null
    private String autor; //Not null
    private Integer anio_publicacion;
    private Integer unidades_disponibles;

    public LibroEntity(String titulo, String autor, Integer anio_publicacion, Integer unidades_disponibles){
        id = 0;
        this.titulo = titulo;
        this.autor = autor;
        this.anio_publicacion = anio_publicacion;
        this.unidades_disponibles = unidades_disponibles;
    }

    @Override
    public String toString() {
        return "\nLibroEntity{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", anio_publicacion=" + anio_publicacion +
                ", unidades_disponibles=" + unidades_disponibles +
                '}';
    }
}

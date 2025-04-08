package Entities;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioEntity {

    private Integer id; //Pk auto-i
    private String nombre; //Not null
    private String email; //Not Null

    public UsuarioEntity(String nombre,String email){
        id = 0;
        this.nombre = nombre;
        this.email = email;
    }

}

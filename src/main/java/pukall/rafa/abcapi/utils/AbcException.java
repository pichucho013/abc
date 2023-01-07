package pukall.rafa.abcapi.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import pukall.rafa.abcapi.enums.ErrorCodigo;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbcException extends Exception{

    private String descripcion;
    private ErrorCodigo codigo;
    private HttpStatus status;

}

package pukall.rafa.abcapi.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pukall.rafa.abcapi.enums.ErrorCodigo;

@Data
@NoArgsConstructor
public class ErrorResponse {

    private ErrorCodigo codigo;
    private String error;

    public ErrorResponse(ErrorCodigo codigo, String error) {
        this.codigo = codigo;
        this.error = error;
    }
}

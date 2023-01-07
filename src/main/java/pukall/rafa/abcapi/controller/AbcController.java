package pukall.rafa.abcapi.controller;

import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pukall.rafa.abcapi.dto.AbcDTO;
import pukall.rafa.abcapi.enums.ErrorCodigo;
import pukall.rafa.abcapi.service.AbcService;
import pukall.rafa.abcapi.utils.AbcException;
import pukall.rafa.abcapi.utils.ErrorResponse;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/abc-api")
public class AbcController {

    @Autowired
    private AbcService abcService;

    @GetMapping("/consulta/")
    public ResponseEntity  buscarNoticiasAbc(@RequestParam String q) {

        List <AbcDTO> abcDTOList = new ArrayList<>();

        try{
            abcDTOList= abcService.getNoticias(q);
            return new ResponseEntity<>( abcDTOList, HttpStatus.OK);
        } catch (AbcException e){
            return new ResponseEntity<>( new ErrorResponse( e.getCodigo(),e.getDescripcion()), e.getStatus());
        } catch (Exception e){
            return new ResponseEntity<> ( new ErrorResponse(ErrorCodigo.g100, "Error interno del servidor"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }



}

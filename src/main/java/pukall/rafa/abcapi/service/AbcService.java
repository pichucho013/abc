package pukall.rafa.abcapi.service;

import org.json.JSONException;
import pukall.rafa.abcapi.dto.AbcDTO;
import pukall.rafa.abcapi.utils.AbcException;

import java.io.IOException;
import java.util.List;

public interface AbcService{

    List<AbcDTO> getNoticiasJsoup(String palabra) throws IOException;

    List<AbcDTO> getNoticias(String palabra) throws IOException, JSONException, AbcException;

}

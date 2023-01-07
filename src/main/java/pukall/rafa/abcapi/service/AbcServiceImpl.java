package pukall.rafa.abcapi.service;

import org.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pukall.rafa.abcapi.dto.AbcDTO;
import org.json.JSONObject;
import org.json.JSONException;
import pukall.rafa.abcapi.enums.ErrorCodigo;
import pukall.rafa.abcapi.utils.AbcException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


@Service
public class AbcServiceImpl implements AbcService{

    String urlabc = "https://www.abc.com.py/buscador/?query=";

    public List<AbcDTO> getNoticiasJsoup(String palabra) throws IOException {

//        Document document = Jsoup.connect(urlabc + palabra).timeout(10000)
//                .get();
//        Element element = document.getElementById("resultdata");
//        Elements elements = element.getElementsByTag("a");
//
//        List<AbcDTO> abcDTOS = new ArrayList<>();
//
//        for (Element ads: elements) {
//
//            AbcDTO abcDTO = new AbcDTO();
//
//            abcDTO.setEnlace(ads.attr("onmousedown"));
//            abcDTO.setResumen(ads.attr(("queryly_item_description")));
//            abcDTO.setResumen(ads.attr("queryly_item_description"));
//
//            abcDTOS.add(abcDTO);
//
//        }

        return  null;

    }


    public List<AbcDTO> getNoticias(String palabra) throws IOException, JSONException, AbcException {

        if(palabra == null || palabra.equals("")){
            throw new AbcException("Parámetros inválidos", ErrorCodigo.g268, HttpStatus.BAD_REQUEST);
        }

        StringBuilder response = new StringBuilder();
        String urlconcatenada = "https://api.queryly.com/json.aspx?queryly_key=33530b56c6aa4c20&query="+ palabra + "%20&endindex=0&batchsize=20&callback=searchPage.resultcallback&showfaceted=true&extendeddatafields=creator,imageresizer,promo_image&timezoneoffset=240";
        URL url = new URL(urlconcatenada);


        // Abrir la conexión e indicar que será de tipo GET
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setRequestMethod("GET");
        // Búferes para leer
        BufferedReader rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
        String linea;
        // Mientras el BufferedReader se pueda leer, agregar contenido a resultado
        while ((linea = rd.readLine()) != null) {
            response.append(linea);
        }
        //cerramos
        rd.close();

        String resultado = response.toString();
        int principio = resultado.indexOf("item");

        String cadenaAeliminar = resultado.substring(0, principio-1);
        resultado = resultado.replace(cadenaAeliminar, "");

        int terminacion = resultado.indexOf("],");
        resultado = resultado.substring(0, terminacion);
        resultado = "{" + resultado + "]}";

        JSONObject jsonObject = new JSONObject(resultado);
        JSONArray jsonArray = jsonObject.getJSONArray("items");
        List<AbcDTO> abcDTOList = new ArrayList();

        if(jsonArray.length() == 0){
            throw new AbcException("No se encuentran noticias para el texto: "+ palabra, ErrorCodigo.g267, HttpStatus.NOT_FOUND);
        }


        for (int i=0; i < jsonArray.length(); i++) {
            AbcDTO abcDTO = new AbcDTO();
            abcDTO.setResumen(jsonArray.getJSONObject(i).get("description").toString());
            abcDTO.setEnlace("https://www.abc.com.py" +jsonArray.getJSONObject(i).get("link").toString());
            abcDTO.setTitulo(jsonArray.getJSONObject(i).get("title").toString());
            abcDTO.setEnlace_foto("https://www.abc.com.py" + jsonArray.getJSONObject(i).get("promo_image").toString());

            //Se formateo la fecha en el formato solicitado
            String unixSegundos =  jsonArray.getJSONObject(i).get("pubdateunix").toString();
            Long unixLong = Long.parseLong(unixSegundos);
            Date date = new Date(unixLong*1000L);
            SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
            jdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
            String java_date = jdf.format(date);
            abcDTO.setFecha(java_date.substring(0,16));

            abcDTOList.add(abcDTO);
        }

        return abcDTOList;
    }

}

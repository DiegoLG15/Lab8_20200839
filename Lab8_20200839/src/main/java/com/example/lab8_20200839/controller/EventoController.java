package com.example.lab8_20200839.controller;

import com.example.lab8_20200839.entity.Evento;
import com.example.lab8_20200839.repository.EventosRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/evento")
public class EventoController {

    private final EventosRepository eventosRepository;

    public EventoController(EventosRepository eventosRepository) {
        this.eventosRepository = eventosRepository;
    }


    @GetMapping("")
    public List<Evento> listarEventos() {
        return eventosRepository.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<HashMap<String,Object>> obtenerEventoPorId(
            @PathVariable("id") String idStr) {


        HashMap<String,Object> responseJson = new HashMap<>();

        try {
            int id = Integer.parseInt(idStr);
            Optional<Evento> optEvent = eventosRepository.findById(id);
            if (optEvent.isPresent()) {
                responseJson.put("result","success");
                responseJson.put("evento",optEvent.get());
                responseJson.put("resultado","Exitoso");
                return ResponseEntity.ok(responseJson);
            } else {
                responseJson.put("msg","Evento no encontrado");
            }
        } catch (NumberFormatException e) {
            responseJson.put("msg","el ID debe ser un número entero positivo");
        }
        responseJson.put("resultado","Falla");
        return ResponseEntity.badRequest().body(responseJson);
    }
    @PostMapping(value = "")
    public ResponseEntity<HashMap<String,Object>> guardarEvento(
            @RequestBody Evento evento,
            @RequestParam (value = "fetchId",required = false) boolean fetchId){
        HashMap<String,Object> responseMap = new HashMap<>();
        eventosRepository.save(evento);
        if (fetchId){
            responseMap.put("id", evento.getId());
        }
        responseMap.put("estado","creado");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMap);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<HashMap<String,String>> gestionExcepcion(HttpServletRequest request) {

        HashMap<String, String> responseMap = new HashMap<>();

        if (request.getMethod().equals("POST") || request.getMethod().equals("PUT")) {
            responseMap.put("estado", "“error");
            responseMap.put("msg", "Debe enviar un producto");

        }
        return ResponseEntity.badRequest().body(responseMap);
    }




}

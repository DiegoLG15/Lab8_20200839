package com.example.lab8_20200839.controller;

import com.example.lab8_20200839.entity.Evento;
import com.example.lab8_20200839.entity.TipoTicketEvento;
import com.example.lab8_20200839.repository.EventoConTipoDeTicketRepository;
import com.example.lab8_20200839.repository.EventosRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/eventoConTipoDeTicket")
public class TipoTicketEventoController {

    private final EventoConTipoDeTicketRepository eventoConTipoDeTicketRepository;
    private final EventosRepository eventosRepository;

    public TipoTicketEventoController(EventoConTipoDeTicketRepository eventoConTipoDeTicketRepository,
                                      EventosRepository eventosRepository) {
        this.eventoConTipoDeTicketRepository = eventoConTipoDeTicketRepository;
        this.eventosRepository = eventosRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<HashMap<String,Object>> obtenerTipoTicketPorId(
            @PathVariable("id") String idStr) {
        HashMap<String,Object> responseJson = new HashMap<>();
        try {
            int id = Integer.parseInt(idStr);
            Optional<TipoTicketEvento> optEvent = eventoConTipoDeTicketRepository.findById(id);
            if (optEvent.isPresent()) {
                responseJson.put("evento",optEvent.get());
                responseJson.put("resultado","Exitoso");
                return ResponseEntity.ok(responseJson);
            } else {
                responseJson.put("msg","Ticket de evento no encontrado");
            }
        } catch (NumberFormatException e) {
            responseJson.put("msg","el ID debe ser un número entero positivo");
        }
        responseJson.put("resultado","Falla");
        return ResponseEntity.badRequest().body(responseJson);
    }
    @PutMapping(value = "", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<HashMap<String, Object>> actualizarParcialX(Evento evento) {//para raw por json
        if (evento.getId() != null && evento.getId() > 0) {
            Optional<Evento> optional = eventosRepository.findById(evento.getId());
            if (optional.isPresent()) {
                Evento eventoOriginal = optional.get();

                if(evento.getFecha()!=null){
                    eventoOriginal.setFecha(evento.getFecha());
                }

                if(evento.getNombre()!=null){
                    eventoOriginal.setNombre(evento.getNombre());
                }

                if(evento.getDescripcion()!=null){
                    eventoOriginal.setDescripcion(evento.getDescripcion());
                }

                if(evento.getPathImage()!=null){
                    eventoOriginal.setPathImage(evento.getPathImage());
                }

                if(evento.getIdLocal()!=null){
                    eventoOriginal.setIdLocal(evento.getIdLocal());
                }
                eventosRepository.save(eventoOriginal);
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("estado", "actualizado");
                return ResponseEntity.ok().body(hashMap);
            } else {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("estado", "error");
                hashMap.put("msg", "el evento a actualizar no exite");
                return ResponseEntity.badRequest().body(hashMap);
            }
        } else {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("estado", "error");
            hashMap.put("msg", "el id debe existir");
            return ResponseEntity.badRequest().body(hashMap);
        }
    }
    @DeleteMapping(value = "/borrarEvento/{id}")
    public ResponseEntity<HashMap<String, String>> borrarEvento(@PathVariable("id") String idStr){
        HashMap<String, String> responseMap = new HashMap<>();
        try {
            int id = Integer.parseInt(idStr);
            if(eventosRepository.existsById(id)){
                eventosRepository.deleteById(id);
                responseMap.put("estado","borrado exitoso");
                return ResponseEntity.ok(responseMap);
            } else {
                responseMap.put("estado","error");
                responseMap.put("msg","no se encontro el evento con id: " + id);
                return ResponseEntity.badRequest().body(responseMap);
            }
        } catch (NumberFormatException ex){
            responseMap.put("estado","error");
            responseMap.put("msg","El ID debe ser un numero");
            return ResponseEntity.badRequest().body(responseMap);
        }
    }


}

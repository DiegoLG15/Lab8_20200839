package com.example.lab8_20200839.repository;


import com.example.lab8_20200839.entity.TipoTicketEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventoConTipoDeTicketRepository extends JpaRepository<TipoTicketEvento, Integer> {

}

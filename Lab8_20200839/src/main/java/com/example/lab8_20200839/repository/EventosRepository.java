package com.example.lab8_20200839.repository;

import com.example.lab8_20200839.entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventosRepository extends JpaRepository<Evento, Integer> {

}

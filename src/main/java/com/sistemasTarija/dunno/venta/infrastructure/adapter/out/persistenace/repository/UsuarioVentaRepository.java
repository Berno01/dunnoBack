package com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.repository;

import com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.entity.UsuarioVentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioVentaRepository extends JpaRepository<UsuarioVentaEntity, Integer> {
}

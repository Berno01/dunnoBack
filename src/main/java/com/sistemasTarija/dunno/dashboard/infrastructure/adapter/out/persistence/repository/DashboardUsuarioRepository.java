package com.sistemasTarija.dunno.dashboard.infrastructure.adapter.out.persistence.repository;

import com.sistemasTarija.dunno.auth.infrastructure.adapter.out.persistence.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para acceder a datos de usuarios en el dashboard
 */
@Repository
public interface DashboardUsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {
    
    /**
     * Encuentra usuarios por lista de IDs
     */
    List<UsuarioEntity> findByIdIn(List<Integer> ids);
}

package com.inventcontrol.inventory_control.repositories;


import com.inventcontrol.inventory_control.entities.Deposito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DepositoRepository extends JpaRepository<Deposito, Long> {

    List<Deposito> findByAtivoTrue();

    @Query("""
                SELECT d FROM Deposito d
                WHERE LOWER(d.descricao) = LOWER(:descricao)
                   OR LOWER(d.sigla) = LOWER(:sigla)
            """)
    List<Deposito> findDepositoByDescricaoOrSigla(@Param("descricao") String descricao,
                                                  @Param("sigla") String sigla);
}

package com.micro9.micro9g3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.micro9.micro9g3.model.PedidoReabastecimiento;

@Repository
public interface PedidoReabastecimientoRepository extends JpaRepository<PedidoReabastecimiento, Integer> {
}

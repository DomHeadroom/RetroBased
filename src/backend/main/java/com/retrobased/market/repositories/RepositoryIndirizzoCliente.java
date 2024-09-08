package com.retrobased.market.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryIndirizzoCliente extends JpaRepository<IndirizzoCliente,Long> {

    boolean existsIndirizzoClienteByIdAndIdCliente(Long idIndirizzo, Cliente idCliente);
}

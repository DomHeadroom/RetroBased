package com.retrobased.market.repositories;

import com.retrobased.market.entities.Cliente;
import com.retrobased.market.entities.IndirizzoCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryIndirizzoCliente extends JpaRepository<IndirizzoCliente,Integer> {

    boolean existsIndirizzoClienteByIdAndIdCliente(Integer idIndirizzo, Cliente idCliente);
}

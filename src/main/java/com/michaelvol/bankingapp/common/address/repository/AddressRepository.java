package com.michaelvol.bankingapp.common.address.repository;

import com.michaelvol.bankingapp.common.address.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}

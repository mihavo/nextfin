package com.michaelvol.nextfin.common.address.repository;

import com.michaelvol.nextfin.common.address.entity.Address;
import com.michaelvol.nextfin.common.address.enums.Floor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query("select (count(a) > 0) from Address a WHERE a.street = ?1 AND a.number = ?2 AND a.floor = ?3 AND a.city = ?4 AND a.zipCode = ?5")
    boolean addressExists(String street, Integer number, Integer floor, String city, String zipCode);

    @Query("select a from Address a WHERE a.street = ?1 AND a.number = ?2 AND a.floor = ?3 AND a.city = ?4 AND a.zipCode = ?5")
    Address findAddress(String street, Integer number, Floor floor, String city, String zipCode);
}

package de.haw.eborrow.repository;

import de.haw.eborrow.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}

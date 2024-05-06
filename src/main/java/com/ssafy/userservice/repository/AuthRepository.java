package com.ssafy.userservice.repository;

import com.ssafy.userservice.entity.Auth;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<Auth, Integer> {

    Optional<Auth> findByUsername(String username);

    Optional<Auth> findByProviderAndProviderId(String provider, String providerId);
}

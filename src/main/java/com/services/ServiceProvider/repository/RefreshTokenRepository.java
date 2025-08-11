package com.services.ServiceProvider.repository;

import com.services.ServiceProvider.entity.RefreshToken;
import com.services.ServiceProvider.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

    Optional<RefreshToken> findByRefreshToken(String token);

    void deleteByRefreshToken(String token);

    RefreshToken findByUser(User user);

}
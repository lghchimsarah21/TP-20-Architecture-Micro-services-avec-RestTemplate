package com.example.service_voiture.repositories;


import com.example.service_voiture.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    // Méthodes CRUD de base fournies par JpaRepository

    // Vous pouvez ajouter des méthodes personnalisées ici, par exemple:
    // List<Car> findByClientId(Long clientId);
}
package com.example.service_voiture.services;

import com.example.service_voiture.entities.Car;
import com.example.service_voiture.models.CarResponse;
import com.example.service_voiture.entities.Client;
import com.example.service_voiture.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private RestTemplate restTemplate;

    // IMPORTANT : Notez le slash final après "client/"
    private static final String CLIENT_SERVICE_URL =
            "http://localhost:8888/service-client/api/client/";

    /**
     * Récupère toutes les voitures avec les détails des clients
     */
    public List<CarResponse> findAll() {
        List<Car> cars = carRepository.findAll();

        return cars.stream()
                .map(this::mapToCarResponse)
                .collect(Collectors.toList());
    }

    /**
     * Récupère une voiture par son ID avec les détails du client
     */
    public CarResponse findById(Long id) throws Exception {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new Exception("Voiture non trouvée avec l'ID: " + id));

        return mapToCarResponse(car);
    }

    /**
     * Crée ou met à jour une voiture
     */
    public Car save(Car car) {
        return carRepository.save(car);
    }

    /**
     * Supprime une voiture par son ID
     */
    public void delete(Long id) throws Exception {
        if (!carRepository.existsById(id)) {
            throw new Exception("Voiture non trouvée avec l'ID: " + id);
        }
        carRepository.deleteById(id);
    }

    /**
     * Convertit une entité Car en CarResponse en récupérant les détails du client
     */
    private CarResponse mapToCarResponse(Car car) {
        Client client = null;
        try {
            // L'URL complète sera : http://localhost:8081/api/client/7 par exemple
            String url = CLIENT_SERVICE_URL + car.getClient_id();
            System.out.println("Tentative de récupération du client depuis : " + url);

            client = restTemplate.getForObject(url, Client.class);

            if (client != null) {
                System.out.println("Client récupéré : " + client.getName());
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération du client: " + e.getMessage());
            e.printStackTrace();
        }

        return CarResponse.builder()
                .id(car.getId())
                .brand(car.getBrand())
                .model(car.getModel())
                .matricule(car.getMatricule())
                .client(client)
                .build();
    }
}
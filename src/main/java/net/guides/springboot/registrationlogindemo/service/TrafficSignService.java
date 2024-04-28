package net.guides.springboot.registrationlogindemo.service;


import net.guides.springboot.registrationlogindemo.entity.TrafficSign;
import net.guides.springboot.registrationlogindemo.repository.TrafficSignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrafficSignService {
    @Autowired
    private TrafficSignRepository trafficSignRepository;

    public List<TrafficSign> findAll() {
        return trafficSignRepository.findAll();
    }

    public TrafficSign save(TrafficSign sign) {
        return trafficSignRepository.save(sign);
    }

    public void deleteById(Long id) {
        trafficSignRepository.deleteById(id);
    }
}

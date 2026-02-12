package com.reindeer.trail.service;

import com.reindeer.trail.model.Property;
import com.reindeer.trail.repository.PropertyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyService {

    private final PropertyRepository repository;

    public PropertyService(PropertyRepository repository) {
        this.repository = repository;
    }

    public List<Property> listAll() {
        return repository.findAll();
    }

    public Property save(Property property) {
        return repository.save(property);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}

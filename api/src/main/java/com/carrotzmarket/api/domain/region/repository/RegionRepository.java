package com.carrotzmarket.api.domain.region.repository;

import com.carrotzmarket.db.region.RegionEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RegionRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(RegionEntity regionEntity) {
        em.persist(regionEntity);
    }

    public void deleteById(Long id) {
        Optional<RegionEntity> optionalEntity = findById(id);
        optionalEntity.ifPresent(em::remove); // Optional 처리
    }

    public Optional<RegionEntity> findById(Long id) {
        RegionEntity regionEntity = em.find(RegionEntity.class, id);
        return Optional.ofNullable(regionEntity);
    }

    public List<RegionEntity> findAllRegions(){
        return em.createQuery("SELECT r FROM RegionEntity r", RegionEntity.class)
                .getResultList();
    }
}

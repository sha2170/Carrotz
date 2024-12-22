package com.carrotzmarket.api.domain.region.repository;

import com.carrotzmarket.db.region.RegionEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RegionRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(RegionEntity regionEntity) {
        em.persist(regionEntity);
    }

    public void deleteById(Long id) {
        RegionEntity entity = findById(id);
        if (entity != null) {
            em.remove(entity);
        }
    }

    public RegionEntity findById(Long id){
        return em.find(RegionEntity.class, id);
    }

    public List<RegionEntity> findAllRegions(){
        return em.createQuery("SELECT r FROM RegionEntity r", RegionEntity.class)
                .getResultList();
    }
}

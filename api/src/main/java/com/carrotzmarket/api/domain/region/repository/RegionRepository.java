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
        if (regionEntity.getId() == null) {
            em.persist(regionEntity); // 새 엔티티인 경우
        } else {
            em.merge(regionEntity); // 이미 존재하는 엔티티인 경우
        }
    }

    public void deleteById(Long id) {
        Optional<RegionEntity> optionalEntity = findById(id);
        optionalEntity.ifPresent(em::remove);
    }

    public Optional<RegionEntity> findById(Long id) {
        return Optional.ofNullable(em.find(RegionEntity.class, id));
    }

    public List<RegionEntity> findAllRegions(){
        return em.createQuery("SELECT r FROM RegionEntity r", RegionEntity.class)
                .getResultList();
    }
}

package biz.kochanski.featureflag.repository;

import biz.kochanski.featureflag.entity.GlobalFeatureFlag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface GlobalFeatureRepository extends JpaRepository<GlobalFeatureFlag, Long> {
    Collection<GlobalFeatureFlag> getAllByEnabled(Boolean enabled);

    Optional<GlobalFeatureFlag> getByName(String name);
}

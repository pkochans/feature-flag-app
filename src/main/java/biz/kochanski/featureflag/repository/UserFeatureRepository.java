package biz.kochanski.featureflag.repository;

import biz.kochanski.featureflag.entity.UserFeatureFlag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface UserFeatureRepository extends JpaRepository<UserFeatureFlag, Long> {
    Collection<UserFeatureFlag> getAllByUsernameAndEnabled(String username, Boolean enabled);
}

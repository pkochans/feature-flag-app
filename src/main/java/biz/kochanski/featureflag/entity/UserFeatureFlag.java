package biz.kochanski.featureflag.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class UserFeatureFlag {
    @Id
    @GeneratedValue
    private Long userFeatureId;
    private Boolean enabled;
    private String username;
    @ManyToOne
    @JoinColumn(name = "global_feature_id", nullable = false)
    private GlobalFeatureFlag globalFeatureFlag;

    public UserFeatureFlag(Boolean enabled, String username, GlobalFeatureFlag globalFeatureFlag) {
        this.enabled = enabled;
        this.username = username;
        this.globalFeatureFlag = globalFeatureFlag;
    }

    public UserFeatureFlag() {
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public GlobalFeatureFlag getGlobalFeatureFlag() {
        return globalFeatureFlag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserFeatureFlag that = (UserFeatureFlag) o;
        return userFeatureId.equals(that.userFeatureId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userFeatureId);
    }
}

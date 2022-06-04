package biz.kochanski.featureflag.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class GlobalFeatureFlag {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long globalFeatureId;
    @Column(unique = true)
    private String name;
    private Boolean enabled;

    protected GlobalFeatureFlag() {
    }

    public GlobalFeatureFlag(String name, Boolean enabled) {
        this.name = name;
        this.enabled = enabled;
    }

    public Long getGlobalFeatureId() {
        return globalFeatureId;
    }

    public String getName() {
        return name;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GlobalFeatureFlag that = (GlobalFeatureFlag) o;
        return globalFeatureId.equals(that.globalFeatureId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(globalFeatureId);
    }
}

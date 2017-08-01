package net.octoplar.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Created by Octoplar.
 */
@Entity
@Table(name="Configuration")
@NamedQueries({
        @NamedQuery(name = "Configuration.byId", query = "SELECT e FROM Configuration AS e WHERE e.id =:id")
})
//not cloneable/serializable. it just implementation of a property source through a database
public class Configuration {

    @Id
    @Column(name = "id")
    @Size(max = 20)
    private String key;

    @Column(name = "value")
    @Size(max = 30)
    private String value;

    public Configuration() {
    }

    public Configuration(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

package domain;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class DaoObject {

    @Id
    @GeneratedValue
    private long id;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

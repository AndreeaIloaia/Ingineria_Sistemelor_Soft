package domain;

import java.io.Serializable;

public interface Entity<ID> extends Serializable {
    void setId(ID id);
    ID getId();
}

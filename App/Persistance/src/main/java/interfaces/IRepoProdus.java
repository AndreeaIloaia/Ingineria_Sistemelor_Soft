package interfaces;

import domain.Produs;

public interface IRepoProdus extends ICrudRepository<Integer, Produs> {
    public Produs cauta(Integer id);
}

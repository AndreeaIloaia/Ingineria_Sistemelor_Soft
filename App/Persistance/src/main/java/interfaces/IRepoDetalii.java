package interfaces;

import domain.Detalii;

public interface IRepoDetalii  extends ICrudRepository<Integer, Detalii> {
    public Detalii cauta(Integer id);
}

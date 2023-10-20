package interfaces;

import domain.Comanda;
import domain.Produs;

public interface IRepoComanda  extends ICrudRepository<Integer, Comanda> {
    public Comanda cauta(Integer id);
}
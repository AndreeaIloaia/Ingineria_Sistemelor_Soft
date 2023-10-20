package interfaces;

import domain.Agent;

public interface IRepoAgent extends ICrudRepository<Integer, Agent> {
    public Agent cauta(String user, String pass);
}

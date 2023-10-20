package interfaces;

import domain.Admin;

public interface IRepoAdmin extends ICrudRepository<Integer, Admin> {
    public Admin cauta(String user, String pass);
}

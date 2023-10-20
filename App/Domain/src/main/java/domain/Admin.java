package domain;

public class Admin implements Entity<Integer> {
    private String nume;
    private String parola;
    private int id;

    public Admin(int id, String nume, String parola) {
        this.id = id;
        this.nume = nume;
        this.parola = parola;
    }


    public Admin(String nume, String parola, int id) {
        this.id = id;
        this.nume = nume;
        this.parola = parola;
    }

    public Admin() {

    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    @Override
    public String toString() {
        return "Casier{id=" + id+
                ", nume='" + nume + '\'' +
                ", parola='" + parola + '\'' +
                '}';
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }
}

package domain;

public class Agent implements Entity<Integer> {
    private int id;
    private String nume;
    private String parola;

    public Agent(int id, String nume, String parola) {
        this.id = id;
        this.nume = nume;
        this.parola = parola;
    }


    public Agent(String nume, String parola, int id) {
        this.id = id;
        this.nume = nume;
        this.parola = parola;
    }

    public Agent() {

    }
    
    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
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
        return "Casier{id=" + id +
                ", nume='" + nume + '\'' +
                ", parola='" + parola + '\'' +
                '}';
    }
}
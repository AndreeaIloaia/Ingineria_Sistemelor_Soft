package jdbc;

import domain.Detalii;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;
import java.util.List;

public class RepoDetalii {
    private static final Logger logger = LogManager.getLogger(RepoProdus.class);
    private int id;

    public RepoDetalii() {
        logger.info("Constructor finished!");
    }

    public List<Integer> cauta(Integer id_comanda) {
        initialize();
        List<Detalii> detalii = new ArrayList<>();
        Detalii c = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                detalii = session.createQuery("from Detalii", Detalii.class).list();
                transaction.commit();
            } catch (RuntimeException ex) {
                if (transaction != null)
                    transaction.rollback();
            } finally {
                close();
            }
        }
        List<Integer> iduri = new ArrayList<>();
        for (Detalii d : detalii) {
            if(d.getId_comanda() == id_comanda) {
                iduri.add(d.getId_produs());
                iduri.add(d.getCantitate());
            }
        }
        return iduri;
    }

    static SessionFactory sessionFactory;

    static void initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    static void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }

    }

    public void save(Detalii detalii) {
        initialize();
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                id = 1;
                List<Detalii> listaDetalii = new ArrayList<>();
                listaDetalii = session.createQuery("from Detalii", Detalii.class).list();
                int max_id = 0;
                for(Detalii d: listaDetalii){
                    if(max_id < d.getId()) {
                        id = d.getId();
                    }
                }
                Detalii detaliu = new Detalii(id + 1, detalii.getId_comanda(), detalii.getId_produs(), detalii.getCantitate());
                session.save(detaliu);
                transaction.commit();
            } catch (RuntimeException e) {
                if (transaction != null) {
                    transaction.rollback();
                }
            } finally {
                close();
            }
        }
    }
}

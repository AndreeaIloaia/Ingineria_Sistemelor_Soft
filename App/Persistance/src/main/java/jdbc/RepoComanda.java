package jdbc;

import domain.Comanda;
import domain.Produs;
import interfaces.IRepoComanda;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import validator.ValidatorProdus;

import java.util.ArrayList;
import java.util.List;

public class RepoComanda implements IRepoComanda {
    private static final Logger logger = LogManager.getLogger(RepoProdus.class);
    private ValidatorProdus validatorProdus;

    public ValidatorProdus getValidatorProdus() {
        return validatorProdus;
    }

    public void setValidatorProdus(ValidatorProdus validatorProdus) {
        this.validatorProdus = validatorProdus;
    }

    public RepoComanda() {
        logger.info("Constructor finished!");
    }

    @Override
    public Comanda cauta(Integer id) {
        initialize();
        Comanda c = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                String query = "from Comanda c where c.id = :id_comanda";
                c = (Comanda) session.createQuery(query)
                        .setInteger("id_comanda", id)
                        .setMaxResults(1)
                        .uniqueResult();
                transaction.commit();
            } catch (RuntimeException ex) {
                if (transaction != null)
                    transaction.rollback();
            } finally {
                close();
            }
        }
        return c;
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

    @Override
    public void save(Comanda comanda) {
        initialize();
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.save(comanda);
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

    @Override
    public void delete(Integer id) {
        initialize();
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

                Comanda crit = session.createQuery("from Comanda where id=" + String.valueOf(id), Comanda.class)
                        .setMaxResults(1)
                        .uniqueResult();
                System.err.println("Stergem comanda " + crit.getId());
                session.delete(crit);
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            } finally {
                close();
            }
        }
    }

    @Override
    public Comanda findOne(Integer id) {
        initialize();
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Comanda p =
                        session.createQuery("from Comanda where id=" + String.valueOf(id), Comanda.class)
                                .getSingleResult();
                tx.commit();
                return p;
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            } finally {
                close();
            }
        }
        return null;
    }

    @Override
    public void update(Integer id, Comanda p) {
        initialize();
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Comanda cmd = (Comanda) session.load(Comanda.class, id);
                cmd.setSuma(p.getSuma());
                session.update(cmd);
                tx.commit();

            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            } finally {
                close();
            }
        }
    }

    @Override
    public Iterable<Comanda> getAll() {
        initialize();
        List<Comanda> comenzi = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                comenzi =
                        session.createQuery("from Comanda ", Comanda.class).
                                //  setFirstResult(1).setMaxResults(5).
                                        list();
                System.out.println(comenzi.size() + " message(s) found:");
                for (Comanda comanda : comenzi) {
                    System.out.println(comanda.getId() + ' ' + comanda.getClient() + ' ' + comanda.getSuma() + ' ' + comanda.getDate());
                }
                transaction.commit();
            } catch (RuntimeException ex) {
                if (transaction != null)
                    transaction.rollback();
            } finally {
                close();
            }
        }
        return comenzi;

    }
}

package jdbc;

import domain.Admin;
import interfaces.IRepoAdmin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import validator.ValidatorAdmin;

import java.util.ArrayList;
import java.util.List;

public class RepoAdmin implements IRepoAdmin {
    private static final Logger logger = LogManager.getLogger(IRepoAdmin.class);
    private ValidatorAdmin validatorAdmin;

    public ValidatorAdmin getValidatorAdmin() {
        return validatorAdmin;
    }

    public void setValidatorAdmin(ValidatorAdmin validatorAdmin) {
        this.validatorAdmin = validatorAdmin;
    }

    public RepoAdmin() {
        logger.info("Constructor finished!");
    }
    @Override
    public Admin cauta(String user, String pass) {
        initialize();
        Admin c = new Admin();
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                String query = "from Admin c where c.nume like :user and c.parola like :pass";
                c = (Admin) session.createQuery(query)
                        .setString("user", user)
                        .setString("pass", pass)
                        .setMaxResults(1)
                        .uniqueResult();
                transaction.commit();
            } catch (RuntimeException ex) {
                if (transaction != null)
                    transaction.rollback();
            }
            finally {
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
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    static void close(){
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }

    }

    @Override
    public void save(Admin admin) {
        initialize();
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.save(admin);
                transaction.commit();
            } catch (RuntimeException e) {
                if(transaction != null){
                    transaction.rollback();
                }
            } finally {
                close();
            }
        }
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public Admin findOne(Integer integer) {
        return null;
    }

    @Override
    public void update(Integer integer, Admin admin) {

    }

    @Override
    public Iterable<Admin> getAll() {
        initialize();
        List<Admin> casieri = new ArrayList<>();
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                casieri =
                        session.createQuery("select c.nume from Admin c order by c.nume asc", Admin.class).
                                //  setFirstResult(1).setMaxResults(5).
                                        list();
                System.out.println(casieri.size() + " message(s) found:");
                for (Admin casier : casieri) {
                    System.out.println(casier.getId() + ' ' + casier.getNume() +' ' + casier.getParola());
                }
                transaction.commit();
            } catch (RuntimeException ex) {
                if (transaction != null)
                    transaction.rollback();
            } finally {
                close();
            }
        }
        return casieri;
    }
}

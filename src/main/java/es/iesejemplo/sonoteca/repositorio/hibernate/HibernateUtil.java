package es.iesejemplo.sonoteca.repositorio.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Gestiona la {@link SessionFactory} de Hibernate (una única instancia para
 * toda la aplicación). Ya está completa: no hace falta modificarla.
 */
public class HibernateUtil {

    private static SessionFactory sessionFactory;

    private HibernateUtil() {
    }

    public static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        }
        return sessionFactory;
    }

    public static void cerrar() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}

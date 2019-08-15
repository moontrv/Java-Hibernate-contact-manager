package com.contactmanager;

import com.contactmanager.model.Contact;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

public class Application {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory(){
        //Create a standard service registry
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static void main(String[] args){
        Contact contact = new Contact.ContactBuilder("James", "Rodrigeze")
                .withEmail("jame@example.com").withPhone(4444423323L).build();
        //System.out.println(contact);

        int id = save(contact);

        //Display a list of contacts before updating
        //for(Contact c : fetchAllContacts()){
        //    System.out.println(c);
        //}
        System.out.println("Before update");
        fetchAllContacts().stream().forEach(System.out::println);

        //Get the persisted contact
        Contact c = findContactById(id);

        //Update the contact
        c.setFirstName("Train");

        //Persist the changes
        System.out.println("After update");
        update(c);
        System.out.println("Update completed");

        //Display a list of contact after updating
        System.out.println("After update completed");
        fetchAllContacts().stream().forEach(System.out::println);
    }

    private static Contact findContactById(int id){
        //Open a session
        Session session = sessionFactory.openSession();

        Contact contact = session.get(Contact.class, id);

        //Close the session
        session.close();
        return contact;
    }

    private static void update(Contact contact){
        //Open a session
        Session session = sessionFactory.openSession();

        session.beginTransaction();

        session.update(contact);

        session.getTransaction().commit();

        //Close the session
        session.update(contact);
    }

    private static List<Contact> fetchAllContacts(){
        //Open a session
        Session session = sessionFactory.openSession();

        Criteria criteria = session.createCriteria(Contact.class);

        //Get a list of contact object - following criteria object
        List<Contact> contacts = criteria.list();

        //Close the session
        session.close();
        return contacts;
    }

    private static int save(Contact contact){
        //Open session
        Session session = sessionFactory.openSession();
        //Begin a transaction
        session.beginTransaction();
        //Use the session to save the contact
        int id = (int)session.save(contact);
        //Commit the transaction
        session.getTransaction().commit();
        //Close the session
        session.close();
        return id;
    }
}

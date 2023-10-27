package com.banana.client;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import com.banana.domain.MusicItem;

public class JPAClient {
  // executed at classload time
  static {
    System.setProperty("java.util.logging.config.file", "logging.properties");  // reduces Hibernate logging output
  }
  
  // one factory instantiated per application - factory is a long-lived "expensive" object
  private EntityManagerFactory emf;
  
  
  public static void main(String[] args) {
    JPAClient client = new JPAClient();

    System.out.println("\n***queryById(1L)");    
    client.queryById(1L);

//    System.out.println("\n*** queryByArtist(\"Madonna\")");
//    client.queryByArtist("madonna");

//    System.out.println("\n*** createItem()");    
//    client.createItem();
//    client.queryByArtist("the worms");  // should be there

//    System.out.println("\n*** deleteItem(1L)");    
//    client.deleteItem(1L);
//    client.queryById(1L);    
    
//    System.out.println("\n*** updateItem(2L)");
//    client.queryById(2L);
//    client.updateItem(2L);
//    client.queryById(2L);
    
    client.cleanUp();
  }  
  
  private JPAClient() {
	// DONE: Initialize the EMF
    emf = Persistence.createEntityManagerFactory("banana");  // named in persistence.xml
    System.out.println("EMF class: " + emf.getClass().getName());
  }
  
  // close the factory
  private void cleanUp() {
    if (emf != null) {
      emf.close();
      System.out.println("EMF closed");
    }
  }
  
  private void queryById(Long id) {
    // DONE: Create the entity manager (EM) - a short-lived "inexpensive" object, used for a single transaction
    EntityManager em = emf.createEntityManager();
    em.getTransaction().begin();
    
    // DONE: Use the EntityManger to find the MusicItem by the given id
    MusicItem item = em.find(MusicItem.class, id);
    System.out.println(item);
    
    em.getTransaction().commit();
    em.close();
  }
  
  private void queryByArtist(String artist) {
    String sql = "SELECT item FROM MusicItem item WHERE LOWER(item.artist) = :artist";  // named parameter
    
    EntityManager em = emf.createEntityManager();
    em.getTransaction().begin();
    
    TypedQuery<MusicItem> query = em.createQuery(sql, MusicItem.class);
    query.setParameter("artist", artist.toLowerCase());
    
    List<MusicItem> resultList = query.getResultList();
    for (MusicItem item : resultList) {
      System.out.println(item);
    }
    
    em.getTransaction().commit();
    em.close();
  }
  
  private void createItem() {
    EntityManager em = emf.createEntityManager();
    em.getTransaction().begin();
    
    MusicItem item = new MusicItem();
    item.setTitle("Persistence Is Everything");
    item.setArtist("The Worms");
    item.setReleaseDate(LocalDate.of(2017, 8, 15));
    item.setPrice(new BigDecimal("11.11"));
    em.persist(item);  // id gets filled in upon insert
    
    System.out.println("MusicItem " + item.getId() + " created");
    
    em.getTransaction().commit();
    em.close();
  }
  
  private void deleteItem(Long id) {
    EntityManager em = emf.createEntityManager();
    em.getTransaction().begin();
    
    MusicItem item = em.find(MusicItem.class, id);
    em.remove(item);
    
    System.out.println("MusicItem " + id + " deleted");
    
    em.getTransaction().commit();
    em.close();
  }
  
  private void updateItem(Long id) {
    EntityManager em = emf.createEntityManager();
    em.getTransaction().begin();
    
    // retrieve it and then call setter methods on it, e.g., uppercase the title
    MusicItem item = em.find(MusicItem.class, id);
    item.setTitle(item.getTitle().toUpperCase());
    
    System.out.println("MusicItem " + id + " updated");
    
    em.getTransaction().commit();
    em.close();
  }
  

}
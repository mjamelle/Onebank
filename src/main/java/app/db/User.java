package app.db;

import java.util.List;
import org.sql2o.*;
import lombok.*;
import com.fasterxml.jackson.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Data
public class User {
  @JsonIgnore  
  private static final Logger LOGGER = LogManager.getLogger();
    
  private int id;
  private String givenName;
  private String surName;
  private String username;
  private String password;
  private String photolink;
  private String email;
  private String function;
  private boolean jabber_use;
  private boolean spark_use;
  private boolean adminprivilege;
  
  public static List<User> all(String jtStartIndex, String jtPageSize, String jtSorting) {
    String sql = "SELECT * FROM users ORDER BY " + jtSorting + " LIMIT " + jtPageSize + " OFFSET " + jtStartIndex;
    try(Connection con = DB.sql2o.open()) {
     return con.createQuery(sql).executeAndFetch(User.class);
    }
  }
  
    public static List<User> all() {
    String sql = "SELECT * FROM users";
    try(Connection con = DB.sql2o.open()) {
     return con.createQuery(sql).executeAndFetch(User.class);
    }
  }
  
  
 public static int getUserCount() {
    //int count = 4; 
    String sql = "SELECT COUNT (*) FROM users";
    try(Connection con = DB.sql2o.open()) {
        Integer  count = con.createQuery(sql)
        .executeScalar(Integer.class);
     return count;
    }
  } 
  
  @JsonIgnore
  public String getDisplayName() {
      return givenName + " " + surName;
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
        String sql = "INSERT INTO users(givenName, surName, email, jabber_use, spark_use, adminprivilege, photolink,"
          + "username, password, function) VALUES (:givenName, :surName, :email, :jabber_use, :spark_use, :adminprivilege,"
          + " :photolink, :username, :password, :function)";
        this.id = (int) con.createQuery(sql, true)
        .addParameter("givenName", this.givenName)
        .addParameter("surName", this.surName)
        .addParameter("email", this.email)
        .addParameter("jabber_use", this.jabber_use)
        .addParameter("spark_use", this.spark_use)
        .addParameter("adminprivilege", this.adminprivilege)
        .addParameter("photolink", this.photolink)
        .addParameter("username", this.username)
        .addParameter("password", this.password)
        .addParameter("function", this.function)      
        .executeUpdate()
        .getKey();
    }
  }

  public static User find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM users where id=:id";
      User task = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(User.class);
      return task;
    }
  }

  public void update() {
    try(Connection con = DB.sql2o.open()) {
    String sql = "UPDATE users SET givenName = :givenName, surName = :surName, email = :email, jabber_use = :jabber_use,"
            + "spark_use = :spark_use, adminprivilege = :adminprivilege, photolink = :photolink, function = :function,"
            + "username = :username, password= :password WHERE id = :id";
    con.createQuery(sql)
      .addParameter("givenName", this.givenName)
      .addParameter("surName", this.surName)
      .addParameter("email", this.email)
      .addParameter("jabber_use", this.jabber_use)
      .addParameter("spark_use", this.spark_use)
      .addParameter("adminprivilege", this.adminprivilege)
      .addParameter("photolink", this.photolink)
      .addParameter("username", this.username)
      .addParameter("password", this.password)
      .addParameter("function", this.function)      
      .addParameter("id", id)
      .executeUpdate();
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
    String sql = "DELETE FROM users WHERE id = :id";
    con.createQuery(sql)
      .addParameter("id", id)
      .executeUpdate();
    }
  }

  
  public static User getUserByUsername(String username) {
      try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM users where username=:username";
      User task = con.createQuery(sql)
        .addParameter("username", username)
        .executeAndFetchFirst(User.class);
      return task;
    }     
  }      
}

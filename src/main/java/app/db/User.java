package app.db;

import java.util.List;
import org.sql2o.*;

public class User {
  private int id;
  private String givenName;
  private String surName;
  private String username;
  private String password;
  private String photolink;
  private String email;
  private boolean jabber_use;
  private boolean spark_use;
  private boolean adminprivilege;
  
  public User(String givenName, String surName, String username, String password, String photolink,
    String email, boolean jabber_use, boolean spark_use, boolean adminprivilege) {
    this.givenName = givenName;
    this.surName = surName;
    this.username = username;
    this.password = password;
    this.photolink = photolink;
    this.email = email;
    this.jabber_use = jabber_use;
    this.spark_use = spark_use;
    this.adminprivilege = adminprivilege;
  }

    public int getId() {
        return id;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhotolink() {
        return photolink;
    }

    public void setPhotolink(String photolink) {
        this.photolink = photolink;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isJabber_use() {
        return jabber_use;
    }

    public void setJabber_use(boolean jabber_use) {
        this.jabber_use = jabber_use;
    }

    public boolean isSpark_use() {
        return spark_use;
    }

    public void setSpark_use(boolean spark_use) {
        this.spark_use = spark_use;
    }

    public boolean isAdminprivilege() {
        return adminprivilege;
    }

    public void setAdminprivilege(boolean adminprivilege) {
        this.adminprivilege = adminprivilege;
    }


  public static List<User> all() {
    String sql = "SELECT id, givenName, surName, email, jabber_use, spark_use, adminprivilege, photolink,"
            + "username, password FROM users";
    try(Connection con = DB.sql2o.open()) {
     return con.createQuery(sql).executeAndFetch(User.class);
    }
  }

  @Override
  public boolean equals(Object otherUser){
    if (!(otherUser instanceof User)) {
      return false;
    } else {
      User newUser = (User) otherUser;
      return this.getUsername().equals(newUser.getUsername()) &&
             this.getId() == newUser.getId();
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO users(givenName, surName, email, jabber_use, spark_use, adminprivilege, photolink,"
              + "username, password) VALUES (:givenName, :surName, :email, :jabber_use, :spark_use, :adminprivilege,"
              + " :photolink, :username, :password)";
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

  public void update(String description) {
    try(Connection con = DB.sql2o.open()) {
    String sql = "UPDATE users SET givenName = :givenName, surName = :surName, email = :email, jabber_use = :jabber_use,"
            + "spark_use = :spark_use, adminprivilege = :adminprivilege, photolink = :photolink,"
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

}

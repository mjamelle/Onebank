package root;

import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;

public class Consultant {
  private int id;
  private String givenName;
  private String surName;
  private String email;
  private boolean jabber_use;
  private boolean spark_use;
  
  public Consultant(String givenName, String surName) {
    this.givenName = givenName;
    this.surName = surName;
    jabber_use = false;
    spark_use = false;
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


  public static List<Consultant> all() {
    String sql = "SELECT id, description, categoryId FROM tasks";
    try(Connection con = DB.sql2o.open()) {
     return con.createQuery(sql).executeAndFetch(Consultant.class);
    }
  }

  @Override
  public boolean equals(Object otherTask){
    if (!(otherTask instanceof Consultant)) {
      return false;
    } else {
      Consultant newTask = (Consultant) otherTask;
      return this.getGivenName().equals(newTask.getGivenName()) &&
             this.getId() == newTask.getId();
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO tasks(description, categoryId) VALUES (:description, :categoryId)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("description", this.givenName)
        .executeUpdate()
        .getKey();
    }
  }

  public static Consultant find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM tasks where id=:id";
      Consultant task = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Consultant.class);
      return task;
    }
  }

  public void update(String description) {
    try(Connection con = DB.sql2o.open()) {
    String sql = "UPDATE tasks SET description = :description WHERE id = :id";
    con.createQuery(sql)
      .addParameter("description", description)
      .addParameter("id", id)
      .executeUpdate();
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
    String sql = "DELETE FROM tasks WHERE id = :id;";
    con.createQuery(sql)
      .addParameter("id", id)
      .executeUpdate();
    }
  }

}

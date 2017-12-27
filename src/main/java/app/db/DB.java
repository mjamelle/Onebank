package app.db;

import app.util.SystemConfig;
import org.sql2o.*;

public class DB {
  public static Sql2o sql2o = new Sql2o(SystemConfig.getPostgresDBLink(), SystemConfig.getPostgresUser(), SystemConfig.getPostgresPassword());
}

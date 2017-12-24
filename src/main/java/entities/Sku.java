package entities;

import org.json.JSONObject;

/**
 * Sku
 */
public class Sku {
  private Integer id;

  private String name;

  private JSONObject body;

  public Sku(String name, Integer id, JSONObject body) {
    this.name = name;
    this.id = id;
    this.body = body;
  }

  /**
   * @param id the id to set
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * @return the id
   */
  public Integer getId() {
    return id;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param body the body to set
   */
  public void setBody(JSONObject body) {
    this.body = body;
  }

  /**
   * @return the body
   */
  public JSONObject getBody() {
    return body;
  }

  public static class Map {
    public static Sku toSku(String name, JSONObject body) {
      String[] nameParts = name.split("-");
      Sku sku = new Sku(name, Integer.valueOf(nameParts[1]), body);

      return sku;
    }
  }
}
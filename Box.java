package de.arvato.lambda;

public class Box {
  private int weight = 0;

  private String color = "";

  public Box(final int weight, final String color) {
    this.weight = weight;
    this.color = color;
  }

  public Integer getWeight() {
    return weight;
  }

  public void setWeight(final Integer weight) {
    this.weight = weight;
  }

  public String getColor() {
    return color;
  }

  public void setColor(final String color) {
    this.color = color;
  }

  @Override
  public String toString() {
    return "Apple{" + "color='" + color + '\'' + ", weight=" + weight + '}';
  }

}

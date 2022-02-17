package de.arvato.lambda;

public class Person {
  public String name;

  public int age;

  public Person(final String name, final int age) {
    this.name = name;
    this.age = age;
  }

  @Override
  public String toString() {
    return name;
  }
}

package de.arvato.lambda;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Ignore;
import org.junit.Test;

//@formatter:off
/**
 *
 * @author gome016
 *
 */
@Ignore
public class Java8Test {

  @Test
  public void executeRunBeforeJava8Test() {
    final Runnable r = new Runnable() {
      @Override
      public void run() {
        // This is our execution code
        System.out.println("My Runnable");
      }
    };
    r.run();
  }

  /**
   * What's happening in the above code 1.Runable is a functional Interface and we can use lambda
   * expression to create an instance
   *
   * @FunctionalInterface public interface Runnable 2. Method run has no arguments, our lambda
   *                      expression has no arguments. 3. Optionally we can use {} as we have a
   *                      single statement
   */
  @Test
  public void executeRunWithFunctionalInterface() {
    final Runnable r1 = () -> System.out.println("My Runnable");
    r1.run();
    final Runnable r2 = () -> {
      System.out.println("My Runnable");
      System.out.println("End My Runnable");
    };
    r2.run();
  }

  // check if a number is prime or not
  @Test
  public void executePrimeBeforeJava8Test() {
    final long mil = System.currentTimeMillis();
    assertTrue(!isPrime(1));
    assertTrue(isPrime(2));
    assertTrue(isPrime(3));
    assertTrue(isPrime(5));
    assertTrue(!isPrime(98937));
    assertTrue(isPrime(983423423));
    System.out.println(System.currentTimeMillis() - mil);
  }

  @Test
  public void executePrimeWithFunctionalTest () {
    final long mil = System.currentTimeMillis();
    assertTrue(!isPrimeWithLambda(1));
    assertTrue(isPrimeWithLambda(2));
    assertTrue(isPrimeWithLambda(3));
    assertTrue(isPrimeWithLambda(5));
    assertTrue(!isPrimeWithLambda(98937));
    assertTrue(isPrimeWithLambda(983423423));
    System.out.println(System.currentTimeMillis() - mil);
  }

  /**
    // Inneficient code why?
    // 1. it's sequential in nature
    // 2. If the number is very huge, it will take time to process it.
   *
   * @param number
   * @return
   */
  private static boolean isPrime(final int number) {
    if(number < 2) return false;
    for(int i=2; i<number; i++){
      if(number % i == 0) return false;
    }
    return true;
  }

  private static boolean isPrimeWithLambda(final int number) {
    return number > 1
        && IntStream.range(2, number).noneMatch(
            index -> number % index == 0);
  }

  @SuppressWarnings("unused")
  private static boolean isPrimeWithLambdaMoreReadable(final int number) {
    final IntPredicate isDivisible = (index) -> number % index == 0;
    return number > 1
        && IntStream.range(2, number).noneMatch(isDivisible);
  }


  /**
   * Using IntPredicate
   */
  @Test
  public void exampleIntPredicateNegateTest() {
    final IntPredicate i = (x) -> x < 0;
    System.out.println(i.negate().test(123));

    final IntPredicate i2 = (x) -> x + 5 < 0;
    System.out.println(i2.negate().test(-3));

  }

  /**
   * Using Consumer
   */
  @Test
  public void exampleConsumerTest() {
    final Consumer<String> c = (x) -> System.out.println(x.toLowerCase());
    c.accept("Hello World");
  }

  @Test
  public void acceptAllEmployee () {
    final List<Student> students = Arrays.asList(
        new Student("Genis", 3),
        new Student("Bernardo", 4));

    System.out.println("AcceptAllEmployee 1");
    acceptAllEmployee(students, e -> System.out.println(e.name));

    System.out.println("AcceptAllEmployee 2");
    acceptAllEmployee(students, e -> {e.gpa *= 1.5; });

    System.out.println("AcceptAllEmployee 3");
    acceptAllEmployee(students, e -> System.out.println(e.name + ": " + e.gpa));
  }

  public static void acceptAllEmployee(final List<Student> student,
      final Consumer<Student> printer) {
    for (final Student e : student) {
      printer.accept(e);
    }
  }

  @Test
  public void examplePredicate () {
    final Predicate<String> i = (s) -> s.length() > 7;
    System.out.println(i.test("arvato SCM"));
    System.out.println(i.test("arvato"));
    final Predicate<String> x = i.or(j -> j.length()> 5);
    System.out.println(x.test("arvato"));

  }

  @Test
  public void testFilter () {
    // final Predicate<Box> myCustomBox = (b) -> b.getWeight() > 0 && b.getWeight() < 100;

    final List<Box> inventory = Arrays.asList(new Box(80, "green"), new Box(
        155, "green"), new Box(120, "red"));

    // final List<Box> myList = filter(inventory, myCustomBox);
    // System.out.println(myList);

    final List<Box> greenApples = filter(inventory, Java8Test::isGreenApple);
    System.out.println(greenApples);

    final List<Box> heavyApples = filter(inventory, Java8Test::isHeavyApple);
    System.out.println(heavyApples);

    final List<Box> greenApples2 = filter(inventory, (final Box a) -> "green".equals(a.getColor()));
    System.out.println(greenApples2);

    final List<Box> heavyApples2 = filter(inventory, (final Box a) -> a.getWeight() > 150);
    System.out.println(heavyApples2);

    final List<Box> weirdApples = filter(inventory,
        (final Box a) -> a.getWeight() < 80 || "brown".equals(a.getColor()));
    System.out.println(weirdApples);

  }

  public static boolean isGreenApple(final Box apple) {
    return "green".equals(apple.getColor());
  }

  public static boolean isHeavyApple(final Box apple) {
    return apple.getWeight() > 150;
  }

  public static List<Box> filter(final List<Box> inventory,
      final Predicate<Box> p) {
    final List<Box> result = new ArrayList<>();
    for (final Box apple : inventory) {
      if (p.test(apple)) {
        result.add(apple);
      }
    }
    return result;
  }


  /////////////////////////////////////////////////////
  // STREAMS
  ///////////////////////////////////////////////////
  /**
   * !! Java Streams is a completely different thing than InputStream or OutputStream !!
   */

  @Test
  public void testStream1 () {
    final List<String> myList = Arrays.asList("a1", "a2", "b1", "c2", "c1");
    myList
    .stream()
    .filter(s -> s.startsWith("c"))
    .map(String::toUpperCase)
    .sorted()
    .forEach(System.out::println);
//    .forEachOrdered(System.out::println);

  }

  @Test
  public void testStream2 () {
    /**
     * Streams are created from various data source (especially collections):
     *  - Lists and Sets support following methods:
     *    - stream ()
     *    - parallelStream ()
     */

    final Optional<String> optional = Arrays.asList("a1", "a2", "a3")
    .stream()
    .findFirst();
    ;
//    .ifPresent(System.out::println);

    // There is no need to craete Collections to be able to work with Streams
    Stream.of("a1", "a2", "a3")
    .findFirst()
    .ifPresent(System.out::println);

    // To work with primitive data types
    IntStream.range(1, 4)
    .forEach(System.out::println);

    // Average example
    Arrays.stream(new int[] {1, 2, 3})
    .map(n -> 2 * n + 1)
    .average()
    .ifPresent(System.out::println);
    /*
    2+1 = 3
    2*2 +1 = 5
    3*2 +1 = 7
    3+5+7 = 15 / 3 = 5
     */

    // Transform Regular Objects to privimive stream o vice versa
    Stream.of("a1", "a2", "a3")
    .map(s -> s.substring(1))
    .mapToInt(Integer::parseInt)
    .max()
    .ifPresent(System.out::println);

    // viceversa
    IntStream.range(1, 4)
    .mapToObj(i -> "a" + i)
    .forEach(System.out::println);
  }

  @Test
  public void testStream3 () {
    // Intermediate Operations are Lazy !!!
    Stream.of("d2", "a2", "b1", "b3", "c")
    .filter(s -> {
      System.out.println("filter: " + s);
      return true;
    });
  }


  @Test
  public void testStream4 () {
    // Adding Terminal Operation forEach
    Stream.of("d2", "a2", "b1", "b3", "c")
    .filter(s -> {
      System.out.println("filter: " + s);
      return true;
    }).sorted();

//    .forEach(s -> System.out.println("forEach: " + s));
  }

  @Test
  public void testStream5 () {
    // This behaviour reduces the number of operations performed on each element
    Stream.of("d2", "a2", "b1", "b3", "c")
    .map(s -> {
      System.out.println("map: " + s);
      return s.toUpperCase();
    })
    .anyMatch(s -> {
      System.out.println("anyMatch: " + s);
      return s.startsWith("A");
    });
  }

  @Test
  public void testStream6 () {
    // Order of operations matters
    Stream.of("d2", "a2", "b1", "b3", "c")
    .filter(s -> {
      System.out.println("filter: " + s);
      return s.startsWith("a");
    })
    .map(s -> {
      System.out.println("map: " + s);
      return s.toUpperCase();
    })
    .forEach(s -> System.out.println("forEach: " + s));
  }

  @Test
  public void testStream7 () {
    // Sorting
    Stream.of("d2", "a2", "b1", "b3", "c")
    .filter(s -> {
      System.out.println("filter: " + s);
      return s.startsWith("a");
    })
    .sorted((s1, s2) -> {
      System.out.printf("sort: %s; %s\n", s1, s2);
      return s1.compareTo(s2);
    })
    .map(s -> {
      System.out.println("map: " + s);
      return s.toUpperCase();
    })
    .forEach(s -> System.out.println("forEach: " + s));
  }

  @Test
  public void testStream8 () {
    // Reusing Streams
    final Stream<String> stream =
        Stream.of("d2", "a2", "b1", "b3", "c")
        .filter(s -> s.startsWith("a"));
    stream.anyMatch(s -> true);
    stream.noneMatch(s -> true);
  }

  @Test
  public void testStream9 () {
    // It chrases because we have to create a new stream chaing
    final Supplier<Stream<String>> streamSupplier =
        () -> Stream.of("d2", "a2", "b1", "b3", "c")
        .filter(s -> s.startsWith("a"));

        System.out.println(streamSupplier.get().anyMatch(s -> true));
        System.out.println(streamSupplier.get().noneMatch(s -> true));

        // Calling get creates a new stream
  }

  @Test
  public void testFlatMap () {
    /**
     *  In Java 8, stream can hold different data type:
        Stream<String[]>
        Stream<Set<String>>
        Stream<List<String>>
        Stream<List<Object>>

        But, Stream operations and collectors do not support it. We need to convert

        Stream<String[]>    -> flatMap -> Stream<String>
        Stream<Set<String>> -> flatMap -> Stream<String>
        Stream<List<String>>  -> flatMap -> Stream<String>
        Stream<List<Object>>  -> flatMap -> Stream<Object>

     **/

    final String[][] data = new String[][]{{"a", "b"}, {"c", "d"}, {"e", "f"}};
    //Stream<String[]>
    final Stream<String[]> temp = Arrays.stream(data);
    //Stream<String>, GOOD!
    final Stream<String> stringStream = temp.flatMap(x -> Arrays.stream(x));
    final Stream<String> stream = stringStream.filter(x -> "a".equals(x.toString()));
    stream.forEach(System.out::println);
  }


  @Test
  public void testFlatMapNoWork () {
    final String[][] data = new String[][]{{"a", "b"}, {"c", "d"}, {"e", "f"}};
    //Stream<String[]>
    final Stream<String[]> temp = Arrays.stream(data);
    //filter a stream of string[], and return a string[]?
    final Stream<String[]> stream = temp.filter(x -> "a".equals(x.toString()));
    stream.forEach(System.out::println);
  }

  @Test
  public void testStream10 () {
    // Collectors common use
    final List<Person> persons =
        Arrays.asList(
            new Person("Max", 18),
            new Person("Peter", 23),
            new Person("Pamela", 23),
            new Person("David", 12));

    final List<Person> filtered =
        persons
        .stream()
        .filter(p -> p.name.startsWith("P"))
        .collect(Collectors.toList());

    System.out.println(filtered);
    /*
     *  Collectors accept 4 different operations:
     *
     *  1. Supplier
     *  2. Accumulator
     *  3. Combiner
     *  4. Finisher
     *
     */
  }

  @Test
  public void testStream11 () {
    // Collectors Grouping by
    final List<Person> persons =
        Arrays.asList(
            new Person("Max", 18),
            new Person("Peter", 23),
            new Person("Pamela", 23),
            new Person("David", 12));

    final Map<Integer, List<Person>> personsByAge = persons
        .stream()
        .collect(Collectors.groupingBy(p -> p.age));

    personsByAge
    .forEach((age, p) -> System.out.format("age %s: %s\n", age, p));
  }

  @Test
  public void testStream12 () {
    // Collectors Statistics
    final List<Person> persons =
        Arrays.asList(
            new Person("Max", 18),
            new Person("Peter", 23),
            new Person("Pamela", 23),
            new Person("David", 12));

    final IntSummaryStatistics ageSummary =
        persons
        .stream()
        .collect(Collectors.summarizingInt(p -> p.age));
    System.out.println(ageSummary);
  }

  /**
   *
   */
  @Test
  public void testStream13 () {
    // Convert List to Map
    final List<Person> persons =
        Arrays.asList(
            new Person("Max", 18),
            new Person("Peter", 23),
            new Person("Pamela", 23),
            new Person("David", 12));
    final Map<Integer, String> map = persons
        .stream()
        .collect(Collectors.toMap(
            p -> p.age,
            p -> p.name,
            (name1, name2) -> name1 + ";" + name2));
    System.out.println(map);
  }

  @Test
  public void testStream14 () {
    // Built our special Collector
    final List<Person> persons =
        Arrays.asList(
            new Person("Max", 18),
            new Person("Peter", 23),
            new Person("Pamela", 23),
            new Person("David", 12));

    final Collector<Person, StringJoiner, String> personNameCollector =
        Collector.of(
            () -> new StringJoiner(" | "),          // supplier
            (j, p) -> j.add(p.name.toUpperCase()),  // accumulator
            (j1, j2) -> j1.merge(j2),               // combiner
            StringJoiner::toString);                // finisher


    final String names = persons
        .stream()
        .collect(personNameCollector);

    System.out.println(names);
    // We need StringJoiner to let the collector construct our string

  }

  @Test
  public void testStream15 () {
    // Reduction Type 1. Accepts a BinaryOperator. Reduce a stream of elements to exactly one element of the stream.
    final List<Person> persons =
        Arrays.asList(
            new Person("Pamela", 23),
            new Person("Max", 18),
            new Person("Peter", 23),
            new Person("David", 12));
    persons
    .stream()
    .reduce((p1, p2) -> {
      System.out.println("p1,p2:" + p1 + p2);
      return p1.age > p2.age ? p1 : p2;
    })
    .ifPresent(System.out::println);
    // Reduce accepts a BinaryOperator accumulation function
  }

  @Test
  public void testStream16 () {
    // Reduction Type 2. Accumulator. Accepts an identity value and a BinaryOperator
    final List<Person> persons =
        Arrays.asList(
            new Person("Max", 18),
            new Person("Peter", 23),
            new Person("Pamela", 23),
            new Person("David", 12));
    final Person result =
        persons
        .stream()
        .reduce(new Person("", 0), (p1, p2) -> {
          p1.age += p2.age;
          p1.name += p2.name;
          return p1;
        });
    System.out.format("name=%s; age=%s", result.name, result.age);
  }

  @Test
  public void testStream17 () {
    // Reduction Type 3. Combiner: Accepts an identify value, BiFunction accumulator and a Combiner Function (BinaryOperator)
    final List<Person> persons =
        Arrays.asList(
            new Person("Max", 18),
            new Person("Peter", 23),
            new Person("Pamela", 23),
            new Person("David", 12));

    final Integer ageSum = persons
        .stream()
        .reduce(0,
            (sum, p) -> {
              System.out.format("accumulator: sum=%s; person=%s\n", sum, p);
              return sum += p.age;
            },
            (sum1, sum2) -> {
              System.out.format("combiner: sum1=%s; sum2=%s\n", sum1, sum2);
              return sum1 + sum2;
            });

    System.out.println(ageSum);
    // What happens with the combiner?
  }

  @Test
  public void testStream18 () {
    // Reduction Type 3. Same than before executing in parallelStream
    final List<Person> persons =
        Arrays.asList(
            new Person("Max", 18),
            new Person("Peter", 23),
            new Person("Pamela", 23),
            new Person("David", 12));
    final Integer ageSum = persons
        .parallelStream()
        .reduce(0,
            (sum, p) -> {
              System.out.format("accumulator: sum=%s; person=%s\n", sum, p);
              return sum += p.age;
            },
            (sum1, sum2) -> {
              System.out.format("combiner: sum1=%s; sum2=%s\n", sum1, sum2);
              return sum1 + sum2;
            });
    System.out.println(ageSum);
    // Here the combiner is called since the accumulator is called in parallel. So then combiner is needed to sum up the
    // separate accumulated values.
  }

  @Test
  public void testStream19 () {
    // Parallel Stream will use the available threads from the common ForkJoinPool.

    final ForkJoinPool commonPool = ForkJoinPool.commonPool();
    System.out.println(commonPool.getParallelism());
    // Can use up to five threads. Depending on available physical CPU

    // -Djava.util.concurrent.ForkJoinPool.common.parallelism=5

    Arrays.asList("a1", "a2", "b1", "c2", "c1")
    .parallelStream()
    .filter(s -> {
      System.out.format("filter: %s [%s]\n",
          s, Thread.currentThread().getName());
      return true;
    })
    .map(s -> {
      System.out.format("map: %s [%s]\n",
          s, Thread.currentThread().getName());
      return s.toUpperCase();
    })
    .forEach(s -> System.out.format("forEach: %s [%s]\n",
        s, Thread.currentThread().getName()));
  }

  @Test
  public void testStream20 () {
    // Parallel Streams
    final Stream<String> res =
        Arrays.asList("a1", "a2", "b1", "c2", "c1")
        .parallelStream()
        .filter(s -> {
          System.out.format("filter: %s [%s]\n",
              s, Thread.currentThread().getName());
          return true;
        })
        .map(s -> {
          System.out.format("map: %s [%s]\n",
              s, Thread.currentThread().getName());
          return s.toUpperCase();
        })
        .sorted((s1, s2) -> {
          System.out.format("sort: %s <> %s [%s]\n",
              s1, s2, Thread.currentThread().getName());
          return s1.compareTo(s2);
        });
    //res.forEach(System.out::println);
    res.forEachOrdered(System.out::println);
  }
}

//@formatter:on
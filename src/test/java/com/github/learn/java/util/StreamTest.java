package com.github.learn.java.util;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author zhanfeng.zhang
 * @date 2020/5/13
 */
@Slf4j
public class StreamTest {

    final int[] intArray = {1, 2, 3};
    final List<Integer> integerList = Arrays.asList(1, 2, 3);
    final double[] doubleArray = {1.1, 2.2, 3.3};
    final List<Double> doubleList = Arrays.asList(1.1, 2.2, 3.3);
    final List<Person> personList = new ArrayList<>();
    final List<String> strList = new LinkedList<>();

    @Test void givenStream_whenCreate_then() {
        Stream<String> emptyStringStream = Stream.empty();
        Stream<Integer> integerStream = Stream.of(1, 2, 3);
        Stream<Integer> iterate = Stream.iterate(0, i -> i + 1);
        then(iterate.skip(8).limit(8).max(Comparator.naturalOrder()).get()).isEqualTo(15);
        Stream<String> build = Stream.<String>builder().add("a").add("b").build();
        then(build.count()).isEqualTo(2);
        // array -> Stream
        IntStream stream = Arrays.stream(intArray);
        final Integer[] integerArray = new Integer[] {1, 2, 3};
        Stream<Integer> stream1 = Arrays.stream(integerArray);
        // IntStream
        then(IntStream.range(1, 10).max()).hasValue(9);
    }

    @Test void whenTestStreamLazyInvocation_thenSuccess() {
        AtomicInteger filter = new AtomicInteger();
        AtomicInteger map = new AtomicInteger();
        String s = Stream.of("abc1", "acb2", "abc3")
            .filter(str -> {
                filter.getAndIncrement();
                return str.contains("4");
            }).map(str -> {
                map.getAndIncrement();
                return str.toUpperCase();
            }).findFirst()
            .orElse("not Found");
        then(filter).hasValue(3);
        then(map).hasValue(0);
    }

    @Test void Collections_toList() {
        then(integerList.stream().collect(toList())).hasSameElementsAs(integerList);
    }

    @Test void Collections_toSet() {
        then(integerList.stream().collect(toSet())).hasSameElementsAs(integerList);
    }

    @Test void Collections_toCollection() {
        then((Collection<Integer>)integerList.stream().collect(toCollection(LinkedList::new)))
            .hasSameElementsAs(integerList);
    }

    @Test void Collections_toMap() {
        then(integerList.stream().collect(toMap(String::valueOf, Function.identity())))
            .containsValues(integerList.toArray(new Integer[0]));
    }

    @Test void Stream_reduce() {
        Optional<Integer> reduce = integerList.stream().reduce(Integer::sum);
        then(reduce).hasValue(6);
        Integer reduce2 = integerList.stream().reduce(10, Integer::sum);
        then(reduce2).isEqualTo(16);
    }

    @Test
    void whenTestStreamReduce_thenSuccess() {
        // Collections.joining()
        assertThat(Stream.of("1", "2", "3")
            .collect(joining(", ", "[", "]"))
        ).isEqualTo("[1, 2, 3]");
        // Collections.summarizingDouble()
        DoubleSummaryStatistics doubleSummaryStatistics = doubleList.stream()
            .collect(summarizingDouble(Double::doubleValue));
        assertThat(doubleSummaryStatistics.getMin()).isCloseTo(1.1, offset(0.0001));
        assertThat(doubleSummaryStatistics.getCount()).isEqualTo(3);
        assertThat(doubleSummaryStatistics.getSum()).isCloseTo(6.6, offset(0.0001));
        // Collections.groupingBy()
        Map<Person.Sex, List<Person>> sexListMap = personList.stream()
            .collect(groupingBy(Person::getSex));
        Set<Integer> collect2 = personList.stream()
            .collect(mapping(Person::getAge, toSet()));
        Map<Person.Sex, Set<Integer>> collect1 = personList.stream()
            .collect(groupingBy(Person::getSex, mapping(Person::getAge, toSet())));
        Map<Boolean, List<Person>> collect3 = personList.stream()
            .collect(Collectors.partitioningBy(p -> p.getSex() == Person.Sex.MALE));
        Map<Boolean, Set<Person>> collect4 = personList.stream()
            .collect(partitioningBy(p -> p.getSex() == Person.Sex.MALE, toSet()));
        // list -> set -> unmodifiableSet
        Set<Person> collect5 = personList.stream()
            .collect(collectingAndThen(toSet(), Collections::unmodifiableSet));
    }

    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    @Data
    public static class Person {

        String name;
        int age;
        Sex sex;

        @Getter
        @RequiredArgsConstructor
        @ToString
        enum Sex {
            MALE(0),
            FAMALE(1);
            final int v;
        }
    }
}

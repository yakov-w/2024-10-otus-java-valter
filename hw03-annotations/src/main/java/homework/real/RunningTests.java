package homework.real;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class RunningTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(RunningTests.class);

    private final Set<String> testClasses;

    public RunningTests(Set<String> testClasses) {
        this.testClasses = testClasses;
    }

    private static class TestStatistics {
        private final String className;
        private int countGoodTests = 0;
        private int countBadTests = 0;
        private int countTotalTests = 0;

        public TestStatistics(String className) {
            this.className = className;
        }

        public int getCountGoodTests() {
            return countGoodTests;
        }

        public void incrementGoodTests() {
            this.countGoodTests++;
        }

        public int getCountBadTests() {
            return countBadTests;
        }

        public void incrementBadTests() {
            this.countBadTests++;
        }

        public int getCountTotalTests() {
            return countTotalTests;
        }

        public void incrementTotalTests() {
            this.countTotalTests++;
        }

        public TestStatistics accumulator(TestStatistics testStatistics) {
            this.countGoodTests += testStatistics.getCountGoodTests();
            this.countBadTests += testStatistics.getCountBadTests();
            this.countTotalTests += testStatistics.getCountTotalTests();
            return this;
        }

        public void printStat() {
            LOGGER.info("TestStatistics: {}, countGoodTests: {}, countBadTests: {}, countTotalTests: {}", className, countGoodTests, countBadTests, countTotalTests);
        }
    }

    public static void main(String[] args) throws ClassNotFoundException {
        Set<String> testClasses = Set.of("homework.SomeClassTestOne", "homework.SomeClassTestTwo");
        RunningTests rt = new RunningTests(testClasses);

        LOGGER.info("Begin execution TESTS!");
        rt.run();
        LOGGER.info("End execution TESTS!");
    }

    private void run() throws ClassNotFoundException {
        List<TestStatistics> statistics = new ArrayList<>();
        for (String name : testClasses) {
            List<Method> methodsBefore = new ArrayList<>();
            Deque<Method> methodsTest = new LinkedList<>();
            List<Method> methodsAfter = new ArrayList<>();

            Class<?> clazz = Class.forName(name);
            Arrays.stream(clazz.getDeclaredMethods()).forEach(method -> {
                Arrays.stream(method.getDeclaredAnnotations()).forEach(a -> {
                    switch (a.annotationType().getSimpleName()) {
                        case "Before" -> methodsBefore.add(method);
                        case "Test" -> methodsTest.add(method);
                        case "After" -> methodsAfter.add(method);
                    }
                });
            });

            TestStatistics stat = runTests(name, methodsBefore, methodsTest, methodsAfter);
            statistics.add(stat);
            stat.printStat();
        }
        statistics.stream().reduce(new TestStatistics("ALL TESTS"), TestStatistics::accumulator).printStat();
    }

    private TestStatistics runTests(String name, List<Method> methodsBefore, Deque<Method> methodsTest, List<Method> methodsAfter) throws ClassNotFoundException {
        TestStatistics stat = new TestStatistics(name);
        Class<?> clazz = Class.forName(name);
        while (!methodsTest.isEmpty()) {
            Method test = methodsTest.poll();
            stat.incrementTotalTests();

            try {
                var obj = clazz.getConstructor().newInstance();

                methodsBefore.forEach(method -> runMethod(method, obj));
                runMethod(test, obj);
                methodsAfter.forEach(method -> runMethod(method, obj));

                stat.incrementGoodTests();
                LOGGER.info("Class {} Test {} is DONE!", name, test.getName());
            } catch (Exception ex) {
                stat.incrementBadTests();
                LOGGER.error("Class {} Test {} is FAILED!", name, test.getName(), ex.getCause());
            }
        }
        return stat;
    }

    private static void runMethod(Method method, Object obj) {
        try {
            method.setAccessible(true);
            method.invoke(obj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}

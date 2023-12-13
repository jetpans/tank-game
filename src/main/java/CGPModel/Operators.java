package CGPModel;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class Operators {
    public static Map<Integer, BiFunction<Double, Double, Double>> operators = new HashMap<>();

    static {
        operators.put(-1, (a, b) -> a);
        operators.put(0, (a, b) -> a + b);
        operators.put(1, (a, b) -> a - b);
        operators.put(2, (a, b) -> a * b);
        operators.put(3, (a, b) -> (b != 0) ? a / b : 0);
        operators.put(4, (a, b) -> (b != 0) ? a % b : 0);
        operators.put(5, (a, b) -> (b != 0) ? a / b : 0); // Integer division
        operators.put(6, (a, b) -> Math.max(a, b));
        operators.put(7, (a, b) -> Math.min(a, b));
        operators.put(8, (a, b) -> Math.min(Math.pow(a, b), 1e6));
        operators.put(9, (a, b) -> Math.sin(a));
        operators.put(10, (a, b) -> Math.cos(a));
        operators.put(11, (a, b) -> (a >= 0) ? Math.sqrt(a) : -Math.sqrt(-a));
        operators.put(12, (a, b) -> Math.pow(a, 2));
    }
}

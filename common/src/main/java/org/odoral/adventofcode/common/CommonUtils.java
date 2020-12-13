package org.odoral.adventofcode.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CommonUtils {
    
    public static <T> List<T> loadResource(String resource, Function<String, T> mapFunction) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resource.getClass().getResourceAsStream(resource)))) {
            return bufferedReader.lines()
                .map(mapFunction)
                .collect(Collectors.toList());
        }
    }

    public static Character[] toCharacterArray(String chain){
        char[] chars = chain.toCharArray();
        Character[] character = new Character[chars.length];
        for (int i = 0; i < chars.length; i++) {
            character[i] = chars[i];
        }
        return character;
    }

    public static long lcm(List<Integer> rates) {
        return rates.stream()
            .map(Long::new)
            .reduce(1L, CommonUtils::lcm);
    }

    public static long lcm(long a, long b) {
        return (a * b) / gcf(a, b);
    }

    public static long gcf(long a, long b) {
        if (b == 0) {
            return a;
        } else {
            return gcf(b, a % b);
        }
    }
    
}

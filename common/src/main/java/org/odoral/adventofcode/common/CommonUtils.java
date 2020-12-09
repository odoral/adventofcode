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
}

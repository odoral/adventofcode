package org.odoral.adventofcode.y2022.day25;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.y2022.day25.FullOfHotAir;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

import static org.junit.Assert.assertEquals;

@Slf4j
public class FullOfHotAirTest {

    protected FullOfHotAir fullOfHotAir;

    @Before public void setUp() {
        fullOfHotAir = new FullOfHotAir();
    }

    @Test public void test_snafuToDecimal() throws IOException {
        String examples = " SNAFU  Decimal\n" +
            "1=-0-2     1747\n" +
            " 12111      906\n" +
            "  2=0=      198\n" +
            "    21       11\n" +
            "  2=01      201\n" +
            "   111       31\n" +
            " 20012     1257\n" +
            "   112       32\n" +
            " 1=-1=      353\n" +
            "  1-12      107\n" +
            "    12        7\n" +
            "    1=        3\n" +
            "   122       37";
        Stream.of(examples.split("\n"))
            .skip(1)
            .map(line -> line.trim().replaceAll("\\s+", ";"))
            .map(line -> line.split(";"))
            .forEach(fields -> {
                String snafuNumber = fields[0];
                BigInteger decimalNumber = BigInteger.valueOf(Long.parseLong(fields[1]));
                assertEquals(decimalNumber, fullOfHotAir.snafuToDecimal(snafuNumber));

            });
    }

    @Test public void test_decimalToSnafu() throws IOException {
        String examples = "  Decimal          SNAFU\n" +
            "        1              1\n" +
            "        2              2\n" +
            "        3             1=\n" +
            "        4             1-\n" +
            "        5             10\n" +
            "        6             11\n" +
            "        7             12\n" +
            "        8             2=\n" +
            "        9             2-\n" +
            "       10             20\n" +
            "       15            1=0\n" +
            "       20            1-0\n" +
            "     2022         1=11-2\n" +
            "    12345        1-0---0\n" +
            "314159265  1121-1110-1=0";
        Stream.of(examples.split("\n"))
            .skip(1)
            .map(line -> line.trim().replaceAll("\\s+", ";"))
            .map(line -> line.split(";"))
            .forEach(fields -> {
                BigInteger decimalNumber = BigInteger.valueOf(Long.parseLong(fields[0]));
                String snafuNumber = fields[1];
                assertEquals(decimalNumber + " > " + snafuNumber, snafuNumber, fullOfHotAir.decimalToSnafu(decimalNumber));
            });
    }

}
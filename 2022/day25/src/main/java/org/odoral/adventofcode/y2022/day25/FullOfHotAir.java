package org.odoral.adventofcode.y2022.day25;

import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.common.exception.AdventOfCodeException;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.function.Function;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FullOfHotAir {

    public static final BigInteger FIVE = BigInteger.valueOf(5L);

    public static void main(String[] args) throws IOException {
        FullOfHotAir fullOfHotAir = new FullOfHotAir();
        String snafuSum = fullOfHotAir.sumSNAFU(Input.loadInput());
        log.info("Snafu: {}", snafuSum);
    }

    public String sumSNAFU(List<String> hotAirBalloons) {
        return decimalToSnafu(hotAirBalloons.stream()
            .map(this::snafuToDecimal)
            .reduce(BigInteger.ZERO, BigInteger::add));
    }

    protected BigInteger snafuToDecimal(String snafuNumber) {
        BigInteger result = BigInteger.ZERO;
        for (int numberIndex = snafuNumber.length() - 1; numberIndex >= 0; numberIndex--) {
            int pow = snafuNumber.length() - 1 - numberIndex;
            result = result.add(snafuToDecimal(snafuNumber.charAt(numberIndex))
                .multiply(FIVE.pow(pow)));
        }
        log.debug("SNAFU {} > {}", snafuNumber, result);
        return result;
    }

    protected BigInteger snafuToDecimal(char snafuNumber) {
        switch (snafuNumber) {
            case '=':
                return BigInteger.valueOf(-2L);
            case '-':
                return BigInteger.valueOf(-1L);
            case '0':
                return BigInteger.ZERO;
            case '1':
                return BigInteger.ONE;
            case '2':
                return BigInteger.valueOf(2L);
            default:
                throw new AdventOfCodeException("Unsupported snafu number: " + snafuNumber);
        }
    }

    protected String decimalToSnafu(BigInteger decimalNumber) {
        StringBuilder result = new StringBuilder();

        int pow = 1;
        BigInteger addFromPowBelow = BigInteger.ZERO;
        BigInteger[] divideAndRemainder;
        String snafu = "";
        do {
            divideAndRemainder = decimalNumber.divideAndRemainder(FIVE.pow(pow));
            int remainder = divideAndRemainder[1].divide(FIVE.pow(pow - 1)).add(addFromPowBelow).intValue();
            snafu = decimalToSnafu(remainder);
            if(divideAndRemainder[0].equals(BigInteger.ZERO)) {
                result.insert(0, snafu);
            } else {
                switch (snafu.length()) {
                    case 1:
                        result.insert(0, snafu.charAt(0));
                        addFromPowBelow = BigInteger.ZERO;
                        break;
                    case 2:
                        result.insert(0, snafu.charAt(1));
                        addFromPowBelow = BigInteger.ONE;
                        break;
                    default:
                        throw new AdventOfCodeException("Unsupported snafu number " + snafu);
                }
            }
            pow++;
        } while (!(divideAndRemainder)[0].equals(BigInteger.ZERO));

        log.debug("Decimal {} > {}", decimalNumber, result);
        return result.toString();
    }

    protected String decimalToSnafu(int decimalNumber) {
        switch (decimalNumber) {
            case 0:
            case 1:
            case 2:
                return String.valueOf(decimalNumber);
            case 3:
                return "1=";
            case 4:
                return "1-";
            case 5:
                return "10";
            default:
                throw new AdventOfCodeException("Unsupported remainder: " + decimalNumber);
        }
    }

    public static class Input {

        public static List<String> loadInput() throws IOException {
            return CommonUtils.loadResource("/input.txt", Function.identity());
        }
    }
}
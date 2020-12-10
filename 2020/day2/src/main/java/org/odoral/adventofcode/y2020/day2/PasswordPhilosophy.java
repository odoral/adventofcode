package org.odoral.adventofcode.y2020.day2;

import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PasswordPhilosophy {
    
    public static void main(String [] args) throws IOException {
        PasswordPhilosophy passwordPhilosophy = new PasswordPhilosophy();
        List<String> passwords = passwordPhilosophy.loadPasswords("/input.txt");
        int counter = passwordPhilosophy.countValidPasswordsForPolicy(passwords, passwordPhilosophy::isValidForFirstPolicy);
        log.info("Matching passwords for first policy: {}", counter);

        counter = passwordPhilosophy.countValidPasswordsForPolicy(passwords, passwordPhilosophy::isValidForSecondPolicy);
        log.info("Matching passwords for second policy: {}", counter);
    }

    protected List<String> loadPasswords(String resource) throws IOException {
        return CommonUtils.loadResource(resource, Function.identity());
    }

    protected int countValidPasswordsForPolicy(List<String> passwords, Predicate<PasswordInfo> passwordPolicy) {
        final AtomicInteger counter = new AtomicInteger(0);
        passwords.stream()
            .map(PasswordInfo::toPasswordInfo)
            .filter(passwordPolicy)
            .peek(p -> counter.incrementAndGet())
            .forEach(pass -> log.info("Valid password configuration: {}", pass));
        return counter.get();
    }

    public boolean isValidForFirstPolicy(PasswordInfo passwordInfo) {
        int count = 0;
        char[] passwordChars = passwordInfo.password.toCharArray();
        for (char passwordChar : passwordChars) {
            if (passwordChar == passwordInfo.character) {
                count++;
            }
        }
        
        return count >= passwordInfo.rangeFrom && count <= passwordInfo.rangeTo;
    }

    public boolean isValidForSecondPolicy(PasswordInfo passwordInfo) {
        int count = 0;
        char[] passwordChars = passwordInfo.password.toCharArray();
        if(passwordChars[passwordInfo.rangeFrom-1] == passwordInfo.character){
            count++;
        }
        if(passwordChars[passwordInfo.rangeTo-1] == passwordInfo.character){
            count++;
        }
        
        return count == 1;
    }
    
    public static class PasswordInfo{
        final String password;
        final int rangeFrom;
        final int rangeTo;
        final char character;

        public PasswordInfo(String password, int rangeFrom, int rangeTo, char character) {
            this.password = password;
            this.rangeFrom = rangeFrom;
            this.rangeTo = rangeTo;
            this.character = character;
        }
        
        public static PasswordInfo toPasswordInfo(String passwordConfiguration){
            String configuration = passwordConfiguration.split(":")[0];
            String password = passwordConfiguration.split(":")[1].trim();
            int rangeFrom = Integer.parseInt(configuration.split(" ")[0].split("-")[0]);
            int rangeTo = Integer.parseInt(configuration.split(" ")[0].split("-")[1]);
            char character = configuration.split(" ")[1].toCharArray()[0];
            
            return new PasswordInfo(password, rangeFrom, rangeTo, character);
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", PasswordInfo.class.getSimpleName() + "[", "]")
                .add("password='" + password + "'")
                .add("rangeFrom=" + rangeFrom)
                .add("rangeTo=" + rangeTo)
                .add("character=" + character)
                .toString();
        }
    }
}
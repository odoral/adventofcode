package org.odoral.adventofcode.y2020.day2;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import static org.junit.Assert.assertEquals;

@Slf4j
public class PasswordPhilosophyTest {

    protected PasswordPhilosophy passwordPhilosophy;

    @Before public void setUp() {
        passwordPhilosophy = new PasswordPhilosophy();
    }
    
    @Test public void test() throws IOException {
        List<String> passwords = passwordPhilosophy.loadPasswords("/scenario1.txt");
        long counter = passwordPhilosophy.countValidPasswordsForPolicy(passwords, passwordPhilosophy::isValidForFirstPolicy);
        assertEquals(2, counter);

        counter = passwordPhilosophy.countValidPasswordsForPolicy(passwords, passwordPhilosophy::isValidForSecondPolicy);
        assertEquals(1, counter);
    }
}
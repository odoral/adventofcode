package org.odoral.adventofcode.y2020.day22.model.player;

import java.util.Deque;
import java.util.LinkedList;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
public class Player {
    public final String name;
    public final Deque<Integer> startingDeck = new LinkedList<>();
    
    public Player getPlayerForRecursiveCombat(){
        Player recursiveCombatPlayer = new Player(name);
        Integer cardsForRecursiveCombat = startingDeck.getFirst();
        recursiveCombatPlayer.startingDeck.addAll(startingDeck.stream().skip(1).limit(cardsForRecursiveCombat).collect(Collectors.toList()));
        return recursiveCombatPlayer;
    }
}

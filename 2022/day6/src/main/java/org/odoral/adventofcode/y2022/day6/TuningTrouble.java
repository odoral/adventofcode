package org.odoral.adventofcode.y2022.day6;

import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.common.exception.AdventOfCodeException;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TuningTrouble {

    public static final int START_OF_PACKET_MARKER_SIZE = 4;
    public static final int START_OF_MESSAGE_MARKER_SIZE = 14;

    public static void main(String[] args) throws IOException {
        TuningTrouble tuningTrouble = new TuningTrouble();
        List<String> dataStreams = CommonUtils.loadResource("/input.txt", Function.identity());
        int position = tuningTrouble.findPacketMarker(dataStreams.get(0), START_OF_PACKET_MARKER_SIZE);
        log.info("First start-of-packet marker after character: {}", position);

        position = tuningTrouble.findPacketMarker(dataStreams.get(0), START_OF_MESSAGE_MARKER_SIZE);
        log.info("First start-of-message marker after character: {}", position);
    }

    protected int findPacketMarker(String dataStream, int markerSize) {
        LinkedList<Character> queue = new LinkedList<>();

        for (int dataStreamIndex = 0; dataStreamIndex < dataStream.length(); dataStreamIndex++) {
            if (queue.size() == markerSize) {
                queue.removeFirst();
            }
            queue.addLast(dataStream.charAt(dataStreamIndex));
            if (new HashSet<>(queue).size() == markerSize) {
                return dataStreamIndex + 1;
            }
        }
        throw new AdventOfCodeException("Start of packet marker not found.");
    }

}
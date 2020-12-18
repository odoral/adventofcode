package org.odoral.adventofcode.y2020.day18;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OperationOrderTest {

    protected OperationOrder operationOrder;

    @Before public void setUp() {
        operationOrder = new OperationOrder();
    }
    
    @Test public void test_firstPart() throws IOException {
        List<String> operations = operationOrder.loadInput("/scenario1.txt");
        
        assertEquals(4, operations.size());
        
        assertEquals(26, operationOrder.processOperations(Collections.singletonList(operations.get(0)), operationOrder::processOperationPart1));
        assertEquals(437, operationOrder.processOperations(Collections.singletonList(operations.get(1)), operationOrder::processOperationPart1));
        assertEquals(12240, operationOrder.processOperations(Collections.singletonList(operations.get(2)), operationOrder::processOperationPart1));
        assertEquals(13632, operationOrder.processOperations(Collections.singletonList(operations.get(3)), operationOrder::processOperationPart1));
        assertEquals(26335, operationOrder.processOperations(operations, operationOrder::processOperationPart1));
        
        assertEquals(4494, operationOrder.processOperations(Collections.singletonList("24+(50*89)+20"), operationOrder::processOperationPart1));
    }
    
    @Test public void test_secondPart() throws IOException {
        List<String> operations = operationOrder.loadInput("/scenario2.txt");
        
        assertEquals(5, operations.size());
        
        assertEquals(51, operationOrder.processOperations(Collections.singletonList(operations.get(0)), operationOrder::processOperationPart2));
        assertEquals(46, operationOrder.processOperations(Collections.singletonList(operations.get(1)), operationOrder::processOperationPart2));
        assertEquals(1445, operationOrder.processOperations(Collections.singletonList(operations.get(2)), operationOrder::processOperationPart2));
        assertEquals(669060, operationOrder.processOperations(Collections.singletonList(operations.get(3)), operationOrder::processOperationPart2));
        assertEquals(23340, operationOrder.processOperations(Collections.singletonList(operations.get(4)), operationOrder::processOperationPart2));
        assertEquals(693942, operationOrder.processOperations(operations, operationOrder::processOperationPart2));
        
    }
}
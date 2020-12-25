package org.odoral.adventofcode.y2020.day21;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.common.model.KeyValue;
import org.odoral.adventofcode.y2020.day21.exception.AllergenAssessmentException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Slf4j
public class AllergenAssessmentTest {

    protected AllergenAssessment allergenAssessment;

    @Before public void setUp() {
        allergenAssessment = new AllergenAssessment();
    }
    
    @Test public void test_firstPart() throws IOException {
        List<AllergenAssessment.Food> foods = allergenAssessment.loadInput("/scenario1.txt");
        assertEquals(4, foods.size());
        assertEquals(4, foods.get(0).ingredients.size());
        assertEquals(2, foods.get(0).allergens.size());
        assertEquals(4, foods.get(1).ingredients.size());
        assertEquals(1, foods.get(1).allergens.size());
        assertEquals(2, foods.get(2).ingredients.size());
        assertEquals(1, foods.get(2).allergens.size());
        assertEquals(3, foods.get(3).ingredients.size());
        assertEquals(1, foods.get(3).allergens.size());

        Map<String, Integer> ingredients = allergenAssessment.filterIngredientsWhichPossiblyCantContainAllergens(foods);
        log.info("Found these ingredients: {}", ingredients);
        assertEquals(4, ingredients.size());
        assertTrue(ingredients.containsKey("kfcds"));
        assertTrue(ingredients.containsKey("nhms"));
        assertTrue(ingredients.containsKey("sbzzf"));
        assertTrue(ingredients.containsKey("trh"));
        int appearances = allergenAssessment.countAppearances(ingredients);
        assertEquals(5, appearances);
    }
    
    @Test public void test_secondPart() throws IOException {
        List<AllergenAssessment.Food> foods = allergenAssessment.loadInput("/scenario1.txt");
        
        List<KeyValue<String, String>> allergenIngredientPairs = allergenAssessment.findOutWhichIngredientIsWhichAllergen(foods);
        log.info("Found these allergen-ingredients pairs: {}", allergenIngredientPairs);
        assertEquals(3, allergenIngredientPairs.size());
        assertEquals("mxmxvkd", filterAllergen(allergenIngredientPairs, "dairy"));
        assertEquals("sqjhc", filterAllergen(allergenIngredientPairs, "fish"));
        assertEquals("fvjkl", filterAllergen(allergenIngredientPairs, "soy"));
        String canonicalDangerousIngredientList = allergenAssessment.buildCanonicalDangerousIngredientList(allergenIngredientPairs);
        assertEquals("mxmxvkd,sqjhc,fvjkl", canonicalDangerousIngredientList);
    }

    protected String filterAllergen(List<KeyValue<String, String>> allergenIngredientPairs, String allergen) {
        return allergenIngredientPairs.stream()
            .filter(e -> e.getKey().equals(allergen))
            .map(KeyValue::getValue)
            .findAny()
            .orElseThrow(() -> new AllergenAssessmentException("There is no allergen-ingredient pair for: "+allergen));
    }
}
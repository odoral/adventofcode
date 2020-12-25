package org.odoral.adventofcode.y2020.day21;

import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.common.model.KeyValue;
import org.odoral.adventofcode.y2020.day21.exception.AllergenAssessmentException;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AllergenAssessment {
    
    public static void main(String[] args) throws IOException {
        AllergenAssessment allergenAssessment = new AllergenAssessment();
        List<AllergenAssessment.Food> foods = allergenAssessment.loadInput("/input.txt");
        Map<String, Integer> ingredients = allergenAssessment.filterIngredientsWhichPossiblyCantContainAllergens(foods);
        log.info("Found these ingredients: {}", ingredients);
        int appearances = allergenAssessment.countAppearances(ingredients);
        log.info("Found these appearances: {}", appearances);

        List<KeyValue<String, String>> allergenIngredientPairs = allergenAssessment.findOutWhichIngredientIsWhichAllergen(foods);
        log.info("Found these allergen-ingredients pairs: {}", allergenIngredientPairs);
        String canonicalDangerousIngredientList = allergenAssessment.buildCanonicalDangerousIngredientList(allergenIngredientPairs);
        log.info("Canonical dangerous ingredient list: {}", canonicalDangerousIngredientList);
    }

    public List<Food> loadInput(String resource) throws IOException {
        return CommonUtils.loadResource(resource, Food::from);
    }

    public Map<String, Integer> filterIngredientsWhichPossiblyCantContainAllergens(List<Food> foods) {
        Set<String> allergenCandidates = buildIngredientCandidatesPerAllergen(foods)
            .stream()
            .map(KeyValue::getValue)
            .collect(Collectors.toSet());
        
        log.info("Final set of allergen candidates is: {}", allergenCandidates);

        return foods.stream()
            .flatMap(f -> f.ingredients.stream())
            .filter(i -> !allergenCandidates.contains(i))
            .collect(Collectors.toMap(Function.identity(), i -> 1, Integer::sum));
    }

    protected List<KeyValue<String, String>> buildIngredientCandidatesPerAllergen(List<Food> foods) {
        Map<String, Map<String, Integer>> possibleIngredientsPerAllergen = groupIngredientsPerAllergen(foods);

        log.info("There are {} different allergens: {}", possibleIngredientsPerAllergen.size(), possibleIngredientsPerAllergen.keySet());

        return possibleIngredientsPerAllergen.entrySet()
            .stream()
            .flatMap(e -> {
                String allergen = e.getKey();
                List<String> ingredientsCandidatesForAllergen = e.getValue()
                    .entrySet()
                    .stream()
                    .collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.mapping(Map.Entry::getKey, Collectors.toList())))
                    .entrySet()
                    .stream()
                    .max(Comparator.comparingInt(Map.Entry::getKey))
                    .map(Map.Entry::getValue)
                    .orElseThrow(() -> new AllergenAssessmentException("There is no ingredient matching with allergen: " + allergen));
                log.info("There are {} ingredient candidates for allergen {}: {}", ingredientsCandidatesForAllergen.size(), allergen, ingredientsCandidatesForAllergen);
                return ingredientsCandidatesForAllergen.stream().map(i -> KeyValue.<String, String>builder().key(allergen).value(i).build());
            })
            .collect(Collectors.toList());
    }

    protected Map<String, Map<String, Integer>> groupIngredientsPerAllergen(List<Food> foods) {
        Map<String, Map<String, Integer>> possibleIngredientsPerAllergen = new HashMap<>();
        foods.forEach(food -> food.ingredients
            .forEach(ingredient -> food.allergens
                .forEach(allergen -> possibleIngredientsPerAllergen.compute(allergen, (k, v) -> {
                    Map<String, Integer> ingredientsMap;
                    if(Objects.isNull(v)){
                        ingredientsMap = new HashMap<>();
                    }else {
                        ingredientsMap = v;
                    }

                    ingredientsMap.compute(ingredient, (k1, v1) -> {
                        if(Objects.isNull(v1)){
                            return 1;
                        }else{
                            return v1 + 1;
                        }
                    });

                    return ingredientsMap;
                }))));
        return possibleIngredientsPerAllergen;
    }

    public int countAppearances(Map<String, Integer> ingredients) {
        return ingredients.values()
            .stream()
            .reduce(0, Integer::sum);
    }

    public List<KeyValue<String, String>> findOutWhichIngredientIsWhichAllergen(List<Food> foods) {
        Map<String, Set<String>> ingredientCandidatesPerAllergen = buildIngredientCandidatesPerAllergen(foods).stream()
            .collect(Collectors.groupingBy(KeyValue::getKey, Collectors.mapping(KeyValue::getValue, Collectors.toSet())));
        
        Set<String> addressedAllergens = new HashSet<>();
        while(thereAreUnaddressedAllergens(ingredientCandidatesPerAllergen)){
            Map.Entry<String, Set<String>> addressedAllergen = ingredientCandidatesPerAllergen.entrySet()
                .stream()
                .filter(e -> e.getValue().size() == 1)
                .filter(e -> !addressedAllergens.contains(e.getKey()))
                .findFirst()
                .orElseThrow(() -> new AllergenAssessmentException("Is not possible to find out: " + ingredientCandidatesPerAllergen));

            String allergen = addressedAllergen.getKey();
            Set<String> ingredient = addressedAllergen.getValue();
            addressedAllergens.add(allergen);
            ingredientCandidatesPerAllergen.entrySet()
                .stream()
                .filter(e -> !e.getKey().equals(allergen))
                .forEach(e -> e.getValue().removeAll(ingredient));
        }
        
        return ingredientCandidatesPerAllergen.entrySet()
            .stream()
            .map(e -> {
                String allergen = e.getKey();
                Set<String> ingredients = e.getValue();

                if(ingredients.size() > 1){
                    throw new AllergenAssessmentException("Is not possible to find out ingredient for allergen: "+allergen+" ["+ingredients+"]");
                }
                String ingredient = ingredients.iterator().next();
                log.info("Addressed ingredient for {}: {}", allergen, ingredient);

                return KeyValue.<String, String>builder()
                    .key(allergen)
                    .value(ingredient)
                    .build();
            }).collect(Collectors.toList());
    }

    protected boolean thereAreUnaddressedAllergens(Map<String, Set<String>> ingredientCandidatesPerAllergen) {
        return ingredientCandidatesPerAllergen.entrySet().stream().anyMatch(e -> e.getValue().size() > 1);
    }

    public String buildCanonicalDangerousIngredientList(List<KeyValue<String, String>> allergenIngredientPairs) {
        return allergenIngredientPairs.stream()
            .sorted(Comparator.comparing(KeyValue::getKey))
            .map(KeyValue::getValue)
            .collect(Collectors.joining(","));
            
    }

    @RequiredArgsConstructor
    public static class Food{
        final List<String> ingredients;
        final List<String> allergens;
        
        public static Food from(String line){
            String [] fields = line.split("\\(contains ");
            return new Food(
                Arrays.asList(fields[0].trim().split(" ")),
                Arrays.asList(fields[1].replace(")", "").trim().split(", "))
            );
        }
    }
}

package guru.springframework.bootstrap;

import guru.springframework.domain.*;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class RecipeBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final CategoryRepository categoryRepository;
    private final RecipeRepository recipeRepository;

    public RecipeBootstrap(UnitOfMeasureRepository unitOfMeasureRepository, CategoryRepository categoryRepository, RecipeRepository recipeRepository) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.categoryRepository = categoryRepository;
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        recipeRepository.saveAll(getRecipe());
    }

    private List<Recipe> getRecipe(){

        List<Recipe> recipes = new ArrayList<>(2);
        String errString = "Expected UOM not found!!!";

        //get UOM Optionals
        Optional<UnitOfMeasure> eachUomOptional = unitOfMeasureRepository.findByDescription("Each");
        Optional<UnitOfMeasure> tablespoonUomOptional = unitOfMeasureRepository.findByDescription("Tablespoon");
        Optional<UnitOfMeasure> teaspoonUomOptional = unitOfMeasureRepository.findByDescription("Teaspoon");
        Optional<UnitOfMeasure> cupUomOptional = unitOfMeasureRepository.findByDescription("Cup");
        Optional<UnitOfMeasure> dashUomOptional = unitOfMeasureRepository.findByDescription("Dash");
        Optional<UnitOfMeasure> pintUomOptional = unitOfMeasureRepository.findByDescription("Pint");

        // generate a common array for throw exception
        List<Optional<UnitOfMeasure>> unitOfMeasureOptionalList = new ArrayList<>();
        unitOfMeasureOptionalList.add(eachUomOptional);
        unitOfMeasureOptionalList.add(tablespoonUomOptional);
        unitOfMeasureOptionalList.add(teaspoonUomOptional);
        unitOfMeasureOptionalList.add(cupUomOptional);
        unitOfMeasureOptionalList.add(dashUomOptional);
        unitOfMeasureOptionalList.add(pintUomOptional);

        for (Optional<UnitOfMeasure> uofOptList : unitOfMeasureOptionalList) {
            if(!uofOptList.isPresent()){
                throw new RuntimeException(errString);
            }
        }

        // get UOM
        UnitOfMeasure eachUom = eachUomOptional.get();
        UnitOfMeasure tablespoonUom = tablespoonUomOptional.get();
        UnitOfMeasure teaspoonUom = teaspoonUomOptional.get();
        UnitOfMeasure cupUom = cupUomOptional.get();
        UnitOfMeasure dashUom = dashUomOptional.get();
        UnitOfMeasure pintUom = pintUomOptional.get();

        // get Category Optionals
        Optional<Category> americanCategoryOptional = categoryRepository.findByDescription("American");
        Optional<Category> mexicanCategoryOptional = categoryRepository.findByDescription("Mexican");
        if(!americanCategoryOptional.isPresent()){
            throw new RuntimeException("Category Not Found!!!");
        } else if(!mexicanCategoryOptional.isPresent()){
            throw new RuntimeException("Category Not Found!!!");
        }

        // get categories
        Category americanCategory = americanCategoryOptional.get();
        Category mexicanCategory = mexicanCategoryOptional.get();

        // Yummy Guacamole
        Recipe guacamoleRecipe = new Recipe();
        guacamoleRecipe.setDescription("Perfect Guacamole");
        guacamoleRecipe.setPrepTime(10);
        guacamoleRecipe.setCookTime(0);
        guacamoleRecipe.setDifficulty(Difficulty.EASY);
        guacamoleRecipe.setDirections("1 Cut avocado, remove flesh: Cut the avocados in half. Remove seed. " +
                "Score the inside of the avocado with a blunt knife and scoop out the flesh with a spoon. " +
                "(See How to Cut and Peel an Avocado.) Place in a bowl.\n" +
                "\n" +
                "2 Mash with a fork: Using a fork, roughly mash the avocado. (Don't overdo it! The guacamole should be a little chunky.)\n" +
                "\n" +
                "3 Add salt, lime juice, and the rest: Sprinkle with salt and lime (or lemon) juice. " +
                "The acid in the lime juice will provide some balance to the richness of the avocado and will help " +
                "delay the avocados from turning brown.\n" +
                "Add the chopped onion, cilantro, black pepper, and chiles. Chili peppers vary individually in their hotness. " +
                "So, start with a half of one chili pepper and add to the guacamole to your desired degree of hotness.\n" +
                "Remember that much of this is done to taste because of the variability in the fresh ingredients. " +
                "Start with this recipe and adjust to your taste.\n" +
                "\n" +
                "4 Cover with plastic and chill to store: Place plastic wrap on the surface of the guacamole " +
                "cover it and to prevent air reaching it. (The oxygen in the air causes oxidation which will turn the guacamole brown.) " +
                "Refrigerate until ready to serve.\n" +
                "\n" +
                "Chilling tomatoes hurts their flavor, so if you want to add chopped tomato to your guacamole, add it just before serving.");

        guacamoleRecipe.getIngredients().add(new Ingredient("rice avocados", new BigDecimal(2), eachUom, guacamoleRecipe));
        guacamoleRecipe.getIngredients().add(new Ingredient("kosher salt", new BigDecimal(".5"), teaspoonUom, guacamoleRecipe));
        guacamoleRecipe.getIngredients().add(new Ingredient("fresh lime juice or lemon juice",
                new BigDecimal(1), tablespoonUom, guacamoleRecipe));
        guacamoleRecipe.getIngredients().add(new Ingredient("minced red onion or thinly sliced green onion",
                new BigDecimal(2), tablespoonUom, guacamoleRecipe));
        guacamoleRecipe.getIngredients().add(new Ingredient("serrano chiles", new BigDecimal(2), eachUom, guacamoleRecipe));
        guacamoleRecipe.getIngredients().add(new Ingredient("scilantro", new BigDecimal(2), tablespoonUom, guacamoleRecipe));
        guacamoleRecipe.getIngredients().add(new Ingredient("black pepper", new BigDecimal(1), dashUom, guacamoleRecipe));
        guacamoleRecipe.getIngredients().add(new Ingredient("ripe tomato, seeds and pulp removed",
                new BigDecimal(".5"), eachUom, guacamoleRecipe));

        // add categories to recipe
        guacamoleRecipe.getCategories().add(americanCategory);
        guacamoleRecipe.getCategories().add(mexicanCategory);

        // get guacamole notes
        Notes guacamoleNotes = new Notes();
        guacamoleNotes.setNote("All you really need to make guacamole is ripe avocados and salt. " +
                "After that, a little lime or lemon juice—a splash of acidity—will help to balance the richness of the avocado. " +
                "Then if you want, add chopped cilantro, chiles, onion, and/or tomato.\n" +
                "Once you have basic guacamole down, feel free to experiment with variations including strawberries, " +
                "peaches, pineapple, mangoes, even watermelon. You can get creative with homemade guacamole!" +
                "\n" +
                "\n" +
                "Read more https://www.simplyrecipes.com/recipes/perfect_guacamole/");

        guacamoleNotes.setRecipe(guacamoleRecipe);

        // set URL
        guacamoleRecipe.setUrl("https://www.simplyrecipes.com/recipes/perfect_guacamole/");

        // set Notes
        guacamoleRecipe.setNotes(guacamoleNotes);

        // add return list
        recipes.add(guacamoleRecipe);

        // Spicy grilled chicken tacos
        Recipe tacosRecipe = new Recipe();
        tacosRecipe.setDescription("Spicy grilled chicken tacos");
        tacosRecipe.setPrepTime(20);
        tacosRecipe.setCookTime(15);
        tacosRecipe.setDifficulty(Difficulty.NORMAL);
        tacosRecipe.setDirections("1 Prepare a gas or charcoal grill for medium-high, direct heat.\n" +
                "\n" +
                "2 Make the marinade and coat the chicken: In a large bowl, stir together the chili powder, oregano, " +
                "cumin, sugar, salt, garlic and orange zest. Stir in the orange juice and olive oil to make a loose paste. " +
                "Add the chicken to the bowl and toss to coat all over.\n" +
                "Set aside to marinate while the grill heats and you prepare the rest of the toppings.\n" +
                "\n" +
                "3 Grill the chicken: Grill the chicken for 3 to 4 minutes per side, or until a thermometer inserted into the thickest part of the meat registers 165F. Transfer to a plate and rest for 5 minutes.\n" +
                "\n" +
                "4 Warm the tortillas: Place each tortilla on the grill or on a hot, dry skillet over medium-high heat. " +
                "As soon as you see pockets of the air start to puff up in the tortilla, turn it with tongs and heat " +
                "for a few seconds on the other side.\n" +
                "Wrap warmed tortillas in a tea towel to keep them warm until serving.\n" +
                "\n" +
                "5 Assemble the tacos: Slice the chicken into strips. On each tortilla, place a small handful of arugula. Top with chicken slices, sliced avocado, radishes, tomatoes, and onion slices. Drizzle with the thinned sour cream. Serve with lime wedges.");

        tacosRecipe.getIngredients().add(new Ingredient("ancho chili powder", new BigDecimal(2), tablespoonUom, tacosRecipe));
        tacosRecipe.getIngredients().add(new Ingredient("dried oregano", new BigDecimal(1), teaspoonUom, tacosRecipe));
        tacosRecipe.getIngredients().add(new Ingredient("dried cumin", new BigDecimal(1), teaspoonUom, tacosRecipe));
        tacosRecipe.getIngredients().add(new Ingredient("sugar", new BigDecimal(1), teaspoonUom, tacosRecipe));
        tacosRecipe.getIngredients().add(new Ingredient("salt", new BigDecimal(".5"), eachUom, tacosRecipe));
        tacosRecipe.getIngredients().add(new Ingredient("clove garlic", new BigDecimal(1), eachUom, tacosRecipe));
        tacosRecipe.getIngredients().add(new Ingredient("orange zest", new BigDecimal(1), tablespoonUom, tacosRecipe));
        tacosRecipe.getIngredients().add(new Ingredient("fresh-squeezed orange juice", new BigDecimal(3), tablespoonUom, tacosRecipe));
        tacosRecipe.getIngredients().add(new Ingredient("olive oil", new BigDecimal(2), tablespoonUom, tacosRecipe));
        tacosRecipe.getIngredients().add(new Ingredient("boneless chicken thighs", new BigDecimal(5), eachUom, tacosRecipe));

        // add categories to recipe
        tacosRecipe.getCategories().add(americanCategory);
        tacosRecipe.getCategories().add(mexicanCategory);

        // get tacos notes
        Notes tacosNotes = new Notes();
        tacosNotes.setNote("We have a family motto and it is this: Everything goes better in a tortilla.\n" +
                "Any and every kind of leftover can go inside a warm tortilla, usually with a healthy dose of pickled jalapenos. " +
                "I can always sniff out a late-night snacker when the aroma of tortillas heating in a hot pan on the stove " +
                "comes wafting through the house.\n" +
                "Today’s tacos are more purposeful – a deliberate meal instead of a secretive midnight snack!\n" +
                "First, I marinate the chicken briefly in a spicy paste of ancho chile powder, oregano, cumin, and " +
                "sweet orange juice while the grill is heating. You can also use this time to prepare the taco toppings.\n" +
                "Grill the chicken, then let it rest while you warm the tortillas. Now you are ready to assemble the tacos and dig in." +
                " The whole meal comes together in about 30 minutes!" +
                "\n" +
                "\n" +
                "Read more https://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/");
        tacosNotes.setRecipe(tacosRecipe);

        //set URL
        tacosRecipe.setUrl("https://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/");

        // set notes
        tacosRecipe.setNotes(tacosNotes);

        // add return list
        recipes.add(tacosRecipe);

        return recipes;
    }

}

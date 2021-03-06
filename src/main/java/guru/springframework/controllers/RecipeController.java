package guru.springframework.controllers;

import guru.springframework.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping({"/recipes", "/recipes/index", "/recipes/index.html"})
    public String getRecipePage(Model model){
        log.debug("I'm in the Recipe controller");
        model.addAttribute("recipes", recipeService.getRecipes());
        return "recipes/index";
    }
}

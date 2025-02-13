package guru.springframework.controllers;

import guru.springframework.dtos.RecipeDto;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/recipe")
public class RecipeController {
    public static final String RECIPE_RECIPEFORM = "recipe/recipeform";
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }


    @GetMapping("/{id}/show")
    public String showById(@PathVariable Long id, Model model){
        log.debug("Showing details of recipe with id " + id);
        model.addAttribute("recipe", recipeService.findById(id));
        return "recipe/show";
    }


    @GetMapping("/new")
    public String newRecipe(Model model){
        log.debug("Returning form to create new recipe");
        model.addAttribute("recipe", new RecipeDto());
        return RECIPE_RECIPEFORM;
    }


    @GetMapping("/{id}/update")
    public String updateRecipe(@PathVariable String id, Model model){
        log.debug("Retrieving from db recipe to update in form");
        model.addAttribute("recipe", recipeService.getRecipeDtoById(Long.valueOf(id)));
        return RECIPE_RECIPEFORM;
    }


    @PostMapping("")
    public String newOrUpdate(@Valid @ModelAttribute("recipe") RecipeDto recipeDto, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            bindingResult.getAllErrors()
                    .forEach( objectError ->
                            {
                                log.debug(objectError.toString());
                            }
                    );
            return RECIPE_RECIPEFORM;
        }

        log.debug("Creating new or updating existing recipe");
        RecipeDto savedRecipeDto = recipeService.saveRecipeDto(recipeDto);
        return "redirect:/recipe/" + savedRecipeDto.getId() + "/show";
    }

    @GetMapping("/{id}/delete")
    public String deleteById(@PathVariable String id){
        log.debug("Deleting id " + id);
        recipeService.deleteById(Long.valueOf(id));
        return "redirect:/";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFoundStatus(Exception exception){
        log.error("Handling not found exception");
        log.error(exception.getMessage());

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("404error");
        modelAndView.addObject("exception", exception);

        return modelAndView;
    }
}



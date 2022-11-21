package guru.springframework.controllers;

import guru.springframework.domain.Recipe;
import guru.springframework.dtos.RecipeDto;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class RecipeControllerTest {

    @Mock
    RecipeService recipeService;

    RecipeController controller;

    MockMvc mockMvc;

    final Long id = 1L;
    RecipeDto recipeDto;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new RecipeController(recipeService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();
        recipeDto = new RecipeDto();
        recipeDto.setId(id);
    }

    @Test
    public void getRecipe() throws Exception {
        Recipe mockRecipe = Recipe.builder()
                .id(id)
                .build();

        when(recipeService.findById(anyLong())).thenReturn(mockRecipe);

        mockMvc.perform(get("/recipe/" + id + "/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/show"));
    }

    @Test
    public void getRecipeNotFound() throws Exception {
        when(recipeService.findById(anyLong())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/recipe/" + id + "/show"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404error"));
    }

    @Test
    public void getRecipeNumberFormatException() throws Exception {
        String stringId = "String_id";

        mockMvc.perform(get("/recipe/" + stringId + "/show"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));
    }

    @Test
    public void getNewRecipeForm() throws Exception {
        //when
        mockMvc.perform(get("/recipe/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void postNewRecipeForm() throws Exception {
        //given
        when(recipeService.saveRecipeDto(any())).thenReturn(recipeDto);

        //when
        mockMvc.perform(post("/recipe")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", "")
                        .param("description", "My description")
                        .param("directions", "My directions"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/" + id + "/show"));
        //then
        verify(recipeService, times(1)).saveRecipeDto(any());

    }

    /**
     * Missing description, directions for recipe
     * **/
    @Test
    public void postNewRecipeFormValidationFail() throws Exception {
        //given
        when(recipeService.saveRecipeDto(any())).thenReturn(recipeDto);

        //when
        mockMvc.perform(post("/recipe")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", ""))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(view().name("recipe/recipeform"));
    }

    @Test
    public void getUpdateForm() throws Exception {
        //given
        when(recipeService.getRecipeDtoById(anyLong())).thenReturn(recipeDto);

        //when
        mockMvc.perform(get("/recipe/" + id + "/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));

        verify(recipeService, times(1)).getRecipeDtoById(anyLong());
    }


    @Test
    public void deleteRecipeById() throws Exception {
        mockMvc.perform(get("/recipe/" + id + "/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(recipeService, times(1)).deleteById(anyLong());
    }
}
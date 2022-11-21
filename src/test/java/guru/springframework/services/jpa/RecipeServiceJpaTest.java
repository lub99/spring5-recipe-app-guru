package guru.springframework.services.jpa;

import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.dtos.RecipeDto;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.mappers.RecipeMapper;
import guru.springframework.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RecipeServiceJpaTest {
    RecipeServiceJpa service;

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    RecipeMapper recipeMapper;

    final Long recipeId = 1L;
    final Long ingredietnId = 1L;
    Recipe mockRecipe;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        service = new RecipeServiceJpa(recipeRepository, recipeMapper);

//        Ingredient ingredient = new Ingredient();
//        ingredient.setId(ingredietnId);

        mockRecipe = Recipe.builder()
                .id(recipeId)
                .build();


//        mockRecipe.addIngredient(ingredient);
    }

    @Test
    public void getRecipes() {
        Set<Recipe> recipesData = new HashSet<>();
        recipesData.add(mockRecipe);

        when(recipeRepository.findAll()).thenReturn(recipesData);
        Set<Recipe> recipes = service.findAll();

        assertEquals(1, recipes.size());
        verify(recipeRepository, times(1)).findAll();
    }

    @Test
    public void getRecipeByIdTest() {
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(mockRecipe));

        Recipe foundRecipe = service.findById(recipeId);

        assertNotNull("Null recipe returned", foundRecipe);
        assertEquals(recipeId, foundRecipe.getId());
        verify(recipeRepository, never()).findAll();
        verify(recipeRepository, times(1)).findById(anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void getRecipeByIdTestNotFound() {
        Optional<Recipe> recipeOptionalEmpty = Optional.empty();
        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptionalEmpty);

        Recipe foundRecipe = service.findById(recipeId);

        //exception throw expected here
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteById() {
        service.deleteById(recipeId);

        verify(recipeRepository, times(1)).deleteById(anyLong());
        // this will throw error
        service.findById(recipeId);
    }

    @Test
    public void getRecipeDtoById() {
        //given
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(mockRecipe));

        RecipeDto mockRecipeDto  = new RecipeDto();
        mockRecipeDto.setId(recipeId);
        when(recipeMapper.recipeToRecipeDto(any())).thenReturn(mockRecipeDto);

        //when
        RecipeDto returnedRecipeDto = service.getRecipeDtoById(recipeId);

        //then
        assertNotNull(returnedRecipeDto);
        verify(recipeRepository, times(1)).findById(anyLong());
        verify(recipeRepository, never()).findAll();
    }
}
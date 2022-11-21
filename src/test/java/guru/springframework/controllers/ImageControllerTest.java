package guru.springframework.controllers;

import guru.springframework.dtos.RecipeDto;
import guru.springframework.services.ImageService;
import guru.springframework.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ImageControllerTest {

    @Mock
    ImageService imageService;

    @Mock
    RecipeService recipeService;

    ImageController controller;

    MockMvc mockMvc;

    final Long recipeId = 1L;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        controller = new ImageController(imageService, recipeService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();
    }

    @Test
    public void getImageUploadForm() throws Exception {
        //given
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(recipeId);

        when(recipeService.getRecipeDtoById(anyLong())).thenReturn(recipeDto);

        //when
        mockMvc.perform(get("/recipe/" + recipeId + "/image"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(view().name("recipe/imageuploadform"));

        //then
        verify(recipeService, times(1)).getRecipeDtoById(anyLong());
    }

    @Test
    public void storeRecipeImage() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("imagefile",
                "testing.txt",
                "text/plain", "I am learning".getBytes());

        mockMvc.perform(multipart("/recipe/" + recipeId + "/image").file(mockMultipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/recipe/" + recipeId + "/show"));

        verify(imageService, times(1)).saveImageFile(anyLong(), any());
    }

    @Test
    public void loadImage() throws Exception {
        //given
        RecipeDto mockRecipeDto = new RecipeDto();
        mockRecipeDto.setId(recipeId);

        String fakeImage = "Fake image";
        int fakeImageBytesLength = fakeImage.getBytes().length;
        Byte[] fakeImageBytes = new Byte[fakeImageBytesLength];

        int i = 0;
        for (byte b : fakeImage.getBytes()) {
            fakeImageBytes[i++] = b;
        }
        mockRecipeDto.setImage(fakeImageBytes);

        when(recipeService.getRecipeDtoById(anyLong())).thenReturn(mockRecipeDto);

        //when
        MockHttpServletResponse response = mockMvc.perform(get("/recipe/" + recipeId + "/recipeimage"))
                .andExpect(status().isOk())
                .andReturn().getResponse();


        //then
        byte[] responseBytes = response.getContentAsByteArray();
        assertEquals(fakeImage.getBytes().length, responseBytes.length);

    }

    @Test
    public void testGetImageNumberFormatException() throws Exception {

        mockMvc.perform(get("/recipe/dafjhkdfajs/image"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));
    }
}
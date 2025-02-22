package com.rickymorty.models;

import com.rickymorty.api.BaseApi;
import com.rickymorty.utils.ApiValidator;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CharacterApi {

    private final BaseApi baseApi;
    private final ApiValidator apiValidator;

    public CharacterApi() {
        this.baseApi = new BaseApi();
        this.apiValidator = new ApiValidator();
    }

    // Lista de campos obligatorios que se esperan en cualquier respuesta de personaje.
    private final List<String> expectedFields = Arrays.asList(
            "id", "name", "status", "species", "type", "gender",
            "origin", "location", "image", "episode", "url", "created"
    );

    public void getFirstCharacterAndVerifyStatus(){
        Response response = baseApi.getElementByid("/character/", 1);
        apiValidator.verifyStatusCode(response, 200);
        response = baseApi.getElementByid("/character/", 9999);
        apiValidator.verifyStatusCode(response, 404);
    }

    public void getFirstCharacterAndVerifyBody(){
        Response response = baseApi.getElementByid("/character/", 1);
        apiValidator.verifyFields(expectedFields, response.jsonPath().get());
    }

    public void getFirstHeaderAndVerify(){
        Response response = baseApi.getElementByid("/character/", 1);
        apiValidator.verifyHeader(response);
    }

    public void invalidTest(){
        Response response = baseApi.getElementByid("/character/", 99999);
        apiValidator.verifyStatusCode(response, 200);
    }

    /**
     * Verifica que la respuesta para el personaje con ID 183 (Johnny Depp) contenga los siguientes campos y valores:
     *   "id": 183,
     *   "name": "Johnny Depp",
     *   "status": "Alive",
     *   "species": "Human",
     *   "type": "",
     *   "gender": "Male",
     *   "url": "https://rickandmortyapi.com/api/character/183"
     */
    public void verifyCharacter183Content(){
        Response response = baseApi.getElementByid("/character/", 183);
        apiValidator.verifyStatusCode(response, 200);
        Map<String, Object> character = response.jsonPath().getMap("");

        Map<String, Object> expectedContent = new HashMap<>();
        expectedContent.put("id", 183);
        expectedContent.put("name", "Johnny Depp");
        expectedContent.put("status", "Alive");
        expectedContent.put("species", "Human");
        expectedContent.put("type", "");
        expectedContent.put("gender", "Male");
        expectedContent.put("url", "https://rickandmortyapi.com/api/character/183");

        apiValidator.verifyJsonContent(character, expectedContent);
    }
    /**
     * Verifica intencionalmente que el location 1 tenga el nombre "Mars".
     * Dado que la respuesta real es "Earth (C-137)", este test debe fallar.
     */
    public void verifyCharacterNotTheRock(){
        Response response = baseApi.getElementByid("/character/", 183);
        apiValidator.verifyStatusCode(response, 200);
        Map<String, Object> location = response.jsonPath().getMap("");

        // Intencionalmente falla si el nombre es distinto de "The Rock" (la respuesta real es "Johnny Deep")
        Assert.assertEquals(location.get("name"), "The Rock", "El nombre no es 'The Rock' como se esperaba (test intencionalmente fallido)");
    }
}

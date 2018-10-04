package com.ahmed_beheiri.kitcheninphone;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


public class Utils {
    public static ArrayList<Recipes> getRecipesFromJSON(Context context, String recipesjsonstr) throws JSONException {
        final String RECIPES = "recipes";
        final String PUBLISHER = "publisher";
        final String RATE = "social_rank";
        final String TITLE = "title";
        final String ID = "recipe_id";
        final String IMAGE = "image_url";
        final String SOURCE = "source_url";

        ArrayList<Recipes> recipeslist = new ArrayList<>();

        JSONObject recipeobject = new JSONObject(recipesjsonstr);
        JSONArray recipearray = recipeobject.getJSONArray(RECIPES);

        for (int i = 0; i < recipearray.length(); i++) {
            recipeslist.add(i, new Recipes(recipearray.getJSONObject(i).getString(ID), recipearray.getJSONObject(i).getString(TITLE),
                    recipearray.getJSONObject(i).getString(PUBLISHER), recipearray.getJSONObject(i).getString(IMAGE),
                    recipearray.getJSONObject(i).getString(SOURCE), recipearray.getJSONObject(i).getDouble(RATE)));


        }
        return recipeslist;

    }


    public static Recipes getIngredients(Context context, String recipejsonstr) throws JSONException {
        final String RACIPE = "recipe";
        final String INGREDIENTS = "ingredients";
        final String SOURCEURL = "source_url";
        final String ID = "recipe_id";


        Recipes recipesdetails;

        JSONObject recipeobject = new JSONObject(recipejsonstr);
        JSONObject recibeinfo = recipeobject.getJSONObject(RACIPE);
        String sourceurl = recibeinfo.getString(SOURCEURL);
        String id = recibeinfo.getString(ID);
        JSONArray ingredientsarray = recibeinfo.getJSONArray(INGREDIENTS);
        String[] ingredients = new String[ingredientsarray.length()];
        for (int i = 0; i < ingredientsarray.length(); i++) {

            ingredients[i] = ingredientsarray.getString(i);

        }
        String ingre = convertArrayToString(ingredients);
        recipesdetails = new Recipes(ID, sourceurl, ingre);

        return recipesdetails;

    }


    public static String strSeparator = ",";

    public static String convertArrayToString(String[] array) {
        String str = "";
        for (int i = 0; i < array.length; i++) {
            str = str + array[i];
            if (i < array.length - 1) {
                str = str + strSeparator;
            }
        }
        return str;
    }

    public static ArrayList<String> convertStringToArray(String str) {
        String[] arr = str.split(strSeparator);
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(arr));
        return arrayList;
    }
}

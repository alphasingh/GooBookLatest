package app.ssandroid.com.goobook;


import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Copyright (c) 2019 <ClientName>. All rights reserved.
 * Created by S.I.N.G.H on 30/6/19.
 * Book object to hold JSON format
 */


class Book{

    private SharedPreferences sharedPreferences;
    private String id, title, subtitle, author, imageUrl, pdf;
    private boolean selected;

    Book(JSONObject bookObject, SharedPreferences sharedPreferences) throws JSONException {
        /*  getting the json object of the particular index inside the array
            JSONObject bookObject = itemsArray.getJSONObject(i);
            contains all the information about the bookObject*/

        this.sharedPreferences = sharedPreferences;

        JSONObject volumeInfo = bookObject.getJSONObject("volumeInfo");

        if(bookObject.has("id")) this.id = bookObject.getString("id");
        else this.id = "0";

        if(volumeInfo.has("title")) this.title = bookObject.getJSONObject("volumeInfo").getString("title");
        else this.title = "No title available";

        if (volumeInfo.has("subtitle")) this.subtitle = bookObject.getJSONObject("volumeInfo").getString("subtitle");
        else this.subtitle = "No subtitles available";

        if (volumeInfo.has("authors")) this.author = bookObject.getJSONObject("volumeInfo").getJSONArray("authors").getString(0);
        else this.author = "No author found";

        if (volumeInfo.has("imageLinks")) this.imageUrl = bookObject.getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("thumbnail");
        else this.imageUrl = "0";

        if (bookObject.getJSONObject("accessInfo").getJSONObject("pdf").has("downloadLink")) this.pdf = bookObject.getJSONObject("accessInfo").getJSONObject("pdf").getString("downloadLink");
        else this.pdf = "0";

        this.selected = sharedPreferences.getBoolean(this.getId(),false);
    }

    String getId() {
        return id;
    }

    String getTitle() {
        return title;
    }

    String getSubtitle() {
        return subtitle;
    }

    String getAuthor() { return author; }

    String getImageUrl() {
        return imageUrl;
    }

    String getPdf() {
        return pdf;
    }

    boolean getSelected() { return selected; }

    void setSelected(boolean select){
        this.selected = select;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(this.getId(),select);
        // rather than calling commit (that returns boolean if successfully committed)
        // apply() is asynchronous and fast (does not return or ensure successful I/O)
        editor.apply();
    }
}


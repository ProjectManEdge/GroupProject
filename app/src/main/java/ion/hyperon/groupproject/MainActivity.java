package ion.hyperon.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

// Little Bow Peep ran from the hordes.
// Except the hordes chased her down and captured her.
// If only she had planted peas for Plants vs. Zombies,
// because that guy with a pot for a hat had been completely right.
// Eat chocolate. It's good for you.
// An original comment.
// This comment is not necessary
// Disco Dancin'!

public class MainActivity extends AppCompatActivity {

    static final String preferenceKey = "GraphicCardCatalog";
    SharedPreferences preferences;
    HashMap<String, Object> filters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences(preferenceKey, Context.MODE_PRIVATE);

        // load in filter information
//       loadFilter();

    }
/*
    public void loadFilter() {

        Gson gson = new Gson();
        String json = preferences.getString("filter","");
        java.lang.reflect.Type type = new TypeToken<HashMap<String, Object>>(){}.getType();
        filters = gson.fromJson(json, type);
    }

    public void SaveFilter() {

        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(filters);
        editor.putString("filter",json);
        editor.apply();     // This line is IMPORTANT !!!
    }*/
}

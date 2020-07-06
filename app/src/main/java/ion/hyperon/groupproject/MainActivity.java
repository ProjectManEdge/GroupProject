package ion.hyperon.groupproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

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

    private View mainView;
    private View addView;

    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdaptor;
    private RecyclerView.LayoutManager mLayoutManage;

    ArrayList<GraphicCard> catalog;
    ArrayList<GraphicCard> filteredLog;
    HashMap<String, Object> filters;

    Button buttonAddCard;
    Button buttonRemoveCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences(preferenceKey, Context.MODE_PRIVATE);

        setupData();
        setupDisplay();

        addView =findViewById(R.id.addScrollView);

        //addView.setVisibility(View.GONE);

        /*GraphicCard fancy = new GraphicCard();
        fancy.name = "Elite Card";
        fancy.price = 1000000; // ! million dollars!
        catalog.add(fancy);*/

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_catalog_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.filter:
                //newGame();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private ArrayList<GraphicCard> filterCatalog() {

        ArrayList<GraphicCard> log = new ArrayList<GraphicCard>();

        // loop through card catalog and and add cards to log that match filters.
        Iterator<GraphicCard> it = catalog.iterator();
        while (it.hasNext()) {
            boolean withinFilter = true;

            // HAS THIS filters

            if (filters.containsKey("Name")) {
                if (it.next().name.startsWith((String) Objects.requireNonNull(filters.get("Name"))))
                    withinFilter = false;
            }

            if (withinFilter && filters.containsKey("")) {

            }

            // IS WITHIN filters

            // Successfully filtered, add card to list and sort if needed
            if (withinFilter)
                log.add(it.next());
        }

        return log;
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (!filters.isEmpty())
            saveFilter();

        if (!catalog.isEmpty())
            saveCatalog();
    }

    public boolean loadFilter() {

        Gson gson = new Gson();
        String json = preferences.getString("filter","");

        // No filters were used, so there is nothing to load.
        if (json.isEmpty())
            return false;

        java.lang.reflect.Type type = new TypeToken<HashMap<String, Object>>(){}.getType();
        filters = gson.fromJson(json, type);

        return true;
    }

    public void saveFilter() {

        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(filters);
        editor.putString("filter",json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public void BarChart(View view){
        Intent intent = new Intent(this,DisplayBarChartsActivity.class);
        startActivity(intent);
    }

    public void saveCatalog() {

        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(catalog);
        editor.putString("catalog",json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public boolean loadCatalog() {

        Gson gson = new Gson();
        String json = preferences.getString("catalog","");

        // No filters were used, so there is nothing to load.
        if (json.isEmpty())
            return false;

        java.lang.reflect.Type type = new TypeToken<ArrayList<GraphicCard>>(){}.getType();
        catalog = gson.fromJson(json, type);

        return true;
    }

    // the point of no return
    public void clearCatalog() {
        catalog.clear();
    }

    public void setupData() {
        // load in catalog, create new if it doesn't exist
        boolean catalogExists = loadCatalog();
        if (catalogExists == false)
            catalog = new ArrayList<GraphicCard>();

        // load in filter information, create new if it doesn't exist
        boolean filterUsed = loadFilter();
        if (filterUsed == false)
            filters = new HashMap<String, Object>();

        filteredLog = filterCatalog();
    }

    public void setupDisplay() {
        //load up recyclerView
        mRecycleView = findViewById(R.id.recyclerView);
        mRecycleView.setHasFixedSize(true);

        mLayoutManage = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(mLayoutManage);

        mAdaptor = new GraphicCardAdaptor(filteredLog);
        mRecycleView.setAdapter(mAdaptor);
    }
}

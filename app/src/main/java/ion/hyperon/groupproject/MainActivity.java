package ion.hyperon.groupproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Comparator;
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
    private View graphView;

    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdaptor;
    private RecyclerView.LayoutManager mLayoutManage;

    ArrayList<GraphicCard> catalog;
    ArrayList<GraphicCard> filteredLog;
    HashMap<String, Object> filters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences(preferenceKey, Context.MODE_PRIVATE);

        // setup materials
        setupData();
        setupCatalogDisplay();
        setupBarChartDisplay();
        //setupOtherDisplays();

        // make references to major view objects... possibly move to display setup functions.
        mainView = findViewById(R.id.mainView);
        graphView = findViewById(R.id.barchart);

        // hide un-needed views
        graphView.setVisibility(View.GONE);

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

    // uses filter functions to adjust the filtered log and the data displayed.
    private void filterCatalog() {

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
                filteredLog.add(it.next());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // save data

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

    /*
    // the point of no return
    public void clearCatalog() {
        catalog.clear();
    }
     */

    public void setupData() {
        // load in catalog, create new if it doesn't exist
        boolean catalogExists = loadCatalog();
        if (!catalogExists)
            catalog = new ArrayList<>();

        // load in filter information, create new if it doesn't exist
        boolean filterUsed = loadFilter();
        if (!filterUsed)
            filters = new HashMap<>();

        filteredLog = new ArrayList<>();
        filterCatalog();
    }

    // setup for the card list of graphic cards
    public void setupCatalogDisplay() {
        //load up recyclerView
        mRecycleView = findViewById(R.id.recyclerView);
        mRecycleView.setHasFixedSize(true);

        mLayoutManage = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(mLayoutManage);

        // setup adapter tool for use elsewhere
        mAdaptor = new GraphicCardAdaptor(filteredLog);
        mRecycleView.setAdapter(mAdaptor);
    }

    // creates and adds views from other XML files.
    // NOTE: DO NOT USE onClick in these XML files! set up button actions in Main using setOnClickListener()
    public void setupBarChartDisplay() {
        ConstraintLayout mainLayout = (ConstraintLayout) findViewById(R.id.mainLayout);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

        View barChartView = inflater.inflate(R.layout.activity_display_bar_charts, null);

        mainLayout.addView(barChartView);
    }

    public void displayBarChart() {
        //setContentView(R.layout.activity_display_bar_charts);

        graphView.setVisibility(View.VISIBLE);
        mainView.setVisibility(View.GONE);

        BarChart barChart = (BarChart) findViewById(R.id.barchart);

        ArrayList<BarEntry> entries = new ArrayList<>();
        //for (int i = 0; i < entries<BarEntry>.length; i++){
        entries.add(new BarEntry(8f, 0));
        entries.add(new BarEntry(2f, 1));
        entries.add(new BarEntry(5f, 2));
        entries.add(new BarEntry(20f, 3));
        entries.add(new BarEntry(15f, 4));
        entries.add(new BarEntry(19f, 5));
        //}

        BarDataSet bardataset = new BarDataSet(entries, "Cells");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("2016");
        labels.add("2015");
        labels.add("2014");
        labels.add("2013");
        labels.add("2012");
        labels.add("2011");

        BarData data = new BarData(labels, bardataset);
        barChart.setData(data); // set the data and list of labels into chart
        barChart.setDescription("Set Bar Chart Description Here");  // set the description
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart.animateY(5000);
    }

    // opens up a view for adding a new graphic card to the catalog.
    public void addCard(View view) {

        // create addView
        final ConstraintLayout mainLayout = findViewById(R.id.mainLayout);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        inflater.inflate(R.layout.card_editor, mainLayout);
        addView = findViewById(R.id.cardEditor);

        // Hide Main View until the end of adding a card.
        mainView.setVisibility(View.GONE);

        Button addButton = findViewById(R.id.button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create new card from data
                GraphicCard newCard = new GraphicCard();
                fillCard(newCard);

                // Add card  to catalog and re-sort it
                catalog.add(newCard);
                catalog.sort(new Comparator<GraphicCard>() {
                    @Override
                    public int compare(GraphicCard o1, GraphicCard o2) {
                        return o1.name.compareTo(o2.name);
                    }
                });

                // Update Filtered data
                filteredLog.clear();
                filterCatalog();
                mAdaptor.notifyDataSetChanged();

                // return to Main View
                mainView.setVisibility(View.VISIBLE);

                // end Add View
                ((ViewGroup) addView.getParent()).removeView(addView);
                hideKeyboard(MainActivity.this);

            }
        });

        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // return to Main View
                mainView.setVisibility(View.VISIBLE);

                // end Add View
                ((ViewGroup) addView.getParent()).removeView(addView);
                hideKeyboard(MainActivity.this);

            }
        });
    }

    private void fillCard(GraphicCard newCard) {

        String temp;

        temp = ((EditText) findViewById(R.id.newCardName)).getText().toString();
        if (!temp.isEmpty()) newCard.name = temp;

        temp = ((EditText) findViewById(R.id.newManufacturor)).getText().toString();
        if (!temp.isEmpty()) newCard.manufacturer = temp;

        temp = ((EditText) findViewById(R.id.newPrice)).getText().toString();
        if (!temp.isEmpty()) newCard.price = Float.parseFloat(temp);

        temp = ((EditText) findViewById(R.id.newRam)).getText().toString();
        if (!temp.isEmpty()) newCard.ram = Float.parseFloat(temp);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

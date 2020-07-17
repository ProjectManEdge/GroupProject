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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

    private RecyclerView mRecycleView;
    private GraphicCardAdaptor mAdaptor;
    private RecyclerView.LayoutManager mLayoutManage;

    ArrayList<GraphicCard> catalog;
    ArrayList<WeakReference<GraphicCard>> filteredLog;
    HashMap<String, Object> filters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences(preferenceKey, Context.MODE_PRIVATE);

        // setup materials
        setupData();
        setupCatalogDisplay();

        // make references to major view objects... possibly move to display setup functions.
        mainView = findViewById(R.id.mainView);
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
                filteredLog.add(new WeakReference<GraphicCard>(it.next()));
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

        // setup onClickListener for each item
        mAdaptor.setOnItemClickListener(new GraphicCardAdaptor.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                editCard((filteredLog.get(position)).get());
            }

            @Override
            public void onDeleteClick(int postion) {
                deleteCard(postion);
            }
        });
    }

    // creates and adds views from other XML files.
    // NOTE: DO NOT USE onClick in these XML files! set up button actions in Main using setOnClickListener()
    public void setupBarChartDisplay() {
        ConstraintLayout mainLayout = (ConstraintLayout) findViewById(R.id.mainLayout);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

        View barChartView = inflater.inflate(R.layout.activity_display_bar_charts, null);

        mainLayout.addView(barChartView);
    }

    public void displayBarChart(View view) {
        //setContentView(R.layout.activity_display_bar_charts);
        //create the layout for displaying the barcharts
        final ConstraintLayout mainLayout = findViewById(R.id.mainLayout);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        inflater.inflate(R.layout.activity_display_bar_charts, mainLayout);

        View graphView = findViewById(R.id.barChartLayout);
        mainView.setVisibility(View.GONE);

        graphicCardPrice(graphView);
    }

    public void graphicCardHeaven(View view){
        BarChart barChart = (BarChart) findViewById(R.id.barchart);

        List<String> GraphicCard1 = new ArrayList<>();

        //fill the barchart information
        ArrayList<BarEntry> entries = new ArrayList<>();
        //for (int i = 0; i < entries<BarEntry>.length; i++){
        entries.add(new BarEntry(2f, 0));
        entries.add(new BarEntry(4f, 1));
        entries.add(new BarEntry(6f, 2));
        entries.add(new BarEntry(8f, 3));
        entries.add(new BarEntry(10f, 4));
        entries.add(new BarEntry(12f, 5));
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
        barChart.setDescription("Graphics Cards Heaven");  // set the description
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart.animateY(5000);
    }

    public void graphicCardPrice(View view){
        BarChart barChart = (BarChart) findViewById(R.id.barchart);

        List<String> GraphicCard1 = new ArrayList<>();

        //fill the barchart information
        ArrayList<BarEntry> entries = new ArrayList<>();
        //for (int i = 0; i < entries<BarEntry>.length; i++){
        entries.add(new BarEntry(8f, 0));
        entries.add(new BarEntry(2f, 1));
        entries.add(new BarEntry(5f, 2));
        entries.add(new BarEntry(12f, 3));
        entries.add(new BarEntry(15f, 4));
        entries.add(new BarEntry(11f, 5));
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
        barChart.setDescription("Graphics Cards Price");  // set the description
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart.animateY(5000);
    }

    public void graphicCardRam(View view){
        BarChart barChart = (BarChart) findViewById(R.id.barchart);

        //fill the barchart information
        ArrayList<BarEntry> entries = new ArrayList<>();
        //for (int i = 0; i < entries<BarEntry>.length; i++){
        entries.add(new BarEntry(12f, 0));
        entries.add(new BarEntry(4f, 1));
        entries.add(new BarEntry(4f, 2));
        entries.add(new BarEntry(7f, 3));
        entries.add(new BarEntry(12f, 4));
        entries.add(new BarEntry(10f, 5));
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
        barChart.setDescription("Graphics Cards RAM");  // set the description
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart.animateY(5000);
    }

    // opens up a view for adding a new graphic card to the catalog.
    public void addCard(View view) {

        View addView;

        // create addView
        final ConstraintLayout mainLayout = findViewById(R.id.mainLayout);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        inflater.inflate(R.layout.card_editor, mainLayout);

        // Hide Main View until the end of adding a card.
        mainView.setVisibility(View.GONE);

        // build the new card from input and return to main view
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

                // end Add View
                View view = findViewById(R.id.cardEditor);
                ((ViewGroup) view.getParent()).removeView(view);
                hideKeyboard(MainActivity.this);

                // return to Main View
                mainView.setVisibility(View.VISIBLE);
            }
        });

        // Cancel adding a graphic card and close the view
        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // end Add View
                View view = findViewById(R.id.cardEditor);
                ((ViewGroup) view.getParent()).removeView(view);
                hideKeyboard(MainActivity.this);

                // return to Main View
                mainView.setVisibility(View.VISIBLE);
            }
        });
    }

    public void editCard(GraphicCard card) {
        // create editView
        final ConstraintLayout mainLayout = findViewById(R.id.mainLayout);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        inflater.inflate(R.layout.card_editor, mainLayout);
        View editview = findViewById(R.id.cardEditor);

        final WeakReference<GraphicCard> reference = new WeakReference<GraphicCard>(card);

        fillEditor(editview, card);
        ((Button) findViewById(R.id.button)).setText("Save Changes");

        // Hide Main View until the end of editing a card.
        mainView.setVisibility(View.GONE);

        Button editButton = findViewById(R.id.button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // save Changes
                fillCard(reference.get());

                // Add card  to catalog and re-sort it
                catalog.sort(new Comparator<GraphicCard>() {
                    @Override
                    public int compare(GraphicCard o1, GraphicCard o2) {
                        return o1.name.compareTo(o2.name);
                    }
                });

                // Update Filtered data
                mAdaptor.notifyDataSetChanged();

                // end Add View
                View view = findViewById(R.id.cardEditor);
                ((ViewGroup) view.getParent()).removeView(view);
                hideKeyboard(MainActivity.this);

                // return to Main View
                mainView.setVisibility(View.VISIBLE);
            }
        });

        // Cancel adding a graphic card and close the view
        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // return to Main View
                mainView.setVisibility(View.VISIBLE);

                // end Add View
                View view = findViewById(R.id.cardEditor);
                ((ViewGroup) view.getParent()).removeView(view);
                hideKeyboard(MainActivity.this);

            }
        });
    }

    public void removeCard(View view) {
        mAdaptor.deleteMode = !mAdaptor.deleteMode;
        mAdaptor.notifyDataSetChanged();
    }

    public void deleteCard(int position) {
        catalog.remove(filteredLog.get(position).get());
        filteredLog.remove(position);
        mAdaptor.notifyItemRemoved(position);
    }

    private void fillEditor(View view, GraphicCard card) {

        ((EditText) findViewById(R.id.newCardName)).setText(card.name);
        ((EditText) findViewById(R.id.newManufacturor)).setText(card.manufacturer);
        ((EditText) findViewById(R.id.newPrice)).setText(Float.toString(card.price));
        ((EditText) findViewById(R.id.newRam)).setText(Float.toString(card.ram_size));
        ((EditText) findViewById(R.id.newRamType)).setText(card.ram_type);
        ((EditText) findViewById(R.id.newPCI)).setText(Float.toString(card.PCI));
        ((EditText) findViewById(R.id.newFans)).setText(Integer.toString(card.fans));
        ((EditText) findViewById(R.id.newHDMI)).setText(Integer.toString(card.hdmi));
        ((EditText) findViewById(R.id.newDisplays)).setText(Integer.toString(card.fans));
        ((EditText) findViewById(R.id.newPCI_Lane)).setText(Integer.toString(card.PCI_Lane));

        // Heaven Score
        ((EditText) findViewById(R.id.newHeavenScore)).setText(Double.toString(card.heavenScore));
        ((EditText) findViewById(R.id.newHeavenAvgFPS)).setText(Float.toString(card.heavenAvgFps));
        ((EditText) findViewById(R.id.newHeavenMaxFPS)).setText(Float.toString(card.heavenMaxFps));
        ((EditText) findViewById(R.id.newHeavenMinFPS)).setText(Float.toString(card.heavenMinFps));

        // Valley Score
        ((EditText) findViewById(R.id.newValleyScore)).setText(Double.toString(card.valleyScore));
        ((EditText) findViewById(R.id.newValleyAvgFPS)).setText(Float.toString(card.valleyAvgFps));
        ((EditText) findViewById(R.id.newValleyMaxFPS)).setText(Float.toString(card.valleyMaxFps));
        ((EditText) findViewById(R.id.newValleyMinFPS)).setText(Float.toString(card.valleyMinFps));

        // Superposition Score
        ((EditText) findViewById(R.id.newSuperpositionScore)).setText(Double.toString(card.superpositionScore));
        ((EditText) findViewById(R.id.newSuperpositionAvgFPS)).setText(Float.toString(card.superpositionAvgFps));
        ((EditText) findViewById(R.id.newSuperpositionMaxFPS)).setText(Float.toString(card.superpositionMaxFps));
        ((EditText) findViewById(R.id.newSuperpositionMinFPS)).setText(Float.toString(card.superpositionMinFps));

        // Other Scores
        ((EditText) findViewById(R.id.newSkydiverScore)).setText(Double.toString(card.skydiverScore));
        ((EditText) findViewById(R.id.newNightRaidScore)).setText(Double.toString(card.nightRaidScore));
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
        if (!temp.isEmpty()) newCard.ram_size = Float.parseFloat(temp);

        temp = ((EditText) findViewById(R.id.newRamType)).getText().toString();
        if (!temp.isEmpty()) newCard.ram_type = temp;

        temp = ((EditText) findViewById(R.id.newPCI)).getText().toString();
        if (!temp.isEmpty()) newCard.PCI = Float.parseFloat(temp);

        temp = ((EditText) findViewById(R.id.newFans)).getText().toString();
        if (!temp.isEmpty()) newCard.fans = Integer.parseInt(temp);

        temp = ((EditText) findViewById(R.id.newHDMI)).getText().toString();
        if (!temp.isEmpty()) newCard.hdmi = Integer.parseInt(temp);

        temp = ((EditText) findViewById(R.id.newDisplays)).getText().toString();
        if (!temp.isEmpty()) newCard.displayPorts = Integer.parseInt(temp);

        temp = ((EditText) findViewById(R.id.newPCI_Lane)).getText().toString();
        if (!temp.isEmpty()) newCard.PCI_Lane = Integer.parseInt(temp);

        // Heaven Score
        temp = ((EditText) findViewById(R.id.newHeavenScore)).getText().toString();
        if (!temp.isEmpty()) newCard.heavenScore = Double.parseDouble(temp);

        temp = ((EditText) findViewById(R.id.newHeavenAvgFPS)).getText().toString();
        if (!temp.isEmpty()) newCard.heavenAvgFps = Float.parseFloat(temp);

        temp = ((EditText) findViewById(R.id.newHeavenMaxFPS)).getText().toString();
        if (!temp.isEmpty()) newCard.heavenMaxFps = Float.parseFloat(temp);

        temp = ((EditText) findViewById(R.id.newHeavenMinFPS)).getText().toString();
        if (!temp.isEmpty()) newCard.heavenMinFps = Float.parseFloat(temp);

        // Valley Score
        temp = ((EditText) findViewById(R.id.newValleyScore)).getText().toString();
        if (!temp.isEmpty()) newCard.valleyScore = Double.parseDouble(temp);

        temp = ((EditText) findViewById(R.id.newValleyAvgFPS)).getText().toString();
        if (!temp.isEmpty()) newCard.valleyAvgFps = Float.parseFloat(temp);

        temp = ((EditText) findViewById(R.id.newValleyMaxFPS)).getText().toString();
        if (!temp.isEmpty()) newCard.valleyMaxFps = Float.parseFloat(temp);

        temp = ((EditText) findViewById(R.id.newValleyMinFPS)).getText().toString();
        if (!temp.isEmpty()) newCard.valleyMinFps = Float.parseFloat(temp);

        // Superposition Score
        temp = ((EditText) findViewById(R.id.newSuperpositionScore)).getText().toString();
        if (!temp.isEmpty()) newCard.superpositionScore = Double.parseDouble(temp);

        temp = ((EditText) findViewById(R.id.newSuperpositionAvgFPS)).getText().toString();
        if (!temp.isEmpty()) newCard.superpositionAvgFps = Float.parseFloat(temp);

        temp = ((EditText) findViewById(R.id.newSuperpositionMaxFPS)).getText().toString();
        if (!temp.isEmpty()) newCard.superpositionMaxFps = Float.parseFloat(temp);

        temp = ((EditText) findViewById(R.id.newSuperpositionMinFPS)).getText().toString();
        if (!temp.isEmpty()) newCard.superpositionMinFps = Float.parseFloat(temp);

        // Other Scores
        temp = ((EditText) findViewById(R.id.newSkydiverScore)).getText().toString();
        if (!temp.isEmpty()) newCard.skydiverScore = Double.parseDouble(temp);

        temp = ((EditText) findViewById(R.id.newNightRaidScore)).getText().toString();
        if (!temp.isEmpty()) newCard.nightRaidScore = Double.parseDouble(temp);
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

package ion.hyperon.groupproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// Adapter class for setting up the catalog list recyclerView.
public class GraphicCardAdaptor extends RecyclerView.Adapter<GraphicCardAdaptor.GraphicCardViewHolder> {

    private ArrayList<GraphicCard> catalog;

    //holder class for individual items in the catalog list.
    public static class GraphicCardViewHolder extends RecyclerView.ViewHolder {

        public TextView textName;
        public TextView textManufacturer;
        public CheckBox select;

        public GraphicCardViewHolder(@NonNull View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.textView);
            textManufacturer = itemView.findViewById(R.id.textView2);
            select = itemView.findViewById(R.id.checkBox);
        }
    }

    // default constructor
    public GraphicCardAdaptor(ArrayList<GraphicCard> filteredList) {
        catalog = filteredList;
    }

    // Grabs activity layout and sets it up with card view.
    @NonNull
    @Override
    public GraphicCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.catalog_item, parent, false);
        GraphicCardViewHolder gvh = new GraphicCardViewHolder(v);

        return gvh;
    }

    // Make a new "view item"
    @Override
    public void onBindViewHolder(@NonNull GraphicCardViewHolder holder, int position) {
        GraphicCard currentCard = catalog.get(position);

        holder.textName.setText(currentCard.name);
        holder.textManufacturer.setText(currentCard.manufacturer);

    }

    // list size
    @Override
    public int getItemCount() {
        return catalog.size();
    }
}

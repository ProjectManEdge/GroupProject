package ion.hyperon.groupproject;

import android.icu.text.DecimalFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

// Adapter class for setting up the catalog list recyclerView.
public class GraphicCardAdaptor extends RecyclerView.Adapter<GraphicCardAdaptor.GraphicCardViewHolder> {

    private ArrayList<WeakReference<GraphicCard>> catalog;

    // interface for clicking through parent activity
    private OnItemClickListener mListener;

    public boolean deleteMode;

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    //holder class for individual items in the catalog list.
    public static class GraphicCardViewHolder extends RecyclerView.ViewHolder {

        public TextView textName;
        public TextView textManufacturer;
        public TextView price;
        public ImageView deleteImage;


        public GraphicCardViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            textName = itemView.findViewById(R.id.textView);
            textManufacturer = itemView.findViewById(R.id.textView2);
            price = itemView.findViewById(R.id.catalog_card_Price);
            deleteImage = itemView.findViewById(R.id.Card_Delete);

            // setup onClickListener for parent Activity
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            deleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    // default constructor
    public GraphicCardAdaptor(ArrayList<WeakReference<GraphicCard>> filteredList) {
        catalog = filteredList;
        deleteMode = false;
    }

    // Grabs activity layout and sets it up with card view.
    @NonNull
    @Override
    public GraphicCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.catalog_item, parent, false);

        return new GraphicCardViewHolder(v, mListener);
    }

    // Make a new "view item"
    @Override
    public void onBindViewHolder(@NonNull GraphicCardViewHolder holder, int position) {
        GraphicCard currentCard = (catalog.get(position)).get();

        DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
        holder.textName.setText(currentCard.name);
        holder.textManufacturer.setText(currentCard.manufacturer);
        holder.price.setText("$" + formatter.format(currentCard.price));

        if (deleteMode) {
            holder.deleteImage.setVisibility(View.VISIBLE);
        } else {
            holder.deleteImage.setVisibility(View.GONE);
        }

    }

    // list size
    @Override
    public int getItemCount() {
        return catalog.size();
    }
}


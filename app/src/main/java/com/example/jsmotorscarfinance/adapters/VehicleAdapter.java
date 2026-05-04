// Vehicle Adapter: Bridges the car inventory data from SQLite to the gallery UI items
package com.example.jsmotorscarfinance.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.jsmotorscarfinance.R;
import com.example.jsmotorscarfinance.models.Vehicle;
import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder> {

    private List<Vehicle> vehicleList;
    private OnVehicleClickListener listener;
    private String actionText;

    // Interface to handle user interactions with specific vehicle items
    public interface OnVehicleClickListener {
        void onVehicleClick(Vehicle vehicle);
    }

    // Constructor to initialize the vehicle adapter with data and button text
    public VehicleAdapter(List<Vehicle> vehicleList, String actionText, OnVehicleClickListener listener) {
        this.vehicleList = vehicleList;
        this.actionText = actionText;
        this.listener = listener;
    }

    // Inflates the vehicle card layout when a new row is created
    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehicle, parent, false);
        return new VehicleViewHolder(v);
    }

    // Binds vehicle details and images to the UI using the Glide library
    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        Vehicle v = vehicleList.get(position);
        holder.tvName.setText(v.getName());
        holder.tvSpecs.setText(v.getSpecs());
        holder.tvPrice.setText("Rs. " + String.format("%.2f", v.getPrice()));
        holder.btnAction.setText(actionText);
        
        // Dynamically loads the car image from URL or Base64 binary data
        if (v.getImageUrl() != null && !v.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(v.getImageUrl())
                    .placeholder(R.drawable.ic_directions_car_24)
                    .error(R.drawable.ic_directions_car_24)
                    .into(holder.ivVehicle);
        } else {
            holder.ivVehicle.setImageResource(R.drawable.ic_directions_car_24);
        }

        holder.btnAction.setOnClickListener(view -> listener.onVehicleClick(v));
    }

    // Returns the current count of vehicles in the inventory list
    @Override
    public int getItemCount() {
        return vehicleList.size();
    }

    // ViewHolder class to cache UI references and improve list scrolling performance
    static class VehicleViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvSpecs, tvPrice;
        ImageView ivVehicle;
        Button btnAction;
        
        public VehicleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvVehicleName);
            tvSpecs = itemView.findViewById(R.id.tvVehicleSpecs);
            tvPrice = itemView.findViewById(R.id.tvVehiclePrice);
            ivVehicle = itemView.findViewById(R.id.ivVehicleImage);
            btnAction = itemView.findViewById(R.id.btnAction);
        }
    }
}

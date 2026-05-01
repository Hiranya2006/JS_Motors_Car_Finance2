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

    public interface OnVehicleClickListener {
        void onVehicleClick(Vehicle vehicle);
    }

    public VehicleAdapter(List<Vehicle> vehicleList, String actionText, OnVehicleClickListener listener) {
        this.vehicleList = vehicleList;
        this.actionText = actionText;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehicle, parent, false);
        return new VehicleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        Vehicle v = vehicleList.get(position);
        holder.tvName.setText(v.getName());
        holder.tvSpecs.setText(v.getSpecs());
        holder.tvPrice.setText("Rs. " + String.format("%.2f", v.getPrice()));
        holder.btnAction.setText(actionText);
        
        // Use Glide to load the car image
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

    @Override
    public int getItemCount() {
        return vehicleList.size();
    }

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

package com.example.jsmotorscarfinance.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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
        holder.tvPrice.setText("Price: $" + String.format("%.2f", v.getPrice()));
        holder.btnAction.setText(actionText);
        holder.btnAction.setOnClickListener(view -> listener.onVehicleClick(v));
    }

    @Override
    public int getItemCount() {
        return vehicleList.size();
    }

    static class VehicleViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvSpecs, tvPrice;
        Button btnAction;
        public VehicleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvVehicleName);
            tvSpecs = itemView.findViewById(R.id.tvVehicleSpecs);
            tvPrice = itemView.findViewById(R.id.tvVehiclePrice);
            btnAction = itemView.findViewById(R.id.btnAction);
        }
    }
}

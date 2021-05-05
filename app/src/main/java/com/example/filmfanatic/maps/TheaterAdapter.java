package com.example.filmfanatic.maps;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.filmfanatic.Film;
import com.example.filmfanatic.R;
import com.example.filmfanatic.WishListActivity;
import com.example.filmfanatic.WishListAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.List;


public class TheaterAdapter extends RecyclerView.Adapter<TheaterAdapter.ViewHolder> {


    private Context context;
    private List<HashMap<String,String>> AllPlaces;


    public TheaterAdapter(Context context,List<HashMap<String,String>> AllPlaces ){
        this.context=context;
        this.AllPlaces=AllPlaces;

    }

    @NonNull
    @Override
    public TheaterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View theater= LayoutInflater.from(context).inflate(R.layout.each_theater, parent, false);
        return new TheaterAdapter.ViewHolder(theater);
    }

    @Override
    public void onBindViewHolder(@NonNull TheaterAdapter.ViewHolder holder, int position) {
        HashMap<String,String> hashMap=AllPlaces.get(position);
        holder.bind(hashMap);
    }

    @Override
    public int getItemCount() {
        return AllPlaces.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvAddress;
        TextView tvDistance;
        Button btnMore;
        Button btnLocation;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           tvName=itemView.findViewById(R.id.tvPlaceName);
           tvAddress=itemView.findViewById(R.id.tvPlaceAddress);
           tvDistance=itemView.findViewById(R.id.tvDistance);
           btnMore=itemView.findViewById(R.id.btnMore);
           btnLocation=itemView.findViewById(R.id.btnLocation);

        }

        public void bind(HashMap hashMap) {
            double Currentlat=Double.parseDouble(hashMap.get("lat").toString());
            double Currentlng=Double.parseDouble(hashMap.get("lng").toString());
            LatLng CurrentLatLng=new LatLng(Currentlat,Currentlng);

            tvName.setText(hashMap.get("place_name").toString());
            tvAddress.setText(hashMap.get("vicinity").toString());

            float f[]=new float[10];
            Location.distanceBetween(MapsFragment.HomeLatLng.latitude,MapsFragment.HomeLatLng.longitude,Currentlat,Currentlng,f);
            double Miles=f[0]*3.28084/5280;

            tvDistance.setText( String.format("%.2f", Miles) + " mi");

            btnLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MapsFragment.TheMap.clear();

                    for (int i=0;i<AllPlaces.size();i++){

                        LatLng latLng=new LatLng(Double.parseDouble(AllPlaces.get(i).get("lat")), Double.parseDouble(AllPlaces.get(i).get("lng")));

                        if (latLng.equals(CurrentLatLng)){
                            Marker marker = MapsFragment.TheMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title(AllPlaces.get(i).get("place_name").toString() )
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                            marker.showInfoWindow();


                        }

                        else{
                            Marker marker = MapsFragment.TheMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title(AllPlaces.get(i).get("place_name").toString() )
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                        }
                    }

                    Marker marker = MapsFragment.TheMap.addMarker(new MarkerOptions()
                            .position(MapsFragment.HomeLatLng)
                            .title(("You"))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                   // MapsFragment.TheMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Currentlat,Currentlng),11));
                    MapsFragment.TheMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Currentlat,Currentlng),11));
                }
            });

            btnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(context,TheaterActivity.class);
                    i.putExtra("TheaterInfo",Parcels.wrap(hashMap));
                    context.startActivity(i);


                }
            });

        }
    }

}

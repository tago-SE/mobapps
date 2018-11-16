package tiago.weatherforecast.ui;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Hashtable;
import java.util.List;

import tiago.weatherforecast.R;
import tiago.weatherforecast.repo.data.Weather;

/**
 * Provide views to RecyclerView with data from a weather forecast data set. .
 */
public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private static final String TAG = "WeatherAdapter";

    /* Hash table containing resource mapping (index to image) */
    private static Hashtable<Integer, Drawable> skyHash = null;
    /* List of weather forecast items displayed */
    private List<Weather> items;
    /* The context which the adapter belongs to */
    private Context context;

    public WeatherAdapter(List<Weather> items, Context context) {
        this.items = items;
        this.context = context;
        if (skyHash == null)
            setupSkyImages();
    }

    private void setupSkyImages() {
        skyHash = new Hashtable<>();
        skyHash.put(1, context.getResources().getDrawable(R.drawable.s1));
        skyHash.put(2, context.getResources().getDrawable(R.drawable.s2));
        skyHash.put(3, context.getResources().getDrawable(R.drawable.s3));
        skyHash.put(4, context.getResources().getDrawable(R.drawable.s4));
        skyHash.put(5, context.getResources().getDrawable(R.drawable.s5));
        skyHash.put(6, context.getResources().getDrawable(R.drawable.s6));
        skyHash.put(7, context.getResources().getDrawable(R.drawable.s7));
        skyHash.put(8, context.getResources().getDrawable(R.drawable.s8));
        skyHash.put(9, context.getResources().getDrawable(R.drawable.s9));
        skyHash.put(10, context.getResources().getDrawable(R.drawable.s10));
        skyHash.put(11, context.getResources().getDrawable(R.drawable.s11));
        skyHash.put(12, context.getResources().getDrawable(R.drawable.s12));
        skyHash.put(13, context.getResources().getDrawable(R.drawable.s13));
        skyHash.put(14, context.getResources().getDrawable(R.drawable.s14));
        skyHash.put(15, context.getResources().getDrawable(R.drawable.s15));
        skyHash.put(16, context.getResources().getDrawable(R.drawable.s16));
        skyHash.put(17, context.getResources().getDrawable(R.drawable.s17));
        skyHash.put(18, context.getResources().getDrawable(R.drawable.s18));
        skyHash.put(19, context.getResources().getDrawable(R.drawable.s19));
        skyHash.put(20, context.getResources().getDrawable(R.drawable.s20));
        skyHash.put(21, context.getResources().getDrawable(R.drawable.s21));
        skyHash.put(22, context.getResources().getDrawable(R.drawable.s22));
        skyHash.put(23, context.getResources().getDrawable(R.drawable.s23));
        skyHash.put(24, context.getResources().getDrawable(R.drawable.s24));
        skyHash.put(25, context.getResources().getDrawable(R.drawable.s25));
        skyHash.put(26, context.getResources().getDrawable(R.drawable.s26));
        skyHash.put(27, context.getResources().getDrawable(R.drawable.s27));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.weather, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) { ;
        Weather item = items.get(i);
        viewHolder.tvTime.setText(item.time);
        viewHolder.tvTemp.setText("" + item.temperature + " °C");
        viewHolder.tvDate.setText(item.date);
        viewHolder.ivWeather.setImageDrawable(skyHash.get(item.sky));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Provide a reference to the view type (Weather ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTime;
        public TextView tvDate;
        public TextView tvTemp;
        public ImageView ivWeather;

        public ViewHolder(View v) {
            super(v);
            tvTime = itemView.findViewById(R.id.time);
            tvDate = itemView.findViewById(R.id.date);
            tvTemp = itemView.findViewById(R.id.temperature);
            ivWeather = itemView.findViewById(R.id.imageView);
        }
    }

}
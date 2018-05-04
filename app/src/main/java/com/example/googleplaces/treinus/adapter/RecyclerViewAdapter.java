package com.example.googleplaces.treinus.adapter;



import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.googleplaces.treinus.R;

import com.example.googleplaces.treinus.model.Places;
import com.example.googleplaces.treinus.model.StoreModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;
import static com.google.android.gms.internal.zzt.TAG;

public class RecyclerViewAdapter  extends  RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> implements View.OnClickListener{



    private List<Places.Custom> stLstStores;
    private Context context;

    private List<StoreModel> models;
    final String urlphotos ="https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=CnRtAAAATLZNl354RwP_9UKbQ_5Psy40texXePv4oAlgP4qNEkdIrkyse7rPXYGd9D_Uj1rVsQdWT4oRz4QrYAJNpFX7rzqqMlZw2h2E2y5IKMUZ7ouD_SlcHxYq1yL4KbKUv3qtWgTK0A6QbGh87GB3sscrHRIQiG2RrmU_jF4tENr9wGS_YxoUSSDrYjWmrNfeEHSGSc3FyhNLlBU&key=AIzaSyBNFTAKdygCi8mGe1-pR2XILQMBu2BnkNA";

    public RecyclerViewAdapter(List<Places.Custom> stores, List<StoreModel> storeModels) {

        stLstStores = stores;
        models = storeModels;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.store_list, parent, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("teste","tesasdas");

            }
        });


        return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {



        holder.setData(stLstStores.get(holder.getAdapterPosition()), holder, models.get(holder.getAdapterPosition()));

/*        public void onClick(View view) {
holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlphotos));
                view.getContext().startActivity(browserIntent);


                Glide
                        .with(view.getContext())
                        .load(urlphotos)
                        .into(holder.imageView);

             Toast.makeText(view.getContext(),"achou"+ position + "old"+ holder.getOldPosition(),LENGTH_SHORT).show();

            }
        });*/


    }

    @Override
    public int getItemCount() {
        return Math.min(20, stLstStores.size());
    }

    @Override
    public void onClick(View view) {


    }




    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtStoreName;
        TextView txtStoreAddr;
        TextView txtStoreDist;
        ImageView imageView;
        StoreModel model;


        public MyViewHolder(View itemView) {
            super(itemView);




            this.imageView    = (ImageView) itemView.findViewById(R.id.place_image);
            this.txtStoreDist = (TextView) itemView.findViewById(R.id.txtStoreDist);
            this.txtStoreName = (TextView) itemView.findViewById(R.id.txtStoreName);
            this.txtStoreAddr = (TextView) itemView.findViewById(R.id.txtStoreAddr);


        }

        public void setData(Places.Custom info, MyViewHolder holder, StoreModel storeModel)  {

            this.model = storeModel;

            holder.txtStoreDist.setText(model.distance + "\n" + model.duration);
            holder.txtStoreName.setText(info.name);
            holder.txtStoreAddr.setText(info.vicinity);
            if (info.photos!=null){
                String test = "https://maps.googleapis.com/maps/api/place/photo?&photoreference=";
                Glide.with(((MyViewHolder) holder).itemView.getContext())
                        .load(test+ info.photos.get(0).photoReference)
                        .into(holder.imageView);
            }
        }

    }


}

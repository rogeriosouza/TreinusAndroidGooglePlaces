package com.example.googleplaces.treinus.adapter;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;



import com.example.googleplaces.treinus.R;

import com.example.googleplaces.treinus.model.Places;
import com.example.googleplaces.treinus.model.StoreModel;

import java.util.List;

public class RecyclerViewAdapter  extends  RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{



    private List<Places.Custom> stLstStores;

    private List<StoreModel> models;

    public RecyclerViewAdapter(List<Places.Custom> stores, List<StoreModel> storeModels) {

        stLstStores = stores;
        models = storeModels;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.store_list, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.setData(stLstStores.get(holder.getAdapterPosition()), holder, models.get(holder.getAdapterPosition()));


    }

    @Override
    public int getItemCount() {
        return Math.min(20, stLstStores.size());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtStoreName;
        TextView txtStoreAddr;
        TextView txtStoreDist;
        ImageView imageView;
        StoreModel model;


        public MyViewHolder(View itemView) {
            super(itemView);




            //this.imageView =  (ImageView) itemView.findViewById(R.id.place_image);
            this.txtStoreDist = (TextView) itemView.findViewById(R.id.txtStoreDist);
            this.txtStoreName = (TextView) itemView.findViewById(R.id.txtStoreName);
            this.txtStoreAddr = (TextView) itemView.findViewById(R.id.txtStoreAddr);


        }

        public void setData(Places.Custom info, MyViewHolder holder, StoreModel storeModel) {

            this.model = storeModel;
            String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=CnRtAAAATLZNl354RwP_9UKbQ_5Psy40texXePv4oAlgP4qNEkdIrkyse7rPXYGd9D_Uj1rVsQdWT4oRz4QrYAJNpFX7rzqqMlZw2h2E2y5IKMUZ7ouD_SlcHxYq1yL4KbKUv3qtWgTK0A6QbGh87GB3sscrHRIQiG2RrmU_jF4tENr9wGS_YxoUSSDrYjWmrNfeEHSGSc3FyhNLlBU&key=AIzaSyBNFTAKdygCi8mGe1-pR2XILQMBu2BnkNA\n";

            Bitmap bMap = BitmapFactory.decodeFile(url);


            //holder.imageView.setImageBitmap(bMap);

            holder.txtStoreDist.setText(model.distance + "\n" + model.duration);
            holder.txtStoreName.setText(info.name);
            holder.txtStoreAddr.setText(info.vicinity);
        }

    }


}

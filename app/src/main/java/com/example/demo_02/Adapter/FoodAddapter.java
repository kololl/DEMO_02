package com.example.demo_02.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo_02.Model.Product;
import com.example.demo_02.R;

import java.util.List;

public class FoodAddapter extends RecyclerView.Adapter<FoodAddapter.FoodViewHoder>{
    private List<Product> mlistProduct;
    //Update
    private Iclicklistener mIclicklistener;


    public interface Iclicklistener{
        void onclickUpdate(Product product);
        void onclickDelete(Product product);
    }

    public FoodAddapter(List<Product> mlistProduct,Iclicklistener listener) {
        this.mlistProduct = mlistProduct;
        this.mIclicklistener = listener;

    }

    @NonNull
    @Override
    public FoodViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item, parent, false);
        return new FoodAddapter.FoodViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHoder holder, int position) {
        Product product=mlistProduct.get(position);
        if (product==null){
            return;
        }
        holder.name.setText("name:"+ product.getName());
        holder.price.setText("price:"+ product.getPrice());

        holder.btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIclicklistener.onclickUpdate(product);
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIclicklistener.onclickDelete(product);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mlistProduct!=null){
            return mlistProduct.size();
        }
        return 0;
    }

    public class FoodViewHoder extends RecyclerView.ViewHolder{
        private TextView name, price;
        //update
     private ImageButton btnupdate;
//        //delete
     private  ImageButton btnDelete;


        public FoodViewHoder(@NonNull View itemView) {
            super(itemView);
            name=(TextView) itemView.findViewById(R.id.txtname);
            price=(TextView) itemView.findViewById(R.id.txtprice);
////update
           btnupdate= itemView.findViewById(R.id.btnUpdate);
///Delete
            btnDelete= itemView.findViewById(R.id.btnDelete);
        }
    }
}

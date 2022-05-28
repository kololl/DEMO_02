package com.example.demo_02;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo_02.Adapter.FoodAddapter;
import com.example.demo_02.Model.Product;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {
    private TextView name, price;
    private RecyclerView recyclerView;
    private FoodAddapter mProductAdapter;
    private List<Product> mlistProduct;
    //Them
    private EditText nameAdd, priceAdd;
    private ImageButton btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        name= findViewById(R.id.txtname);
        price= findViewById(R.id.txtprice);
        recyclerView= findViewById(R.id.rycyler);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration= new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        mlistProduct= new ArrayList<>();
        mProductAdapter= new FoodAddapter(mlistProduct, new FoodAddapter.Iclicklistener() {
            @Override
            public void onclickUpdate(Product product) {
                openDialogUpdateItem(product);
            }

            @Override
            public void onclickDelete(Product product) {
                onClickDelete(product);

            }
        });
        recyclerView.setAdapter(mProductAdapter);

        getListCatefromFireBase();

        //Add
        nameAdd= findViewById(R.id.txtNameAdd);
        priceAdd= findViewById(R.id.txtPriceAdd);
        btnAdd= findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name= nameAdd.getText().toString().trim();
                String price= priceAdd.getText().toString().trim();
                Product product=new Product(name,price);
                onClickAdd(product);

            }
        });
    }
    private void getListCatefromFireBase(){
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference reference= database.getReference("Product");


        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Product product= snapshot.getValue(Product.class);
                if (product!=null){
                    mlistProduct.add(product);
                    mProductAdapter.notifyDataSetChanged();


                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Product product= snapshot.getValue(Product.class);
                if (mlistProduct==null|| mlistProduct.isEmpty()){
                    return;
                }
                for (int i=0; i<mlistProduct.size(); i++){
                    if (product.getName()==mlistProduct.get(i).getName() && product.getPrice()==mlistProduct.get(i).getPrice()){
                        mlistProduct.set(i, product);
                        break;
                    }
                }
                mProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Product product= snapshot.getValue(Product.class);
                if (mlistProduct==null|| mlistProduct.isEmpty()){
                    return;
                }
                for (int i=0; i<mlistProduct.size(); i++){
                    if (product.getName()==mlistProduct.get(i).getName() && product.getPrice()==mlistProduct.get(i).getPrice()){
                        mlistProduct.remove(mlistProduct.get(i));
                        break;
                    }
                }
                mProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void onClickAdd(Product product){
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference reference= database.getReference("Product");

        String pathObject=String.valueOf(product.getName());
        reference.child(pathObject).setValue(product);

    }
    private void openDialogUpdateItem(Product product){
        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_update);
        Window window=dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        EditText txtName2=dialog.findViewById(R.id.txtNameUpdate);
        EditText txtPrice2=dialog.findViewById(R.id.txtPriceUpdate);
        ImageButton btnUpdate1= dialog.findViewById(R.id.btnUpdate2);
        ImageButton btnCanel= dialog.findViewById(R.id.btnCancel);

        txtName2.setText(product.getName());
        txtPrice2.setText(product.getPrice());
        btnCanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnUpdate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database= FirebaseDatabase.getInstance();
                DatabaseReference reference= database.getReference("Product");

                String newName= txtName2.getText().toString().trim();
                String newPrice= txtPrice2.getText().toString().trim();

                product.setName(newName);
                product.setPrice(newPrice);
                reference.child(String.valueOf(product.getName())).updateChildren(product.toMap(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(Home.this, "Update suscess", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });

        dialog.show();

    }
    private void onClickDelete(Product product){
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage("ban co chac chan xoa dong nay khong?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase database= FirebaseDatabase.getInstance();
                        DatabaseReference reference= database.getReference("Product");

                        reference.child(String.valueOf(product.getName())).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(Home.this, "Delete suscess", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                }).setNegativeButton("Cancel", null).show();


    }
}
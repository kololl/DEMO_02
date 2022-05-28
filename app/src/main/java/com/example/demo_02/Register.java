package com.example.demo_02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.demo_02.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {
    private EditText edtName, edtPhone, edtpass, edtEmail;
    private ImageButton btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edtName= findViewById(R.id.edtName);
        edtPhone= findViewById(R.id.edtPhone);
        edtpass= findViewById(R.id.edtPass1);
        edtEmail= findViewById(R.id.edtEmail1);

        btnRegister= findViewById(R.id.btnRegister);

        final FirebaseDatabase database= FirebaseDatabase.getInstance();
        final DatabaseReference table_user=database.getReference("User");

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProgressDialog mDialog=new ProgressDialog(Register.this);
                mDialog.setMessage("Wait");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child(edtPhone.getText().toString()).exists()){
                            mDialog.dismiss();
                            Toast.makeText(Register.this, "Susscess", Toast.LENGTH_SHORT).show();
                        }else {
                            mDialog.dismiss();
                            User user= new User(edtName.getText().toString(),

                                    edtpass.getText().toString(),edtEmail.getText().toString());
                            table_user.child(edtPhone.getText().toString()).setValue(user);
                            Toast.makeText(Register.this, "Register sussces", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }


                });
                Intent intent= new Intent(Register.this, Home.class);
                startActivity(intent);

            }
        });
    }
}
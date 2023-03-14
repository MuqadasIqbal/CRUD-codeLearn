package com.example.codelearningp1;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.codelearningp1.databinding.ActivityMain2Binding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class MainActivity2 extends AppCompatActivity {
ActivityMain2Binding binding;
    String rollnumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.buttonfectch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               rollnumber=binding.editTextTextPersonName4.getText().toString();
                FirebaseDatabase.getInstance().getReference().child("Student").child(rollnumber).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Map map=(Map) snapshot.getValue();
                        if (snapshot.exists()){

                            String first=(String) map.get("first");
                            String second=(String) map.get("second");
                            String third=(String) map.get("third");
                            /*String four=(String) map.get("four");
                            String five=(String) map.get("five");*/
                           /* String image=(String) map.get("image");*/
                            String Imageuri=(String) map.get("ImageUri");

                            binding.textView4.setText(first);
                            binding.textView5.setText(second);
                            binding.textView6.setText(third);
                    // binding.imageView.setImageResource(Integer.parseInt(image));
                           /* binding.textView7.setText(four);
                            binding.textView8.setText(five);*/

                            Glide.with(MainActivity2.this).load(Imageuri).into(binding.imageView);
                        }
                        else{
                            Toast.makeText(MainActivity2.this, "data not found", Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}
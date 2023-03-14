package com.example.codelearningp1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.codelearningp1.databinding.ActivityMainBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
ActivityMainBinding binding;
int maxid;
Uri uri;
String imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickphoto=new Intent(Intent.ACTION_PICK);
                pickphoto.setType("image/*");
                startActivityForResult(pickphoto,45);
            }
        });
        FirebaseDatabase.getInstance().getReference().child("Student").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                maxid=(int) snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.insertbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseStorage.getInstance().getReference().child("FolderName").child(String.valueOf(maxid+1)).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri>uriTask=taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        imageUri=uriTask.getResult().toString();


                        HashMap<String,Object>map=new HashMap<>();
                        map.put("first",binding.editTextTextPersonName.getText().toString());
                        map.put("second",binding.editTextTextPersonName2.getText().toString());
                        map.put("third",binding.editTextTextPersonName3.getText().toString());
              /*  map.put("four", binding.editTextTextPassword.getText().toString());
                map.put("five",binding.editTextPhone.getText().toString());*/
                         map.put("ImageUri",imageUri);


                        FirebaseDatabase.getInstance().getReference().child("Student").child(String.valueOf(maxid+1)).setValue(map);

                    }
                });

                Toast.makeText(MainActivity.this, "Data Send", Toast.LENGTH_SHORT).show();
                  Intent intent=new Intent(MainActivity.this,MainActivity2.class);
                startActivity(intent);
            }
        });
        binding.fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               FirebaseDatabase.getInstance().getReference().child("Student").addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                     Map<String,Object> getmap=(Map) snapshot.getValue();
                       if (snapshot.exists()){
                           String first= (String) getmap.get("first");
                           String second=(String) getmap.get("second");
                           String third=(String) getmap.get("third");
                           String four=(String) getmap.get("four");
                           String five=(String) getmap.get("five");
                           String image=(String) getmap.get("ImageUri");

                           binding.textView.setText(first);
                           binding.textView2.setText(second);
                           binding.textView3.setText(third);
                           binding.password.setText(four);
                           binding.phone.setText(five);
                           Glide.with(MainActivity.this).load(image).into(binding.imageView);
                       }else{
                           Toast.makeText(MainActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                       }

                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });
            }
        });
        binding.updatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap  updatemap=new HashMap();
                updatemap.put("first",binding.editTextTextPersonName.getText().toString());
                updatemap.put("second",binding.editTextTextPersonName2.getText().toString());
                updatemap.put("third",binding.editTextTextPersonName3.getText().toString());
              /*  updatemap.put("four", binding.editTextTextPassword.getText().toString());
                updatemap.put("five",binding.editTextPhone.getText().toString());*/
                updatemap.put("ImageUri",imageUri);
                FirebaseDatabase.getInstance().getReference().child("Student").updateChildren(updatemap);
                Toast.makeText(MainActivity.this, "update", Toast.LENGTH_SHORT).show();
               /* Intent intent=new Intent(MainActivity.this,M ainActivity2.class);
                startActivity(intent);*/
            }
        });
     binding.DeleteButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             binding.textView.setText(null);
            /* binding.phone.setText(null);
             binding.password.setText(null);*/
             binding.textView3.setText(null);
             binding.textView2.setText(null);
             binding.imageView.setImageResource(0);
             FirebaseStorage.getInstance().getReference().child("FolderName").child("rollno").delete();
             FirebaseDatabase.getInstance().getReference().child("Student").removeValue();
             Toast.makeText(MainActivity.this, "Delete", Toast.LENGTH_SHORT).show();

             /*Intent intent=new Intent(MainActivity.this,MainActivity2.class);
             startActivity(intent);*/
         }
     });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            uri=data.getData();
            binding.imageView2.setImageURI(uri);

        }
    }
}
package com.example.halt.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.halt.Models.User;
import com.example.halt.R;
import com.example.halt.interfaces.FirebaseCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Objects;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<User> userList;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    public MyAdapter(ArrayList<User> userList){
        this.userList = userList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.recycler_view_friends, parent, false);
        return new UserViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        User user = (User) userList.get(position);
        ((UserViewHolder) holder).bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{

        private  TextView name;
        private ImageView image;
        private Button acceptBtn;

        public UserViewHolder(@NonNull View itemView){
            super(itemView);
            name= itemView.findViewById(R.id.friends_name_tv);
            image= itemView.findViewById(R.id.profile_image_iv);
            acceptBtn= itemView.findViewById(R.id.accept_btn);
        }

        void bind(User user){
            name.setText(user.getEmail());
            downloadImage(user);
            acceptBtn.setOnClickListener(v -> setAcceptBtn(user.getUserId()));

            checkIfUserAccepted(user, new FirebaseCallback<String>() {
                @Override
                public void onCallback(String item) {
                    if(item.equals("true"))
                        acceptBtn.setVisibility(View.GONE);
                }
            });
        }

        private void checkIfUserAccepted(User user, FirebaseCallback<String> callback){
            databaseReference.child("MeetPoints").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Participants").child(user.getUserId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    callback.onCallback(Objects.requireNonNull(snapshot.getValue()).toString());
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

        private void setAcceptBtn(String userId){
            databaseReference.child("MeetPoints").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Participants").child(userId).setValue("true");
            Toast.makeText(itemView.getContext(), "accepted", Toast.LENGTH_SHORT).show();
        }

        private void downloadImage(User user){
            FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
            StorageReference storageReference= firebaseStorage.getReference();
            StorageReference ricersRef= storageReference.child("images/profile/"+ user.getUserId());
            final long ONE_MEGABYTE = 1024 * 1024;
            ricersRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    image.setImageBitmap(bmp);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        }
    }

}

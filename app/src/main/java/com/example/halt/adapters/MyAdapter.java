package com.example.halt.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.halt.Models.User;
import com.example.halt.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<User> userList;

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

        public UserViewHolder(@NonNull View itemView){
            super(itemView);
            name= itemView.findViewById(R.id.friends_name_tv);
            image= itemView.findViewById(R.id.profile_image_iv);
        }

        void bind(User user){
            name.setText(user.getEmail());
            downloadImage(user);
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

package com.example.HealthT.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.HealthT.Models.MeetPoint;
import com.example.HealthT.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MeetPointAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<MeetPoint> meetPoints;

    public MeetPointAdapter(ArrayList<MeetPoint> meetPoints){
        this.meetPoints = meetPoints;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.recycler_view_meet_points, parent, false);
        return new MeetPointViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MeetPoint meetPoint = (MeetPoint) meetPoints.get(position);
        ((MeetPointAdapter.MeetPointViewHolder) holder).bind(meetPoint);
    }

    @Override
    public int getItemCount() {
        return meetPoints.size();
    }

    class MeetPointViewHolder extends RecyclerView.ViewHolder{

        private TextView activity;
        private TextView time;
        private ImageView image;

        public MeetPointViewHolder(@NonNull View itemView){
            super(itemView);
            activity= itemView.findViewById(R.id.meet_point_activity_tv);
            time= itemView.findViewById(R.id.meet_point_time_tv);
            image= itemView.findViewById(R.id.meet_point_image_iv);
        }

        void bind(MeetPoint meetPoint){
            String time;

            time= Integer.toString(meetPoint.getDate().getHours()) + ':' + Integer.toString(meetPoint.getDate().getMinutes());

            activity.setText(meetPoint.getActivity());
            this.time.setText(time);
            downloadImage(meetPoint);
        }


        private void downloadImage(MeetPoint meetPoint){
            FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
            StorageReference storageReference= firebaseStorage.getReference();
            StorageReference ricersRef= storageReference.child("images/meetPoints/"+ meetPoint.getMeetPointId());
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
package com.moapp.meet.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.moapp.meet.BoardDetailActivity;
import com.moapp.meet.BoardListWrite;
import com.moapp.meet.R;
import com.moapp.meet.model.category_search.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {
    Context context;
    ArrayList<Document> items;
    EditText editText;
    RecyclerView recyclerView;
    String category;
    private Activity m_activity;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String user_id = user.getUid();
    int tcount;
    String tname;
    public LocationAdapter(Activity act,ArrayList<Document> items, Context context, EditText editText, RecyclerView recyclerView, String category) {
        this.context = context;
        this.items = items;
        this.editText = editText;
        this.recyclerView = recyclerView;
        this.m_activity =act;
        this.category=category;
        db.collection("users").document(user_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                //닉네임 받아오기
                                tname = document.getString("NickName");
                                tcount = Integer.valueOf(document.getString("BoardCount"));
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void addItem(Document item) {
        items.add(item);
    }


    public void clear() {
        items.clear();
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(items.get(position).getId());
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_location, viewGroup, false);

        return new LocationViewHolder(view);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    Intent intent = new Intent(m_activity, BoardListWrite.class);
                    intent.putExtra("SearchLng", items.get(pos).getX());
                    intent.putExtra("SearchLat",  items.get(pos).getY());
                    intent.putExtra("SearchTitle",items.get(pos).getY());
                    m_activity.startActivity(intent);
                    if (pos != RecyclerView.NO_POSITION) {
                    }
                }
            });
        }


    }
    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int i) {
        final Document model = items.get(i);
        holder.placeNameText.setText(model.getPlaceName());
        holder.addressText.setText(model.getAddressName());
        holder.placeNameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> map = new HashMap<>();
                map.put("SearchLng",model.getX());
                map.put("SearchLat",model.getY());
                map.put("SearchTitle",model.getPlaceName());
                map.put("category",category);

                // Add a new document with a generated ID
                db.collection("map").document(user_id + tcount)
                        .set(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });
                editText.setText(model.getPlaceName());
                recyclerView.setVisibility(View.GONE);
                Toast.makeText(m_activity,"장소가 선택되었습니다",Toast.LENGTH_LONG);
//                System.out.println("선택된 item"+model.getPlaceName()+model.getX()+model.getY());
//                Intent intent = new Intent(m_activity);
//                intent.putExtra("SearchLng", model.getX());
//                intent.putExtra("SearchLat",  model.getY());
//                intent.putExtra("SearchTitle",model.getPlaceName());
//                m_activity.startActivity(intent);
                if (i != RecyclerView.NO_POSITION) {
                }

            }
        });
    }


    public class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView placeNameText;
        TextView addressText;

        public LocationViewHolder(@NonNull final View itemView) {
            super(itemView);
            placeNameText = itemView.findViewById(R.id.ltem_location_tv_placename);
            addressText = itemView.findViewById(R.id.ltem_location_tv_address);
        }
    }

}

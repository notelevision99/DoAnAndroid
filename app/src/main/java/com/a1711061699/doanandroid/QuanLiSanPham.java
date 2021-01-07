package com.a1711061699.doanandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.a1711061699.doanandroid.firestore.Food;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class QuanLiSanPham extends AppCompatActivity {

    class QuanLySPAdapter extends ArrayAdapter<Food> {

        public QuanLySPAdapter(Context context, int resource) {
            super(context,resource);
        }

        public QuanLySPAdapter() {
            super(QuanLiSanPham.this,android.R.layout.simple_list_item_1, foods);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if(row == null)
            {
                LayoutInflater layoutInflater = getLayoutInflater();
                row = layoutInflater.inflate(R.layout.item_food,null);
            }
            Food food = foods.get(position);
            TextView textViewTenSp = (TextView)row.findViewById(R.id.textViewTenSP);
            TextView textViewGia = (TextView)row.findViewById(R.id.textViewGia);
            TextView textViewLoaiSP = (TextView) row.findViewById(R.id.textViewLoaiSP);
            textViewTenSp.setText(String.valueOf(food.getName()));
            textViewGia.setText(String.valueOf(food.getPrice()));
            textViewLoaiSP.setText(String.valueOf(food.getCategory()));

            return row;
        }
    }

    private final static String TAG = "Asd";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public List<Food> foods = new ArrayList<Food>();
    private QuanLySPAdapter adapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_li_san_pham);
        ListView list = (ListView)findViewById(R.id.list_item_food);

        db.collection("food")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " item " + document.getData());
                                String name = document.toObject(Food.class).getName();
                                Integer price = document.toObject(Food.class).getPrice();
                                foods.add(new Food(name,price));
                                adapter.notifyDataSetChanged();
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        adapter = new QuanLySPAdapter();
        list.setAdapter(adapter);

    }

}
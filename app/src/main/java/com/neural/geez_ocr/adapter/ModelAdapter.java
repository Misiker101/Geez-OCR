package com.neural.geez_ocr.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.neural.geez_ocr.R;
import com.neural.geez_ocr.domain.CategoryDomain;
import com.neural.geez_ocr.domain.ModelDomain;

import java.util.ArrayList;

public class ModelAdapter extends RecyclerView.Adapter<ModelAdapter.ViewHolder> {

    ArrayList<ModelDomain> categoryModel;

    public ModelAdapter(ArrayList<ModelDomain> categoryModel) {
        this.categoryModel = categoryModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_model , parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(categoryModel.get(position).getTitle());
        holder.trained.setText(categoryModel.get(position).getTrained());
        holder.scanBtn.setText(categoryModel.get(position).getScanBtn_text());
        int drawableResourceId = holder.itemView.getContext().getResources().getIdentifier(categoryModel.get(position).getPic(), "drawable", holder.itemView.getContext().getPackageName());

        Glide.with(holder.itemView.getContext())
                .load(drawableResourceId)
                .into(holder.pic);

    }

    @Override
    public int getItemCount() {
        return categoryModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, trained;
        ImageView pic;
        TextView scanBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title   = itemView.findViewById(R.id.title);
            trained = itemView.findViewById(R.id.trained);
            pic     = itemView.findViewById(R.id.pic);
            scanBtn = itemView.findViewById(R.id.scanBtn);
        }
    }
}

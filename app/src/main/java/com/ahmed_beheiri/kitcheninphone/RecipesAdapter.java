package com.ahmed_beheiri.kitcheninphone;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder> {

    private ArrayList<Recipes> recipesList;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public RecipesAdapter(ArrayList<Recipes> recipesList, Context context, OnItemClickListener listener) {
        this.recipesList = recipesList;
        this.context = context;
        this.listener = listener;
    }


    @Override
    public RecipesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.food_list_item, parent, false);
        return new RecipesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipesViewHolder holder, int position) {

        Recipes recipes = recipesList.get(position);
        Picasso.with(getContext()).load(recipes.getImageurl()).into(holder.recipeImage);
        holder.vote.setText(String.valueOf(recipes.getRate()));
        holder.Recipetitle.setText(recipes.getTitle());
    }

    @Override
    public int getItemCount() {
        return recipesList.size();
    }

    private Context getContext() {
        return this.context;
    }


    public class RecipesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView recipeImage;
        public TextView vote;
        public TextView Recipetitle;

        public RecipesViewHolder(View itemView) {
            super(itemView);
            recipeImage = (ImageView) itemView.findViewById(R.id.recipe_image);
            vote = (TextView) itemView.findViewById(R.id.votes);
            Recipetitle = (TextView) itemView.findViewById(R.id.title);

            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            listener.onItemClick(v, clickedPosition);

        }

    }

}

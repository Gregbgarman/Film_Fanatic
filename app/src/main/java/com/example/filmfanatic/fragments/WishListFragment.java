package com.example.filmfanatic.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.filmfanatic.Film;
import com.example.filmfanatic.R;
import com.example.filmfanatic.WishListAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class WishListFragment extends Fragment {

    public static List<Film> films;
    private RecyclerView rvWishList;
    public static WishListAdapter wishListAdapter;
    private int WishListCount;

    public WishListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (HomeFragment.WishListCount>0) {
            rvWishList = view.findViewById(R.id.rvWishList);
            films = new ArrayList<>();
            wishListAdapter = new WishListAdapter(getContext(), films);
            rvWishList.setAdapter(wishListAdapter);
            rvWishList.setLayoutManager(new LinearLayoutManager(getContext()));
            queryWishList();
        }

    }

    public void queryWishList(){                                    //querying user's wishlist of movies
        ParseQuery<Film> query=ParseQuery.getQuery(Film.class);
        query.include(Film.KEY_USER);
        query.whereEqualTo(Film.KEY_USER, ParseUser.getCurrentUser());
        query.addDescendingOrder(Film.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Film>() {
            @Override
            public void done(List<Film> thefilms, ParseException e) {
                if (e!=null){
                    return;
                }

                films.addAll(thefilms);
                wishListAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (HomeFragment.WishListCount==0)
            return inflater.inflate(R.layout.empty_wishlist, container, false);

        return inflater.inflate(R.layout.fragment_wish_list, container, false);
    }
}
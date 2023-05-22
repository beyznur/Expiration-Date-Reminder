package com.beyzanur.expiration_date_reminder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    View v;
    private RecyclerView myrecyclerview;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference;
    private RecyclerView toolbar_recycler;
    private List<Categories> lstCategories;
    private  ArrayList<Products> productsArrayList;

    private SearchProductAdapter searchProductAdapter;
    ImageView search_ic, profile_ic;
    TextView tittle;
    EditText searchtext;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v;
        v = inflater.inflate(R.layout.fragment_home, container, false);
        tittle= v.findViewById(R.id.title);
        searchtext= v.findViewById(R.id.search_bar);
        productsArrayList = new ArrayList<>();



        collectionReference = db.collection("users"). document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("urunler");

        toolbar_recycler=v.findViewById(R.id.toolbar_recycler_view);
        toolbar_recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        myrecyclerview = (RecyclerView) v.findViewById(R.id.recView);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        HomeFragAdapter recyclerAdapter = new HomeFragAdapter(getContext(), lstCategories);
        myrecyclerview.setAdapter(recyclerAdapter);
        readProduct();

        searchProductAdapter = new SearchProductAdapter(getContext(),productsArrayList);
        toolbar_recycler.setAdapter(searchProductAdapter);

        Toolbar toolbar = v.findViewById(R.id.toolbar);


        search_ic = v.findViewById(R.id.search);
        search_ic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tittle.setVisibility(View.GONE);
                searchtext.setVisibility(View.VISIBLE);
               searchtext.requestFocus();
                toolbar_recycler.setVisibility(View.VISIBLE);
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
               imm.showSoftInput(searchtext, InputMethodManager.SHOW_IMPLICIT);
                myrecyclerview.setVisibility(View.GONE);
            }
        });

        profile_ic=v.findViewById(R.id.profile);
        profile_ic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(),ProfileActivity.class);
                startActivity(intent);
            }
        });
        searchtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchProduct(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return v;

    }




    private void readProduct(){
        productsArrayList.clear();
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        String name = (String) document.getData().get("productName");
                        String date = (String) document.getData().get("productDate");
                        Products products = new Products(name,date);
                        productsArrayList.add(products);


                    }
                    searchProductAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    private void searchProduct(String searchTerm) {
        collectionReference.whereGreaterThanOrEqualTo("productName", searchTerm)
                .whereLessThanOrEqualTo("productName", searchTerm + "\uf8ff")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        productsArrayList.clear();

                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            String productName = document.getString("productName");
                            String productDate = document.getString("productDate");
                            Products product = new Products(productName,productDate);
                            productsArrayList.add(product);
                        }

                        searchProductAdapter.notifyDataSetChanged();
                    }
                });
    }




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lstCategories = new ArrayList<>();
        lstCategories.add(new Categories("FRUIT", R.drawable.fruit));
        lstCategories.add(new Categories("VEGETABLE", R.drawable.vegetables));
        lstCategories.add(new Categories("BEVERAGE", R.drawable.beverage));
        lstCategories.add(new Categories("DESSERT", R.drawable.dessert));
        lstCategories.add(new Categories("MEDICINE", R.drawable.medicine));
        lstCategories.add(new Categories("OTHER", R.drawable.others));


    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (searchtext.getVisibility() == View.VISIBLE) {
                    searchtext.setVisibility(View.GONE);
                    toolbar_recycler.setVisibility(View.GONE);
                    tittle.setVisibility(View.VISIBLE);
                    myrecyclerview.setVisibility(View.VISIBLE);

                    InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchtext.getWindowToken(), 0);

                } else {

                    requireActivity().onBackPressed();
                }
            }
        });
    }

}
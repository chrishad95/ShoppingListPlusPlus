package com.udacity.firebase.shoppinglistplusplus.ui.activeLists;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.model.ShoppingList;
import com.udacity.firebase.shoppinglistplusplus.ui.activeListDetails.ActiveListDetailsActivity;
import com.udacity.firebase.shoppinglistplusplus.ui.activeListDetails.ShoppingListAdapter;
import com.udacity.firebase.shoppinglistplusplus.utils.Constants;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass that shows a list of all shopping lists a user can see.
 * Use the {@link ShoppingListsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingListsFragment extends Fragment {

    private static final String TAG = "shopping-list-fragment";

    private ListView mListView;
    private ActiveListAdapter mShoppingListAdapter;

    // firebase stuff
    private DatabaseReference mFirebaseListsReference;


    public ShoppingListsFragment() {
        /* Required empty public constructor */
    }

    /**
     * Create fragment and pass bundle with data as it's arguments
     * Right now there are not arguments...but eventually there will be.
     */
    public static ShoppingListsFragment newInstance() {
        ShoppingListsFragment fragment = new ShoppingListsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Initialize instance variables with data from bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /**
         * Initalize UI elements
         */
        View rootView = inflater.inflate(R.layout.fragment_shopping_lists, container, false);
        initializeScreen(rootView);

        mFirebaseListsReference = FirebaseDatabase.getInstance().getReference().child("lists");

        mShoppingListAdapter = new ActiveListAdapter(getActivity(), ShoppingList.class, R.layout.single_active_list, mFirebaseListsReference);
        mListView.setAdapter(mShoppingListAdapter);


        mFirebaseListsReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ShoppingList shoppingList = dataSnapshot.getValue(ShoppingList.class);
                //mShoppingListAdapter.add(shoppingList);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                ShoppingList shoppingList = dataSnapshot.getValue(ShoppingList.class);
                Log.d(TAG, shoppingList.getKey() + " has changed.");
                int shoppingListIndex = -1;
                for(int i=0; i<shoppingLists.size(); i++) {
                    if (shoppingLists.get(i).getKey() == shoppingList.getKey()) {
                        shoppingListIndex = i;
                    }
                }

                if (shoppingListIndex >= 0) {
                    shoppingLists.set(shoppingListIndex, shoppingList);
                }
                mShoppingListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        /**
         * Set interactive bits, such as click events and adapters
         */
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShoppingList shoppingList = shoppingLists.get(position);

                Intent intent = new Intent(getActivity(), ActiveListDetailsActivity.class);
                intent.putExtra(Constants.EXTRA_LIST_KEY, shoppingList.getKey());
                startActivity(intent);

            }
        });

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /**
     * Link layout elements from XML
     */
    private void initializeScreen(View rootView) {
        mListView = (ListView) rootView.findViewById(R.id.list_view_active_lists);
    }
}

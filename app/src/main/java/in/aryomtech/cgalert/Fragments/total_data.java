package in.aryomtech.cgalert.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shreyaspatil.firebase.recyclerpagination.DatabasePagingOptions;
import com.shreyaspatil.firebase.recyclerpagination.FirebaseRecyclerPagingAdapter;
import com.shreyaspatil.firebase.recyclerpagination.LoadingState;

import in.aryomtech.cgalert.Fragments.holder.Excel_holder;
import in.aryomtech.cgalert.Fragments.model.Excel_data;
import in.aryomtech.cgalert.R;

import io.michaelrocks.paranoid.Obfuscate;

@Obfuscate
public class total_data extends Fragment {

    View view;
    private Context contextNullSafe;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    DatabaseReference mDatabase;
    FirebaseRecyclerPagingAdapter<Excel_data, Excel_holder> mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_total_data, container, false);
        if (contextNullSafe == null) getContextNullSafety();

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        //Initialize RecyclerView
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager mManager = new LinearLayoutManager(getContextNullSafety());
        mRecyclerView.setLayoutManager(mManager);

        //Initialize Database
        mDatabase = FirebaseDatabase.getInstance().getReference().child("data");
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(5)
                .setPageSize(10)
                .build();

        DatabasePagingOptions<Excel_data> options = new DatabasePagingOptions.Builder<Excel_data>()
                .setLifecycleOwner(this)
                .setQuery(mDatabase, config, Excel_data.class)
                .build();

        //Initialize Adapter
        mAdapter = new FirebaseRecyclerPagingAdapter<Excel_data, Excel_holder>(options) {
            @NonNull
            @Override
            public Excel_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new Excel_holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.total_card, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull Excel_holder holder,
                                            int position,
                                            @NonNull Excel_data model) {
                    holder.setItem(model,getContextNullSafety());
            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                switch (state) {
                    case LOADING_INITIAL:
                    case LOADING_MORE:
                        // Do your loading animation
                        mSwipeRefreshLayout.setRefreshing(true);
                        break;

                    case LOADED:

                    case FINISHED:
                        //Reached end of Data set
                        // Stop Animation
                        mSwipeRefreshLayout.setRefreshing(false);
                        break;

                    case ERROR:
                        retry();
                        break;
                }
            }

            @Override
            protected void onError(@NonNull DatabaseError databaseError) {
                super.onError(databaseError);
                mSwipeRefreshLayout.setRefreshing(false);
                databaseError.toException().printStackTrace();
            }
        };

        //Set Adapter to RecyclerView
        mRecyclerView.setAdapter(mAdapter);

        //Set listener to SwipeRefreshLayout for refresh action
        mSwipeRefreshLayout.setOnRefreshListener(() -> mAdapter.refresh());

        return view;
    }

    /**CALL THIS IF YOU NEED CONTEXT*/
    public Context getContextNullSafety() {
        if (getContext() != null) return getContext();
        if (getActivity() != null) return getActivity();
        if (contextNullSafe != null) return contextNullSafe;
        if (getView() != null && getView().getContext() != null) return getView().getContext();
        if (requireContext() != null) return requireContext();
        if (requireActivity() != null) return requireActivity();
        if (requireView() != null && requireView().getContext() != null)
            return requireView().getContext();

        return null;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        contextNullSafe = context;
    }

    //Start Listening Adapter
    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    //Stop Listening Adapter
    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}
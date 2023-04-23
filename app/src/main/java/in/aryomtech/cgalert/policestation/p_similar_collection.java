package in.aryomtech.cgalert.policestation;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import in.aryomtech.cgalert.Fragments.Adapter.similarAdapter;
import in.aryomtech.cgalert.Fragments.model.Excel_data;
import in.aryomtech.cgalert.Fragments.model.filterdata;
import in.aryomtech.cgalert.R;

import io.michaelrocks.paranoid.Obfuscate;

@Obfuscate
public class p_similar_collection extends Fragment {

    View view;
    private Context contextNullSafe;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    Query query;
    ImageView cg_logo;
    TextView no_data;
    List<Excel_data> excel_data=new ArrayList<>();
    List<Excel_data> excel_data_duplicates=new ArrayList<>();
    List<filterdata> filtered_mylist=new ArrayList<>();
    EditText search;
    int onback=0;
    String stat_name;
    List<filterdata> save_locally_list=new ArrayList<>();
    List<String> filtered_data=new ArrayList<>();
    List<String> joined_list=new ArrayList<>();
    List<String> station_dist=new ArrayList<>();
    List<String> filtered_station_dist=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_p_similar_collection, container, false);
        if (contextNullSafe == null) getContextNullSafety();
        //Hide the keyboard
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        search=view.findViewById(R.id.search);
        cg_logo=view.findViewById(R.id.imageView3);
        no_data=view.findViewById(R.id.no_data);
        //Initialize RecyclerView
        mRecyclerView = view.findViewById(R.id.recycler_view);
        LinearLayoutManager mManager = new LinearLayoutManager(getContextNullSafety());
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mRecyclerView.setLayoutManager(mManager);
        //adapter
        stat_name= getContextNullSafety().getSharedPreferences("station_name_K",Context.MODE_PRIVATE)
                .getString("the_station_name2003","");
        //Initialize Database
        query = FirebaseDatabase.getInstance().getReference().child("data").orderByChild("type").equalTo("RM CALL");
        getdata();
        //Set listener to SwipeRefreshLayout for refresh action
        mSwipeRefreshLayout.setOnRefreshListener(this::getdata);
        search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search(s+"");
            }
        });

        OnBackPressedCallback callback=new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                ((FragmentActivity) getContextNullSafety()).finish();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),callback);


        return view;
    }
    private void search(String str) {
        filtered_mylist.clear();
        for(filterdata object:save_locally_list){
            if (object.getCn().toLowerCase().contains(str.toLowerCase().trim())) {
                filtered_mylist.add(object);
            } else if (object.getYear().toLowerCase().contains(str.toLowerCase().trim())) {
                filtered_mylist.add(object);
            } else if (object.getStn().toLowerCase().contains(str.toLowerCase().trim())) {
                filtered_mylist.add(object);
            }
            else if(object.getDis_n().toLowerCase().contains(str.toLowerCase().trim())){
                filtered_mylist.add(object);
            }

        }
        similarAdapter similarAdapter=new similarAdapter(getContextNullSafety(),filtered_mylist,excel_data_duplicates);
        similarAdapter.notifyDataSetChanged();
        if(mRecyclerView!=null)
            mRecyclerView.setAdapter(similarAdapter);
        //adapter
    }
    private void getdata() {
        cg_logo.setVisibility(View.VISIBLE);
        no_data.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                excel_data.clear();
                joined_list.clear();
                excel_data_duplicates.clear();
                save_locally_list.clear();
                filtered_data.clear();
                station_dist.clear();
                filtered_station_dist.clear();
                for(DataSnapshot ds:snapshot.getChildren()) {
                    if (snapshot.child(ds.getKey()).child("B").getValue(String.class) != null) {
                        if (snapshot.child(ds.getKey()).child("B").getValue(String.class).toUpperCase().equals(stat_name.substring(3))) {
                            excel_data.add(snapshot.child(Objects.requireNonNull(ds.getKey())).getValue(Excel_data.class));
                            joined_list.add(excel_data.get(excel_data.size() - 1).getHh().trim() + " " + excel_data.get(excel_data.size() - 1).getIi().trim() + "=" + excel_data.get(excel_data.size() - 1).getBb().toUpperCase().trim() + " " + excel_data.get(excel_data.size() - 1).getCc().toUpperCase().trim());
                            station_dist.add(excel_data.get(excel_data.size() - 1).getBb().toUpperCase().trim() + " " + excel_data.get(excel_data.size() - 1).getCc().toUpperCase().trim());
                        }
                    }
                }
                mSwipeRefreshLayout.setRefreshing(false);
                Collections.reverse(excel_data);
                Collections.reverse(joined_list);
                Collections.reverse(station_dist);
                //filtering data
                for(int i=0;i<joined_list.size();i++){//n
                    if(Collections.frequency(joined_list,joined_list.get(i))>1){ //n   7292 = 7
                        excel_data_duplicates.add(excel_data.get(i));
                        if(!filtered_data.contains(joined_list.get(i))){//n
                            filtered_data.add(joined_list.get(i)); // n
                            filtered_station_dist.add(station_dist.get(i));
                            remove_spaces_and_store(filtered_data,filtered_station_dist);
                        }
                    }
                }
                //Log.e("data_excel",excel_data_duplicates.get(1).getH()+"");
                Log.e("filtered data",filtered_data+"");
                Log.e("station list",filtered_station_dist+"");
                if(excel_data_duplicates.size()!=0){
                    cg_logo.setVisibility(View.GONE);
                    no_data.setVisibility(View.GONE);
                }
                similarAdapter similarAdapter=new similarAdapter(getContextNullSafety(),save_locally_list,excel_data_duplicates);
                similarAdapter.notifyDataSetChanged();
                if(mRecyclerView!=null)
                    mRecyclerView.setAdapter(similarAdapter);
                //adapter
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void remove_spaces_and_store(List<String> filtered_data,List<String> filtered_station_dist) {
        String case_no="",year="",station_name="",district="";
        for(int i=0;i<filtered_data.size();i++){
            String str=filtered_data.get(i);
            String stat_dist=filtered_station_dist.get(i);
            int temp_j=0;
            for(int j=0;j<str.length();j++) {
                if(' '==str.charAt(j)){
                    case_no = str.substring(0,j);
                    temp_j=j;
                }
                if('='==str.charAt(j)){
                    year=str.substring(temp_j,j);
                    break;
                }
            }
            for(int j=0;j<stat_dist.length();j++) {
                if(' '==stat_dist.charAt(j)){
                    station_name= stat_dist.substring(0,j);
                    district=stat_dist.substring(j+1);
                    break;
                }
            }
        }
        save_locally_list.add(new filterdata(case_no,year,station_name,district));

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
}
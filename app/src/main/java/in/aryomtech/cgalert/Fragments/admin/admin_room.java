package in.aryomtech.cgalert.Fragments.admin;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import in.aryomtech.cgalert.Fragments.Adapter.admin_room_Adapter;
import in.aryomtech.cgalert.Fragments.model.Excel_data;
import in.aryomtech.cgalert.R;

import io.michaelrocks.paranoid.Obfuscate;

@Obfuscate
public class admin_room extends Fragment {

    View view;
    RecyclerView recyclerView;
    View menu_;
    private Context contextNullSafe;
    List<String> the_not_list,the_not_sms_list
            ,the_return_not_list,the_return_not_sms_list
            ,the_pending_call_not_list,the_pending_call_not_sms_list
            ,the_pending_return_not_list,the_pending_return_not_sms_list
            ,the_showing_call_not_list,the_showing_call_not_sms_list
            ,the_showing_return_not_list,the_showing_return_not_sms_list
            ,the_today_not_list,the_today_not_sms_list
            ,the_urgent_not_list,the_urgent_not_sms_list;
    ImageView imageView, back;
    DatabaseReference reference;
    List<Excel_data> list=new ArrayList<>();
    List<String> main_not_list=new ArrayList<>();
    List<String> main_not_sms_list=new ArrayList<>();
    List<String> main_list=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_admin_room, container, false);
        if (contextNullSafe == null) getContextNullSafety();

        reference= FirebaseDatabase.getInstance().getReference().child("data");
        recyclerView=view.findViewById(R.id.recycler_view);
        imageView=view.findViewById(R.id.imageView);
        menu_=view.findViewById(R.id.imageView4);

        LinearLayoutManager mManager = new LinearLayoutManager(getContextNullSafety());
        recyclerView.setItemViewCacheSize(500);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setLayoutManager(mManager);
        back = view.findViewById(R.id.back);


        back.setOnClickListener(v->{
            assert getFragmentManager() != null;
            getFragmentManager().beginTransaction().remove(admin_room.this).commit();
        });

        String the_call_not=getContextNullSafety().getSharedPreferences("saving_RM_Call_not_noti",Context.MODE_PRIVATE)
                .getString("RM_CALL_list","");
        String the_call_not_sms=getContextNullSafety().getSharedPreferences("saving_RM_Call_not_sms",Context.MODE_PRIVATE)
                .getString("RM_CALL_list","");

        String the_return_not=getContextNullSafety().getSharedPreferences("saving_RM_return_not_noti",Context.MODE_PRIVATE)
                .getString("RM_return_list","");
        String the_return_not_sms=getContextNullSafety().getSharedPreferences("saving_RM_return_not_sms",Context.MODE_PRIVATE)
                .getString("RM_return_list","");

        String the_pending_call_not=getContextNullSafety().getSharedPreferences("saving_RM_pending_call_not_noti",Context.MODE_PRIVATE)
                .getString("RM_pending_call_list","");
        String the_pending_call_not_sms=getContextNullSafety().getSharedPreferences("saving_RM_pending_call_not_sms",Context.MODE_PRIVATE)
                .getString("RM_pending_call_list","");

        String the_pending_return_not=getContextNullSafety().getSharedPreferences("saving_RM_pending_return_not_noti",Context.MODE_PRIVATE)
                .getString("RM_pending_return_list","");
        String the_pending_return_not_sms=getContextNullSafety().getSharedPreferences("saving_RM_pending_return_not_sms",Context.MODE_PRIVATE)
                .getString("RM_pending_return_list","");

        String the_showing_call_not=getContextNullSafety().getSharedPreferences("saving_RM_showing_call_not_noti",Context.MODE_PRIVATE)
                .getString("RM_showing_call_list","");
        String the_showing_call_not_sms=getContextNullSafety().getSharedPreferences("saving_RM_showing_call_not_sms",Context.MODE_PRIVATE)
                .getString("RM_showing_call_list","");

        String the_showing_return_not=getContextNullSafety().getSharedPreferences("saving_RM_showing_return_not_noti",Context.MODE_PRIVATE)
                .getString("RM_showing_return_list","");
        String the_showing_return_not_sms=getContextNullSafety().getSharedPreferences("saving_RM_showing_return_not_sms",Context.MODE_PRIVATE)
                .getString("RM_showing_return_list","");

        String the_today_not=getContextNullSafety().getSharedPreferences("saving_RM_today_not_noti",Context.MODE_PRIVATE)
                .getString("RM_today_list","");
        String the_today_not_sms=getContextNullSafety().getSharedPreferences("saving_RM_today_not_sms",Context.MODE_PRIVATE)
                .getString("RM_today_list","");

        String the_urgent_not=getContextNullSafety().getSharedPreferences("saving_RM_urgent_not_noti",Context.MODE_PRIVATE)
                .getString("RM_urgent_list","");
        String the_urgent_not_sms=getContextNullSafety().getSharedPreferences("saving_RM_urgent_not_sms",Context.MODE_PRIVATE)
                .getString("RM_urgent_list","");

        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        the_not_list =  gson.fromJson(the_call_not, type);
        the_not_sms_list =  gson.fromJson(the_call_not_sms, type);

        the_return_not_list =  gson.fromJson(the_return_not, type);
        the_return_not_sms_list =  gson.fromJson(the_return_not_sms, type);

        the_pending_call_not_list =  gson.fromJson(the_pending_call_not, type);
        the_pending_call_not_sms_list =  gson.fromJson(the_pending_call_not_sms, type);

        the_pending_return_not_list =  gson.fromJson(the_pending_return_not, type);
        the_pending_return_not_sms_list =  gson.fromJson(the_pending_return_not_sms, type);

        the_showing_call_not_list =  gson.fromJson(the_showing_call_not, type);
        the_showing_call_not_sms_list =  gson.fromJson(the_showing_call_not_sms, type);

        the_showing_return_not_list =  gson.fromJson(the_showing_return_not, type);
        the_showing_return_not_sms_list =  gson.fromJson(the_showing_return_not_sms, type);

        the_today_not_list =  gson.fromJson(the_today_not, type);
        the_today_not_sms_list =  gson.fromJson(the_today_not_sms, type);

        the_urgent_not_list =  gson.fromJson(the_urgent_not, type);
        the_urgent_not_sms_list =  gson.fromJson(the_urgent_not_sms, type);

        if(the_not_list!=null)
            main_not_list.addAll(the_not_list);
        if(the_not_sms_list!=null)
            main_not_sms_list.addAll(the_not_sms_list);
        if(the_return_not_list!=null)
            main_not_list.addAll(the_return_not_list);
        if(the_return_not_sms_list!=null)
            main_not_sms_list.addAll(the_return_not_sms_list);
        if(the_pending_call_not_list!=null)
            main_not_list.addAll(the_pending_call_not_list);
        if(the_pending_call_not_sms_list!=null)
            main_not_sms_list.addAll(the_pending_call_not_sms_list);
        if(the_pending_return_not_list!=null)
            main_not_list.addAll(the_pending_return_not_list);
        if(the_pending_return_not_sms_list!=null)
            main_not_sms_list.addAll(the_pending_return_not_sms_list);
        if(the_showing_call_not_list!=null)
            main_not_list.addAll(the_showing_call_not_list);
        if(the_showing_call_not_sms_list!=null)
            main_not_sms_list.addAll(the_showing_call_not_sms_list);
        if(the_showing_return_not_list!=null)
            main_not_list.addAll(the_showing_return_not_list);
        if(the_showing_return_not_sms_list!=null)
            main_not_sms_list.addAll(the_showing_return_not_sms_list);
        if(the_today_not_list!=null)
            main_not_list.addAll(the_today_not_list);
        if(the_today_not_sms_list!=null)
            main_not_sms_list.addAll(the_today_not_sms_list);
        if(the_urgent_not_list!=null)
            main_not_list.addAll(the_urgent_not_list);
        if(the_urgent_not_sms_list!=null)
            main_not_sms_list.addAll(the_urgent_not_sms_list);


        Log.e("size_total",main_not_list+"");
        Log.e("size_total",main_not_sms_list+"");
        imageView.setVisibility(View.VISIBLE);
        final PopupMenu dropDownMenu = new PopupMenu(getContext(), menu_);

        final Menu menu = dropDownMenu.getMenu();
        // add your items:
        menu.add(0, 0, 0, "Not Notified");
        menu.add(0, 1, 0, "Number Error");
        menu.add(0, 2, 0, "Clear");
        menu.add(0, 3, 0, "Installation");
        // OR inflate your menu from an XML:
        dropDownMenu.getMenuInflater().inflate(R.menu.layout_menu, menu);
        dropDownMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case 0:
                    // item ID 0 was clicked
                    imageView.setVisibility(View.VISIBLE);
                    get_data(main_not_list,"Not notified");
                    return true;
                case 1:
                    // item ID 1 was clicked
                    imageView.setVisibility(View.VISIBLE);
                    get_data(main_not_sms_list,"Number not found");
                    return true;
                case 2:
                    Dialog dialog = new Dialog(getContext());
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.dialog_for_sure);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    TextView cancel=dialog.findViewById(R.id.textView96);
                    TextView yes=dialog.findViewById(R.id.textView95);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.show();
                    cancel.setOnClickListener(vi-> dialog.dismiss());
                    yes.setOnClickListener(vi-> {
                       clear_all();
                       admin_room_Adapter admin_room_adapter=new admin_room_Adapter(getContextNullSafety(),"",list);
                       if(recyclerView!=null)
                            recyclerView.setAdapter(admin_room_adapter);
                       admin_room_adapter.notifyDataSetChanged();
                       dialog.dismiss();
                    });
                    return true;
                case 3:
                    ((FragmentActivity) getContext()).getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations( R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left, R.anim.exit_to_right)
                            .add(R.id.inst_cont,new Installation())
                            .addToBackStack(null)
                            .commit();
                    return true;
            }
            return false;
        });

        menu_.setOnClickListener(v-> dropDownMenu.show());
        main_list.addAll(main_not_list);
        main_list.addAll(main_not_sms_list);

        get_data(main_list,"");

        OnBackPressedCallback callback=new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager fm=((FragmentActivity) getContextNullSafety()).getSupportFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                if(fm.getBackStackEntryCount()>0) {
                    fm.popBackStack();
                }
                ft.commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),callback);

        return view;
    }



    private void clear_all() {
        if(main_list!=null)
            main_list.clear();
        if(main_not_list!=null)
            main_not_list.clear();
        if(main_not_sms_list!=null)
            main_not_sms_list.clear();
        if(list!=null)
            list.clear();
        if(the_not_list!=null)
            the_not_list.clear();
        if(the_not_sms_list!=null)
            the_not_sms_list.clear();
        if(the_return_not_list!=null)
            the_return_not_list.clear();
        if(the_return_not_sms_list!=null)
            the_return_not_sms_list.clear();
        if(the_pending_call_not_list!=null)
            the_pending_call_not_list.clear();
        if(the_pending_call_not_sms_list!=null)
            the_pending_call_not_sms_list.clear();
        if(the_pending_return_not_list!=null)
            the_pending_return_not_list.clear();
        if(the_pending_return_not_sms_list!=null)
            the_pending_return_not_sms_list.clear();
        if(the_showing_call_not_list!=null)
            the_showing_call_not_list.clear();
        if(the_showing_call_not_sms_list!=null)
            the_showing_call_not_sms_list.clear();
        if(the_showing_return_not_list!=null)
            the_showing_return_not_list.clear();
        if(the_showing_return_not_sms_list!=null)
            the_showing_return_not_sms_list.clear();
        if(the_today_not_list!=null)
            the_today_not_list.clear();
        if(the_today_not_sms_list!=null)
            the_today_not_sms_list.clear();
        if(the_urgent_not_list!=null)
            the_urgent_not_list.clear();
        if(the_urgent_not_sms_list!=null)
            the_urgent_not_sms_list.clear();

        getContextNullSafety().getSharedPreferences("saving_RM_Call_not_noti",Context.MODE_PRIVATE).edit()
                .putString("RM_CALL_list","").apply();
        getContextNullSafety().getSharedPreferences("saving_RM_Call_not_sms",Context.MODE_PRIVATE).edit()
                .putString("RM_CALL_list","").apply();

        getContextNullSafety().getSharedPreferences("saving_RM_return_not_noti",Context.MODE_PRIVATE).edit()
                .putString("RM_return_list","").apply();
        getContextNullSafety().getSharedPreferences("saving_RM_return_not_sms",Context.MODE_PRIVATE).edit()
                .putString("RM_return_list","").apply();

        getContextNullSafety().getSharedPreferences("saving_RM_pending_call_not_noti",Context.MODE_PRIVATE).edit()
                .putString("RM_pending_call_list","").apply();
        getContextNullSafety().getSharedPreferences("saving_RM_pending_call_not_sms",Context.MODE_PRIVATE).edit()
                .putString("RM_pending_call_list","").apply();

        getContextNullSafety().getSharedPreferences("saving_RM_pending_return_not_noti",Context.MODE_PRIVATE).edit()
                .putString("RM_pending_return_list","").apply();
        getContextNullSafety().getSharedPreferences("saving_RM_pending_return_not_sms",Context.MODE_PRIVATE).edit()
                .putString("RM_pending_return_list","").apply();

        getContextNullSafety().getSharedPreferences("saving_RM_showing_call_not_noti",Context.MODE_PRIVATE).edit()
                .putString("RM_showing_call_list","").apply();
        getContextNullSafety().getSharedPreferences("saving_RM_showing_call_not_sms",Context.MODE_PRIVATE).edit()
                .putString("RM_showing_call_list","").apply();

        getContextNullSafety().getSharedPreferences("saving_RM_showing_return_not_noti",Context.MODE_PRIVATE).edit()
                .putString("RM_showing_return_list","").apply();
        getContextNullSafety().getSharedPreferences("saving_RM_showing_return_not_sms",Context.MODE_PRIVATE).edit()
                .putString("RM_showing_return_list","").apply();

        getContextNullSafety().getSharedPreferences("saving_RM_today_not_noti",Context.MODE_PRIVATE).edit()
                .putString("RM_today_list","").apply();
        getContextNullSafety().getSharedPreferences("saving_RM_today_not_sms",Context.MODE_PRIVATE).edit()
                .putString("RM_today_list","").apply();

        getContextNullSafety().getSharedPreferences("saving_RM_urgent_not_noti",Context.MODE_PRIVATE).edit()
                .putString("RM_urgent_list","").apply();
        getContextNullSafety().getSharedPreferences("saving_RM_urgent_not_sms",Context.MODE_PRIVATE).edit()
                .putString("RM_urgent_list","").apply();

        Toast.makeText(getContextNullSafety(), "Data Cleared", Toast.LENGTH_SHORT).show();
    }

    private void get_data(List<String> keys_list,String error) {
        list.clear();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e("list size 0:", keys_list.size() + "");
                for(int i=0;i<keys_list.size();i++){
                    imageView.setVisibility(View.GONE);
                    if(snapshot.child(keys_list.get(i)).exists())
                        list.add(snapshot.child(keys_list.get(i)).getValue(Excel_data.class));
                    else
                        keys_list.remove(i);
                }
                Log.e("list size 1:", keys_list.size() + "");
                Log.e("asdasd", list + "");
                admin_room_Adapter admin_room_adapter = new admin_room_Adapter(getContextNullSafety(), error, list);
                if (recyclerView != null)
                    recyclerView.setAdapter(admin_room_adapter);
                admin_room_adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }


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
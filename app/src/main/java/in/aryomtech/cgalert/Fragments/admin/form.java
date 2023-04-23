package in.aryomtech.cgalert.Fragments.admin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import in.aryomtech.cgalert.Fragments.model.Excel_data;
import in.aryomtech.cgalert.R;
import io.michaelrocks.paranoid.Obfuscate;

@Obfuscate
public class form extends Fragment {

    View view;
    TextView submit_txt,diary_re_txt;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    TextView rm,before,diary;
    ImageView back,put_number;
    int check_;
    int c;
    FirebaseUser user;
    private Context contextNullSafe;
    EditText case_no_edt,name_edt,case_year_edt,crime_no_edt,crime_year_edt;
    CheckBox checkBox_RM_call,checkBox_RM_return;
    List<String> district,ps_list;
    String rm_Date,fd_dot;
    ConstraintLayout lay;
    Dialog dialog1;
    Excel_data excel_data;
    AutoCompleteTextView ac_district,policeStation,ac_caseType;
    DatabaseReference reference,reference_phone,gs_ref,ref_users;
    String pushkey,gsID="";
    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.activity_form, container, false);
        if (contextNullSafe == null) getContextNullSafety();
        district =new ArrayList<>();
        ps_list=new ArrayList<>();
        user=FirebaseAuth.getInstance().getCurrentUser();
        if(getArguments()!=null){
            excel_data= (Excel_data) getArguments().getSerializable("excel_data_sending");
        }
        db = FirebaseFirestore.getInstance();
        disableSSLCertificateChecking();
        ac_district = view.findViewById(R.id.ac_district);
        put_number = view.findViewById(R.id.put_number);
        diary_re_txt = view.findViewById(R.id.diary_re_txt);
        policeStation = view.findViewById(R.id.policeStation);
        ac_caseType = view.findViewById(R.id.ac_case_type);
        case_no_edt=view.findViewById(R.id.case_no_edt);
        name_edt=view.findViewById(R.id.name_edt);
        case_year_edt=view.findViewById(R.id.case_year_edt);
        crime_no_edt=view.findViewById(R.id.crime_no_edt);
        crime_year_edt=view.findViewById(R.id.crime_year_edt);
        checkBox_RM_call=view.findViewById(R.id.checkBox_RM_call);
        checkBox_RM_return=view.findViewById(R.id.checkBox_RM_return);
        submit_txt=view.findViewById(R.id.submit_txt);
        lay=view.findViewById(R.id.lay);
        back=view.findViewById(R.id.imageView4);
        back.setOnClickListener(v-> back());

        String[] caseType = {"CRA","CRR","MCRC", "MCRCA"};
        //Creating the instance of ArrayAdapter containing list of language names
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>
                (getContextNullSafety(), android.R.layout.select_dialog_item, caseType);
        //Getting the instance of AutoCompleteTextView
        ac_caseType.setThreshold(1);//will start working from first character
        ac_caseType.setAdapter(adapter1);//setting the adapter data into the AutoCompleteTextView
        ac_caseType.setTextColor(Color.RED);

        reference = FirebaseDatabase.getInstance().getReference().child("data");
        ref_users = FirebaseDatabase.getInstance().getReference().child("users");
        gs_ref = FirebaseDatabase.getInstance().getReference().child("gskey");
        reference_phone = FirebaseDatabase.getInstance().getReference().child("Phone numbers");
        get_districts_phone();
        rm=view.findViewById(R.id.rm_date);
        diary=view.findViewById(R.id.diary);
        before=view.findViewById(R.id.before);
        rm.setOnClickListener(v->{

            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    getActivity(),
                    mDateSetListener,
                    year,month,day);
            check_=0;
            dialog.show();
        });
        diary.setOnClickListener(v->{

            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    getActivity(),
                    mDateSetListener,
                    year,month,day);
            check_=1;
            dialog.show();
        });
        before.setOnClickListener(v->{

            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    getActivity(),
                    mDateSetListener,
                    year,month,day);
            check_=2;
            dialog.show();
        });
        mDateSetListener = (datePicker, year, month, day) -> {

            String d=String.valueOf(day);
            String m=String.valueOf(month+1);
            Log.e("month",m+"");
            month = month + 1;
            Log.e("month",month+"");
            if(String.valueOf(day).length()==1)
                d="0"+ day;
            if(String.valueOf(month).length()==1)
                m="0"+ month;
            String date = d + "." + m + "." + year;
            if(check_==0) {
                rm.setText(date);
                rm_Date=year+m+d;
                fd_dot=year+"-"+m+"-"+d;
            }
            else if(check_==1)
                diary.setText(date);
            else if(check_==2)
                before.setText(date);
        };
        if(excel_data!=null)
            filling_values();

        ac_district.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                get_police_station(ac_district.getText().toString().trim().replaceAll("[^-()a-zA-Z0-9]", ""));
            }
        });

        checkBox_RM_call.setOnClickListener(v->{
            checkBox_RM_return.setChecked(false);
        });
        checkBox_RM_return.setOnClickListener(v->{
            checkBox_RM_call.setChecked(false);
        });
        gs_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gsID=snapshot.getValue(String.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        submit_txt.setOnClickListener(v->{
            if(!ac_district.getText().toString().trim().equals("")){
                if(!policeStation.getText().toString().trim().equals("")){
                    if(!crime_no_edt.getText().toString().trim().equals("")){
                        if(!crime_year_edt.getText().toString().trim().equals("")){
                            if(!ac_caseType.getText().toString().trim().equals("")){
                                if(!case_no_edt.getText().toString().trim().equals("")){
                                    if(!case_year_edt.getText().toString().trim().equals("")){
                                        if(!name_edt.getText().toString().trim().equals("")){
                                            if(!rm.getText().toString().equals("")){
                                                if(!before.getText().toString().equals("")){
                                                    if(checkBox_RM_call.isChecked()){
                                                        push_to_database_and_excel("RM CALL");
                                                    }
                                                    else if(checkBox_RM_return.isChecked()){
                                                        push_to_database_and_excel("RM RETURN");
                                                    }
                                                    else{
                                                        Snackbar.make(lay,"Please Select Sheet Name.",Snackbar.LENGTH_LONG)
                                                                .setActionTextColor(Color.parseColor("#171746"))
                                                                .setTextColor(Color.parseColor("#FF7F5C"))
                                                                .setBackgroundTint(Color.parseColor("#171746"))
                                                                .show();
                                                    }
                                                }
                                                else{
                                                    before.setError("Empty");
                                                    Snackbar.make(lay,"Please Add Before Date.",Snackbar.LENGTH_LONG)
                                                            .setActionTextColor(Color.parseColor("#171746"))
                                                            .setTextColor(Color.parseColor("#FF7F5C"))
                                                            .setBackgroundTint(Color.parseColor("#171746"))
                                                            .show();
                                                }
                                            }
                                            else{
                                                rm.setError("Empty");
                                                Snackbar.make(lay,"Please Add RM Date.",Snackbar.LENGTH_LONG)
                                                        .setActionTextColor(Color.parseColor("#171746"))
                                                        .setTextColor(Color.parseColor("#FF7F5C"))
                                                        .setBackgroundTint(Color.parseColor("#171746"))
                                                        .show();
                                            }
                                        }
                                        else{
                                            name_edt.setError("Empty");
                                            Snackbar.make(lay,"Please Add Criminal Name.",Snackbar.LENGTH_LONG)
                                                    .setActionTextColor(Color.parseColor("#171746"))
                                                    .setTextColor(Color.parseColor("#FF7F5C"))
                                                    .setBackgroundTint(Color.parseColor("#171746"))
                                                    .show();
                                        }
                                    }
                                    else{
                                        case_year_edt.setError("Empty");
                                        Snackbar.make(lay,"Please Add Case Year.",Snackbar.LENGTH_LONG)
                                                .setActionTextColor(Color.parseColor("#171746"))
                                                .setTextColor(Color.parseColor("#FF7F5C"))
                                                .setBackgroundTint(Color.parseColor("#171746"))
                                                .show();
                                    }
                                }
                                else{
                                    case_no_edt.setError("Empty");
                                    Snackbar.make(lay,"Please Add Case No.",Snackbar.LENGTH_LONG)
                                            .setActionTextColor(Color.parseColor("#171746"))
                                            .setTextColor(Color.parseColor("#FF7F5C"))
                                            .setBackgroundTint(Color.parseColor("#171746"))
                                            .show();
                                }
                            }
                            else{
                                ac_caseType.setError("Empty");
                                Snackbar.make(lay,"Please Add Case Type.",Snackbar.LENGTH_LONG)
                                        .setActionTextColor(Color.parseColor("#171746"))
                                        .setTextColor(Color.parseColor("#FF7F5C"))
                                        .setBackgroundTint(Color.parseColor("#171746"))
                                        .show();
                            }
                        }
                        else{
                            crime_year_edt.setError("Empty");
                            Snackbar.make(lay,"Please Add Crime Year.",Snackbar.LENGTH_LONG)
                                    .setActionTextColor(Color.parseColor("#171746"))
                                    .setTextColor(Color.parseColor("#FF7F5C"))
                                    .setBackgroundTint(Color.parseColor("#171746"))
                                    .show();
                        }
                    }
                    else{
                        crime_no_edt.setError("Empty");
                        Snackbar.make(lay,"Please Add Crime no.",Snackbar.LENGTH_LONG)
                                .setActionTextColor(Color.parseColor("#171746"))
                                .setTextColor(Color.parseColor("#FF7F5C"))
                                .setBackgroundTint(Color.parseColor("#171746"))
                                .show();
                    }
                }
                else{
                    policeStation.setError("Empty");
                    Snackbar.make(lay,"Please Add Police Station.",Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.parseColor("#171746"))
                            .setTextColor(Color.parseColor("#FF7F5C"))
                            .setBackgroundTint(Color.parseColor("#171746"))
                            .show();
                }
            }
            else{
                ac_district.setError("Empty");
                Snackbar.make(lay,"Please Add District.",Snackbar.LENGTH_LONG)
                        .setActionTextColor(Color.parseColor("#171746"))
                        .setTextColor(Color.parseColor("#FF7F5C"))
                        .setBackgroundTint(Color.parseColor("#171746"))
                        .show();
            }
        });
        view.findViewById(R.id.download_txt).setOnClickListener(v->{
            getFileUrl();
        });
     /*   view.findViewById(R.id.put_number).setOnClickListener(v->{
            ((FragmentActivity) getContextNullSafety()).getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations( R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left, R.anim.exit_to_right)
                    .add(R.id.drawer,new Entries())
                    .addToBackStack(null)
                    .commit();
        });*/

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
    private void back(){
        FragmentManager fm=((FragmentActivity) getContextNullSafety()).getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        if(fm.getBackStackEntryCount()>0) {
            fm.popBackStack();
        }
        ft.commit();
    }
    private void filling_values() {
        diary_re_txt.setVisibility(View.VISIBLE);
        diary.setVisibility(View.VISIBLE);

        ac_district.setText(excel_data.getCc());
        policeStation.setText(excel_data.getBb());
        crime_no_edt.setText(excel_data.getHh());
        crime_year_edt.setText(excel_data.getIi());
        ac_caseType.setText(excel_data.getDd());
        case_no_edt.setText(excel_data.getEe());
        case_year_edt.setText(excel_data.getGg());
        case_year_edt.setText(excel_data.getGg());
        name_edt.setText(excel_data.getFf());
        rm.setText(excel_data.getKk());
        String year=excel_data.getKk().substring(6);
        String month=excel_data.getKk().substring(3,5);
        String day=excel_data.getKk().substring(0,2);
        rm_Date=year+month+day;
        fd_dot=year+"-"+month+"-"+day;
        before.setText(excel_data.getLl());
        if(excel_data.getType().equals("RM CALL")){
            checkBox_RM_call.setChecked(true);
            checkBox_RM_return.setChecked(false);
        }
        else{
            checkBox_RM_return.setChecked(true);
            checkBox_RM_call.setChecked(false);
        }
        if(excel_data.getJj().equals("None"))
            diary.setText("");
        else
            diary.setText(excel_data.getJj());
    }

    private void push_to_database_and_excel(String sheet) {
        dialog1 = new Dialog(getContextNullSafety());
        dialog1.setCancelable(false);
        dialog1.setContentView(R.layout.loading_dialog);
        dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        LottieAnimationView lottieAnimationView=dialog1.findViewById(R.id.animate);
        lottieAnimationView.setAnimation("done.json");
        dialog1.show();
        fetch_number(ac_district.getText().toString().toUpperCase().trim(),"PS "+  policeStation.getText().toString().toUpperCase().trim(),sheet);
        /*String pushkey=rm_Date
                       +policeStation.getText().toString().trim()
                       +ac_district.getText().toString().trim()
                       +ac_caseType.getText().toString().trim()
                       +case_no_edt.getText().toString().trim()
                       +case_year_edt.getText().toString().trim()
                       +crime_no_edt.getText().toString().trim()
                       +crime_year_edt.getText().toString().trim();*/

    }

    private void fetch_number(String dis, String ps,String sheet) {
        reference_phone.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(dis).child(ps).exists()){
                    get_uid(snapshot.child(dis).child(ps).getValue(String.class),sheet);
                }
                else{
                    Dialog dialog11 = new Dialog(getContextNullSafety());
                    dialog11.setCancelable(true);
                    dialog11.setContentView(R.layout.number_exist_dialog);
                    dialog11.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    TextView text=dialog11.findViewById(R.id.textView15);
                    text.setText("The number for Distict - "+dis+" and Station "+ps+" does not exist, Please add it first then add the data.");
                    TextView button=dialog11.findViewById(R.id.but);
                    button.setOnClickListener(v->{
                        ((FragmentActivity) getContextNullSafety()).getSupportFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations( R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left, R.anim.exit_to_right)
                                .add(R.id.drawer,new Entries())
                                .addToBackStack(null)
                                .commit();
                        dialog11.dismiss();
                    });
                    dialog1.dismiss();
                    dialog11.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(contextNullSafe, "Database Error...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void get_uid(String number,String sheet) {
        c=0;
        ref_users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    if(snapshot.child(Objects.requireNonNull(ds.getKey())).child(number).exists()){
                        c=1;
                        bundle_data(number,sheet,ds.getKey());
                        break;
                    }
                }
                if(c==0){
                    bundle_data(number,sheet,"");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void bundle_data(String number,String sheet,String uid) {
        if(excel_data==null)
            pushkey=reference.push().getKey();
        else
            pushkey=excel_data.getPushkey();
        Map<String,String> data_packet=new HashMap<>();
        data_packet.put("A","");
        data_packet.put("B",policeStation.getText().toString().trim().toUpperCase().replaceAll("[^-()a-zA-Z0-9]", ""));
        data_packet.put("C",ac_district.getText().toString().trim().toUpperCase().replaceAll("[^-()a-zA-Z0-9]", ""));
        data_packet.put("D",ac_caseType.getText().toString().trim().toUpperCase().replaceAll("[^-()a-zA-Z0-9]", ""));
        data_packet.put("E",case_no_edt.getText().toString().trim().replaceAll("[^-()a-zA-Z0-9]", ""));
        data_packet.put("F",name_edt.getText().toString().trim().toUpperCase().replaceAll("[^-()a-zA-Z0-9]", ""));
        data_packet.put("G",case_year_edt.getText().toString().trim().replaceAll("[^-()a-zA-Z0-9]", ""));
        data_packet.put("H",crime_no_edt.getText().toString().trim().replaceAll("[^-()a-zA-Z0-9]", ""));
        data_packet.put("I",crime_year_edt.getText().toString().trim().replaceAll("[^-()a-zA-Z0-9]", ""));
        data_packet.put("number",number.replaceAll("[^-()a-zA-Z0-9]", ""));
        if(diary.getText().toString().trim().equals(""))
            data_packet.put("J","None");
        else
            data_packet.put("J",diary.getText().toString().trim()+"");
        data_packet.put("K",rm.getText().toString().trim());
        data_packet.put("L",before.getText().toString().trim());
        data_packet.put("date",fd_dot);
        data_packet.put("pushkey",pushkey);
        data_packet.put("type",sheet);
        data_packet.put("uid",uid);
        Log.e("Success","Called "+pushkey);
        if(excel_data==null) {
            update_bulk_excel(sheet, data_packet);
            clear_field();
        }
        else {
            data_packet.put("url", excel_data.getUrl());
            update_J_Excel(sheet, data_packet);
        }
        Snackbar.make(lay,"Initiating data upload to Excel...",Snackbar.LENGTH_LONG)
                .setActionTextColor(Color.parseColor("#171746"))
                .setTextColor(Color.parseColor("#FF7F5C"))
                .setBackgroundTint(Color.parseColor("#171746"))
                .show();
    }

    /*private void update_bulk_excel(String sheet, Map<String, String> data_packet) {

            JSONObject jsonBody = new JSONObject();
            try
            {
                jsonBody.put("ps", policeStation.getText().toString().trim());
                jsonBody.put("nod", ac_district.getText().toString().trim());
                jsonBody.put("ct", ac_caseType.getText().toString().trim());
                jsonBody.put("cn", Integer.parseInt(case_no_edt.getText().toString().trim()));
                jsonBody.put("n", name_edt.getText().toString().trim());
                jsonBody.put("yoc", Integer.parseInt(case_year_edt.getText().toString().trim()));
                jsonBody.put("crno", Integer.parseInt(crime_no_edt.getText().toString().trim()));
                jsonBody.put("yocr", Integer.parseInt(crime_year_edt.getText().toString().trim()));
                jsonBody.put("rmd", rm.getText().toString().trim());
                jsonBody.put("b", before.getText().toString().trim());
                jsonBody.put("subject", sheet);
                jsonBody.put("keygen",hashGenerator(policeStation.getText().toString().trim()+"-"+name_edt.getText().toString().trim()+"-"+case_no_edt.getText().toString().trim()+"-"+crime_no_edt.getText().toString().trim()));

                Log.d("body", "httpCall_collect: "+jsonBody);
            }
            catch (Exception e)
            {
                Log.e("Error","JSON ERROR");
            }

            RequestQueue requestQueue = Volley.newRequestQueue(getContextNullSafety());
            String URL = "https://sangyan.co.in/total_data_push";

            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URL,jsonBody,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // enjoy your response
                            String code=response.optString("code")+"";
                            if(code.equals("202")){
                                reference.child(pushkey).setValue(data_packet);
                                Snackbar.make(lay,"Data Uploaded to Excel.",Snackbar.LENGTH_LONG)
                                        .setActionTextColor(Color.parseColor("#171746"))
                                        .setTextColor(Color.parseColor("#FF7F5C"))
                                        .setBackgroundTint(Color.parseColor("#171746"))
                                        .show();
                                new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog1.dismiss();
                                    }
                                },2000);
                                clear_field();
                            }
                            else{
                                Snackbar.make(lay,"Failed to Upload in Excel.",Snackbar.LENGTH_LONG)
                                        .setActionTextColor(Color.parseColor("#000000"))
                                        .setTextColor(Color.parseColor("#000000"))
                                        .setBackgroundTint(Color.parseColor("#FF5252"))
                                        .show();
                                LottieAnimationView lottieAnimationView=dialog1.findViewById(R.id.animate);
                                lottieAnimationView.setAnimation("error.json");
                                dialog1.show();
                                new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog1.dismiss();
                                    }
                                },2000);
                            }
                            Log.e("BULK code",code+"");
                            Log.e("BULK response",response.toString());
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // enjoy your error status
                    Log.e("Status of code = ","Wrong");
                    Snackbar.make(lay,"Failed to Upload in Excel.",Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.parseColor("#000000"))
                            .setTextColor(Color.parseColor("#000000"))
                            .setBackgroundTint(Color.parseColor("#FF5252"))
                            .show();
                    LottieAnimationView lottieAnimationView=dialog1.findViewById(R.id.animate);
                    lottieAnimationView.setAnimation("error.json");
                    dialog1.show();
                    new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog1.dismiss();
                        }
                    },2000);
                }
            });
            stringRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 15000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 15000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {
                }
            });
            Log.d("string", stringRequest.toString());
            requestQueue.add(stringRequest);


    }*/
    private void update_bulk_excel(String sheet, Map<String, String> data_packet) {

        String URL = "https://script.google.com/macros/s/"
                + gsID+"/exec?ps="+policeStation.getText().toString().toLowerCase().trim()+"&nod="
                +ac_district.getText().toString().toLowerCase().trim()+"&ct="+ac_caseType.getText().toString().toLowerCase().trim()+"&cno="+case_no_edt.getText().toString().toLowerCase().trim()
                +"&n="+name_edt.getText().toString().toLowerCase().trim()+"&yoc="+case_year_edt.getText().toString().toLowerCase().trim()+"&crno="+crime_no_edt.getText().toString().toLowerCase().trim()
                +"&yocr="+crime_year_edt.getText().toString().toLowerCase().trim()+"&rmd="+rm.getText().toString().toLowerCase().trim()+"&b="+before.getText().toString().toLowerCase().trim()
                +"&subject="+sheet+"&keygen="+hashGenerator(policeStation.getText().toString().toLowerCase().trim()+"-"+name_edt.getText().toString().toLowerCase().trim()+"-"+case_no_edt.getText().toString().toLowerCase().trim()+"-"+crime_no_edt.getText().toString().toLowerCase().trim())
                +"&action=addData";

        RequestQueue queue = Volley.newRequestQueue(getContextNullSafety());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String code="";
                        String url = "";
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            code=jsonObj.get("code")+"";
                            url = jsonObj.get("url") + "";
                            Log.e("checking url" , url);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(code.equals("202")){
                            reference.child(pushkey).setValue(data_packet);
                            reference.child(pushkey).child("url").setValue(url);
                            Snackbar.make(lay,"Data Uploaded to Excel.",Snackbar.LENGTH_LONG)
                                    .setActionTextColor(Color.parseColor("#171746"))
                                    .setTextColor(Color.parseColor("#FF7F5C"))
                                    .setBackgroundTint(Color.parseColor("#171746"))
                                    .show();
                            new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog1.dismiss();
                                }
                            },2000);
                            clear_field();
                        }
                        else if(code.equals("205")){
                            Snackbar.make(lay,"Duplicate data exist please enter different data.",Snackbar.LENGTH_LONG)
                                    .setActionTextColor(Color.parseColor("#000000"))
                                    .setTextColor(Color.parseColor("#000000"))
                                    .setBackgroundTint(Color.parseColor("#FF5252"))
                                    .show();
                            LottieAnimationView lottieAnimationView=dialog1.findViewById(R.id.animate);
                            lottieAnimationView.setAnimation("copy.json");
                            dialog1.show();
                            new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog1.dismiss();
                                }
                            },1500);
                        }
                        else{
                            Snackbar.make(lay,"Failed to Upload in Excel.",Snackbar.LENGTH_LONG)
                                    .setActionTextColor(Color.parseColor("#000000"))
                                    .setTextColor(Color.parseColor("#000000"))
                                    .setBackgroundTint(Color.parseColor("#FF5252"))
                                    .show();
                            LottieAnimationView lottieAnimationView=dialog1.findViewById(R.id.animate);
                            lottieAnimationView.setAnimation("error.json");
                            dialog1.show();
                            new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog1.dismiss();
                                }
                            },2000);
                        }
                        Log.e("BULK code", response +"");
                        Log.e("BULK response",response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // enjoy your error status
                Log.e("Status of code = ","Wrong");
                Snackbar.make(lay,"Failed to Upload in Excel.",Snackbar.LENGTH_LONG)
                        .setActionTextColor(Color.parseColor("#000000"))
                        .setTextColor(Color.parseColor("#000000"))
                        .setBackgroundTint(Color.parseColor("#FF5252"))
                        .show();
                LottieAnimationView lottieAnimationView=dialog1.findViewById(R.id.animate);
                lottieAnimationView.setAnimation("error.json");
                dialog1.show();
                new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog1.dismiss();
                    }
                },2000);
            }
        });

        queue.add(stringRequest);
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
    }

    /*private void update_J_Excel(String sheet, Map<String, String> data_packet) {
        Log.e("Sheet",sheet.toUpperCase()+"");
        JSONObject jsonBody = new JSONObject();
        try
        {
            jsonBody.put("cno", Integer.parseInt(case_no_edt.getText().toString().trim()));
            jsonBody.put("crno", Integer.parseInt(crime_no_edt.getText().toString().trim()));
            jsonBody.put("ps", policeStation.getText().toString().toLowerCase().trim());
            jsonBody.put("nod", ac_district.getText().toString().toLowerCase().trim());
            jsonBody.put("subject", sheet.toUpperCase());
            jsonBody.put("j_column", diary.getText().toString().trim());
            jsonBody.put("keygen",hashGenerator(policeStation.getText().toString().toLowerCase().trim()+"-"+case_no_edt.getText().toString().trim()));
            Log.d("body", "httpCall_collect: "+jsonBody);
        }
        catch (Exception e)
        {
            Log.e("Error","JSON ERROR");
        }

        RequestQueue requestQueue = Volley.newRequestQueue(getContextNullSafety());
        String URL = "https://sangyan.co.in/j_column";

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URL,jsonBody,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // enjoy your response
                        String code=response.optString("code")+"";
                        if(code.equals("202")){
                            reference.child(pushkey).setValue(data_packet);
                            Snackbar.make(lay,"Data Uploaded to Excel.",Snackbar.LENGTH_LONG)
                                    .setActionTextColor(Color.parseColor("#171746"))
                                    .setTextColor(Color.parseColor("#FF7F5C"))
                                    .setBackgroundTint(Color.parseColor("#171746"))
                                    .show();
                            new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog1.dismiss();
                                }
                            },2000);
                        }
                        else{
                            Snackbar.make(lay,"Failed to Upload in Excel.",Snackbar.LENGTH_LONG)
                                    .setActionTextColor(Color.parseColor("#000000"))
                                    .setTextColor(Color.parseColor("#000000"))
                                    .setBackgroundTint(Color.parseColor("#FF5252"))
                                    .show();
                            LottieAnimationView lottieAnimationView=dialog1.findViewById(R.id.animate);
                            lottieAnimationView.setAnimation("error.json");
                            dialog1.show();
                            new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog1.dismiss();
                                }
                            },2000);
                        }
                        Log.e("BULK code",code+"");
                        Log.e("response",response.toString());
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // enjoy your error status
                Log.e("Status of code = ","Wrong");
            }
        });
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 15000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 15000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
            }
        });
        Log.d("string", stringRequest.toString());
        requestQueue.add(stringRequest);

    }*/
    private void update_J_Excel(String sheet, Map<String, String> data_packet) {

        String URL = "https://script.google.com/macros/s/"
                +gsID+ "/exec?"
                +"ps="+policeStation.getText().toString().trim()
                +"&cno=" +case_no_edt.getText().toString().trim()
                +"&crno="+crime_no_edt.getText().toString().trim()
                +"&nod="+ac_district.getText().toString().toLowerCase().trim()
                +"&j_column="+diary.getText().toString().trim()
                +"&subject="+sheet
                +"&keygen="+hashGenerator(policeStation.getText().toString().trim()+"-"+case_no_edt.getText().toString().trim())
                +"&action=jColumn";

        RequestQueue queue = Volley.newRequestQueue(getContextNullSafety());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String code="";
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            code=jsonObj.get("code")+"";
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(code.equals("202")){
                            reference.child(pushkey).setValue(data_packet);
                            Snackbar.make(lay,"Data Uploaded to Excel.",Snackbar.LENGTH_LONG)
                                    .setActionTextColor(Color.parseColor("#171746"))
                                    .setTextColor(Color.parseColor("#FF7F5C"))
                                    .setBackgroundTint(Color.parseColor("#171746"))
                                    .show();
                            new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog1.dismiss();
                                }
                            },2000);
                        }
                        else{
                            Snackbar.make(lay,"Failed to Upload in Excel.",Snackbar.LENGTH_LONG)
                                    .setActionTextColor(Color.parseColor("#000000"))
                                    .setTextColor(Color.parseColor("#000000"))
                                    .setBackgroundTint(Color.parseColor("#FF5252"))
                                    .show();
                            LottieAnimationView lottieAnimationView=dialog1.findViewById(R.id.animate);
                            lottieAnimationView.setAnimation("error.json");
                            dialog1.show();
                            new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog1.dismiss();
                                }
                            },2000);
                        }
                        Log.e("BULK code", response +"");
                        Log.e("BULK response",response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // enjoy your error status
                Log.e("Status of code = ","Wrong");
                Snackbar.make(lay,"Failed to Upload in Excel.",Snackbar.LENGTH_LONG)
                        .setActionTextColor(Color.parseColor("#000000"))
                        .setTextColor(Color.parseColor("#000000"))
                        .setBackgroundTint(Color.parseColor("#FF5252"))
                        .show();
                LottieAnimationView lottieAnimationView=dialog1.findViewById(R.id.animate);
                lottieAnimationView.setAnimation("error.json");
                dialog1.show();
                new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog1.dismiss();
                    }
                },2000);
            }
        });

        queue.add(stringRequest);

    }

    protected String hashGenerator(String str_hash) {
        // TODO Auto-generated method stub
        StringBuffer finalString=new StringBuffer();
        finalString.append(str_hash);
        //		logger.info("Parameters for SHA-512 : "+finalString);
        String hashGen=finalString.toString();
        StringBuffer sb = null;
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
            md.update(hashGen.getBytes());
            byte byteData[] = md.digest();
            //convert the byte to hex format method 1
            sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sb.toString();
    }

    private void clear_field() {
        ac_district.setText("");
        policeStation.setText("");
        crime_no_edt.setText("");
        crime_year_edt.setText("");
        ac_caseType.setText("");
        case_no_edt.setText("");
        name_edt.setText("");
    }

    private void getFileUrl(){
        String url="https://docs.google.com/spreadsheets/d/1FCq7LxEFUYtSjrHcRYXFGPdruuGmQur_JHuxrgp2zao/edit?usp=sharing";
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void get_districts_phone() {
        reference_phone.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    district.add(ds.getKey());
                    //Creating the instance of ArrayAdapter containing list of language names
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>
                            (getContextNullSafety(), android.R.layout.select_dialog_item, district);
                    //Getting the instance of AutoCompleteTextView
                    ac_district.setThreshold(1);//will start working from first character
                    ac_district.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
                    ac_district.setTextColor(Color.RED);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void get_police_station(String district) {
        ps_list.clear();
        reference_phone.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.child(district).getChildren()) {
                    try {
                        if (dataSnapshot.getKey().substring(0, 2).equals("PS")) {
                            ps_list.add(dataSnapshot.getKey().substring(2));
                            //Creating the instance of ArrayAdapter containing list of language names
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                    (getContextNullSafety(), android.R.layout.select_dialog_item, ps_list);
                            //Getting the instance of AutoCompleteTextView
                            policeStation.setThreshold(1);//will start working from first character
                            policeStation.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
                            policeStation.setTextColor(Color.RED);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Log.e("PS = ",ps_list+"");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
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
    private static void disableSSLCertificateChecking() {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }
        } };

        try {
            SSLContext sc = SSLContext.getInstance("TLS");

            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
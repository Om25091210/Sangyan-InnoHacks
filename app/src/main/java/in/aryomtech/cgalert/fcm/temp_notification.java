package in.aryomtech.cgalert.fcm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.aryomtech.cgalert.Fragments.model.Excel_data;
import in.aryomtech.cgalert.R;
import in.aryomtech.cgalert.Splash;

import in.aryomtech.cgalert.databinding.ActivityTempNotificationBinding;
import io.michaelrocks.paranoid.Obfuscate;

@Obfuscate
public class temp_notification extends AppCompatActivity {

    ActivityTempNotificationBinding binding;
    DatabaseReference ref_data,ref_notice,ref_writ;
    String key,which_section;
    List<Excel_data> excel_data=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTempNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        key = getIntent().getStringExtra("sending_msg_data");

        binding.caseDiary.linearLayout2.setVisibility(View.GONE);
        binding.caseDiary.linearLayout3.setVisibility(View.GONE);
        binding.caseDiary.type.setVisibility(View.GONE);
        binding.caseDiary.dayLeft.setVisibility(View.GONE);
        binding.caseDiary.share.setVisibility(View.GONE);
        binding.caseDiary.imageView2.setVisibility(View.GONE);

        ref_data = FirebaseDatabase.getInstance().getReference().child("data");
        ref_notice = FirebaseDatabase.getInstance().getReference().child("notice");
        ref_writ = FirebaseDatabase.getInstance().getReference().child("writ");

        check_key();
    }

    private void check_key() {

        ref_data.child(key).child("B").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Log.e("oooo","herer");
                    binding.caseDiary.getRoot().setVisibility(View.VISIBLE);
                    ref_data.child(key+"").child("seen").setValue("1");
                    populate_data();
                }
            }@Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    private void populate_data() {
        ref_data.child(key+"").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                excel_data.add(snapshot.getValue(Excel_data.class));
                show_data();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void show_data() {
        binding.caseDiary.progressBar2.setVisibility(View.GONE);

        binding.caseDiary.linearLayout2.setVisibility(View.VISIBLE);
        binding.caseDiary.linearLayout3.setVisibility(View.VISIBLE);
        binding.caseDiary.type.setVisibility(View.VISIBLE);
        binding.caseDiary.dayLeft.setVisibility(View.VISIBLE);
        binding.caseDiary.imageView2.setVisibility(View.VISIBLE);
        binding.caseDiary.share.setVisibility(View.VISIBLE);

        binding.caseDiary.lastDate.setText(excel_data.get(0).getLl());
        binding.caseDiary.stationName.setText(excel_data.get(0).getBb());
        binding.caseDiary.distName.setText(excel_data.get(0).getCc());
        binding.caseDiary.caseNo.setText(excel_data.get(0).getEe() + "/" + excel_data.get(0).getGg());
        binding.caseDiary.nameRm.setText(excel_data.get(0).getKk());
        binding.caseDiary.caseType.setText(excel_data.get(0).getDd());
        binding.caseDiary.personName.setText(excel_data.get(0).getFf());
        binding.caseDiary.crimeNo.setText(excel_data.get(0).getHh() + "/" + excel_data.get(0).getIi());
        binding.caseDiary.receivingDate.setText(excel_data.get(0).getJj());

        //return call logic
        if (excel_data.get(0).getType().equals("RM CALL")) {
            binding.caseDiary.message.setText("उपरोक्त मूल केश डायरी दिनाँक " + excel_data.get(0).getKk() + " तक बेल शाखा, कार्यालय महाधिवक्ता,उच्च न्यायालय छतीसगढ़ में  अनिवार्यतः जमा करें।");
            binding.caseDiary.type.setVisibility(View.VISIBLE);
            binding.caseDiary.type.setImageResource(R.drawable.ic_submit_type);
        } else if (excel_data.get(0).getType().equals("RM RETURN")) {
            binding.caseDiary.message.setText("उपरोक्त मूल केश डायरी " + excel_data.get(0).getKk() + " से पांच दिवस के भीतर बेल शाखा, कार्यालय महाधिवक्ता,उच्च न्यायालय से वापिस ले जावें।");
            binding.caseDiary.type.setVisibility(View.VISIBLE);
            binding.caseDiary.type.setImageResource(R.drawable.ic_return_type);
        } else
            binding.caseDiary.type.setVisibility(View.GONE);

        //red white logic
        if (excel_data.get(0).getJj().equals("None") || excel_data.get(0).getJj().equals("nan"))
            binding.layout.setBackgroundColor(Color.parseColor("#FAD8D9"));
        else
            binding.layout.setBackgroundColor(Color.parseColor("#FFFFFF"));

        //tick logic
        if (excel_data.get(0).getReminded() != null) {
            if (excel_data.get(0).getReminded().equals("once")) {
                binding.caseDiary.imageView2.setVisibility(View.VISIBLE);
                binding.caseDiary.imageView2.setImageResource(R.drawable.ic_blue_tick);
            } else if (excel_data.get(0).getReminded().equals("twice")) {
                binding.caseDiary.imageView2.setVisibility(View.VISIBLE);
                binding.caseDiary.imageView2.setImageResource(R.drawable.ic_green_tick);
            }
        }

        if (excel_data.get(0).getLl() != null) {
            if (!excel_data.get(0).getLl().equals("None")) {
                binding.caseDiary.dayLeft.setVisibility(View.VISIBLE);
                if (nDays_Between_Dates(excel_data.get(0).getLl()) == 0) {
                    binding.caseDiary.dayLeft.setText("0d");
                    binding.caseDiary.dayLeft.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clock_time, 0, 0, 0);
                } else if (nDays_Between_Dates(excel_data.get(0).getLl()) <= 5) {
                    binding.caseDiary.dayLeft.setText(nDays_Between_Dates(excel_data.get(0).getLl()) + "d");
                    binding.caseDiary.dayLeft.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clock_time, 0, 0, 0);
                } else {
                    binding.caseDiary.dayLeft.setText("--");
                    binding.caseDiary.dayLeft.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_red_clock, 0, 0, 0);
                }
            } else
                binding.caseDiary.dayLeft.setVisibility(View.GONE);
        } else
            binding.caseDiary.dayLeft.setVisibility(View.GONE);
        String message;
        if(excel_data.get(0).getType().equals("RM CALL")) {
             message= "हाईकोर्ट अलर्ट:-डायरी माँग" + "\nदिनाँक:- " + excel_data.get(0).getDate() + "\n\n" + "Last Date - " + excel_data.get(0).getLl() + "\n"
                    + "District - " + excel_data.get(0).getCc() + "\n" +
                    "Police Station - " + excel_data.get(0).getBb() + "\n" +
                    excel_data.get(0).getDd() + " No. - " + excel_data.get(0).getEe() + "/" + excel_data.get(0).getGg() + "\n" +
                    "RM Date - " + excel_data.get(0).getKk() + "\n" +
                    "Case Type - " + excel_data.get(0).getDd() + "\n" +
                    "Name - " + excel_data.get(0).getFf() + "\n" +
                    "Crime No. - " + excel_data.get(0).getHh() + "/" + excel_data.get(0).getIi() + "\n" +
                    "Received - " + excel_data.get(0).getJj() + "\n\n"
                    + "उपरोक्त मूल केश डायरी दिनाँक " + excel_data.get(0).getLl() + " तक बेल शाखा, कार्यालय महाधिवक्ता,उच्च न्यायालय छतीसगढ़ में  अनिवार्यतः जमा करें।";
        }
        else{
            message = "हाईकोर्ट अलर्ट:-डायरी वापसी"+"\nदिनाँक:- "+ excel_data.get(0).getDate()  +" \n\n" + "Last Date - " + excel_data.get(0).getLl() + "\n"
                    + "District - " + excel_data.get(0).getCc() + "\n" +
                    "Police Station - " + excel_data.get(0).getBb() + "\n"+
                    excel_data.get(0).getDd() + " No. - " + excel_data.get(0).getEe() +"/"+ excel_data.get(0).getGg()+"\n" +
                    "RM Date - " + excel_data.get(0).getKk()+ "\n" +
                    "Case Type - " + excel_data.get(0).getDd() +  "\n" +
                    "Name - " + excel_data.get(0).getFf()+  "\n" +
                    "Crime No. - " + excel_data.get(0).getHh() +"/"+ excel_data.get(0).getIi()+  "\n" +
                    "Received - " + excel_data.get(0).getJj() + "\n\n" + "1)उपरोक्त मूल केश डायरी  महाधिवक्ता कार्यालय द्वारा दी गयी मूल पावती लाने पर ही दी जाएगी।\n"
                    +"2) उपरोक्त मूल केश डायरी "+ excel_data.get(0).getKk() +" से पांच दिवस के भीतर बेल शाखा, कार्यालय महाधिवक्ता,उच्च न्यायालय से वापिस ले जावें।";

        }
        binding.caseDiary.share.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(shareIntent, "Share link using"));
        });

        binding.caseDiary.imageView4.setOnClickListener(v -> {
            Intent intent = new Intent(temp_notification.this, Splash.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    public static int nDays_Between_Dates(String date1) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String str = formatter.format(date);
        int diffDays = 0;
        try {
            SimpleDateFormat dates = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            Date startDate = dates.parse(date1);
            Date endDate = dates.parse(str);
            if(startDate.after(endDate)) {
                long diff = endDate.getTime() - startDate.getTime();
                diffDays = (int) (diff / (24 * 60 * 60 * 1000));
            }
            else if(startDate.equals(endDate))
                return 0;
            else{
                return 6;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Math.abs(diffDays);
    }
}
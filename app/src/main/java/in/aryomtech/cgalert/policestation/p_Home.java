package in.aryomtech.cgalert.policestation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Objects;

import in.aryomtech.cgalert.Login;
import in.aryomtech.cgalert.R;
import io.michaelrocks.paranoid.Obfuscate;

@Obfuscate
public class p_Home extends AppCompatActivity{

    TextView station_name_txt;
    String stat_name;
    FirebaseAuth auth;
    FirebaseUser user;
    private ArrayList<String> mTitles = new ArrayList<>();
    DatabaseReference reference;
    private static final int PERMISSION_SEND_SMS = 123;
    public static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    int downspeed;
    int upspeed;
    String DeviceToken;
    ImageView phone_num,back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phome);

        Window window = p_Home.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(p_Home.this, R.color.use_bg));

        requestSmsPermission();
        reference= FirebaseDatabase.getInstance().getReference().child("users");


        getSharedPreferences("authorized_entry",MODE_PRIVATE).edit()
                .putBoolean("entry_done",true).apply();

        station_name_txt=findViewById(R.id.textView4);
        back=findViewById(R.id.imageView4);
        phone_num=findViewById(R.id.entry2);
        stat_name= getSharedPreferences("station_name_K",MODE_PRIVATE)
                .getString("the_station_name2003","");

        station_name_txt.setText(stat_name);
        back.setOnClickListener(v->{
            onBackPressed();
        });
        // Show main fragment in container
        goToFragment(new Frag_p_Home());

        getting_device_token();
        check_if_token();

    }

    private void check_if_token() {
        String pkey=reference.push().getKey();
        if(DeviceToken!=null){
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {//
                    int c=0;
                    if(snapshot.child(user.getUid()).child("token").exists()) {
                        for (DataSnapshot ds : snapshot.child(user.getUid()).child("token").getChildren()) {
                            Log.e("forloop", "YES");
                            if (snapshot.child(user.getUid()).child("token").child(Objects.requireNonNull(ds.getKey())).child(DeviceToken).exists()) {
                                c = 1;
                                Log.e("loop if", "YES");
                            }
                        }
                        if (c == 0) {
                            reference.child(user.getUid()).child("token").child(pkey).setValue(DeviceToken);
                        }
                    }
                    else{
                        reference.child(user.getUid()).child("token").child(pkey).setValue(DeviceToken);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }
    }
    private void getting_device_token() {
        ConnectivityManager connectivityManager = (ConnectivityManager)this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkCapabilities nc = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        if(nc!=null) {
            downspeed = nc.getLinkDownstreamBandwidthKbps()/1000;
            upspeed = nc.getLinkUpstreamBandwidthKbps()/1000;
        }else{
            downspeed=0;
            upspeed=0;
        }

        if((upspeed!=0 && downspeed!=0) || getWifiLevel()!=0) {
            FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
                if (!TextUtils.isEmpty(token)) {
                    Log.d("token", "retrieve token successful : " + token);
                } else {
                    Log.w("token121", "token should not be null...");
                }
            }).addOnFailureListener(e -> {
                //handle e
            }).addOnCanceledListener(() -> {
                //handle cancel
            }).addOnCompleteListener(task ->
            {
                try {
                    DeviceToken = task.getResult();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
    private void requestSmsPermission() {

        // check permission is given
        if (ContextCompat.checkSelfPermission(p_Home.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // request permission (see result in onRequestPermissionsResult() method)
            ActivityCompat.requestPermissions(p_Home.this,
                    new String[]{Manifest.permission.SEND_SMS},
                    PERMISSION_SEND_SMS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length>0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(p_Home.this, "Permission granted!", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(p_Home.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }

    }
    public int getWifiLevel()
    {
        try {
            WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            int linkSpeed = wifiManager.getConnectionInfo().getRssi();
            return WifiManager.calculateSignalLevel(linkSpeed, 5);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void goToFragment(Fragment fragment) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.container, fragment,"mainFrag").commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        if(user==null){
            Intent i = new Intent(p_Home.this, Login.class);
            startActivity(i);
            finish();
        }
    }
}
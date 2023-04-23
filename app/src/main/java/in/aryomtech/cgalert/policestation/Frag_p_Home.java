package in.aryomtech.cgalert.policestation;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import in.aryomtech.cgalert.Fragments.model.Excel_data;
import in.aryomtech.cgalert.R;
import in.aryomtech.cgalert.policestation.Pending.p_pending_coll;
import in.aryomtech.cgalert.policestation.Pending.p_pending_return;
import in.aryomtech.myapplication.v4.FragmentPagerItemAdapter;
import in.aryomtech.myapplication.v4.FragmentPagerItems;
import soup.neumorphism.NeumorphCardView;

import io.michaelrocks.paranoid.Obfuscate;

@Obfuscate
public class Frag_p_Home extends Fragment {

    String stat_name;
    List<Excel_data> pending_return=new ArrayList<>();
    long total_coll,total_return;
    Query query_coll,query_return;
    TextView coll_text,text_return;
    NeumorphCardView blue,back;
    View view;
    private Context contextNullSafe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_frag_p__home, container, false);
        if (contextNullSafe == null) getContextNullSafety();

        stat_name= getContextNullSafety().getSharedPreferences("station_name_K",MODE_PRIVATE)
                .getString("the_station_name2003","");
        disableSSLCertificateChecking();
        blue=view.findViewById(R.id.blue);
        back=view.findViewById(R.id.back);
        blue.setOnClickListener(v->{
            ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations( R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left, R.anim.exit_to_right)
                    .add(R.id.constraint,new p_pending_coll())
                    .addToBackStack(null)
                    .commit();
        });
        back.setOnClickListener(v->{
            ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations( R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left, R.anim.exit_to_right)
                    .add(R.id.constraint,new p_pending_return())
                    .addToBackStack(null)
                    .commit();
        });
        coll_text=view.findViewById(R.id.textView6);
        text_return=view.findViewById(R.id.textView8);

        query_coll = FirebaseDatabase.getInstance().getReference().child("data").orderByChild("type").equalTo("RM CALL");
        query_return = FirebaseDatabase.getInstance().getReference().child("data").orderByChild("type").equalTo("RM RETURN");
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                ((FragmentActivity)getContextNullSafety()).getSupportFragmentManager(), FragmentPagerItems.with(getContextNullSafety())
                .add("Urgent",urgent_data.class)
                .add("Today",today.class)
                .add("RM CALL", p_mcrc_rm_coll.class)
                .add("RM RETURN", p_mcrc_rm_return.class)
                .add("Similar CALL", p_similar_collection.class)
                .add("Similar Return", p_similar_return.class)
                .create());

        ViewPager viewPager = view.findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = view.findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);

        get_pending();
        return view;
    }

    private void get_pending() {
        query_coll.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    if(snapshot.child(ds.getKey()).child("B").getValue(String.class).toUpperCase().equals(stat_name.substring(3))) {
                        if (snapshot.child(ds.getKey()).child("J").getValue(String.class).equals("None")) {
                            total_coll++;
                        }
                    }
                }
                String text=total_coll+"";
                coll_text.setText(text);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        query_return.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    if(snapshot.child(ds.getKey()).child("B").getValue(String.class).toUpperCase().equals(stat_name.substring(3))) {
                        if (snapshot.child(ds.getKey()).child("J").getValue(String.class).equals("None")) {
                            total_return++;
                            pending_return.add(snapshot.child(ds.getKey()).getValue(Excel_data.class));
                        }
                    }
                }
                String text=total_return+"";
                text_return.setText(text);
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
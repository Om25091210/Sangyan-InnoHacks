package in.aryomtech.cgalert;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import soup.neumorphism.NeumorphCardView;

import io.michaelrocks.paranoid.Obfuscate;

@Obfuscate
public class about_dev extends Fragment {

    View view;
    ImageView back;
    private Context contextNullSafe;
    NeumorphCardView card_fb_n,card_twitter_n,card_linkedin_n,card_whatsapp,card_insta_n
                    ,card_fb2,card_twitter2,card_linkedin2,card_whatsapp2,card_insta2
                    ,card_fb1,card_linkedin1,card_whatsapp1,card_insta1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_about_dev, container, false);
        if (contextNullSafe == null) getContextNullSafety();

        back=view.findViewById(R.id.imageView4);

        card_fb_n=view.findViewById(R.id.card_fb);
        card_fb_n.setOnClickListener(v->{
            String facebookUrl ="https://www.facebook.com/profile.php?id=100014456672694";
            Intent facebookAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl));
            facebookAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            startActivity(facebookAppIntent);
        });

        card_twitter_n=view.findViewById(R.id.card_twitter);
        card_twitter_n.setOnClickListener(v->{
            String url = "https://twitter.com/OmYadav21950675?t=LQBcHVZaSySd7fVzv0-m9Q&s=08";
            Intent twitterAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            twitterAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            startActivity(twitterAppIntent);
        });

        card_linkedin_n=view.findViewById(R.id.card_linkedin);
        card_linkedin_n.setOnClickListener(v->{
            String url = "https://www.linkedin.com/in/om-yadav-3760921a7";
            Intent linkedInAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            linkedInAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            startActivity(linkedInAppIntent);
        });

        card_whatsapp=view.findViewById(R.id.card_whatsapp);
        card_whatsapp.setOnClickListener(v->{
            String url = "https://api.whatsapp.com/send?phone=" +"+919301982112";
            try {
                PackageManager pm = v.getContext().getPackageManager();
                pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                v.getContext().startActivity(i);
            } catch (PackageManager.NameNotFoundException e) {
                v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });

        card_insta_n=view.findViewById(R.id.card_insta);
        card_insta_n.setOnClickListener(v->{
            Intent insta_in;
            String scheme = "http://instagram.com/_u/"+"i_m_om_kumar";
            String path = "https://instagram.com/"+"i_m_om_kumar";
            String nomPackageInfo ="com.instagram.android";
            try {
                requireContext().getPackageManager().getPackageInfo(nomPackageInfo, 0);
                insta_in = new Intent(Intent.ACTION_VIEW, Uri.parse(scheme));
            } catch (Exception e) {
                insta_in = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
            }
            startActivity(insta_in);
        });

        card_fb2=view.findViewById(R.id.card_fb2);
        card_fb2.setOnClickListener(v->{
            String facebookUrl ="https://www.facebook.com/profile.php?id=100023407477458";
            Intent facebookAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl));
            facebookAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            startActivity(facebookAppIntent);
        });

        card_twitter2=view.findViewById(R.id.card_twitter2);
        card_twitter2.setOnClickListener(v->{
            String url = "https://twitter.com/___nikz_";
            Intent twitterAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            twitterAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            startActivity(twitterAppIntent);
        });

        card_linkedin2=view.findViewById(R.id.card_linkedin2);
        card_linkedin2.setOnClickListener(v->{
            String url = "https://www.linkedin.com/in/nikhil-verma2002/";
            Intent linkedInAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            linkedInAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            startActivity(linkedInAppIntent);
        });

        card_whatsapp2=view.findViewById(R.id.card_whatsapp2);
        card_whatsapp2.setOnClickListener(v->{
            String url = "https://api.whatsapp.com/send?phone=" +"+916264714906";
            try {
                PackageManager pm = v.getContext().getPackageManager();
                pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                v.getContext().startActivity(i);
            } catch (PackageManager.NameNotFoundException e) {
                v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });

        card_insta2=view.findViewById(R.id.card_insta2);
        card_insta2.setOnClickListener(v->{
            Intent insta_in;
            String scheme = "http://instagram.com/_u/"+"nikhil__vermaa_";
            String path = "https://instagram.com/"+"nikhil__vermaa_";
            String nomPackageInfo ="com.instagram.android";
            try {
                requireContext().getPackageManager().getPackageInfo(nomPackageInfo, 0);
                insta_in = new Intent(Intent.ACTION_VIEW, Uri.parse(scheme));
            } catch (Exception e) {
                insta_in = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
            }
            startActivity(insta_in);
        });

        card_fb1=view.findViewById(R.id.card_fb1);
        card_fb1.setOnClickListener(v->{
            String facebookUrl ="https://www.facebook.com/saiaditya.mantrarathnam";
            Intent facebookAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl));
            facebookAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            startActivity(facebookAppIntent);
        });

        card_linkedin1=view.findViewById(R.id.card_linkedin1);
        card_linkedin1.setOnClickListener(v->{
            String url = "https://www.linkedin.com/in/mbsaiaditya/";
            Intent twitterAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            twitterAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            startActivity(twitterAppIntent);
        });

        card_whatsapp1=view.findViewById(R.id.card_whatsapp1);
        card_whatsapp1.setOnClickListener(v->{
            String url = "https://api.whatsapp.com/send?phone=" +"+917974378884";
            try {
                PackageManager pm = v.getContext().getPackageManager();
                pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                v.getContext().startActivity(i);
            } catch (PackageManager.NameNotFoundException e) {
                v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });

        card_insta1=view.findViewById(R.id.card_insta1);
        card_insta1.setOnClickListener(v->{
            Intent insta_in;
            String scheme = "http://instagram.com/_u/"+"m.b.saiaditya";
            String path = "https://instagram.com/"+"m.b.saiaditya";
            String nomPackageInfo ="com.instagram.android";
            try {
                requireContext().getPackageManager().getPackageInfo(nomPackageInfo, 0);
                insta_in = new Intent(Intent.ACTION_VIEW, Uri.parse(scheme));
            } catch (Exception e) {
                insta_in = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
            }
            startActivity(insta_in);
        });


        back.setOnClickListener(v-> back());
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
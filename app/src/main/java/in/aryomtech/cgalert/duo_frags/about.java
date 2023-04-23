package in.aryomtech.cgalert.duo_frags;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import in.aryomtech.cgalert.R;
import io.michaelrocks.paranoid.Obfuscate;

@Obfuscate
public class about extends Fragment {

    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter ;
    TabLayout tabIndicator;
    Button btnNext;
    int position = 0 ;
    private Context contextNullSafe;
    Animation btnAnim ;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_about, container, false);
        if (contextNullSafe == null) getContextNullSafety();

        // ini views
        btnNext = view.findViewById(R.id.btn_next);
        tabIndicator = view.findViewById(R.id.tab_indicator);
        btnAnim = AnimationUtils.loadAnimation(getContextNullSafety().getApplicationContext(),R.anim.button_animation);
        // fill list screen
        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("","माननीय मुख्यमंत्री जी, माननीय महाधिवक्ता जी तथा श्रीमान पुलिस महानिदेशक जी के सानिध्य में स्थापित पुलिस लीगल सेक्शन,कैंप बिलासपुर के द्वारा उच्च न्यायालय से संबंधित विधिक प्रक्रियाओं को पेपरलेस तथा रियल टाइम मॉनिटरिंग करने के प्रयास में इस एंड्रायड एप \"SANGYAN\" को निर्मित किया गया है।\n" +
                "\n" +
                "जोकि निम्नलिखित उद्देश्यों की पूर्ति करता है → ",R.drawable.cg_logo));
        mList.add(new ScreenItem("केस डायरी अलर्ट","माननीय न्यायालय द्वारा आपराधिक मामलों में मांग किए जाने वाले केश डायरियों की सूचना उचित समय पर थानों तक संप्रेषित कर निर्धारित समयावधि में न्यायालय को उपलब्ध करवाना।",R.drawable.case_diary));
        mList.add(new ScreenItem("रिट केस अलर्ट","'संज्ञान' एप के माध्यम से पुलिस विभाग से संबंधित रिट याचिकाओं की सूचना पुलिस मुख्यालय तथा संबंधित जिले के वरिष्ठ अधिकारियो तक संप्रेषित करना।",R.drawable.writ));
        mList.add(new ScreenItem("पीड़ित को नोटिस","एसटी/एससी तथा पॉक्सो से संबंधित याचिकाओं में पीड़िता/सूचनाकर्ता को आरोपी के जमानत विरोध करने के संबंध में सूचनाओं की निर्धारित समयावधि में तामिली करवाना।",R.drawable.notice_to_victim));
        mList.add(new ScreenItem("छत्तीसगढ़ पुलिस संपर्क","विधिक प्रक्रिया से संबंधित विभाग के दूरस्थ अधिकारियों के मध्य 'संचार एवम समन्वय' को सरल और सुगम बनाने हेतु टेलीफोन निर्देशिका का डिजिटलाइजेशन।",R.drawable.police_contacts));
        mList.add(new ScreenItem("Operations and Managed By:-\nPolice Legal Cell, Camp Bilaspur Advocate General Office, High Court Chhattisgarh","Incharge: PANKAJ AWASTHI (DSP)\nWorking Staff:-\n1) SHWETA MISHRA GOURAHA (TI)\n2) ABHINAW VERMA (SI(T/C))\n3) RAKESH BHARDWAJ (CONS.496)",R.drawable.cg_logo));
        mList.add(new ScreenItem("Man Behind The Idea\nABHINAW VERMA (SI(T/C))","Man who guided the project from scratch to the final stage. His guidance made the project efficient and easy to use.\n\nHelp & FAQ\nContact :- 8269737971\nEmail :- abhinawtheverma@gmail.com",R.drawable.cg_logo));
       /* mList.add(new ScreenItem("Submission Alert","The High Court needs case diaries to be submitted on time for case hearings and this app will help the court via alerting the Police Stations.",R.drawable.ic_onboarding_one));
        mList.add(new ScreenItem("How it works?","The color red indicates that the case diary is supposed to be submitted or withdrawn whereas the clock shows the number of days left for the same.",R.drawable.ic_onboarding_two));
        mList.add(new ScreenItem("Finally","Once the diary is submitted, it will be inspected by the respective judges of the court.",R.drawable.ic_onboarding_three));*/
        // setup viewpager
        screenPager =view.findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(getContextNullSafety(),mList);
        screenPager.setAdapter(introViewPagerAdapter);
        // setup tablayout with viewpager
        tabIndicator.setupWithViewPager(screenPager);
        // next button click Listner


        btnNext.setOnClickListener(v -> {
            position = screenPager.getCurrentItem();
            if (position < mList.size()) {
                position++;
                screenPager.setCurrentItem(position);
            }
            if (position == mList.size()-1) { // when we rech to the last screen
                // TODO : show the GETSTARTED Button and hide the indicator and the next button
                loaddLastScreen();
            }
        });
        // tablayout add change listener
        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == mList.size()-1) {
                    loaddLastScreen();
                }
                else{
                    btnNext.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        // skip button click listener

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

    // show the GETSTARTED Button and hide the indicator and the next button
    private void loaddLastScreen() {
        btnNext.setVisibility(View.INVISIBLE);
       // tabIndicator.setVisibility(View.INVISIBLE);
        // TODO : ADD an animation the getstarted button
        // setup animation
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

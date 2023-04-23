package in.aryomtech.cgalert.Fragments.holder;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import in.aryomtech.cgalert.Fragments.model.Excel_data;
import in.aryomtech.cgalert.R;
import io.michaelrocks.paranoid.Obfuscate;

@Obfuscate
public class Excel_holder extends RecyclerView.ViewHolder {
    TextView day_left;
    TextView textViewTitle;
    TextView textViewBody;
    TextView Rm,mcrc,crime_no,case_no,pr_case_no,name,receiving_date;
    ConstraintLayout layout;
    ImageView tick,type;
    LinearLayout layout_details;

    public Excel_holder(@NonNull View itemView) {
        super(itemView);
        textViewTitle = itemView.findViewById(R.id.station_name);
        textViewBody = itemView.findViewById(R.id.dist_name);
        day_left = itemView.findViewById(R.id.day_left);//
        Rm = itemView.findViewById(R.id.name_rm);//
        mcrc = itemView.findViewById(R.id.case_type);//
        pr_case_no = itemView.findViewById(R.id.pr_case_no);//
        crime_no = itemView.findViewById(R.id.crime_no);
        case_no = itemView.findViewById(R.id.case_no);
        tick = itemView.findViewById(R.id.imageView2);
        layout = itemView.findViewById(R.id.layout);
        layout_details = itemView.findViewById(R.id.linearLayout3);
        type = itemView.findViewById(R.id.type);
        name = itemView.findViewById(R.id.person_name);//
        receiving_date = itemView.findViewById(R.id.receiving_date);//
    }

    public void setItem(Excel_data post, Context contextNullSafety){

        if(post.getDate_of_alert()!=null){
            day_left.setVisibility(View.VISIBLE);
            if(nDays_Between_Dates(post.getDate_of_alert())==0) {
                day_left.setText("0d");
                day_left.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clock_time, 0, 0, 0);
            }
            else if(nDays_Between_Dates(post.getDate_of_alert())<=5) {
                day_left.setText(nDays_Between_Dates(post.getDate_of_alert()) + "d");
                day_left.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clock_time, 0, 0, 0);
            }
            else {
                day_left.setText("--");
                day_left.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_red_clock, 0, 0, 0);
            }
        }
        else
            day_left.setVisibility(View.GONE);

        textViewTitle.setText(post.getBb()+"");
        textViewBody.setText(post.getCc());
        Rm.setText(post.getKk());
        mcrc.setText(post.getDd());
        pr_case_no.setText(post.getDd()+" No.");
        crime_no.setText(post.getHh() +"/"+ post.getIi());
        case_no.setText(post.getEe() +"/"+ post.getGg());
        name.setText(post.getFf());
        receiving_date.setText(post.getJj());
        if(post.getReminded()!=null) {
            if(post.getReminded().equals("once")){
                tick.setImageResource(R.drawable.ic_blue_tick);
            }
            else if(post.getReminded().equals("twice")){
                tick.setImageResource(R.drawable.ic_green_tick);
            }
        }
        else{
            tick.setImageResource(R.color.transparent);
        }
        if(post.getJj().equals("None") || post.getJj().equals("nan"))
            layout.setBackgroundResource(R.drawable.bg_card_red);
        else
            layout.setBackgroundResource(R.drawable.bg_card_white);
        if(post.getType().equals("RM CALL")) {
            type.setVisibility(View.VISIBLE);
            type.setImageResource(R.drawable.ic_submit_type);
        }else if(post.getType().equals("RM RETURN")) {
            type.setVisibility(View.VISIBLE);
            type.setImageResource(R.drawable.ic_return_type);
        }else
            type.setVisibility(View.GONE);

        layout.setOnClickListener(v->{
            if (layout_details.getVisibility() == View.VISIBLE) {
                layout_details.setVisibility(View.GONE);
                // Its visible
            } else {
                layout_details.setVisibility(View.VISIBLE);
                // Either gone or invisible
            }
        });
    }

    public static int nDays_Between_Dates(String date1) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String str = formatter.format(date);
        int diffDays = 0;
        try {
            SimpleDateFormat dates = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
            Date startDate = dates.parse(date1);
            Date endDate = dates.parse(str);
            long diff = endDate.getTime() - startDate.getTime();
            diffDays = (int) (diff / (24 * 60 * 60 * 1000));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Math.abs(diffDays);
    }
}

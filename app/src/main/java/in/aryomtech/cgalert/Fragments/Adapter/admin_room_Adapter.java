package in.aryomtech.cgalert.Fragments.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.aryomtech.cgalert.Fragments.model.Excel_data;
import in.aryomtech.cgalert.R;
import io.michaelrocks.paranoid.Obfuscate;

@Obfuscate
public class admin_room_Adapter extends RecyclerView.Adapter<admin_room_Adapter.ViewHolder> {

    Context context;
    List<Excel_data> list;
    String error;

    public admin_room_Adapter(Context context, String error,List<Excel_data> list) {
        this.context = context;
        this.list = list;
        this.error=error;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.textViewTitle.setText(list.get(position).getBb() + "");
        holder.textViewBody.setText(list.get(position).getCc());
        holder.Rm.setText(list.get(position).getKk());
        holder.mcrc.setText(list.get(position).getDd());
        holder.pr_case_no.setText(list.get(position).getDd()+" No.");
        holder.crime_no.setText(list.get(position).getHh() +"/"+ list.get(position).getIi());
        holder.case_no.setText(list.get(position).getEe() +"/"+ list.get(position).getGg());
        holder.name.setText(list.get(position).getFf());
        holder.receiving_date.setText(list.get(position).getJj());
        if(list.get(position).getLl()!=null){
            if(!list.get(position).getLl().equals("None")) {
                holder.day_left.setVisibility(View.VISIBLE);
                if (nDays_Between_Dates(list.get(position).getLl()) == 0) {
                    holder.day_left.setText("0d");
                    holder.day_left.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clock_time, 0, 0, 0);
                } else if (nDays_Between_Dates(list.get(position).getLl()) <= 5) {
                    holder.day_left.setText(nDays_Between_Dates(list.get(position).getLl()) + "d");
                    holder.day_left.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clock_time, 0, 0, 0);
                } else {
                    holder.day_left.setText("--");
                    holder.day_left.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_red_clock, 0, 0, 0);
                }
            }
            else
                holder.day_left.setVisibility(View.GONE);
        }
        else
            holder.day_left.setVisibility(View.GONE);

        if(list.get(position).getReminded()!=null) {
            if(list.get(position).getReminded().equals("once")){
                holder.tick.setImageResource(R.drawable.ic_blue_tick);
            }
            else if(list.get(position).getReminded().equals("twice")){
                holder.tick.setImageResource(R.drawable.ic_green_tick);
            }
        }
        if (list.get(position).getJj().equals("None") || list.get(position).getJj().equals("nan"))
            holder.layout.setBackgroundResource(R.drawable.bg_card_red);
        else
            holder.layout.setBackgroundResource(R.drawable.bg_card_white);
        if (list.get(position).getType().equals("RM CALL")) {
            holder.type.setVisibility(View.VISIBLE);
            holder.type.setImageResource(R.drawable.ic_submit_type);
        } else if (list.get(position).getType().equals("RM RETURN")) {
            holder.type.setVisibility(View.VISIBLE);
            holder.type.setImageResource(R.drawable.ic_return_type);
        } else
            holder.type.setVisibility(View.GONE);

        holder.layout.setOnClickListener(v->{
            if (holder.layout_details.getVisibility() == View.VISIBLE) {
                holder.layout_details.setVisibility(View.GONE);
                // Its visible
            } else {
                holder.layout_details.setVisibility(View.VISIBLE);
                // Either gone or invisible
            }
        });
        holder.message.setText(error);
        holder.add_button.setVisibility(View.GONE);
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

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewTitle,add_button,message;
        TextView textViewBody;
        TextView day_left;
        TextView Rm,mcrc,crime_no,case_no,pr_case_no,name,receiving_date;
        ConstraintLayout layout;
        LinearLayout layout_details;
        ImageView tick,type;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.station_name);//
            message = itemView.findViewById(R.id.textView9);//
            textViewBody = itemView.findViewById(R.id.dist_name);//
            add_button = itemView.findViewById(R.id.textView21);
            Rm = itemView.findViewById(R.id.name_rm);//
            day_left = itemView.findViewById(R.id.day_left);//
            mcrc = itemView.findViewById(R.id.case_type);//
            crime_no = itemView.findViewById(R.id.crime_no);//
            case_no = itemView.findViewById(R.id.case_no);//
            pr_case_no = itemView.findViewById(R.id.pr_case_no);//
            tick = itemView.findViewById(R.id.imageView2);
            layout = itemView.findViewById(R.id.layout);
            layout_details = itemView.findViewById(R.id.linearLayout3);
            type = itemView.findViewById(R.id.type);
            name = itemView.findViewById(R.id.person_name);//
            receiving_date = itemView.findViewById(R.id.receiving_date);//
        }
    }
}

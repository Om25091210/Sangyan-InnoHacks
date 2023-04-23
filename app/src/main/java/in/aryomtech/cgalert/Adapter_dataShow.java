package in.aryomtech.cgalert;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.aryomtech.cgalert.Fragments.Adapter.Return_Adapter;
import in.aryomtech.cgalert.Fragments.model.stationData;
import in.aryomtech.cgalert.Fragments.pending_coll;
import io.michaelrocks.paranoid.Obfuscate;

@Obfuscate
public class Adapter_dataShow extends RecyclerView.Adapter<Adapter_dataShow.ViewHolder> {

    List<String> list;
    Context context;

        public Adapter_dataShow(Context context, List<String> list){
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_district_data,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(list.get(position));

        holder.name.setOnClickListener(v-> {
            Phone_numberData phone_numberData = new Phone_numberData();
            Bundle bundle = new Bundle();
            bundle.putString("DistrictName", list.get(position));

            phone_numberData.setArguments(bundle);
            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations( R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left, R.anim.exit_to_right)
                    .add(R.id.data_show,phone_numberData)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.district_name);
        }
    }
}

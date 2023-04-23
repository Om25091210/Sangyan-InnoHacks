package in.aryomtech.cgalert.Fragments.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.aryomtech.cgalert.Fragments.model.user_data;
import in.aryomtech.cgalert.R;
import io.michaelrocks.paranoid.Obfuscate;

@Obfuscate
public class installation_Adapter extends RecyclerView.Adapter<installation_Adapter.ViewHolder> {

    Context context;
    List<user_data> list;

    public installation_Adapter(Context context,List<user_data> list){
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_installations, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(list.get(position).getName()!=null){
            holder.name.setText(list.get(position).getName());
        }
        if (list.get(position).getPhone()!=null){
            holder.phone_number.setText(list.get(position).getPhone());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,phone_number;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            phone_number= itemView.findViewById(R.id.phone_number);
        }
    }
}

package id.ac.umn.nutrimo.menu;
import id.ac.umn.nutrimo.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class MenuAdapter extends FirebaseRecyclerAdapter<MenuModel, MenuAdapter.myViewHolder> {


    public MenuAdapter(@NonNull FirebaseRecyclerOptions<MenuModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MenuAdapter.myViewHolder holder, int position, @NonNull MenuModel model) {
        holder.menuName.setText(model.getName());
        Glide.with(holder.menuImg.getContext()).load(model.getImage()).into(holder.menuImg);
    }

    @NonNull
    @Override
    public MenuAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent,false);

        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder
    {
        ImageView menuImg;
        TextView menuName;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            menuImg = itemView.findViewById(R.id.menuImg);
            menuName = itemView.findViewById(R.id.menuName);

        }
    }
}

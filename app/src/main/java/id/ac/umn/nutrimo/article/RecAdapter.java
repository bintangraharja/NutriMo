package id.ac.umn.nutrimo.article;
import id.ac.umn.nutrimo.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class RecAdapter extends FirebaseRecyclerAdapter<ArticleModel, RecAdapter.myViewHolder> {

    public RecAdapter(@NonNull FirebaseRecyclerOptions<ArticleModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull ArticleModel model) {
        holder.title.setText(model.getTitle());
        Glide.with(holder.articleImg.getContext()).load(model.getImage()).into(holder.articleImg);

        holder.articleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity=(AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.articleWrapper, new detailArticle(model.getImage(),model.getTitle())).addToBackStack(null).commit();
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_item, parent, false);
        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder
    {
        ImageView articleImg;
        TextView title;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            articleImg = itemView.findViewById(R.id.articleImg);
            title = itemView.findViewById(R.id.articleTitle);

        }
    }

}

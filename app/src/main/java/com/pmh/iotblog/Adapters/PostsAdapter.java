package com.pmh.iotblog.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pmh.iotblog.Constant;
import com.pmh.iotblog.Models.Post;
import com.pmh.iotblog.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsHolder>  {

    private Context context;
    private ArrayList<Post> list;
    private ArrayList<Post> ListAll;

    public PostsAdapter(Context context, ArrayList<Post> list) {
        this.context = context;
        this.list    = list;
        this.ListAll = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public PostsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_post, parent, false);
        return new PostsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsHolder holder, int position) {
        Post post = list.get(position);
        Picasso.get().load(Constant.URL+"storage/profiles/"+post.getUser().getPhoto()).into(holder.imgProfile);
        Picasso.get().load(Constant.URL+"storage/posts/"+post.getPhoto()).into(holder.imgPost);
        holder.txtName.setText(post.getUser().getUserName());
        holder.txtComments.setText("View all "+post.getComments()+" comment");
        holder.txtLikes.setText(post.getLikes()+" Likes");
        holder.txtDate.setText(post.getDate());
        holder.txtDesc.setText("@ " +post.getDesc());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // Loc tim kiem
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // kiem tra nhap tim kiem
            ArrayList<Post> filteredList = new ArrayList<>();
            if( constraint.toString().isEmpty()) {
                filteredList.addAll(ListAll);
            } else {
                for(Post post: ListAll ){
                    if (post.getDesc().toLowerCase().contains(constraint.toString().toLowerCase())
                        || post.getUser().getUserName().toLowerCase().contains(constraint.toString().toLowerCase())){
                            filteredList.add(post);
                    }
                }
                FilterResults results = new FilterResults();
                results.values        = filteredList;
                return  results;
            }
            FilterResults results = new FilterResults();
            results.values        = filteredList;
            return  results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((Collection<? extends Post>) results.values);
            notifyDataSetChanged();

        }
    };

    public Filter getFilter() {
        return filter;
    }

    class PostsHolder extends RecyclerView.ViewHolder{

        private TextView txtName,txtDate,txtDesc,txtLikes,txtComments;
        private CircleImageView imgProfile;
        private ImageView imgPost;
        private ImageButton btnPostOption,btnLike,btnComment;


        public PostsHolder(@NonNull View itemView) {
            super(itemView);
            txtName     = itemView.findViewById(R.id.txtPostName);
            txtDate     = itemView.findViewById(R.id.txtPostDate);
            txtDesc     = itemView.findViewById(R.id.txtPostDesc);
            txtLikes    = itemView.findViewById(R.id.txtPostLikes);
            txtComments = itemView.findViewById(R.id.txtPostComments);

            imgProfile      = itemView.findViewById(R.id.imgPostProfile);
            imgPost         = itemView.findViewById(R.id.imgPostPhoto);
            btnPostOption   = itemView.findViewById(R.id.btnPostOption);
            btnLike         = itemView.findViewById(R.id.btnPostLike);
            btnComment      = itemView.findViewById(R.id.btnPostComment);

        }
    }


















}

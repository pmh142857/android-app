package com.pmh.iotblog.Adapters;

import android.app.AlertDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pmh.iotblog.CommentActivity;
import com.pmh.iotblog.Constant;
import com.pmh.iotblog.EditPostActivity;
import com.pmh.iotblog.HomeActivity;
import com.pmh.iotblog.Models.Post;
import com.pmh.iotblog.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsHolder>  {

    private Context context;
    private ArrayList<Post> list;
    private ArrayList<Post> listAll;
    private SharedPreferences preferences;

    public PostsAdapter(Context context, ArrayList<Post> list) {
        this.context = context;
        this.list    = list;
        this.listAll = new ArrayList<>(list);
        preferences  = context.getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);

    }

    @NonNull
    @Override
    public PostsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_post, parent, false);
        return new PostsHolder(view);
    }

    // Cap nhat noi dung ,thiet lap cac truong rieng
    @Override
    public void onBindViewHolder(@NonNull PostsHolder holder, int position) {
        Post post = list.get(position);
        // load anh su dung picasso
        Picasso.get().load(Constant.URL+"storage/profiles/"+post.getUser().getPhoto()).into(holder.imgProfile);
        Picasso.get().load(Constant.URL+"storage/posts/"+post.getPhoto()).into(holder.imgPost);
        holder.txtName.setText(post.getUser().getUserName());
        holder.txtComments.setText("View all "+post.getComments()+" comment");
        holder.txtLikes.setText(post.getLikes()+" Likes");
        holder.txtDate.setText(post.getDate());
        holder.txtDesc.setText("@ " +post.getDesc());

        // Set Like click icon
        holder.btnLike.setImageResource(
            post.isSelfLike() ? R.drawable.ic_favorite_red : R.drawable.ic_favorite_outline
        );

        // Set su kien Like post
        holder.btnLike.setOnClickListener(v->{
            // thay doi icon like
            holder.btnLike.setImageResource(
                    post.isSelfLike() ? R.drawable.ic_favorite_outline : R.drawable.ic_favorite_red
            );
            //
            StringRequest request = new StringRequest(Request.Method.POST,Constant.LIKE_POST,response -> {

                Post mPost = list.get(position);
                try {
                    JSONObject object = new JSONObject(response);
                    // neu request success thi count +-1
                    if(object.getBoolean("success")){

                        mPost.setSelfLike(!post.isSelfLike());
                        mPost.setLikes(mPost.isSelfLike() ? post.getLikes()+1 : post.getLikes()-1 );
                        list.set(position,mPost);
                        notifyItemChanged(position);
                        notifyDataSetChanged();

                    }
                    else {
                        holder.btnLike.setImageResource(
                                post.isSelfLike() ? R.drawable.ic_favorite_red : R.drawable.ic_favorite_outline
                        );
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            },error -> {
                error.printStackTrace();

            }){
                // add token header
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    String token = preferences.getString("token","");
                    HashMap<String, String> map = new HashMap<>();
                    map.put("Authorization","Bearer "+token);
                    return map;
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("id",post.getId()+"");
                    return  map;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(request);

        });

        //Kiem tra nguoi dung va chi hien thi tren bai post cua nguoi dung menu popup
        if(post.getUser().getId() == preferences.getInt("id",0)){
           holder.btnPostOption.setVisibility(View.VISIBLE );
        } else {
            holder.btnPostOption.setVisibility(View.GONE);
        }

        // Menu option
        holder.btnPostOption.setOnClickListener(v->{
            PopupMenu popupMenu = new PopupMenu(context,holder.btnPostOption );
            //tao menu popup
            popupMenu.inflate(R.menu.menu_post_options);
            // set su kien
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getItemId()){
                        // Goi den cac giao dien chinh sua, xoa
                        case R.id.item_edit :{
                            Intent i = new Intent(((HomeActivity) context), EditPostActivity.class);
                           //gui vi tri bai viet
                            i.putExtra("postId",post.getId());
                            i.putExtra("position",position );
                            i.putExtra("text",post.getDesc());
                            context.startActivity(i);
                            return true;

                        }

                        case R.id.item_delete:{
                            //Goi ham delete, gui vi tri bai viet can xoa
                            deletePost(post.getId(),position);
                            return true;

                        }
                        case R.id.item_report:{
                            reportPost();
                        }

                    }
                    return false;
                }
            });
            popupMenu.show();

        });

        // Comment txt
        holder.txtComments.setOnClickListener(v->{
            Intent i = new Intent(((HomeActivity) context), CommentActivity.class );
            // post id
            i.putExtra("postId", post.getId());
            // vi tri bai viet
            i.putExtra("postPosition", position);
            context.startActivity(i);
        });

        // Comment btn
        holder.btnComment.setOnClickListener(v->{
            Intent i = new Intent(((HomeActivity) context),CommentActivity.class );
            // post id
            i.putExtra("postId", post.getId());
            i.putExtra("postPosition", position);
            context.startActivity(i);
        });

    }
    // reportPost
    public void reportPost(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Sorry :((");
        builder.setMessage("Function is in the process of updating!");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();

    }

    // Xoa bai post
    public  void  deletePost(int postId, int position){
       //Tao hop thoai canh bao
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm");
        builder.setMessage("Delete post?");

        // Lua chon
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringRequest request = new StringRequest(Request.Method.POST,Constant.DELETE_POST,response -> {
                    try {
                        JSONObject object = new JSONObject(response);
                        if(object.getBoolean("success")){

                            list.remove(position);
                            notifyItemRemoved(position);
                            notifyDataSetChanged();
                            listAll.clear();
                            listAll.addAll(list);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                },error -> {

                }){
                    // add token

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        String token = preferences.getString("token","");
                        HashMap <String, String> map = new HashMap<>();
                        map.put("Authorization","Bearer"+token);
                        return map;
                    }

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap <String, String> map = new HashMap<>();
                        map.put("id", postId+"");
                        return  map;
                    }
                };

                RequestQueue queue = Volley.newRequestQueue(context);
                queue.add(request);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
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
                filteredList.addAll(listAll);
            } else {
                for(Post post: listAll ){
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

    // Hien thi cac view trong list the hien qua ViewHolder
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

            //hien thi menu popup
            btnPostOption   = itemView.findViewById(R.id.btnPostOption);
            //
            btnLike         = itemView.findViewById(R.id.btnPostLike);
            btnComment      = itemView.findViewById(R.id.btnPostComment);
            //
            btnPostOption.setVisibility(View.GONE);

        }
    }
}

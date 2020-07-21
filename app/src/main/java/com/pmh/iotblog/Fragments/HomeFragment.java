package com.pmh.iotblog.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;
import com.pmh.iotblog.Adapters.PostsAdapter;
import com.pmh.iotblog.Constant;
import com.pmh.iotblog.HomeActivity;
import com.pmh.iotblog.Models.Post;
import com.pmh.iotblog.Models.User;
import com.pmh.iotblog.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    private View view;
    public static RecyclerView recyclerView;
    public static ArrayList<Post> arrayList;
    private SwipeRefreshLayout refreshLayout;
    private PostsAdapter postsAdapter;
    private MaterialToolbar toolbar;
    private SharedPreferences sharedPreferences;

    public HomeFragment(){
        //
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_home,container,false);

        init();
        return view;
    }

    private void init() {

        sharedPreferences = getContext().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        recyclerView = view.findViewById(R.id.recyclerHome);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        refreshLayout = view.findViewById(R.id.swipeHome);
        toolbar = view.findViewById(R.id.toolbarHome);
        ((HomeActivity) getContext()).setSupportActionBar(toolbar);
        
        getPosts();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPosts();
            }
        });
        
    }

    // Duyet mang va lay cac gia tri trong Api

    private void getPosts() {
        arrayList = new ArrayList<>();
        refreshLayout.setRefreshing(true);

        StringRequest request = new StringRequest(Request.Method.GET, Constant.POSTS,response -> {

            try {
                JSONObject object = new JSONObject(response);
                if(object.getBoolean("success")){
                    JSONArray array = new JSONArray(object.getString("posts"));
                    for (int i = 0; i < array.length(); i++){
                        JSONObject postObject = array.getJSONObject(i);
                        JSONObject userObject = postObject.getJSONObject("user");

                        User user = new User();
                        user.setId(userObject.getInt("id"));
                        user.setUserName(userObject.getString("name")+" "+userObject.getString("lastname"));
                        user.setPhoto(userObject.getString("photo"));

                        Post post = new Post();
                        post.setId(postObject.getInt("id"));
                        post.setUser(user);

                        post.setLikes(postObject.getInt("likesCount"));
                        post.setComments(postObject.getInt("commentsCount"));
                        post.setDate(postObject.getString("created_at"));
                        post.setDesc(postObject.getString("desc"));
                        post.setPhoto(postObject.getString("photo"));
                        post.setSelfLike(postObject.getBoolean("selfLike"));

                        arrayList.add(post);

                    }

                    // Do du lieu ra
                    postsAdapter = new PostsAdapter(getContext(),arrayList);
                    recyclerView.setAdapter(postsAdapter);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            refreshLayout.setRefreshing(false);


        }, error -> {
            error.printStackTrace();
            refreshLayout.setRefreshing(false);
        }){

            // provide token in header

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = sharedPreferences.getString("token","");
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization","Bearer "+token);
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }






}

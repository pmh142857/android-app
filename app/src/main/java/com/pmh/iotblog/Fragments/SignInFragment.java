package com.pmh.iotblog.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.pmh.iotblog.AuthActivity;
import com.pmh.iotblog.Constant;
import com.pmh.iotblog.HomeActivity;
import com.pmh.iotblog.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignInFragment extends Fragment {

    private View view;
    private TextInputLayout layoutEmail, layoutPassword;
    private TextInputEditText txtEmail,txtPassword;
    private TextView txtSignUp;
    private Button btnSignIn;
    private ProgressDialog dialog;


    public  SignInFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_sign_in,container,false);
        init();
        return view;

    }

    private void init() {
        layoutPassword  = view.findViewById(R.id.textLayoutPasswordSignIn);
        layoutEmail     = view.findViewById(R.id.textLayoutEmailSignIn);
        txtPassword     = view.findViewById(R.id.txtPasswordSignIn);
        txtSignUp       = view.findViewById(R.id.txtSignUp);
        txtEmail        = view.findViewById(R.id.txtEmailSignIn);
        btnSignIn       = view.findViewById(R.id.btnSignIn);
        dialog          = new ProgressDialog(getContext());
        dialog.setCancelable(false);

        txtSignUp.setOnClickListener(v->{
            //change fragments
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameAuthContainer,new SignUpFragment()).commit() ;

        });
        btnSignIn.setOnClickListener(v->{
            // validate fields first
            if (validate() ){
                login();
            }
        });

        //
        txtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!txtEmail.getText().toString().isEmpty()){
                    layoutEmail.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //
        txtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(txtPassword.getText().toString().length()>7){
                    layoutPassword.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private boolean validate(){
        if (txtEmail.getText().toString().isEmpty()){
            layoutEmail.setErrorEnabled(true);
            layoutEmail.setError("Email is Reguired");
            return  false;
        }
        if (txtPassword.getText().toString().length()< 8){
            layoutPassword.setErrorEnabled(true);
            layoutPassword.setError("Required at least 8 characters");
            return  false;
        }
        return true;
    }

    // login
    public void login(){
        dialog.setMessage("Logging in");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Constant.LOGIN,response ->{
            // get response if connection success
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")){
                    JSONObject user = object.getJSONObject("user");
                    //make shared preference user
                    SharedPreferences userPref  = getActivity().getApplicationContext()
                            .getSharedPreferences("user",getContext().MODE_PRIVATE );
                    SharedPreferences.Editor editor = userPref.edit();
                    editor.putString("token",object.getString("token"));
                    editor.putString("name",user.getString("name"));
                    editor.putString("lastname",user.getString("lastname"));
                    editor.putString("photo",user.getString("photo"));
                    editor.putBoolean("isLoggedIn",true);
                    editor.apply();
                    //if success goto intent Home and Toast
                    startActivity(new Intent(((AuthActivity) getContext()), HomeActivity.class));
                    ((AuthActivity) getContext()).finish();
                    Toast.makeText(getContext(), "Login Success", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialog.dismiss();

        },error -> {
            // error if connection not success
            error.printStackTrace();
            dialog.dismiss();
        }){
            // add parameters
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap <String, String> map = new HashMap<>();
                map.put("email",txtEmail.getText().toString().trim());
                map.put("password",txtPassword.getText().toString());
                return map;
            }
        };
        //add this request to request
        Context context;
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }

}

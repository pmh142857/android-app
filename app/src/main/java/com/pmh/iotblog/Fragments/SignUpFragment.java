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
import com.pmh.iotblog.UserInfoActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpFragment extends Fragment {
    private View view;
    private TextInputLayout layoutEmail, layoutPassword,layoutConfirm;
    private TextInputEditText txtEmail,txtPassword,txtConfirm;
    private TextView txtSignIn;
    private Button btnSignUp;
    private ProgressDialog dialog;

    public  SignUpFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_sign_up,container,false);
        init();
        return view;
    }

    private void init() {
        layoutPassword  = view.findViewById(R.id.textLayoutPasswordSignUp);
        layoutEmail     = view.findViewById(R.id.textLayoutEmailSignUp);
        layoutConfirm   = view.findViewById(R.id.textLayoutConfirmSignUp);
        txtConfirm      = view.findViewById(R.id.txtConfirmSignUp);
        txtPassword     = view.findViewById(R.id.txtPasswordSignUp);
        txtSignIn       = view.findViewById(R.id.txtSignIn);
        txtEmail        = view.findViewById(R.id.txtEmailSignUp);
        btnSignUp       = view.findViewById(R.id.btnSignUp);
        dialog          = new ProgressDialog(getContext());
        dialog.setCancelable(false);

        txtSignIn.setOnClickListener(v->{
            //change fragments
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameAuthContainer,new SignInFragment()).commit() ;

        });
        btnSignUp.setOnClickListener(v->{
            // validate fields first
            if (validate() ){
                register();

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

        //
        txtConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(txtConfirm.getText().toString().equals(txtPassword.getText().toString())){

                    layoutConfirm.setErrorEnabled(false);
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

        if (!txtConfirm.getText().toString().equals(txtPassword.getText().toString())){
            layoutConfirm.setErrorEnabled(true);
            layoutConfirm.setError("Password dose not match");
            return  false;
        }
        return true;
    }

    //register
    public void register(){
        dialog.setMessage("Registering");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Constant.REGISTER, response ->{
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

                    // neu dang ky hoan thanh hien thi thong tin dang ky
                    startActivity(new Intent(((AuthActivity) getContext()), UserInfoActivity.class));
                    ((AuthActivity) getContext()).finish();
                    Toast.makeText(getContext(), "Register Success", Toast.LENGTH_SHORT).show();

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
                HashMap<String, String> map = new HashMap<>();
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

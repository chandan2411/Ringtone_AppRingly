package com.ringly.customer_app.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ringly.customer_app.R;
import com.ringly.customer_app.entities.CheckNetwork;
import com.ringly.customer_app.entities.Constant;
import com.ringly.customer_app.entities.DatabaseReferences;
import com.ringly.customer_app.entities.Logger;
import com.ringly.customer_app.entities.MySharedPref;
import com.ringly.customer_app.models.UserModel;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class OtpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "OtpActivity.class";
    private String verificationid;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText editText;
    private String phonenumber;
    private CallbackManager callbackManager;
    private MySharedPref sharedPref;
    private final static int RC_SIGN_IN = 2;
    private GoogleSignInClient mGoogleSignInClient;
    private Button buttonSignIn;
    private ImageView gsign_in_button;
    private ImageView ivBtnFacebookLogin;
    private ImageView ivBack;
    FirebaseAuth.AuthStateListener mAuthListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        setTitle("OTP");
        if (getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressbar);
        editText = findViewById(R.id.editTextCode);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        gsign_in_button = findViewById(R.id.gsign_in_button);
        ivBtnFacebookLogin = findViewById(R.id.ivBtnFacebookLogin);
        ivBack = findViewById(R.id.ivBack);
        sharedPref = new MySharedPref(this);
        findViewById(R.id.gsign_in_button).setOnClickListener(this);
        editText.addTextChangedListener(mTextWatcher);
        ivBack.setOnClickListener(this);
        buttonSignIn.setOnClickListener(this);
        ivBtnFacebookLogin.setOnClickListener(this);
        gsign_in_button.setOnClickListener(this);

        phonenumber = getIntent().getStringExtra("phonenumber");
        sendVerificationCode(phonenumber);
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        saveFaceBookData(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(OtpActivity.this, "Facebook login cancel", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(OtpActivity.this, exception.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = firebaseAuth -> {
            if (firebaseAuth.getCurrentUser() != null) {
                startActivity(new Intent(OtpActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }

        };
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // check Fields For Empty Values
            checkFieldsForEmptyValues();
        }
    };

    void checkFieldsForEmptyValues() {
        String number = editText.getText().toString().trim();
        if (number.equals("")) {
            buttonSignIn.setEnabled(false);
        } else {
            buttonSignIn.setEnabled(true);
            if (number.length() ==6) {
                buttonSignIn.setBackgroundResource(R.drawable.roundbtn2);
            } else {
                buttonSignIn.setBackgroundResource(R.drawable.roundbtn1);
            }
        }
    }


    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationid, code);
        signInWithCredential(credential);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //      updateUI(null);
                    }
                });
    }


    private void saveFaceBookData(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("TAG", object.toString());
                        try {
                            String fName = object.getString("first_name");
                            String lName = object.getString("last_name");
                            String userName = fName+" "+lName;
                            String userEmail="";

                            try {
                                userEmail = object.getString("email");
                            }catch (Exception ex){
                                Logger.logE(TAG, "Email Not found", ex);
                            }
                            String userId = object.getString("id");
                            String userImageUrl = "https://graph.facebook.com/" + userId + "/picture?type=normal";

                            /*saving to session manager User Id, Email, Name*/
                            sharedPref.writeBoolean(Constant.IS_USER_LOGIN, true);
                            sharedPref.writeString(Constant.SIGN_UP_MODE, Constant.GMAIL);
                            sharedPref.writeString(Constant.USER_EMAIL, userEmail);
                            sharedPref.writeString(Constant.USER_NAME, userName);
                            sharedPref.writeString(Constant.USER_ID, userId);
                            if (userImageUrl!=null)
                                sharedPref.writeString(Constant.USER_IMAGE_URL, userImageUrl.toString());
                            /*Create table for user in firebase realtime database*/
                            registerUserInTable(userId, userName, userEmail);

                            Intent intent = new Intent(OtpActivity.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }
    private void registerUserInTable(String userId, String userName, String userEmail) {
        DatabaseReferences.registerUser(new UserModel(userId, userName, userEmail));
    }




    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveUserDetails(Constant.PHONE);
                            Intent intent = new Intent(OtpActivity.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(OtpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }

    private void saveUserDetails(String mode) {
        MySharedPref sharedPref = new MySharedPref(this);
        if (mode.equals(Constant.PHONE)){
            sharedPref.writeBoolean(Constant.IS_USER_LOGIN, true);
            sharedPref.writeString(Constant.SIGN_UP_MODE, mode);
            sharedPref.writeString(Constant.MOBILE_NUMBER, phonenumber);
            sharedPref.writeString(Constant.USER_ID, phonenumber);
            DatabaseReferences.registerUser(new UserModel(phonenumber, "",""));

        }else if (mode.equals(Constant.GMAIL)){
            FirebaseUser user = mAuth.getCurrentUser();
            String userName = user.getDisplayName();
            String userEmail = user.getEmail();
            String userId = user.getUid();
            Uri userImageUrl = user.getPhotoUrl();
            sharedPref.writeBoolean(Constant.IS_USER_LOGIN, true);
            sharedPref.writeString(Constant.SIGN_UP_MODE, mode);
            sharedPref.writeString(Constant.USER_EMAIL, userEmail);
            sharedPref.writeString(Constant.USER_NAME, userName);
            sharedPref.writeString(Constant.USER_ID, userId);
            if (userImageUrl != null)
                sharedPref.writeString(Constant.USER_IMAGE_URL, userImageUrl.toString());
            DatabaseReferences.registerUser(new UserModel(userId, userName, userEmail));
        }


    }

    private void sendVerificationCode(String number){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationid = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null){
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(OtpActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();

        }
    };

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onClick(View v) {
        int Id = v.getId();

        switch (Id){
            case R.id.ivBack:
                finish();
                break;
             case R.id.buttonSignIn:
                 String code = editText.getText().toString().trim();
                 if ((code.isEmpty() || code.length() < 6)){
                     editText.setError("Enter code...");
                     editText.requestFocus();
                     return;
                 }
                 verifyCode(code);
                break;
             case R.id.gsign_in_button:
                 if (CheckNetwork.checkNet(this))
                     signIn();
                 else
                     Toast.makeText(this, Constant.NO_INTERNET, Toast.LENGTH_SHORT).show();
                break;
             case R.id.ivBtnFacebookLogin:
                 LoginManager.getInstance().logInWithReadPermissions(OtpActivity.this, Arrays.asList("public_profile"));
                 break;
             case R.id.btnSignIn:
                 signOut();
                break;
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            saveUserDetails(Constant.GMAIL);
                            //    updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //     updateUI(null);
                        }

                        // ...
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

}

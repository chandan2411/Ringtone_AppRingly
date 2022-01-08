package com.ringly.customer_app.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ringly.customer_app.R;
import com.ringly.customer_app.entities.CheckNetwork;
import com.ringly.customer_app.entities.Constant;
import com.ringly.customer_app.entities.CountryData;
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
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class RegisterActivity extends AppCompatActivity implements
        View.OnClickListener {
    private static final String TAG = "length";
    private Spinner spinner;
    private EditText editText;
    SignInButton button;
    private Button buttonContinue;
    FirebaseAuth mAuth;
    private final static int RC_SIGN_IN = 2;
    private GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth.AuthStateListener mAuthListener;
    private MySharedPref sharedPref;
    private ImageView ivBtnFacebookLogin;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("SignUp");
        buttonContinue = findViewById(R.id.buttonContinue);
        spinner = findViewById(R.id.spinnerCountries);
        ivBtnFacebookLogin = findViewById(R.id.ivBtnFacebookLogin);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, CountryData.countryNames));
        editText = findViewById(R.id.editTextPhone);

        findViewById(R.id.gsign_in_button).setOnClickListener((View.OnClickListener) this);
        sharedPref = new MySharedPref(this);

        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        editText.addTextChangedListener(mTextWatcher);

        // run once to disable if empty
        checkFieldsForEmptyValues();

        /*Facebook login*/
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        saveFaceBookData(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(RegisterActivity.this, "Facebook login cancel", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(RegisterActivity.this, exception.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];

                String number = editText.getText().toString().trim();

                if (number.isEmpty() || number.length() < 10) {
                    editText.setError("Valid number is required");
                    editText.requestFocus();
                    return;
                }


                String phonenumber = "+" + code + number;

                Intent intent = new Intent(RegisterActivity.this, OtpActivity.class);
                intent.putExtra("phonenumber", phonenumber);
                startActivity(intent);
            }
        });

        ivBtnFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(RegisterActivity.this, Arrays.asList("public_profile"));
            }
        });


        mAuthListener = firebaseAuth -> {
            if (firebaseAuth.getCurrentUser() != null) {
                startActivity(new Intent(RegisterActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }

        };

        TextView tv_by_continuing = (TextView) findViewById(R.id.view_terms_and_privacy);
        tv_by_continuing.setMovementMethod(LinkMovementMethod.getInstance());


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

                            Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
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
        Button b = (Button) findViewById(R.id.buttonContinue);
        String number = editText.getText().toString().trim();
        if (number.equals("")) {
            b.setEnabled(false);
        } else {
            b.setEnabled(true);
            if (number.length() == 10) {
                buttonContinue.setBackgroundResource(R.drawable.roundbtn2);
            } else {
                buttonContinue.setBackgroundResource(R.drawable.roundbtn1);
            }
        }
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

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            saveUserDetails();
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

    private void saveUserDetails() {
        FirebaseUser user = mAuth.getCurrentUser();

        String userName = user.getDisplayName();
        String userEmail = user.getEmail();
        String userId = user.getUid();
        Uri userImageUrl = user.getPhotoUrl();
        /*saving to session manager User Id, Email, Name*/
        sharedPref.writeBoolean(Constant.IS_USER_LOGIN, true);
        sharedPref.writeString(Constant.SIGN_UP_MODE, Constant.GMAIL);
        sharedPref.writeString(Constant.USER_EMAIL, userEmail);
        sharedPref.writeString(Constant.USER_NAME, userName);
        sharedPref.writeString(Constant.USER_ID, userId);
        if (userImageUrl != null)
            sharedPref.writeString(Constant.USER_IMAGE_URL, userImageUrl.toString());
        /*Create table for user in firebase realtime database*/
        registerUserInTable(userId, userName, userEmail);
    }

    private void registerUserInTable(String userId, String userName, String userEmail) {
        DatabaseReferences.registerUser(new UserModel(userId, userName, userEmail));
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
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

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.gsign_in_button) {
            if (CheckNetwork.checkNet(this))
                signIn();
            else
                Toast.makeText(this, Constant.NO_INTERNET, Toast.LENGTH_SHORT).show();
        } else if (i == R.id.btnSignIn) {
            signOut();
        }
    }


}


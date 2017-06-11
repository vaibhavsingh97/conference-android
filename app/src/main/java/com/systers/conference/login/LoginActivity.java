package com.systers.conference.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.systers.conference.R;
import com.systers.conference.model.User;
import com.systers.conference.register.RegisterActivity;
import com.systers.conference.util.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A login screen that offers login via Google and Facebook.
 */
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    public static final String USER_DATA = "user_data";
    private static final String LOG_TAG = LogUtils.makeLogTag(LoginActivity.class);
    private static final int GOOGLE_SIGN_IN = 9001;
    @BindView(R.id.google_sign_in_button)
    SignInButton mSignInButton;
    @BindView(R.id.fb_login_button)
    LoginButton mLoginButton;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mCallbackManager;

    @OnClick(R.id.google_sign_in_button)
    public void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
        mSignInButton.setSize(SignInButton.SIZE_WIDE);
        mCallbackManager = CallbackManager.Factory.create();
        mLoginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_work_history"));
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFbSignInResult(loginResult);
            }

            @Override
            public void onCancel() {
                LogUtils.LOGE(LOG_TAG, "Canceled");
            }

            @Override
            public void onError(FacebookException error) {
                LogUtils.LOGE(LOG_TAG, error.toString());
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        LogUtils.LOGE(LOG_TAG, connectionResult.getErrorMessage());
    }

    private void handleFbSignInResult(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            User user = new User();
                            if (!object.isNull("first_name")) {
                                String firstName = object.getString("first_name");
                                user.setFirstName(firstName);
                            }
                            if (!object.isNull("last_name")) {
                                String lastName = object.getString("last_name");
                                user.setLastName(lastName);
                            }
                            if (!object.isNull("email")) {
                                String email = object.getString("email");
                                user.setEmail(email);
                            }
                            if (!object.isNull("work")) {
                                JSONArray workArray = object.getJSONArray("work");
                                JSONObject workObject = workArray.getJSONObject(0);
                                if (!workObject.isNull("employer")) {
                                    JSONObject employer = workObject.getJSONObject("employer");
                                    LogUtils.LOGE(LOG_TAG, employer.toString());
                                    if (!employer.isNull("name")) {
                                        String employerName = employer.getString("name");
                                        user.setCompanyName(employerName);
                                        LogUtils.LOGE(LOG_TAG, employerName);
                                    }
                                }
                                if (!workObject.isNull("position")) {
                                    JSONObject position = workObject.getJSONObject("position");
                                    LogUtils.LOGE(LOG_TAG, position.toString());
                                    if (!position.isNull("name")) {
                                        String role = position.getString("name");
                                        user.setRole(role);
                                        LogUtils.LOGE(LOG_TAG, role);
                                    }
                                }
                            }
                            startRegisterActivity(user);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,email,work");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void startRegisterActivity(User user) {
        startActivity(new Intent(this, RegisterActivity.class).putExtra("user_data", user));
    }

    private void handleGoogleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            User user = new User();
            user.setFirstName(account.getGivenName());
            user.setLastName(account.getFamilyName());
            user.setEmail(account.getEmail());
            startRegisterActivity(user);
        } else {
            LogUtils.LOGE(LOG_TAG, "Failed Sign In");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(result);
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
}


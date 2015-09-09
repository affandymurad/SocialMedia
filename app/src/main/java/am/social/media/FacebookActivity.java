package am.social.media;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;


public class FacebookActivity extends ActionBarActivity {
    private TextView info;
    private TextView pengguna;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private ProfilePictureView profilePictureView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.FacebookTheme);
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.setApplicationId(getResources().getString(R.string.facebook_app_id));
        setContentView(R.layout.activity_facebook);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        info = (TextView)findViewById(R.id.info);
        pengguna = (TextView)findViewById(R.id.pengguna);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton)findViewById(R.id.login_button);
        profilePictureView = (ProfilePictureView) findViewById(R.id.userImage);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getFaceBookProfileDetails(loginResult.getAccessToken());
                info.setText(
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + loginResult.getAccessToken().getToken()

                );
            }

            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                info.setText("Login attempt failed.");
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void getFaceBookProfileDetails(final AccessToken accessToken) {

        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(final JSONObject object,GraphResponse response) {
                        JSONObject json = response.getJSONObject();
                        try {
                            Toast.makeText(getApplicationContext(), "User Name is: "+object.getString("name").toString(), Toast.LENGTH_LONG).show();
                            profilePictureView.setProfileId(object.optString("id"));
                            pengguna.setText(object.getString("name").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        request.executeAsync();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

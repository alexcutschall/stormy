package com.example.alexcutschall.stormy;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexcutschall.stormy.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private CurrentWeather currentWeather;
    private ImageView iconImageView;
    final double latitude = 37.8267;
    final double longitude = -122.4233;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getForecast(latitude, longitude);
    }

    private void getForecast(double latitude, double longitude) {
        final ActivityMainBinding binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        iconImageView = findViewById(R.id.iconImageView);

        TextView darkSky = findViewById(R.id.darkSkyAttribution);
        darkSky.setMovementMethod(LinkMovementMethod.getInstance());

        String apiKey = "1b24f7bd21d0691e0ceb56b26d81b448";

        String forecastUrl = "https://api.darksky.net/forecast/" + apiKey + "/" + latitude + "," + longitude;

        if (isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(forecastUrl)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            currentWeather = getCurrentDetails(jsonData);

                            final CurrentWeather displayWeather = new CurrentWeather(
                                    currentWeather.getLocation(),
                                    currentWeather.getIcon(),
                                    currentWeather.getTime(),
                                    currentWeather.getTemperature(),
                                    currentWeather.getHumidity(),
                                    currentWeather.getPrecipChance(),
                                    currentWeather.getSummary(),
                                    currentWeather.getTimezone()
                            );

                            binding.setWeather(displayWeather);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Drawable drawable = getResources().getDrawable(displayWeather.getIconId());
                                    iconImageView.setImageDrawable(drawable);
                                }
                            });

                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "IO exception Caught", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON exception Caught", e);
                    }
                }
            });
        }
    }

    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);

        String timeZone = forecast.getString("timezone");
        JSONObject currently = forecast.getJSONObject("currently");
        Log.i(TAG, "From JSON: " + timeZone);

        CurrentWeather currentWeather = new CurrentWeather();

        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setTemperature(currently.getDouble("temperature"));
        currentWeather.setLocation("Alcatraz Island, CA");
        currentWeather.setPrecipChance(currently.getDouble("precipProbability"));
        currentWeather.setSummary(currently.getString("summary"));
        currentWeather.setTimezone(timeZone);

        Log.d(TAG, currentWeather.getFormattedTime());

        return currentWeather;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        } else {
            NetworkUnavailableDialogueFragment networkDialogue = new NetworkUnavailableDialogueFragment();
            networkDialogue.show(getFragmentManager(), "Network Unavailable Dialogue");
        }
        return isAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogueFragment dialogue = new AlertDialogueFragment();
        dialogue.show(getFragmentManager(), "Error Dialogue");
    }

    public void refreshOnClick(View view) {
        Toast.makeText(this, "Refreshing data", Toast.LENGTH_LONG).show();
        getForecast(longitude, latitude);
    }
}

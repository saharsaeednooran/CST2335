package com.example.sahar.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherForecast extends Activity {

    protected static final String urlString = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric";
    protected static final String URL_IMAGE = "http://openweathermap.org/img/w/";
    protected static final String ACTIVITY_NAME = "WeatherForecast";
    private ImageView weatherImageView;
    private TextView currentTextView, minTextView, maxTextView;
    private ProgressBar normProgBar;
    private TextView targetLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        weatherImageView = (ImageView) findViewById(R.id.currentWeatherImageView);
        currentTextView = (TextView) findViewById(R.id.currentTemp);
        minTextView = (TextView) findViewById(R.id.minTemp);
        maxTextView = (TextView) findViewById(R.id.maxTemp);
        normProgBar = (ProgressBar) findViewById(R.id.progressBar);
        normProgBar.setVisibility(View.VISIBLE);
        normProgBar.setMax(100);

        new ForecastQuery().execute(null, null, null);
        //new ForecastQuery().execute();

    }

    private class ForecastQuery extends AsyncTask<String, Integer, String> {

        private String currentTemp = null;
        private String minTemp = null;
        private String maxTemp = null;
        private Bitmap weatherImage = null;
        private String iconFilename = null;

        @Override
        protected String doInBackground(String... strings) {
            InputStream in = null;
            HttpURLConnection conn = null;
            try {
                URL url = new URL(urlString);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                Log.i(ACTIVITY_NAME, "HttpURLConnection established");

                //------------------------------------------
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(conn.getInputStream(), null);
                parser.nextTag();

                while (parser.next() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    String name = parser.getName();
                    if (name.equals("temperature")) {
                        currentTemp = parser.getAttributeValue(null, "value");
                        //Log.i(ACTIVITY_NAME, "Current temperature: " + currentTemp);
                        publishProgress(25);
                        minTemp = parser.getAttributeValue(null, "min");
                        //Log.i(ACTIVITY_NAME, "minTemp:" + minTemp);
                        Thread.sleep(4000);
                        publishProgress(50);
                        maxTemp = parser.getAttributeValue(null, "max");
                        Thread.sleep(4000);
                        publishProgress(75);
                    } else if (name.equals("weather")) {
                        iconFilename = parser.getAttributeValue(null, "icon") + ".png";
                        Log.i(ACTIVITY_NAME, "Looking for image: " + iconFilename);
                        if (fileExistence(iconFilename)) {

                            weatherImage = readImage(iconFilename);
                            Log.i(ACTIVITY_NAME, "locally read image: " + iconFilename);

                        } else {
                            String bitmapUrl = URL_IMAGE + iconFilename;
                            weatherImage = getImage(new URL(bitmapUrl), iconFilename);
                            Log.i(ACTIVITY_NAME, "Download image: " + iconFilename);
                        }
                        publishProgress(100);
                    }
                }
            } catch (MalformedURLException e) {
                Log.i(ACTIVITY_NAME, "MalformedURLException: " + e.getMessage());
            } catch (IOException e) {
                Log.i(ACTIVITY_NAME, "IOException: " + e.getMessage());
            } catch (XmlPullParserException e) {
                Log.i(ACTIVITY_NAME, "XmlPullParserException: " + e.getMessage());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            normProgBar.setProgress(values[0]);
            normProgBar.setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            currentTextView.setText("Current Temperature: " + currentTemp);
            minTextView.setText("Minimum Temperature: " + minTemp);
            maxTextView.setText("Maximum Temperature: " + maxTemp);
            weatherImageView.setImageBitmap(weatherImage);
            normProgBar.setVisibility(View.INVISIBLE);

        }

        public Bitmap getImage(URL url, String fName) {
            HttpURLConnection connection = null;
            Bitmap image = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    image = BitmapFactory.decodeStream(connection.getInputStream());
                    FileOutputStream outputStream = openFileOutput(fName, Context.MODE_PRIVATE);
                    image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (Exception e) {
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return image;
        }

        public Bitmap readImage(String fname) {

            try {
                FileInputStream fis = openFileInput(fname);
                Bitmap b = BitmapFactory.decodeStream(fis);
                fis.close();
                return b;
            } catch (Exception e) {
            }
            return null;
        }


        public boolean fileExistence(String fname) {
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

    }
}


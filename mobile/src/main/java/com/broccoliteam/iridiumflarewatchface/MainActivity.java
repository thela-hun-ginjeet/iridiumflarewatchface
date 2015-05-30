package com.broccoliteam.iridiumflarewatchface;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.MessageApi;

import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.DataEventBuffer;

public class MainActivity extends ActionBarActivity {
    private Button syncButton;
    private TextView infoTextView;

    private GoogleApiClient mGoogleApiClient;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        syncButton = (Button) this.findViewById(R.id.syncButton);
        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoTextView.setText("ВАУ Я СМОГ!");
            }
        });

        infoTextView = (TextView) this.findViewById(R.id.infoTextView);

        mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
            .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(Bundle bundle) {
                    Log.d(TAG, "GoogleApiClient is Connected");
                    Wearable.NodeApi.addListener(mGoogleApiClient, new NodeApi.NodeListener() {
                        @Override
                        public void onPeerConnected(Node node) {
                            Log.d(TAG, "A node is connected and its id: " + node.getId());
                        }

                        @Override
                        public void onPeerDisconnected(Node node) {
                            Log.d(TAG, "A node is disconnected and its id: " + node.getId());
                        }
                    });

                    Wearable.MessageApi.addListener(mGoogleApiClient, new MessageApi.MessageListener() {
                        @Override
                        public void onMessageReceived(MessageEvent messageEvent) {
                            Log.d(TAG, "You have a message from " + messageEvent.getPath());
                        }
                    });

                    Wearable.DataApi.addListener(mGoogleApiClient, new DataApi.DataListener() {
                        @Override
                        public void onDataChanged(DataEventBuffer dataEvents) {
                            Log.d(TAG, "Your data is changed");
                        }
                    });
                }

                @Override
                public void onConnectionSuspended(int i) {
                    Log.d(TAG, "GoogleApiClient is ConnectionSuspended");
                }
            })
            .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(ConnectionResult connectionResult) {
                    Log.d(TAG, "The connection of GoogleApiClient is failed");
                }
            })
            .addApi(Wearable.API) // tell Google API that we want to use Warable API
            .build();
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

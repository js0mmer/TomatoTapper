package tk.jsommer.tomatotapper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private int tomatoes;
    private int fingers;
    private int gardens;
    private int orchards;
    private int shipments;

    private Handler handler;
    private int track;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData(getApplicationContext());

        handler = new Handler();
        startRepeatingTask();
    }

    public void resetButtonClicked(View view) {
        EditText editText = (EditText) findViewById(R.id.resetConfirm);

        if(editText.getVisibility() == View.INVISIBLE) {
            editText.setVisibility(View.VISIBLE);
        } else {
            editText.setVisibility(View.INVISIBLE);

            if(editText.getText().toString().equals("YES")) {
                tomatoes = 0;
                fingers = 0;
                gardens = 0;
                orchards = 0;
                shipments = 0;

                updateTomatoes();
                updateTps();
                updateFingers();
                updateGardens();
                updateOrchards();
                updateShipments();

                saveData(getApplicationContext());

                Toast.makeText(getApplicationContext(), getString(R.string.gameReset), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void addTomato(View view) {
        tomatoes++;
        updateTomatoes();
        final ImageButton button = (ImageButton) findViewById(R.id.tomatoButton);

        button.setScaleX(0.75f);
        button.setScaleY(0.75f);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                button.setScaleX(0.8f);
                button.setScaleY(0.8f);
            }
        }, 100);
    }

    public void addTomatoes(int count) {
        tomatoes += count;
        updateTomatoes();
    }

    private void updateTomatoes() {
        TextView text = (TextView) findViewById(R.id.tomatoText);
        text.setText(tomatoes + " " + getString(R.string.tomatoes));
    }

    private int getTps() {
        return fingers + gardens * 8 + orchards * 64 + shipments * 512;
    }

    private void updateTps() {
        TextView text = (TextView) findViewById(R.id.tpsText);
        text.setText(getTps() + " " + getString(R.string.tps));
    }

    public void buyFinger(View view) {
        int price = fingers == 0 ? 10 : fingers * 10;

        if(tomatoes < price) {
            Toast.makeText(getApplicationContext(), getString(R.string.notEnough), Toast.LENGTH_SHORT).show();
            Button button = (Button) findViewById(R.id.buyFingerButton);
            button.setEnabled(false);
            return;
        }

        tomatoes -= price;
        fingers++;
        updateTomatoes();
        updateTps();
        updateFingers();
    }

    private void updateFingers() {
        TextView text = (TextView) findViewById(R.id.fingersText);

        String str = getString(R.string.fingers);

        TextView price = (TextView) findViewById(R.id.fingersPrice);

        if(fingers == 1) {
            str = str.substring(0, str.length() - 1);
        } else if(fingers == 100) {
            Button button = (Button) findViewById(R.id.buyFingerButton);
            button.setEnabled(false);

            price.setVisibility(View.INVISIBLE);
        } else if(fingers == 0) {
            price.setText(String.valueOf(10));
            return;
        }

        text.setText(fingers + " " + str);
        price.setText(String.valueOf(fingers * 10));
    }

    public void buyGarden(View view) {
        int price = gardens == 0 ? 100 : gardens * 100;

        if(tomatoes < price) {
            Toast.makeText(getApplicationContext(), getString(R.string.notEnough), Toast.LENGTH_SHORT).show();
            Button button = (Button) findViewById(R.id.buyGardenButton);
            button.setEnabled(false);
            return;
        }

        tomatoes -= price;
        gardens++;
        updateTomatoes();
        updateTps();
        updateGardens();
    }

    private void updateGardens() {
        TextView text = (TextView) findViewById(R.id.gardensText);

        String str = getString(R.string.gardens);

        TextView price = (TextView) findViewById(R.id.gardensPrice);

        if(gardens == 1) {
            str = str.substring(0, str.length() - 1);
        } else if(gardens == 100) {
            Button button = (Button) findViewById(R.id.buyGardenButton);
            button.setEnabled(false);

            price.setVisibility(View.INVISIBLE);
        } else if(gardens == 0) {
            price.setText(String.valueOf(100));
            return;
        }

        text.setText(gardens + " " + str);
        price.setText(String.valueOf(gardens * 100));
    }

    public void buyOrchard(View view) {
        int price = orchards == 0 ? 1000 : orchards * 1000;

        if(tomatoes < price) {
            Toast.makeText(getApplicationContext(), getString(R.string.notEnough), Toast.LENGTH_SHORT).show();
            Button button = (Button) findViewById(R.id.buyOrchardButton);
            button.setEnabled(false);
            return;
        }

        tomatoes -= price;
        orchards++;
        updateTomatoes();
        updateTps();
        updateOrchards();
    }

    private void updateOrchards() {
        TextView text = (TextView) findViewById(R.id.orchardsText);

        String str = getString(R.string.orchards);

        TextView price = (TextView) findViewById(R.id.orchardsPrice);

        if(orchards == 1) {
            str = str.substring(0, str.length() - 1);
        } else if(orchards == 100) {
            Button button = (Button) findViewById(R.id.buyOrchardButton);
            button.setEnabled(false);

            price.setVisibility(View.INVISIBLE);
        } else if(orchards == 0) {
            price.setText(String.valueOf(1000));
            return;
        }

        text.setText(orchards + " " + str);
        price.setText(String.valueOf(orchards * 1000));
    }

    public void buyShipment(View view) {
        int price = shipments == 0 ? 10000 : shipments * 10000;

        if(tomatoes < price) {
            Toast.makeText(getApplicationContext(), getString(R.string.notEnough), Toast.LENGTH_SHORT).show();
            Button button = (Button) findViewById(R.id.buyShipmentButton);
            button.setEnabled(false);
            return;
        }

        tomatoes -= price;
        shipments++;
        updateTomatoes();
        updateTps();
        updateShipments();
    }

    private void updateShipments() {
        TextView text = (TextView) findViewById(R.id.shipmentsText);

        String str = getString(R.string.shipments);

        TextView price = (TextView) findViewById(R.id.shipmentsPrice);

        if(shipments == 1) {
            str = str.substring(0, str.length() - 1);
        } else if(shipments == 100) {
            Button button = (Button) findViewById(R.id.buyShipmentButton);
            button.setEnabled(false);

            price.setVisibility(View.INVISIBLE);
        } else if(shipments == 0) {
            price.setText(String.valueOf(10000));
            return;
        }

        text.setText(shipments + " " + str);
        price.setText(String.valueOf(shipments * 10000));
    }

    private void updateButtons() {
        Button finger = (Button) findViewById(R.id.buyFingerButton);
        Button garden = (Button) findViewById(R.id.buyGardenButton);
        Button orchard = (Button) findViewById(R.id.buyOrchardButton);
        Button shipment = (Button) findViewById(R.id.buyShipmentButton);

        if(!finger.isEnabled()) {
            finger.setEnabled(tomatoes >= fingers * 10 || fingers == 0);
        }

        if(!garden.isEnabled()) {
            garden.setEnabled(tomatoes >= gardens * 100 || gardens == 0);
        }

        if(!orchard.isEnabled()) {
            orchard.setEnabled(tomatoes >= orchards * 1000 || orchards == 0);
        }

        if(!shipment.isEnabled()) {
            shipment.setEnabled(tomatoes >= shipments * 10000 || shipments == 0);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
        saveData(getApplicationContext());
    }

    Runnable tomatoUpdater = new Runnable() {
        @Override
        public void run() {
            try {
                addTomatoes(getTps());

                updateButtons();

                if(track >= 20) {
                    saveData(getApplicationContext());
                    track = 0;
                }

                track++;
            } finally {
                handler.postDelayed(tomatoUpdater, 1000);
            }
        }
    };

    private void startRepeatingTask() {
        track = 0;
        tomatoUpdater.run();
    }

    private void stopRepeatingTask() {
        handler.removeCallbacks(tomatoUpdater);
    }

    private void loadData(Context context) {
        int data[] = new int[]{0, 0, 0, 0, 0};

        try {
            InputStream inputStream = context.openFileInput("tomatotapper.dat");

            if(inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String result;

                while ((result = bufferedReader.readLine()) != null) {
                    data[0] = Integer.parseInt(result.split(";")[0]);
                    data[1] = Integer.parseInt(result.split(";")[1]);
                    data[2] = Integer.parseInt(result.split(";")[2]);
                    data[3] = Integer.parseInt(result.split(";")[3]);
                    data[4] = Integer.parseInt(result.split(";")[4]);
                }

                inputStream.close();
            }
        } catch(FileNotFoundException e) {
            Log.e("load data", "File not found: " + e.toString());
        } catch(IOException e) {
            Log.e("load data", "Can not read file: " + e.toString());
        }

        tomatoes = data[0];
        fingers = data[1];
        gardens = data[2];
        orchards = data[3];
        shipments = data[4];
        updateFingers();
        updateGardens();
        updateOrchards();
        updateShipments();
        updateTps();
    }

    private void saveData(Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("tomatotapper.dat", Context.MODE_PRIVATE));
            outputStreamWriter.write(String.valueOf(tomatoes) + ";"
            + String.valueOf(fingers) + ";"
            + String.valueOf(gardens) + ";"
            + String.valueOf(orchards) + ";"
            + String.valueOf(shipments));
            outputStreamWriter.close();
            Log.i("save data", tomatoes + ", " + fingers + ", " + gardens + ", " + orchards + ", " + shipments);
        } catch(IOException e) {
            Log.e("save data", "File write failed: " + e.toString());
        }
    }
}

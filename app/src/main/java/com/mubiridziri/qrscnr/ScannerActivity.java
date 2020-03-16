package com.mubiridziri.qrscnr;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.room.Room;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.mubiridziri.qrscnr.appdatabase.AppDatabase;
import com.mubiridziri.qrscnr.entity.StoredData;
import com.mubiridziri.qrscnr.repository.StoredDataRepository;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class ScannerActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 201;
    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private CameraSource cameraSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        //getActionBar().setDisplayHomeAsUpEnabled(true); //back button

        initViews();
    }

    private void initViews() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
    }

    private void initialiseDetectorsAndSources() {
        final BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScannerActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScannerActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }   //Required implementation

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    //Need learn about it operation
                    txtBarcodeValue.post(new Runnable() {

                        @Override
                        public void run() {
                            if (barcodes.valueAt(0).email != null) {
                                sendToast(R.string.email_isnt_supported);
                            } else if (barcodes.valueAt(0).wifi != null) {
                                sendToast(R.string.wifi_isnt_supported);
                            } else if (barcodes.valueAt(0).url != null) {
                                cameraSource.stop();
                                final String urlPath = barcodes.valueAt(0).displayValue;
                                final Thread databaseThread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        saveRecognizedEntity(urlPath, StoredData.URL_TYPE);
                                    }
                                });
                                AlertDialog.Builder alertDialog = getAlertDialogBuilder(urlPath);
                                alertDialog.setPositiveButton(R.string.save_button, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        databaseThread.start();
                                    }
                                });
                                showAlertDialog(alertDialog);

                            } else {
                                cameraSource.stop();
                                final String content = barcodes.valueAt(0).displayValue;
                                final Thread databaseThread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        saveRecognizedEntity(content, StoredData.TEXT_TYPE);
                                    }
                                });
                                AlertDialog.Builder alertDialog = getAlertDialogBuilder(content);
                                alertDialog.setPositiveButton(R.string.save_button, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        databaseThread.start();
                                    }
                                });
                                showAlertDialog(alertDialog);
                            }
                        }
                    });

                }
            }
        });
    }

    private AlertDialog.Builder getAlertDialogBuilder(String content) {
        AlertDialog.Builder alertDialog = getDialogView();
        alertDialog.setTitle(R.string.qr_code);
        alertDialog.setMessage(content);
        return alertDialog;
    }

    private void showAlertDialog(AlertDialog.Builder builder) {
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void saveRecognizedEntity(String content, String type) {
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "qrscnr").build();
        StoredDataRepository storedDataRepository = db.getLinkRepository();
        StoredData storedData = new StoredData();

        if(type.equals(StoredData.URL_TYPE)) {
            Document doc = null; // Fetches the HTML document
            try {
                doc = Jsoup.connect(content).get();
            } catch (IOException e) {
                storedData.title = "Страница недоступна";
            }
            if (doc != null) {
                storedData.title = doc.title();
            } else storedData.title = "Страница недоступна";
            storedData.type = StoredData.URL_TYPE;
        } else {
            storedData.title = getResources().getString(R.string.the_note_title);
            storedData.type = StoredData.TEXT_TYPE;
        }
        storedData.content = content;

        storedDataRepository.insertAll(storedData);
        db.close();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }

    public void sendToast(int resId) {
        Toast.makeText(getApplicationContext(), resId, Toast.LENGTH_SHORT).show();
    }

    public void sendToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    public AlertDialog.Builder getDialogView() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ScannerActivity.this);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                try {
                    cameraSource.start(surfaceView.getHolder());
                } catch (IOException e) {
                    sendToast(R.string.error_open_camera);
                }

            }
        });

        return builder;
    }
}

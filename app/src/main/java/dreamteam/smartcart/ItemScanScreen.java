package dreamteam.smartcart;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.w3c.dom.Text;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Item scanning screen -> needs to use sql to look at what item it is
 */
public class  ItemScanScreen extends AppCompatActivity {

    private Button scanButton;
    private ImageView scannedImage;
    private Uri file;
    private TextView barcode_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_item);

        scanButton = (Button) findViewById(R.id.scanButton);
        scannedImage = (ImageView) findViewById(R.id.scannedImage);
        barcode_txt= (TextView) findViewById(R.id.barcode_txt);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            scanButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE }, 0);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2]==PackageManager.PERMISSION_GRANTED) {
                scanButton.setEnabled(true);
            }
        }
    }

    public void scanItem(View view) {

        Context context=this;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

        startActivityForResult(intent, 100);


       /*
        //Recoginze the Barcode
        int barcode=-1;

        if(barcode==-1){
            //Error with reading code
            Toast.makeText(this, "Error: Cannot recognize barcode. Please take another picture and try again." , Toast.LENGTH_LONG).show();
            return;
        }

        //Look it up in Carts table
        String btaddr = findItemByBarcode(barcode);
        if(btaddr.equals("-1")){
            Toast.makeText(this, "Error: Cart not recognized in system. Please take another picture and try again." ,Toast.LENGTH_LONG).show();
            //Error with look up
            return;
        }


        //Return to previous screen
        finish();
    }

  */
    }
    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "ScannedPictures");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                Log.d("ScannedPictures", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

        /*private String findItemByBarcode(int barcode) {
        //look it up in sql table

        return "-1";
    }
*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                 scannedImage.setImageURI(file);
               Bitmap myBitmap= BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.barcode2);
               // scannedImage.setImageBitmap(myBitmap);
                if(scannedImage.getDrawable()==null){
                    Toast.makeText(this, "scanned drawable is null", Toast.LENGTH_SHORT).show();
                    return;
                }
               // Bitmap myBitmap=((BitmapDrawable)scannedImage.getDrawable()).getBitmap();

                 scannedImage.setImageBitmap(myBitmap);
                detectBarcode(myBitmap);
            }
        }
    }

    public void detectBarcode(Bitmap bitmap){
        BarcodeDetector detector=new BarcodeDetector.Builder(getApplicationContext()).build();
        if (!detector.isOperational()){
            barcode_txt.setText("Could not setup barcode detector");
            return;
        }

        Frame frame=new Frame.Builder().setBitmap(bitmap).build();

        SparseArray<Barcode> barcodes=detector.detect(frame);
        if (barcodes==null){
            barcode_txt.setText("Invalid barcode");
            return;
        }
        else{
            if (barcodes==null){
                System.out.println("barcodes is null");
            }
            // barcode_txt.setText("No Barcode Detected");
            if (barcodes.size()>0) {
                Barcode thisCode = barcodes.valueAt(0);
                barcode_txt.setText(thisCode.rawValue);
            }
            else{
                System.out.println("barcodes is null");
                barcode_txt.setText("barcodes is null");
                return;
            }
        }


    }


}

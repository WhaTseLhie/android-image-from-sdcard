package pardillo.john.jv.gallery;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddImageActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int IMAGE_REQUEST = 100;
    private static final String TAG = "ADD IMAGE ACTIVITY";

    private ImageView iv;
    private Button btnSave, btnCancel;
    private Uri imageUri = null;
    private Intent addImageIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);

        this.iv = this.findViewById(R.id.imageView);
        this.btnSave = this.findViewById(R.id.button);
        this.btnCancel = this.findViewById(R.id.button2);

        this.iv.setOnClickListener(this);
        this.btnSave.setOnClickListener(this);
        this.btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.imageView:
                // request to use the gallery to select an image
                addImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE}, 30);
                    }
                } else {
                    this.startActivityForResult(addImageIntent, IMAGE_REQUEST);
                }

                break;
            case R.id.button:
                if(imageUri != null) {
                    // return the image to the activity who requested to open this activity
                    Intent blindIntent = new Intent(); // blind intent
                    blindIntent.putExtra("image", imageUri); // string key is image
                    this.setResult(Activity.RESULT_OK, blindIntent); // set result_ok
                    this.finish(); // close the activity after saving image
                } else {
                    // prompt when saving without selecting an image
                    Toast.makeText(this, "Please select an image", Toast.LENGTH_LONG).show();
                }

                break;

            case R.id.button2:
                // close the activity after saving image
                this.finish();

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageUri = null;

        // throws exception if no image is selected
        try {
            // getting the uri of the image
            imageUri = data.getData();
            // show image selected
            this.iv.setImageURI(imageUri);
        } catch(Exception e) {
            Log.d("ERROR: ", TAG);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode) {
            case 30:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    final Dialog dialog = new Dialog(this);
                    dialog.setContentView(R.layout.layout_permission);

                    TextView t1 = (TextView) dialog.findViewById(R.id.textView);
                    TextView t2 = (TextView) dialog.findViewById(R.id.textView2);
                    t1.setText("Request Permissions");
                    t2.setText("Please allow permissions if you want this application to perform the task.");

                    Button dialogButton = (Button) dialog.findViewById(R.id.button);
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                } else {
                    this.startActivityForResult(addImageIntent, IMAGE_REQUEST);
                }

                break;
        }
    }
}
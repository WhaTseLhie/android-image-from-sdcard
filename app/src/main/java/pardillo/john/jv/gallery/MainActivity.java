package pardillo.john.jv.gallery;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final static int REQUEST_RESULT = 100;
    private final static String TAG = "MAIN ACTIVITY";

    private GridView gridView;
    private GridAdapter adapter;
    private ArrayList<Uri> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.gridView = this.findViewById(R.id.gridView);
        this.adapter = new GridAdapter(this, list);
        this.gridView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate the menu if exist
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // setup the intent to what activity to open
        Intent addImageIntent = new Intent(this, AddImageActivity.class);
        // start the activity and wait for the result
        startActivityForResult(addImageIntent, REQUEST_RESULT);

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // receive the uri of the image and update the gridView
        // throws an exception if the data is null
        try {
            // check, if no error occurred
            if (resultCode == Activity.RESULT_OK) {
                // check for the requester
                if (requestCode == REQUEST_RESULT) {
                    // retrieve the data
                    Bundle b = data.getExtras();
                    // get the specific data passed from the other class
                    Uri imageUri = b.getParcelable("image");
                    // place the received data to the list
                    list.add(imageUri);
                    // update the adapter
                    adapter.notifyDataSetChanged();
                }
            }
        } catch(Exception e) {
            // check logcat if there is an error
            Log.d("ERROR: ", TAG);
        }
    }
}
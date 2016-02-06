package nation.ebbi.sharelove;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.parse.ParseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends ActionBarActivity  implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    public static final int TAKE_PHOTO_REQUEST=0;
    public static final int TAKE_VIDEO_REQUEST=1;
    public static final int PICK_PHOTO_REQUEST=2;
    public static final int PICK_VIDEO_REQUEST=3;
    public static final int MEDIA_TYPE_IMAGE = 4;
    public static final int MEDIA_TYPE_VIDEO = 5;
    public static final int FILE_SIZE_LIMIT = 1024*1024*10;

    protected Uri mMediaUri;



    SectionsPagerAdapter mSectionsPagerAdapter;
    protected DialogInterface.OnClickListener mDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case 0: Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    mMediaUri= getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    if(mMediaUri ==null ){
                       error("Media Storage Error");
                    }

                    takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                   startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);


                    break;
                case 1: Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    mMediaUri= getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
                    if (mMediaUri==null) error("Media Storage Error");
                    else {videoIntent.putExtra(MediaStore.EXTRA_OUTPUT,mMediaUri);
                        videoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15);
                        videoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                        startActivityForResult(videoIntent, TAKE_VIDEO_REQUEST);
                    }
                    break;
                case 2:
                    Intent choosePhotoIntent= new Intent(Intent.ACTION_GET_CONTENT);
                    choosePhotoIntent.setType("image/*");
                    startActivityForResult(choosePhotoIntent, PICK_PHOTO_REQUEST);
                    break;
                case 3:   Intent chooseVideoIntent= new Intent(Intent.ACTION_GET_CONTENT);
                    chooseVideoIntent.setType("video/*");
                    startActivityForResult(chooseVideoIntent, PICK_VIDEO_REQUEST);
                    error("The selected video must be less that than 10 MB");
                    break;
            }

        }




    private Uri getOutputMediaFileUri(int mediaType) {
        if(isExternalStorageAvailable())
        {   String appName= MainActivity.this.getString(R.string.app_name);
            File mediStorageDir =new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    appName);
            if(!mediStorageDir.exists())
                if(!mediStorageDir.mkdirs()){
                    error("directory creation error");
                    return null;
                }
            File mediafile;
            Date now = new Date();
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CANADA).format(now);
            String path = mediStorageDir.getPath()+File.separator;
            if(mediaType == MEDIA_TYPE_IMAGE) {
                mediafile = new File(path + "IMG_"+ timestamp+".jpg");
            }
            else if(mediaType==MEDIA_TYPE_VIDEO){
                mediafile = new File(path + "VID_"+ timestamp+".mp4");

            }
            else {return null;}
            error(Uri.fromFile(mediafile).toString());
            return Uri.fromFile(mediafile);
        }
        else {

            return null;
        }
    }
    private boolean isExternalStorageAvailable(){
        String state= Environment.getExternalStorageState();
        if(state.equals(Environment.MEDIA_MOUNTED))
            return true;
        else return false;

    }};

    private void error(String error){
        Toast.makeText(MainActivity.this, error,
                Toast.LENGTH_LONG).show();}

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StartThisShit();
        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(this,getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);



        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(resultCode== RESULT_OK){
            if(requestCode == PICK_PHOTO_REQUEST || requestCode == PICK_VIDEO_REQUEST){
                if( data==null) error("There was a error #3");
                else {mMediaUri = data.getData();}
            }
            if(requestCode==PICK_VIDEO_REQUEST){
                int fileSize=0;
                InputStream inputStream = null;
                try {
                    inputStream = getContentResolver().openInputStream(mMediaUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    error("There was error opening file");return;                }
                try {
                    fileSize = inputStream.available();
                } catch (IOException e) {
                    e.printStackTrace();
                            error("There was a error opening file");
                    return;                }
                finally {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                    if(fileSize>= FILE_SIZE_LIMIT){
                        error("The selected file is too large please select another one");
                        return;
                    }
                }
            }
        else{
            //add to gallery
            Intent mediaScanIntent= new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(mMediaUri);
            sendBroadcast(mediaScanIntent);
        }}
        else if (resultCode != RESULT_CANCELED){
            error("Sorry, There was a error; ");

        }

    }
    private void StartThisShit() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser == null)
        {
            Intent intent = new Intent(this,StartActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Inflate the menu items for use in the action bar
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
            if(id == R.id.action_logout)
            { ParseUser.logOut();
                StartThisShit(); }
        else if(id==R.id.action_EditFriendsLabel)
            {
                Intent intent = new Intent(this, EditPeopleActivity.class);
                startActivity(intent);

            }

        else if(id==R.id.action_camera){
                AlertDialog.Builder builder= new AlertDialog.Builder(this);
                builder.setItems(R.array.camera_choices, mDialogListener);
                AlertDialog dialog = builder.create();
                dialog.show();

            }


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */




}



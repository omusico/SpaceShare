package nation.ebbi.sharelove;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by 100569443 on 16/06/2015.
 */
public class ShareApplication extends Application {
    public void onCreate(){
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "u6D7Fqw2wHGCoAHkErLZLfmpZXbwy7MPngjASbbM", "kpognGTZTxQZ8V7boHVC8nVKe5xbdJ9iSAwt6auj");

    }


}

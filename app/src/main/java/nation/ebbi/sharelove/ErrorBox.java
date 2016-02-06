package nation.ebbi.sharelove;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by 100569443 on 17/06/2015.
 */
public class ErrorBox {
    public void CreateErrorMessage(Context context,String message, String title, String buttonText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setTitle(title)
                .setPositiveButton(buttonText, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
/*AlertDialog.Builder builder = new AlertDialog.Builder(EditPeopleActivity.this);
builder.setMessage(e.getMessage())
        .setTitle("Oops!")
        .setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();*/


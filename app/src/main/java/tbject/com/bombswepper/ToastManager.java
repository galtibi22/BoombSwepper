package tbject.com.bombswepper;

import android.widget.Toast;

import tbject.com.bombswepper.activity.Menu;

/**
 * Created by Gal on 18/01/2017.
 */

public class ToastManager {

    private static Toast toast;
    public static void showAToast (final String st){ //"Toast toast" is declared in the class
        Menu.getInstance().runOnUiThread(new Runnable() {
            public void run() {
                try{ toast.getView().isShown();     // true if visible

                    toast.setText(st);
                } catch (Exception e) {         // invisible if exception
                    toast = Toast.makeText(Menu.getInstance().getBaseContext(), st, Toast.LENGTH_SHORT);
                }
                toast.show();  //finally display it
            }
        });
    }
}

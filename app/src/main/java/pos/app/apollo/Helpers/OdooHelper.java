package pos.app.apollo.Helpers;

import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.ArrayList;

import io.github.mthli.sugartask.SugarTask;

public class OdooHelper {

    final String url = "192.168.99.125";
    final int port = 8011;
    final String db = "odoo11_pos";
    final String username = "odoo_admin@apollo.com.ph";
    final String password = "ap0ll0";


    public void odooConnectionTest(final Context context, Activity activity) {


        SugarTask.with(activity) // Activity|FragmentActivity(v4)|Fragment|Fragment(v4)
                .assign(new SugarTask.TaskDescription() {
                    @Override
                    public Object onBackground() {


                        OdooConnect oc = OdooConnect.connect(url, port, db, username, password);

                        Object[] param = {new Object[]{
                                new Object[]{"asList", "=", false}}};

                        Object[] fields = {new Object[]{"name", "price", "category"}};

                        ArrayList params = new ArrayList();


                        String idW = oc.call("product.product", "sync_product_category_data");



                        /*Boolean ocT = OdooConnect.testConnection(url, port,
                                db, username, password);*/

                        // Return your finally result(Nullable).
                        return idW;
                    }
                })
                .handle(new SugarTask.MessageListener() {
                    @Override
                    public void handleMessage(@NonNull Message message) {
                        // Receive message in MainThread which sent from WorkerThread,
                        // update your UI just in time.
                    }
                })
                .finish(new SugarTask.FinishListener() {
                    @Override
                    public void onFinish(@Nullable Object result) {
                        Toast.makeText(context, "Return " + result, Toast.LENGTH_LONG).show();
                        // If WorkerThread finish without Exception and lifecycle safety,
                        // deal with your WorkerThread result at here.
                    }
                })
                .broken(new SugarTask.BrokenListener() {
                    @Override
                    public void onBroken(@NonNull Exception e) {
                        Toast.makeText(context, "Error" + e, Toast.LENGTH_LONG).show();
                        // If WorkerThread finish with Exception and lifecycle safety,
                        // deal with Exception at here.
                    }
                })
                .execute();


    }


}

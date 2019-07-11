package pos.app.apollo.Helpers;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import pos.app.apollo.Models.ProductModel;

public class OdooHelper {

    final String url = "192.168.99.125";
    final int port = 8011;
    final String db = "odoo11_pos";
    final String username = "odoo_admin@apollo.com.ph";
    final String password = "ap0ll0";


    public List<ProductModel> productList = new ArrayList<>();

    public void odooConnection(final Context context, Activity activity) {

      /*  HomePage homePage = new HomePage();
        homePage.test();*/


        // Toast.makeText(context, "size"+productList.size(), Toast.LENGTH_SHORT).show();


    }


}

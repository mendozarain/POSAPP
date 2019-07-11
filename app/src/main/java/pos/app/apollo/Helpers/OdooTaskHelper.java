package pos.app.apollo.Helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pos.app.apollo.Models.ProductModel;

public class OdooTaskHelper extends AsyncTask<String, String, String> {

    final String url = "192.168.99.125";
    final int port = 8011;
    final String db = "odoo11_pos";
    final String username = "odoo_admin@apollo.com.ph";
    final String password = "ap0ll0";
    private Context context;


    public OdooTaskHelper(Context context) {
        this.context = context;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(String... strings) {
        List<ProductModel> productList = new ArrayList<>();

        OdooConnect oc = OdooConnect.connect(url, port, db, username, password);


        String data = oc.call("product.product", "sync_product_category_data");


        try {
            productList = (ArrayList<ProductModel>) addData(String.valueOf(data));
            //Toast.makeText(context, "size"+productList.size(), Toast.LENGTH_SHORT).show();

            // FancyToast.makeText(context,"Success",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
        } catch (JSONException e) {
            Toast.makeText(context, "Error: " + e, Toast.LENGTH_SHORT).show();
            // FancyToast.makeText(context,"Error: ",FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
        }


        return data;
    }


    @Override
    protected void onPostExecute(String s) {

/*

        try {
            productList = (ArrayList<ProductModel>) addData(String.valueOf(result));
            //Toast.makeText(context, "size"+productList.size(), Toast.LENGTH_SHORT).show();

            // FancyToast.makeText(context,"Success",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
        } catch (JSONException e) {
            Toast.makeText(context, "Error: "+e, Toast.LENGTH_SHORT).show();
            // FancyToast.makeText(context,"Error: ",FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
        }

*/


        super.onPostExecute(s);
    }


    public List<ProductModel> addData(String result) throws JSONException {
        List<ProductModel> personModelList = new ArrayList<>();

        JSONObject jsnobject = new JSONObject(result);
        JSONArray jsonArray = jsnobject.getJSONArray("products");
        //JSONArray jsonarray = new JSONArray(result);

        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject jsonobj = jsonArray.getJSONObject(i);

            personModelList.add(new ProductModel(jsonobj.getString("name"), jsonobj.getDouble("price"), jsonobj.getInt("category")));

        }


        return personModelList;
    }


}

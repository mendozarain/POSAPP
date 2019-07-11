package pos.app.apollo.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.shashank.sony.fancytoastlib.FancyToast;
import com.transitionseverywhere.Rotate;
import com.transitionseverywhere.TransitionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import io.github.mthli.sugartask.SugarTask;
import pos.app.apollo.Adapters.ProductAdapter;
import pos.app.apollo.Helpers.AutoGridHelper;
import pos.app.apollo.Helpers.OdooConnect;
import pos.app.apollo.Helpers.OdooHelper;
import pos.app.apollo.Models.ProductModel;
import pos.app.library.ntb.NavigationTabBar;
import pos.app.sample.R;


public class HomePage extends Activity {

    private BottomSheetBehavior mBottomSheetBehavior;
    OdooHelper odooHelper = new OdooHelper();


    final String url = "192.168.99.125";
    final int port = 8011;
    final String db = "odoo11_pos";
    final String username = "odoo_admin@apollo.com.ph";
    final String password = "ap0ll0";


    public List<ProductModel> productList = new ArrayList<>();
    public List<ProductModel> clothes = new ArrayList<>();
    public List<ProductModel> shoes = new ArrayList<>();
    public List<ProductModel> jewelry = new ArrayList<>();


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        initUI();
        bottomsheet();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action

                final AlertDialog dialog = new SpotsDialog(HomePage.this);
                dialog.show();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        dialog.dismiss();


                    }
                }, 500);

                initUI();
            }
        });

    }


    private void initUI() {


        final ViewPager viewPager = (ViewPager) findViewById(R.id.vp_horizontal_ntb);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public boolean isViewFromObject(final View view, final Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(final View container, final int position, final Object object) {
                ((ViewPager) container).removeView((View) object);
            }

            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {


                final int mNoOfColumns = AutoGridHelper.calculateNoOfColumns(getApplicationContext(), 190);

                final View view = LayoutInflater.from(
                        getBaseContext()).inflate(R.layout.item_vp_list, null, false);

                final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv);


                SugarTask.with(HomePage.this) // Activity|FragmentActivity(v4)|Fragment|Fragment(v4)
                        .assign(new SugarTask.TaskDescription() {
                            @Override
                            public String onBackground() {

                                List<ProductModel> productList = new ArrayList<>();

                                OdooConnect oc = OdooConnect.connect(url, port, db, username, password);


                                String data = oc.call("product.product", "sync_product_category_data");


                                try {
                                    productList = (ArrayList<ProductModel>) addData(String.valueOf(data));
                                    //Toast.makeText(context, "size"+productList.size(), Toast.LENGTH_SHORT).show();

                                    // FancyToast.makeText(context,"Success",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                                } catch (JSONException e) {
                                    Toast.makeText(HomePage.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                                    // FancyToast.makeText(context,"Error: ",FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
                                }


                                return data;
                            }
                        })
                        .handle(new SugarTask.MessageListener() {
                            @Override
                            public void handleMessage(@NonNull Message message) {


                            }
                        })
                        .finish(new SugarTask.FinishListener() {
                            @Override
                            public void onFinish(@Nullable Object result) {


                                try {

                                    productList = (ArrayList<ProductModel>) addData(String.valueOf(result));
                                    // Toast.makeText(context, "size"+productList.size(), Toast.LENGTH_SHORT).show();


                                    // FancyToast.makeText(context,"Success",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                                } catch (JSONException e) {
                                    Toast.makeText(HomePage.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                                    // FancyToast.makeText(context,"Error: ",FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
                                }

                                for (ProductModel item : productList) {
                                    int category = item.getCategory();
                                    if (category == 3) {
                                        shoes.add(item);
                                    }
                                    if (category == 4) {
                                        jewelry.add(item);
                                    }
                                    if (category == 5) {
                                        clothes.add(item);
                                    }

                                }


                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(), mNoOfColumns));


                                if (position == 0) {
                                    recyclerView.setAdapter(new ProductAdapter(getBaseContext(), getApplicationContext(), clothes));

                                }
                                if (position == 1) {

                                    recyclerView.setAdapter(new ProductAdapter(getBaseContext(), getApplicationContext(), shoes));
                                }

                                if (position == 2) {

                                    recyclerView.setAdapter(new ProductAdapter(getBaseContext(), getApplicationContext(), jewelry));
                                }


                                container.addView(view);

                                clothes = new ArrayList<>();
                                shoes = new ArrayList<>();
                                jewelry = new ArrayList<>();



                                // FancyToast.makeText(HomePage.this,"Huhu"+ productList.size() ,FancyToast.LENGTH_LONG,FancyToast.INFO,true).show();
                                // If WorkerThread finish without Exception and lifecycle safety,
                                // deal with your WorkerThread result at here.
                            }
                        })
                        .broken(new SugarTask.BrokenListener() {
                            @Override
                            public void onBroken(@NonNull Exception e) {
                                FancyToast.makeText(HomePage.this, "Error" + e, FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                                // If WorkerThread finish with Exception and lifecycle safety,
                                // deal with Exception at here.
                            }
                        })
                        .execute();


                return view;

            }
        });

        final String[] colors = getResources().getStringArray(R.array.pos_app_tab_colors);

        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_clothes),
                        Color.parseColor(colors[0]))
                        .title("Clothes")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_high_heel),
                        Color.parseColor(colors[1]))
                        .title("Foot Wear")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_diamond),
                        Color.parseColor(colors[2]))
                        .title("Jewelry")
                        .build()
        );

        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 2);

        navigationTabBar.post(new Runnable() {
            @Override
            public void run() {
                final View viewPager = findViewById(R.id.vp_horizontal_ntb);
                ((ViewGroup.MarginLayoutParams) viewPager.getLayoutParams()).topMargin =
                        (int) -navigationTabBar.getBadgeMargin();
                viewPager.requestLayout();
            }
        });

        navigationTabBar.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(final NavigationTabBar.Model model, final int index) {

            }

            @Override
            public void onEndTabSelected(final NavigationTabBar.Model model, final int index) {
                model.hideBadge();
            }
        });



       /* findViewById(R.id.mask).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final NavigationTabBar.Model model = navigationTabBar.getModels().get(3);
                model.setBadgeTitle("Coming Soon");
                model.showBadge();

               *//* for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
                    final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            final String title = String.valueOf(new Random().nextInt(15));
                            if (!model.isBadgeShowed()) {
                                model.setBadgeTitle("wew");
                                model.showBadge();
                            } else model.updateBadgeTitle(title);
                        }
                    }, i * 100);
                }*//*
            }
        });*/
    }



    public void bottomsheet() {


        final View bottomSheet = findViewById(R.id.bottom_sheet);
        final ViewGroup transitionContainer = findViewById(R.id.transitionContainer);

        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        final ImageView buttonExpand = findViewById(R.id.button_expand);

        buttonExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TransitionManager.beginDelayedTransition(transitionContainer, new Rotate());


                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    buttonExpand.setRotation(0);

                }
                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    buttonExpand.setRotation(180);
                }

            }
        });


        transitionContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TransitionManager.beginDelayedTransition(transitionContainer, new Rotate());


                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    buttonExpand.setRotation(0);

                }
                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    buttonExpand.setRotation(180);
                }

            }
        });


        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            final BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                TransitionManager.beginDelayedTransition(transitionContainer, new Rotate());
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    buttonExpand.setRotation(180);
                }
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    buttonExpand.setRotation(0);
                }

                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }


            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    public List<ProductModel> addData(String result) throws JSONException {
        List<ProductModel> personModelList = new ArrayList<>();

        JSONObject jsnobject = new JSONObject(result);
        JSONArray jsonArray = jsnobject.getJSONArray("products");
        //JSONArray jsonarray = new JSONArray(result);

        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject jsonobj = jsonArray.getJSONObject(i);

            personModelList.add(new ProductModel(jsonobj.getString("name"), jsonobj.getDouble("price"), jsonobj.getInt("category"), jsonobj.getString("image")));

        }

        productList = personModelList;


        return personModelList;
    }



}

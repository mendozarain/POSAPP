package pos.app.apollo.Activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.transitionseverywhere.Rotate;
import com.transitionseverywhere.TransitionManager;

import java.util.ArrayList;

import pos.app.apollo.Helpers.AutoGridHelper;
import pos.app.apollo.Helpers.OdooHelper;
import pos.app.library.ntb.NavigationTabBar;
import pos.app.sample.R;


public class HomePage extends Activity {

    private BottomSheetBehavior mBottomSheetBehavior;
    OdooHelper odooHelper = new OdooHelper();


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        initUI();
        bottomsheet();
        odooHelper.odooConnectionTest(getApplicationContext(), this);



    }

    private void initUI() {


        final ViewPager viewPager = (ViewPager) findViewById(R.id.vp_horizontal_ntb);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 4;
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
                int mNoOfColumns = AutoGridHelper.calculateNoOfColumns(getApplicationContext(), 190);

                if(position != 3) {

                    final View view = LayoutInflater.from(
                            getBaseContext()).inflate(R.layout.item_vp_list, null, false);

                    final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new GridLayoutManager( getBaseContext(), mNoOfColumns));
                   /* recyclerView.setLayoutManager(new LinearLayoutManager(
                                    getBaseContext(), LinearLayoutManager.VERTICAL, false
                            )
                    );*/
                    recyclerView.setAdapter(new RecycleAdapter());

                    container.addView(view);
                    return view;
                }
                else {
                    final View view = LayoutInflater.from(
                            getBaseContext()).inflate(R.layout.cart, null, false);

                    final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new GridLayoutManager( getBaseContext(), mNoOfColumns));
                    /*recyclerView.setLayoutManager(new LinearLayoutManager(
                                    getBaseContext(), LinearLayoutManager.VERTICAL, false
                            )
                    );*/
                    recyclerView.setAdapter(new RecycleAdapter());

                    container.addView(view);
                    return view;
                }
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
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic__cart),
                        Color.parseColor(colors[4]))
                        .title("Cart")
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

    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {
        private Context mContext;




        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            final View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.txt.setText(String.format("Item #%d", position));
            Glide.with(getApplicationContext()).load("https://via.placeholder.com/160x190").into(holder.item_img);

        }

        @Override
        public int getItemCount() {
            return 20;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ImageView item_img;
            public TextView txt;

            public ViewHolder(final View itemView) {
                super(itemView);
                item_img = (ImageView) itemView.findViewById(R.id.item_img);
                txt = (TextView) itemView.findViewById(R.id.item_name);
            }
        }
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


}

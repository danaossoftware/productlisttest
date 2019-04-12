package bluetoothprintertest.dn.com.productlisttest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import bluetoothprintertest.dn.com.productlisttest.bannerfragments.BannerFragment;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ViewPager viewPager;
    RecyclerView productList;
    ViewPagerAdapter adapter;
    Handler h;
    int bannerPosition = 0;
    ProductAdapter productAdapter;
    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle(R.string.app_name);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        viewPager = findViewById(R.id.viewpager);
        productList = findViewById(R.id.products);
        productList.setLayoutManager(new GridLayoutManager(this, 2));
        productList.setItemAnimator(new DefaultItemAnimator());
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        products = new ArrayList<>();
        productAdapter = new ProductAdapter(this, products);
        productList.setAdapter(productAdapter);
        collectProducts();
        h = new Handler(Looper.getMainLooper());
        h.postDelayed(bannerRunnable, 3000);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return false;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.logout) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setMessage(R.string.text7)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Util.write(HomeActivity.this, "email", "");
                            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.no, null)
                    .create();
            dialog.show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public Runnable bannerRunnable = new Runnable() {

        @Override
        public void run() {
            if (bannerPosition < adapter.getCount()) {
                bannerPosition++;
            } else {
                bannerPosition = 0;
            }
            viewPager.setCurrentItem(bannerPosition);
        }
    };

    public void collectProducts() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient.Builder()
                            .build();
                    Request req = new Request.Builder()
                            .url(Constants.BASE_URL+"list-banner")
                            .build();
                    final String response = client.newCall(req).execute().body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject obj = new JSONObject(response);
                                JSONArray data = obj.getJSONArray("data");
                                for (int i=0; i<data.length(); i++) {
                                    JSONObject productJSON = data.getJSONObject(i);
                                    String imageURL = Constants.BASE_IMAGE_URL+productJSON.getString("image");
                                    String title = productJSON.getString("title");
                                    Util.log("Title: "+title);
                                    String productImageURL = Constants.BASE_IMAGE_URL+productJSON.getString("image2");
                                    Product product = new Product();
                                    product.setName(title);
                                    product.setImageURL(productImageURL);
                                    Util.log("Product image URL: "+productImageURL);
                                    products.add(product);
                                    BannerFragment bannerFragment = new BannerFragment();
                                    Bundle args = new Bundle();
                                    args.putString("image_url", imageURL);
                                    bannerFragment.setArguments(args);
                                    adapter.addFragment(bannerFragment);
                                }
                                productAdapter.notifyDataSetChanged();
                                viewPager.setAdapter(adapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> fragments;
        ArrayList<String> fragmentTitles;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<>();
            fragmentTitles = new ArrayList<>();
        }

        public void addFragment(Fragment fr) {
            fragments.add(fr);
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}

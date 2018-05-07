package org.zimmob.zimlx.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.util.TypedValue;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.zimmob.zimlx.R;
import org.zimmob.zimlx.activity.Home;
import org.zimmob.zimlx.manager.Setup;
import org.zimmob.zimlx.model.Item;
import org.zimmob.zimlx.viewutil.ItemGestureListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by saul on 04-25-18.
 * Project ZimLX
 * henriquez.saul@gmail.com
 */
public class Tool {

    public static void hideKeyboard(Context context, View view) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void showKeyboard(Context context, View view) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInputFromWindow(view.getWindowToken(), InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void visibleViews(View... views) {
        visibleViews(200, 0, views);
    }

    public static void visibleViews(long duration, View... views) {
        visibleViews(duration, 0, views);
    }

    public static void visibleViews(long duration, long delay, View... views) {
        if (views == null) return;
        for (View view : views) {
            if (view == null) continue;
            view.setVisibility(View.VISIBLE);
            view.animate().alpha(1).setStartDelay(delay).setDuration(duration).setInterpolator(new AccelerateDecelerateInterpolator());
        }
    }

    public static void invisibleViews(View... views) {
        if (views == null) return;
        for (final View view : views) {
            if (view == null) continue;
            view.animate().alpha(0).setStartDelay(0).setDuration(200).setInterpolator(new AccelerateDecelerateInterpolator()).withEndAction(() -> view.setVisibility(View.INVISIBLE));
        }
    }

    public static void invisibleViews(long duration, View... views) {
        if (views == null) return;
        for (final View view : views) {
            if (view == null) continue;
            view.animate()
                    .alpha(0)
                    .setStartDelay(0)
                    .setDuration(duration)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .withEndAction(() -> view.setVisibility(View.INVISIBLE));
        }
    }

    public static void invisibleViews(long duration, long delay, View... views) {
        if (views == null) return;
        for (final View view : views) {
            if (view == null) continue;
            view.animate().alpha(0).setStartDelay(delay).setDuration(duration).setInterpolator(new AccelerateDecelerateInterpolator()).withEndAction(() -> view.setVisibility(View.INVISIBLE));
        }
    }

    public static void goneViews(View... views) {
        if (views == null) return;
        for (final View view : views) {
            if (view == null) continue;
            view.animate().alpha(0).setStartDelay(0).setDuration(200).setInterpolator(new AccelerateDecelerateInterpolator()).withEndAction(() -> view.setVisibility(View.GONE));
        }
    }

    public static void goneViews(long duration, View... views) {
        if (views == null) return;
        for (final View view : views) {
            if (view == null) continue;
            view.animate().alpha(0).setStartDelay(0).setDuration(duration).setInterpolator(new AccelerateDecelerateInterpolator()).withEndAction(() -> view.setVisibility(View.GONE));
        }
    }

    public static final void createScaleInScaleOutAnim(@NonNull final View view, @NonNull final Runnable endAction, float runActionAtPercent) {
        final long animTime = (long) (Setup.appSettings().getOverallAnimationSpeedModifier() * ((float) ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION));
        ViewPropertyAnimator duration = view.animate().scaleX(0.85f).scaleY(0.85f).setDuration(animTime);

        duration.setInterpolator(new AccelerateDecelerateInterpolator());
        new Handler().postDelayed(() -> {
            ViewPropertyAnimator duration1 = view.animate().scaleX(1.0f).scaleY(1.0f).setDuration(animTime);
            duration1.setInterpolator(new AccelerateDecelerateInterpolator());
            new Handler().postDelayed(() -> endAction.run(), animTime);
        }, (long) (animTime * runActionAtPercent));
    }

    public static void toast(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public static void toast(Context context, int str) {
        Toast.makeText(context, context.getResources().getString(str), Toast.LENGTH_SHORT).show();
    }

    public static int toPx(int dp) {
        float f = (float) dp;
        Resources system = Resources.getSystem();
        return (int) (f * system.getDisplayMetrics().density);
    }

    public static final boolean isPackageInstalled(@NonNull String packageName, @NonNull PackageManager packageManager) {

        try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    public static int dp2px(int dp, Context context) {
        Resources resources = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics()));
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int clampInt(int target, int min, int max) {
        return Math.max(min, Math.min(max, target));
    }

    public static float clampFloat(float target, float min, float max) {
        return Math.max(min, Math.min(max, target));
    }

    public static void startApp(Context context, App app) {
        if (Home.Companion.getLauncher() != null)
            Home.Companion.getLauncher().onStartApp(context, app, null);
    }

    public static void startApp(Context context, Intent intent) {
        if (Home.Companion.getLauncher() != null)
            Home.Companion.getLauncher().onStartApp(context, intent, null);
    }

    public static final void startApp(@NonNull Context context, @NonNull App app, @Nullable View view) {
        Home launcher = Home.Companion.getLauncher();
        if (launcher != null) {
            launcher.onStartApp(context, app, view);
        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        Bitmap bitmap;
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            // single color bitmap will be created of 1x1 pixel
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static Point convertPoint(Point fromPoint, View fromView, View toView) {
        int[] fromCoord = new int[2];
        int[] toCoord = new int[2];
        fromView.getLocationOnScreen(fromCoord);
        toView.getLocationOnScreen(toCoord);

        Point toPoint = new Point(fromCoord[0] - toCoord[0] + fromPoint.x,
                fromCoord[1] - toCoord[1] + fromPoint.y);

        return toPoint;
    }

    //
    public static void vibrate(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
    }
    //

    public static void print(Object o) {
        if (o != null) {
            Log.e("ZimLX", o.toString());
        }
    }

    public static void print(Object... o) {
        StringBuilder sb = new StringBuilder();
        for (Object anO : o) {
            sb.append(anO.toString()).append("  ");
        }
        Log.e("ZimLX", sb.toString());
    }

    public static boolean isIntentActionAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List resolveInfo = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.size() > 0;
    }

    public static String getIntentAsString(Intent intent) {
        if (intent == null) {
            return "";
        } else {
            return intent.toUri(0);
        }
    }

    public static Intent getIntentFromString(String string) {
        if (string == null || string.isEmpty()) {
            return new Intent();
        } else {
            try {
                return new Intent().parseUri(string, 0);
            } catch (URISyntaxException e) {
                return new Intent();
            }
        }
    }

    public static Drawable getIcon(Context context, String filename) {
        if (filename == null) {
            return null;
        }
        Drawable icon = null;
        Bitmap bitmap = BitmapFactory.decodeFile(context.getFilesDir() + "/icons/" + filename + ".png");
        if (bitmap != null) {
            icon = new BitmapDrawable(context.getResources(), bitmap);
        }
        return icon;
    }

    public static void saveIcon(Context context, Bitmap icon, String filename) {
        File directory = new File(context.getFilesDir() + "/icons");
        if (!directory.exists()) {
            directory.mkdir();
        }

        File file = new File(context.getFilesDir() + "/icons/" + filename + ".png");
        removeIcon(context, filename);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            icon.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeIcon(Context context, String filename) {
        File file = new File(context.getFilesDir() + "/icons/" + filename + ".png");
        if (file.exists()) {
            try {
                file.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static View.OnTouchListener getItemOnTouchListener(Item item, final ItemGestureListener.ItemGestureCallback itemGestureCallback) {
        final ItemGestureListener itemGestureListener = Definitions.ENABLE_ITEM_TOUCH_LISTENER && itemGestureCallback != null ? new ItemGestureListener(Setup.appContext(), item, itemGestureCallback) : null;
        return (view, motionEvent) -> {
            Home.Companion.setItemTouchX((int) motionEvent.getX());
            Home.Companion.setItemTouchY((int) motionEvent.getY());
            return itemGestureListener != null && itemGestureListener.onTouchEvent(motionEvent);
        };
    }

    public static void copy(Context context, String stringIn, String stringOut) {
        try {
            File desktopData = new File(stringOut);
            desktopData.delete();
            File dockData = new File(stringOut);
            dockData.delete();
            File generalSettings = new File(stringOut);
            generalSettings.delete();
            Tool.print("deleted");

            FileInputStream in = new FileInputStream(stringIn);
            FileOutputStream out = new FileOutputStream(stringOut);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();

            // write the output file
            out.flush();
            out.close();
            Tool.print("copied");

        } catch (Exception e) {
            Toast.makeText(context, R.string.dialog__backup_app_settings__error, Toast.LENGTH_SHORT).show();
        }
    }

    public static <A extends App> List<A> getRemovedApps(List<A> oldApps, List<A> newApps) {
        List<A> removed = new ArrayList<>();
        // if this is the first call to this function and we did not know any app yet, we return an empty list
        if (oldApps.size() == 0) {
            return removed;
        }
        // we can't rely on sizes because apps may have been installed and deinstalled!
        //if (oldApps.size() > newApps.size()) {
        for (int i = 0; i < oldApps.size(); i++) {
            if (!newApps.contains(oldApps.get(i))) {
                removed.add(oldApps.get(i));
                break;
            }
        }
//        }
        return removed;
    }
}
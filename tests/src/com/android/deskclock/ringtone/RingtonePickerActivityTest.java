package com.android.deskclock.ringtone;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.InstrumentationRegistry;

import com.android.deskclock.ItemAdapter;
import com.android.deskclock.ItemAdapter.ItemHolder;
import com.android.deskclock.R;
import com.android.deskclock.Utils;
import com.android.deskclock.data.DataModel;
import com.android.deskclock.provider.Alarm;

import androidx.test.filters.SmallTest;
import android.test.AndroidTestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/** Exercise the user interface that adjusts the selected ringtone. */
@RunWith(AndroidJUnit4.class)
public class RingtonePickerActivityTest extends AndroidTestCase {

  private RingtonePickerActivity activity;
  private RecyclerView ringtoneList;
  private ItemAdapter<ItemHolder<Uri>> ringtoneAdapter;

  @Override
  public void setUp() {

  }

  @Override
  public void tearDown() {
    ringtoneAdapter = null;
    activity = null;
    ringtoneList = null;
  }

  @SmallTest
  public void testValidateDefaultState_TimerRingtonePicker_WithIntentResponder() {
    createTimerRingtonePickerActivity();

//    assertEquals(1, providerAdapter.getItemCount());

    final List<ItemHolder<Uri>> systemRingtoneHolders = ringtoneAdapter.getItems();

    assertEquals(5, systemRingtoneHolders.size());
    final Iterator<ItemHolder<Uri>> itemsIter = systemRingtoneHolders.iterator();

    final RingtoneHolder filesHeaderHolder = (RingtoneHolder) itemsIter.next();
    assertEquals("Your sounds", filesHeaderHolder.getName());
    assertEquals(HeaderViewHolder.VIEW_TYPE_ITEM_HEADER, filesHeaderHolder.getItemViewType());

    final AddCustomRingtoneHolder addNewHolder = (AddCustomRingtoneHolder) itemsIter.next();
//    assertEquals("Add new", addNewHolder.getName());
    assertEquals(AddCustomRingtoneViewHolder.VIEW_TYPE_ADD_NEW, addNewHolder.getItemViewType());

    final RingtoneHolder systemHeaderHolder = (RingtoneHolder) itemsIter.next();
    assertEquals("Device sounds", systemHeaderHolder.getName());
    assertEquals(HeaderViewHolder.VIEW_TYPE_ITEM_HEADER, systemHeaderHolder.getItemViewType());

    final RingtoneHolder silentHolder = (RingtoneHolder) itemsIter.next();
    assertEquals("Silent", silentHolder.getName());
    assertEquals(Utils.RINGTONE_SILENT, silentHolder.getUri());
    assertEquals(RingtoneViewHolder.VIEW_TYPE_SYSTEM_SOUND, silentHolder.getItemViewType());

    final RingtoneHolder defaultHolder = (RingtoneHolder) itemsIter.next();
    assertEquals("Timer Expired", defaultHolder.getName());
    assertEquals(DataModel.getDataModel().getDefaultTimerRingtoneUri(), defaultHolder.getUri());
    assertEquals(RingtoneViewHolder.VIEW_TYPE_SYSTEM_SOUND, defaultHolder.getItemViewType());

    // Verify initial selection.
    assertSame(
            DataModel.getDataModel().getTimerRingtoneUri(),
            DataModel.getDataModel().getDefaultTimerRingtoneUri());
  }

  private void createTimerRingtonePickerActivity() {
    Runnable createRingtonePickerActivityRunnable = () -> {
      final Context context = getContext();
      final Intent intent = RingtonePickerActivity.createTimerRingtonePickerIntent(context);
      createRingtonePickerActivity(intent);
    };
    InstrumentationRegistry.getInstrumentation().runOnMainSync(createRingtonePickerActivityRunnable);
  }

  private void createAlarmRingtonePickerActivity(Uri ringtone) {
    // Use the custom ringtone in some alarms.
    final Alarm alarm = new Alarm(1, 1);
    alarm.enabled = true;
    alarm.vibrate = true;
    alarm.alert = ringtone;
    alarm.deleteAfterUse = true;

    final Context context = getContext();
    final Intent intent = RingtonePickerActivity.createAlarmRingtonePickerIntent(context, alarm);
    createRingtonePickerActivity(intent);
  }

  @SuppressWarnings("unchecked")
  private void createRingtonePickerActivity(Intent intent) {
    activity = new RingtonePickerActivity();
    activity.onCreate(null);

    ringtoneList = activity.findViewById(R.id.ringtone_content);
    ringtoneAdapter = (ItemAdapter<ItemHolder<Uri>>) ringtoneList.getAdapter();
  }
}

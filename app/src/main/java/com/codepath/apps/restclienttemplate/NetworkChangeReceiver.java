package com.codepath.apps.restclienttemplate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

public class NetworkChangeReceiver extends BroadcastReceiver {

  public interface NetworkChange {
    void onNetworkChange(boolean isConnected);
  }
  private final NetworkChange networkChange;

  public NetworkChangeReceiver(NetworkChange networkChange) {
    this.networkChange = networkChange;
  }

  @Override
  public void onReceive(final Context context, final Intent intent) {
    networkChange.onNetworkChange(NetworkUtil.isConnected(context));
  }

  public static class NetworkUtil {
    public static boolean isConnected(Context context) {
      ConnectivityManager connMgr =
          (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      return connMgr.getActiveNetworkInfo() != null && connMgr.getActiveNetworkInfo().isConnected();
    }
  }
}

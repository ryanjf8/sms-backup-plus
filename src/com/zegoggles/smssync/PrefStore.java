/* Copyright (c) 2009 Christoph Studer <chstuder@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zegoggles.smssync;

import android.util.Log;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefStore {
    /**
     * Preference key containing the maximum date of messages that were
     * successfully synced.
     */
    static final String PREF_MAX_SYNCED_DATE = "max_synced_date";

    /** Preference key containing the Google account username. */
    static final String PREF_LOGIN_USER = "login_user";

    /** Preference key containing the Google account password. */
    static final String PREF_LOGIN_PASSWORD = "login_password";

    /** Preference key containing a UID used for the threading reference header. */
    static final String PREF_REFERENECE_UID = "reference_uid";

    /** Preference key containing the server address */
    static final String PREF_SERVER_ADDRESS = "server_address";

    /** Preference key containing the server protocol */
    static final String PREF_SERVER_PROTOCOL = "server_protocol";

    static final String PREF_SERVER_AUTHENTICATION = "server_authentication";

    static final String PREF_OAUTH_TOKEN = "oauth_token";
    static final String PREF_OAUTH_TOKEN_SECRET = "oauth_token_secret";

    /** Preference key containing the IMAP folder name where SMS should be backed up to. */
    static final String PREF_IMAP_FOLDER = "imap_folder";

    /** Preference key for storing whether to enable auto sync or not. */
    static final String PREF_ENABLE_AUTO_SYNC = "enable_auto_sync";

    /** Preference key for the timeout between an SMS is received and the scheduled sync. */
    static final String PREF_INCOMING_TIMEOUT_SECONDS = "incoming_timeout_seconds";

    /** Preference key for the interval between backup of outgoing SMS. */
    static final String PREF_REGULAR_TIMEOUT_SECONDS = "regular_timeout_seconds";

    /** Preference for storing the time of the last sync. */
    static final String PREF_LAST_SYNC = "last_sync";

    /** Preference for storing the maximum items per sync. */
    static final String PREF_MAX_ITEMS_PER_SYNC = "max_items_per_sync";

    /** Preference for storing the maximum items per restore. */
    static final String PREF_MAX_ITEMS_PER_RESTORE = "max_items_per_restore";

    /** Preference for storing whether backed up messages should be marked as read on Gmail. */
    static final String PREF_MARK_AS_READ = "mark_as_read";

    /** Preference for storing whether restored messages should be marked as read. */
    static final String PREF_MARK_AS_READ_ON_RESTORE = "mark_as_read_on_restore";

    static final String PREF_PREFILLED  = "prefilled";
    static final String PREF_CONNECTED  = "connected";
    static final String PREF_WIFI_ONLY  = "wifi_only";

    /** Default value for {@link PrefStore#PREF_MAX_SYNCED_DATE}. */
    static final long DEFAULT_MAX_SYNCED_DATE = -1;

    /** Default value for {@link PrefStore#PREF_IMAP_FOLDER}. */
    static final String DEFAULT_IMAP_FOLDER = "SMS";

    /** Default value for {@link PrefStore#PREF_ENABLE_AUTO_SYNC}. */
    static final boolean DEFAULT_ENABLE_AUTO_SYNC = false;

    /** Default value for {@link PrefStore#PREF_INCOMING_TIMEOUT_SECONDS}. */
    static final int DEFAULT_INCOMING_TIMEOUT_SECONDS = 60 * 3;

    /** Default value for {@link PrefStore#PREF_REGULAR_TIMEOUT_SECONDS}. */
    static final int DEFAULT_REGULAR_TIMEOUT_SECONDS = 2 * 60 * 60; // 2h

    /** Default value for {@link #PREF_LAST_SYNC}. */
    static final long DEFAULT_LAST_SYNC = -1;

    /** Default value for {@link #PREF_MAX_ITEMS_PER_SYNC}. */
    static final String DEFAULT_MAX_ITEMS_PER_SYNC = "-1";

    static final String DEFAULT_MAX_ITEMS_PER_RESTORE = "-1";

    /** Default value for {@link #PREF_MARK_AS_READ}. */
    static final boolean DEFAULT_MARK_AS_READ = true;

    static final boolean DEFAULT_MARK_AS_READ_ON_RESTORE = true;

    /** Default value for {@link #PREF_SERVER_ADDRESS}. */
    static final String DEFAULT_SERVER_ADDRESS = "imap.gmail.com:993";

    /** Default value for {@link #PREF_SERVER_PROTOCOL}. */
    static final String DEFAULT_SERVER_PROTOCOL = "ssl";

    enum AuthMode { PLAIN, XOAUTH };

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    static long getMaxSyncedDate(Context ctx) {
        return getSharedPreferences(ctx).getLong(PREF_MAX_SYNCED_DATE, DEFAULT_MAX_SYNCED_DATE);
    }

    static boolean isMaxSyncedDateSet(Context ctx) {
        return getSharedPreferences(ctx).contains(PREF_MAX_SYNCED_DATE);
    }

    static void setMaxSyncedDate(Context ctx, long maxSyncedDate) {
        getSharedPreferences(ctx).edit()
          .putLong(PREF_MAX_SYNCED_DATE, maxSyncedDate)
          .commit();
    }

    static String getLoginUsername(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_LOGIN_USER, null);
    }

    static void setLoginUsername(Context ctx, String s) {
       getSharedPreferences(ctx).edit().putString(PREF_LOGIN_USER, s).commit();
    }

    static String getLoginPassword(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_LOGIN_PASSWORD, null);
    }

    static XOAuthConsumer getOAuthConsumer(Context ctx) {
        return new XOAuthConsumer(
            getLoginUsername(ctx),
            getOauthToken(ctx),
            getOauthTokenSecret(ctx));
    }

    static String getOauthToken(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_OAUTH_TOKEN, null);
    }

    static String getOauthTokenSecret(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_OAUTH_TOKEN_SECRET, null);
    }

    static boolean hasOauthTokens(Context ctx) {
        return getOauthToken(ctx) != null &&
               getOauthTokenSecret(ctx) != null;
    }

    static void setOauthTokens(Context ctx, String token, String secret) {
      getSharedPreferences(ctx).edit()
        .putString(PREF_OAUTH_TOKEN, token)
        .putString(PREF_OAUTH_TOKEN_SECRET, secret)
        .commit();
    }

    static AuthMode getAuthMode(Context ctx) {
        return AuthMode.valueOf(
          getSharedPreferences(ctx).getString(PREF_SERVER_AUTHENTICATION, AuthMode.XOAUTH.toString())
                                   .toUpperCase());
    }

    static boolean useXOAuth(Context ctx) {
        return getAuthMode(ctx) == AuthMode.XOAUTH && isGmail(ctx);
    }

    public static boolean isLoginUsernameSet(Context ctx) {
        return getLoginUsername(ctx) != null;
    }

    static boolean isLoginInformationSet(Context ctx) {
        if (getAuthMode(ctx) == AuthMode.PLAIN) {
            return isLoginUsernameSet(ctx) && getLoginPassword(ctx) != null;
        } else {
            return hasOauthTokens(ctx) && getLoginUsername(ctx) != null;
        }
    }

    static String getReferenceUid(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_REFERENECE_UID, null);
    }

    static void setReferenceUid(Context ctx, String referenceUid) {
        getSharedPreferences(ctx).edit()
          .putString(PREF_REFERENECE_UID, referenceUid)
          .commit();
    }

    static String getImapFolder(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_IMAP_FOLDER, DEFAULT_IMAP_FOLDER);
    }

    static boolean isImapFolderSet(Context ctx) {
        return getSharedPreferences(ctx).contains(PREF_IMAP_FOLDER);
    }

    static int getMaxItemsPerSync(Context ctx) {
      return getStringAsInt(ctx, PREF_MAX_ITEMS_PER_SYNC, DEFAULT_MAX_ITEMS_PER_SYNC);
    }

    static int getMaxItemsPerRestore(Context ctx) {
      return getStringAsInt(ctx, PREF_MAX_ITEMS_PER_RESTORE, DEFAULT_MAX_ITEMS_PER_RESTORE);
    }

    static boolean isWifiOnly(Context ctx) {
      return (getSharedPreferences(ctx).getBoolean(PREF_WIFI_ONLY, false));
    }

    private static int getStringAsInt(Context ctx, String key, String def) {
        try {
          return Integer.valueOf(getSharedPreferences(ctx).getString(key, def));
        } catch (NumberFormatException e) {
           return Integer.valueOf(def);
        }
      }

    /**
     * Returns whether an IMAP folder is valid. This is the case if the name
     * only contains unaccented latin letters <code>[a-zA-Z]</code>.
     */
    static boolean isValidImapFolder(String imapFolder) {
        for (int i = 0; i < imapFolder.length(); i++) {
            char currChar = imapFolder.charAt(i);
            if (!((currChar >= 'a' && currChar <= 'z')
                    || (currChar >= 'A' && currChar <= 'Z')
                    || (currChar == '.')
                    || (currChar == '/')
                    )) {
                return false;
            }
        }
        return true;
    }

    static void setImapFolder(Context ctx, String imapFolder) {
        getSharedPreferences(ctx).edit()
          .putString(PREF_IMAP_FOLDER, imapFolder)
          .commit();
    }

    static boolean isEnableAutoSync(Context ctx) {
        return getSharedPreferences(ctx).getBoolean(PREF_ENABLE_AUTO_SYNC,
                DEFAULT_ENABLE_AUTO_SYNC);
    }

    static boolean isEnableAutoSyncSet(Context ctx) {
        return getSharedPreferences(ctx).contains(PREF_ENABLE_AUTO_SYNC);
    }

    static void setEnableAutoSync(Context ctx, boolean enableAutoSync) {
        getSharedPreferences(ctx).edit()
          .putBoolean(PREF_ENABLE_AUTO_SYNC, enableAutoSync)
          .commit();
    }

    static int getIncomingTimeoutSecs(Context ctx) {
       return getSharedPreferences(ctx).getInt(PREF_INCOMING_TIMEOUT_SECONDS,
               DEFAULT_INCOMING_TIMEOUT_SECONDS);
    }

    static int getRegularTimeoutSecs(Context ctx) {
        return getSharedPreferences(ctx).getInt(PREF_REGULAR_TIMEOUT_SECONDS,
                DEFAULT_REGULAR_TIMEOUT_SECONDS);
    }

    static long getLastSync(Context ctx) {
        return getSharedPreferences(ctx).getLong(PREF_LAST_SYNC, DEFAULT_LAST_SYNC);
    }

    static void setLastSync(Context ctx) {
        getSharedPreferences(ctx).edit()
          .putLong(PREF_LAST_SYNC, System.currentTimeMillis())
          .commit();
    }

    static boolean getMarkAsRead(Context ctx) {
        return getSharedPreferences(ctx).getBoolean(PREF_MARK_AS_READ, DEFAULT_MARK_AS_READ);
    }

    static void setMarkAsRead(Context ctx, boolean markAsRead) {
        getSharedPreferences(ctx).edit()
          .putBoolean(PREF_MARK_AS_READ, markAsRead)
          .commit();
    }

    static boolean getMarkAsReadOnRestore(Context ctx) {
        return getSharedPreferences(ctx).getBoolean(PREF_MARK_AS_READ_ON_RESTORE, DEFAULT_MARK_AS_READ_ON_RESTORE);
    }

    static void setMarkAsReadOnRestore(Context ctx, boolean markAsRead) {
        getSharedPreferences(ctx).edit()
          .putBoolean(PREF_MARK_AS_READ_ON_RESTORE, markAsRead)
          .commit();
    }

    static boolean isFirstSync(Context ctx) {
        return !getSharedPreferences(ctx).contains(PREF_MAX_SYNCED_DATE);
    }

    static boolean isFirstUse(Context ctx) {
        final String key = "first_use";

        if (isFirstSync(ctx) &&
            !getSharedPreferences(ctx).contains(key)) {
            getSharedPreferences(ctx).edit().putBoolean(key, false).commit();
            return true;
        } else {
            return false;
        }
    }

    static void clearSyncData(Context ctx) {
        getSharedPreferences(ctx).edit()
          .remove(PREF_LOGIN_USER)
          .remove(PREF_LOGIN_PASSWORD)
          .remove(PREF_OAUTH_TOKEN)
          .remove(PREF_OAUTH_TOKEN_SECRET)
          .remove(PREF_MAX_SYNCED_DATE)
          .remove(PREF_LAST_SYNC)
          .commit();
    }

    static String getServerAddress(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_SERVER_ADDRESS, DEFAULT_SERVER_ADDRESS);
    }

    static void setServerAddress(Context ctx, String serverAddress) {
         getSharedPreferences(ctx).edit()
           .putString(PREF_SERVER_ADDRESS, serverAddress)
           .commit();
     }

     static String getServerProtocol(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_SERVER_PROTOCOL, DEFAULT_SERVER_PROTOCOL);
    }

    static boolean isGmail(Context ctx) {
        return "imap.gmail.com:993".equalsIgnoreCase(getServerAddress(ctx));
    }

    static String getVersion(Context context, boolean code) {
      android.content.pm.PackageInfo pInfo = null;
      try {
        pInfo = context.getPackageManager().getPackageInfo(
                SmsSync.class.getPackage().getName(),
                android.content.pm.PackageManager.GET_META_DATA);
        return ""+ (code ? pInfo.versionCode : pInfo.versionName);
      } catch (android.content.pm.PackageManager.NameNotFoundException e) {
        Log.e(Consts.TAG, "error", e);
        return null;
      }
    }

    static boolean showUpgradeMessage(Context ctx) {
      final String key = "upgrade_message_seen";
      boolean seen = getSharedPreferences(ctx).getBoolean(key, false);
      if (!seen && isOldSmsBackupInstalled(ctx)) {
        getSharedPreferences(ctx).edit().putBoolean(key, true).commit();
        return true;
      } else {
        return false;
      }
    }

    static boolean isOldSmsBackupInstalled(Context context) {
      try {
        context.getPackageManager().getPackageInfo(
            "tv.studer.smssync",
            android.content.pm.PackageManager.GET_META_DATA);
        return true;
      } catch (android.content.pm.PackageManager.NameNotFoundException e) {
        return false;
      }
    }
}

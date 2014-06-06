package ie.ucc.bis.supportinglife.dao;

import ie.ucc.bis.supportinglife.ui.utilities.LoggerUtils;

import java.util.Map;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.util.Base64;

/**
 * 
 * Usage:
 *    CustomSharedPreferences preferences = CustomSharedPreferences.getPrefs(this, MY_APP_NAME, Context.MODE_PRIVATE);
 *    //to get data
 *    
 *    preferences.getString("key", null);
 *    //to store data
 *    preferences.edit().putString("key","value").commit();
 *    
 */
public class CustomSharedPreferences implements SharedPreferences {
	
	private final String LOG_TAG = "ie.ucc.bis.supportinglife.dao.CustomSharedPreferences";
	
    protected static final String UTF8 = "UTF-8";
    // this key is defined at runtime
    private static char[] SEKRIT = null; 

    protected SharedPreferences delegate;
    protected Context context;
    private static CustomSharedPreferences preferences = null;
    
    // Set to true if a decryption error was detected
    // in the case of float, int, and long we can tell if there was a parse error
    // this does not detect an error in strings or boolean - that requires more sophisticated checks
    public static boolean decryptionErrorFlag = false; 
  
    /**
     * Constructor
     * @param context
     * @param delegate - SharedPreferences object from the system
     */
    public CustomSharedPreferences(Context context, SharedPreferences delegate) {
        setDelegate(delegate);
        setContext(context);
        SEKRIT = Settings.Secure.ANDROID_ID.toCharArray();
    }
    
    /**
     * Only used to change to a new key during runtime.
     * If you don't want to use the default per-device key for example
     * @param key
     */
    public static void setNewKey(String key) {
    	SEKRIT = key.toCharArray();
    }

    /**
     * Accessor to grab the preferences in a singleton.  This stores the reference in a singleton so it can be accessed repeatedly with 
     * no performance penalty
     * @param context - the context used to access the preferences.
     * @param appName - domain the shared preferences should be stored under
     * @param contextMode - Typically Context.MODE_PRIVATE
     * @return
     */
    public synchronized static CustomSharedPreferences getPrefs(Context context, String appName, int contextMode) {
    	if (getPreferences() == null) {
    		//make sure to use application context since preferences live outside an Activity
    		//use for objects that have global scope like: prefs or starting services
    		setPreferences(new CustomSharedPreferences( 
    				context.getApplicationContext(), context.getApplicationContext().getSharedPreferences(appName, contextMode)));
    	}
    	return getPreferences();
    }
    
    public class Editor implements SharedPreferences.Editor {
        protected SharedPreferences.Editor delegate;

        public Editor() {
            this.delegate = CustomSharedPreferences.this.delegate.edit();                    
        }

        @Override
        public Editor putBoolean(String key, boolean value) {
            delegate.putString(key, encrypt(Boolean.toString(value)));
            return this;
        }

        @Override
        public Editor putFloat(String key, float value) {
            delegate.putString(key, encrypt(Float.toString(value)));
            return this;
        }

        @Override
        public Editor putInt(String key, int value) {
            delegate.putString(key, encrypt(Integer.toString(value)));
            return this;
        }

        @Override
        public Editor putLong(String key, long value) {
            delegate.putString(key, encrypt(Long.toString(value)));
            return this;
        }

        @Override
        public Editor putString(String key, String value) {
            delegate.putString(key, encrypt(value));
            return this;
        }

        @Override
        public void apply() {
        	//to maintain compatibility with android level 7 
        	delegate.commit();
        }

        @Override
        public Editor clear() {
            delegate.clear();
            return this;
        }

        @Override
        public boolean commit() {
            return delegate.commit();
        }

        @Override
        public Editor remove(String s) {
            delegate.remove(s);
            return this;
        }

		@Override
		public android.content.SharedPreferences.Editor putStringSet(String key, Set<String> values) {
			throw new RuntimeException("Implementation does not currently support String Sets.");
		}
    } // end of inner class 'Editor'

    public Editor edit() {
        return new Editor();
    }


    @Override
    public Map<String, ?> getAll() {
        throw new UnsupportedOperationException("Implementation does not currently support Maps.");
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
    	// note: if this wasn't encrypted, then it won't be a string
    	String encryptedValue;
    	try {
    		encryptedValue = getDelegate().getString(key, null);
    	} catch (ClassCastException e) {
    		return getDelegate().getBoolean(key, defValue);
    	}
        return encryptedValue!=null ? Boolean.parseBoolean(decrypt(encryptedValue)) : defValue;
    }

    @Override
    public float getFloat(String key, float defValue) {
    	String encryptedValue;
    	try {
    		encryptedValue = getDelegate().getString(key, null);
    	} catch (ClassCastException e) {
    		return getDelegate().getFloat(key, defValue);
    	}
    	try {
			return Float.parseFloat(decrypt(encryptedValue));
		} catch (NumberFormatException e) {
			// could not decrypt the number.  Maybe we are using the wrong key?
			decryptionErrorFlag = true;
			LoggerUtils.i(LOG_TAG, "Warning, could not decrypt the value.  Possible incorrect key. " + e.getMessage());
		}
    	return defValue;
    }

    @Override
    public int getInt(String key, int defValue) {
    	String encryptedValue;
    	try {
    		encryptedValue = getDelegate().getString(key, null);
    	} catch (ClassCastException e) {
    		return getDelegate().getInt(key, defValue);
    	}
    	try {
			return Integer.parseInt(decrypt(encryptedValue));
		} catch (NumberFormatException e) {
			// could not decrypt the number.  Maybe we are using the wrong key?
			decryptionErrorFlag = true;
			LoggerUtils.i(LOG_TAG, "Warning, could not decrypt the value.  Possible incorrect key. " + e.getMessage());
		}
    	return defValue;
    }

    @Override
    public long getLong(String key, long defValue) {
    	String encryptedValue;
    	try {
    		encryptedValue = getDelegate().getString(key, null);
    	} catch (ClassCastException e) {
    		return getDelegate().getLong(key, defValue);
    	}
    	try {
			return Long.parseLong(decrypt(encryptedValue));
		} catch (NumberFormatException e) {
			// could not decrypt the number.  Maybe we are using the wrong key?
			decryptionErrorFlag = true;
			LoggerUtils.i(LOG_TAG, "Warning, could not decrypt the value.  Possible incorrect key. " + e.getMessage());
		}
    	return defValue;
    }

    @Override
    public String getString(String key, String defValue) {
        final String encryptedValue = getDelegate().getString(key, null);
        return encryptedValue != null ? decrypt(encryptedValue) : defValue;
    }

    @Override
    public boolean contains(String s) {
        return getDelegate().contains(s);
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
    	getDelegate().registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
    	getDelegate().unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

	@Override
	public Set<String> getStringSet(String key, Set<String> defValues) {
		throw new RuntimeException("Implementation does not currently support String Sets.");
	}


    protected String encrypt(String plaintextValue) {
        try {
            final byte[] bytes = plaintextValue!=null ? plaintextValue.getBytes(UTF8) : new byte[0];
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(SEKRIT));
            Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
            pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(Settings.Secure.getString(context.getContentResolver(), Secure.ANDROID_ID).getBytes(UTF8), 20));
            return new String(Base64.encode(pbeCipher.doFinal(bytes), Base64.NO_WRAP),UTF8);
        } catch(Exception e) {
        	LoggerUtils.i(LOG_TAG, "Runtime exception on encryption of plaintext shared preferences value " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    protected String decrypt(String encryptedValue){
        try {
            final byte[] bytes = encryptedValue!=null ? Base64.decode(encryptedValue,Base64.DEFAULT) : new byte[0];
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(SEKRIT));
            Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
            pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(Settings.Secure.getString(context.getContentResolver(), Secure.ANDROID_ID).getBytes(UTF8), 20));
            return new String(pbeCipher.doFinal(bytes),UTF8);
        } catch(Exception e) {
        	LoggerUtils.i(LOG_TAG, "Warning, could not decrypt the value. It may be stored in plaintext. " + e.getMessage());
        	return encryptedValue;
        }
    }

	public SharedPreferences getDelegate() {
		return delegate;
	}

	public void setDelegate(SharedPreferences delegate) {
		this.delegate = delegate;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public static CustomSharedPreferences getPreferences() {
		return preferences;
	}

	public static void setPreferences(CustomSharedPreferences preferences) {
		CustomSharedPreferences.preferences = preferences;
	}
}
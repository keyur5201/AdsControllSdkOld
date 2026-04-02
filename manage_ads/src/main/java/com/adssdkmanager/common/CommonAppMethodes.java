package com.adssdkmanager.common;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import com.adssdkmanager.R;
import com.adssdkmanager.loadData.AdsPreferences;
import com.facebook.ads.AudienceNetworkAds;
import com.onesignal.OneSignal;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class CommonAppMethodes {

    public Activity activity;

    public CommonAppMethodes(Activity activity) {
        this.activity = activity;
    }

    public void StatusbarColor(int setColor) {
        activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, setColor));
    }

    public void HideNavbarWithLightStatus(int setColor) {
        activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, setColor));
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public void HideNavbarWithLightStatus() {
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public void HideNavigationBar() {
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public void LightStatus(int setColor) {
        activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, setColor));
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    public void LightStatus() {
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    public void SetSystemFullScreen() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = activity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public void StatusbarTrans() {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    //============== Rate, Share & Privacy Click =================//
    //============== Rate, Share & Privacy Click =================//

    public void RedirectToPlay(String packageName) {
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
        } catch (ActivityNotFoundException anfe) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
        }
    }

    public void RateApp() {
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + activity.getPackageName())));
        } catch (ActivityNotFoundException anfe) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + activity.getPackageName())));
        }
    }

    public void ShareApp(int appName) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.SUBJECT", activity.getString(appName));
        intent.putExtra("android.intent.extra.TEXT", "\nhttps://play.google.com/store/apps/details?id=" + activity.getPackageName());
        intent.setType("text/plain");
        activity.startActivity(Intent.createChooser(intent, "Share"));
    }

    public void PrivacyPolicy() {
        new CommonAppMethodes(activity).ChromeView(new AdsPreferences(activity).getPrivacyUrl());
    }


    //============== Read Data From Raw-Json =================//
    //============== Read Data From Raw-Json =================//

    public String ReadJsonDataFromFile(int fileName) throws IOException {

        InputStream inputStream = null;
        StringBuilder builder = new StringBuilder();

        try {
            String jsonDataString = null;
            inputStream = activity.getResources().openRawResource(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            while ((jsonDataString = bufferedReader.readLine()) != null) {
                builder.append(jsonDataString);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return new String(builder);
    }


    //============== Chromview Android =================//
    //============== Chromview Android =================//

    public void ChromeView(String url) {
        try {
            CustomTabsIntent.Builder customIntent = new CustomTabsIntent.Builder();

            customIntent.setToolbarColor(ContextCompat.getColor(activity, android.R.color.black));

            String packageName = "com.android.chrome";
            if (packageName != null) {
                customIntent.build().intent.setPackage(packageName);
                customIntent.build().launchUrl(activity, Uri.parse(url));
            } else {
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        } catch (ActivityNotFoundException | NullPointerException | WindowManager.BadTokenException e) {
            e.printStackTrace();
        }
    }

    public static void encryptImage(String filePath, String password) throws Exception {
        // Initialize a secure random number generator
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);

        // Derive the key from the password
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKey secretKey = factory.generateSecret(keySpec);

        // Create the cipher
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(new byte[16]));

        // Encrypt the image
        FileInputStream inputStream = new FileInputStream(filePath);
        FileOutputStream outputStream = new FileOutputStream(filePath + ".enc");
        outputStream.write(salt);
        byte[] input = new byte[64];
        int bytesRead;
        while ((bytesRead = inputStream.read(input)) != -1) {
            byte[] output = cipher.update(input, 0, bytesRead);
            if (output != null) {
                outputStream.write(output);
            }
        }

        byte[] output = cipher.doFinal();
        if (output != null) {
            outputStream.write(output);
        }

        inputStream.close();
        outputStream.flush();
        outputStream.close();
    }

    public static void decryptImage(String filePath, String password) throws Exception {
        // Read the salt
        FileInputStream inputStream = new FileInputStream(filePath);
        byte[] salt = new byte[16];
        inputStream.read(salt);

        // Derive the key from the password
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKey secretKey = factory.generateSecret(keySpec);

        // Create the cipher
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(new byte[16]));

        // Decrypt the image
        FileOutputStream outputStream = new FileOutputStream(filePath.replace(".enc", ""));
        byte[] input = new byte[64];
        int bytesRead;
        while ((bytesRead = inputStream.read(input)) != -1) {
            byte[] output = cipher.update(input, 0, bytesRead);
            if (output != null) {
                outputStream.write(output);
            }
        }

        byte[] output = cipher.doFinal();
        if (output != null) {
            outputStream.write(output);
        }

        inputStream.close();
    }


    private String encryptedFileName = "encrypted_Audio.mp3";
    private static String algorithm = "AES";
    static SecretKey yourKey = null;

    public void saveAndDecodeAudio() {
        try {
            saveFile(getAudioFile());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        decodeFile();
    }


    public static SecretKey generateKey(char[] passphraseOrPin, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Number of PBKDF2 hardening rounds to use. Larger values increase
        // computation time. You should select a value that causes computation
        // to take >100ms.
        final int iterations = 1000;

        // Generate a 256-bit key
        final int outputKeyLength = 256;

        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec keySpec = new PBEKeySpec(passphraseOrPin, salt, iterations, outputKeyLength);
        SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
        return secretKey;
    }

    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        // Generate a 256-bit key
        final int outputKeyLength = 256;
        SecureRandom secureRandom = new SecureRandom();
        // Do *not* seed secureRandom! Automatically seeded from system entropy.
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(outputKeyLength, secureRandom);
        yourKey = keyGenerator.generateKey();
        return yourKey;
    }

    public static byte[] encodeFile(SecretKey yourKey, byte[] fileData) throws Exception {
        byte[] encrypted = null;
        byte[] data = yourKey.getEncoded();
        SecretKeySpec skeySpec = new SecretKeySpec(data, 0, data.length, algorithm);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        encrypted = cipher.doFinal(fileData);
        return encrypted;
    }

    public static byte[] decodeFile(SecretKey yourKey, byte[] fileData) throws Exception {
        byte[] decrypted = null;
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, yourKey, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        decrypted = cipher.doFinal(fileData);
        return decrypted;
    }

    public void saveFile(byte[] stringToSave) {
        try {
            File file = new File(Environment.getExternalStorageDirectory() + File.separator, encryptedFileName);

            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            yourKey = generateKey();
            byte[] filesBytes = encodeFile(yourKey, stringToSave);
            bos.write(filesBytes);
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void decodeFile() {

        try {
            byte[] decodedData = decodeFile(yourKey, readFile());
            // String str = new String(decodedData);
            //System.out.println("DECODED FILE CONTENTS : " + str);
            playMp3(decodedData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] readFile() {
        byte[] contents = null;

        File file = new File(Environment.getExternalStorageDirectory() + File.separator, encryptedFileName);
        int size = (int) file.length();
        contents = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            try {
                buf.read(contents);
                buf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return contents;
    }

    public byte[] getAudioFile() throws FileNotFoundException {
        byte[] audio_data = null;
        byte[] inarry = null;
        AssetManager am = activity.getAssets();
        try {
            InputStream is = am.open("Sleep Away.mp3"); // use recorded file instead of getting file from assets folder.
            int length = is.available();
            audio_data = new byte[length];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            while ((bytesRead = is.read(audio_data)) != -1) {
                output.write(audio_data, 0, bytesRead);
            }
            inarry = output.toByteArray();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return inarry;

    }

    private void playMp3(byte[] mp3SoundByteArray) {

        try {
            // create temp file that will hold byte array
            File tempMp3 = File.createTempFile("kurchina", "mp3", activity.getCacheDir());
            tempMp3.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tempMp3);
            fos.write(mp3SoundByteArray);
            fos.close();
            // Tried reusing instance of media player
            // but that resulted in system crashes...
            MediaPlayer mediaPlayer = new MediaPlayer();
            FileInputStream fis = new FileInputStream(tempMp3);
            mediaPlayer.setDataSource(fis.getFD());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException ex) {
            ex.printStackTrace();

        }

    }

}

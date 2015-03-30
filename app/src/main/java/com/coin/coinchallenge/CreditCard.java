package com.coin.coinchallenge;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by joe-work on 3/27/15.
 */
public class CreditCard implements Comparable<CreditCard>{

    /* EXAMPLE RESPONSE
    "updated" : "2015-01-21T18:28:07-0800",
      "card_number" : "3795 709408 58249",
      "background_image_url" : "https://s3.amazonaws.com/mobile.coin.vc/ios/
assignment/card_background_amex.png",
      "enabled" : true,
      "last_name" : "McMullen",
      "expiration_date" : "10/2015",
      "created" : "2014-11-26T13:48:07-0800",
      "guid" : "a4f74bc9-05b4-4afd-976a-e25b1258fe67",
      "first_name" : "Jeremy"
     */

    private static final String TIME_PATTERN = "yyyy-MM-ddHH:mm:ssz";

    public Date updated;
    public Date created;
    public boolean enabled;
    public URL background_image_url;
    public String first_name;
    public String last_name;
    public String card_number;
    public String expiration_date;
    public String guid;

    public CreditCard() {

    }

    //used for debug
    public CreditCard(final String firstName, final String lastName, final String cardNumber, final String expData) {
        this.first_name = firstName;
        this.last_name = lastName;
        this.card_number = cardNumber;
        this.expiration_date = expData;
    }

    //sort by newest first
    public int compareTo(CreditCard compareCard) {
        return compareCard.created.compareTo(this.created);

    }

    public Date stringToDate(final String time) {
//        String string = "2015-01-21T18:28:07-0800"; //example time
        String timeFormatted = time.replace("T", ""); //get rid of
        DateFormat format = new SimpleDateFormat(TIME_PATTERN, Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(timeFormatted);
        } catch (Throwable e) {

        }
        return date;
    }

    public void setTimeUpdated(final String timeUpdated) {
        this.updated = stringToDate(timeUpdated);
    }

    public void setTimeCreated(final String timeCreated) {
        this.created = stringToDate(timeCreated);
    }

    //there are actually different spaces based on formatting.
    public String getDisplayCCNumber() {
        //need to indent spaces based on number of them
        if(card_number==null){
            return "Voided Number".toUpperCase();
        }
        int count = card_number.length() - card_number.replace(" ", "").length();

        int indent = 1;
        if (count == 2) {
            indent = 4;
        } else if (count == 3) {
            indent = 3;
        }
        String repeatedSpace = new String(new char[indent]).replace("\0", " ");
        return card_number.replace(" ", repeatedSpace);
    }

    public String getFullDisplayName() {
        if(first_name==null || last_name==null){
            return "Your Name Here".toUpperCase();
        }
        return (first_name + " " + last_name).toUpperCase();
    }

    public String getDisplayExpDate() {
        if(expiration_date==null){
            return "mm / yy";
        }
        return expiration_date.replace("/", " / ");
    }
}

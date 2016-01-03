package srhodes1.villanova.ece.globechat;

import android.app.Application;

import com.parse.Parse;


/**
 * Created by wildcat on 11/21/2015.
 */
public class ParseApplication extends Application {

    public void onCreate() {
        Parse.enableLocalDatastore(this);
        Parse.initialize(this,"o0v9q23VTtoK5TE9vAySz3Ru1D6nGXrUonoqMCdZ","csM9B6SMQVDbpfTBEZjqtgotyO88cWWVmdYTccyD");
    }
}

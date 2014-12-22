package com.insanedevelopers.doglovers.quickblox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.insanedevelopers.doglovers.MainActivity;
import com.insanedevelopers.doglovers.R;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.content.QBContent;
import com.quickblox.content.model.QBFile;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.QBSettings;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.customobjects.QBCustomObjects;
import com.quickblox.customobjects.model.QBCustomObject;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
@SuppressWarnings("deprecation")
public class SplashActivity extends Activity{

    private Context context;
    private ProgressBar progressBar;
    String userName = null;
	String userPassword = null;
	ImageView btnFbLogin;

	// Your Facebook APP ID
	private static String APP_ID = "724679807624078"; // Replace with your App ID
	private Facebook facebook = new Facebook(APP_ID);
	private AsyncFacebookRunner mAsyncRunner;
	String FILENAME = "AndroidSSO_data";
	private SharedPreferences mPrefs;
	
	SQLiteOpenHelper dbHelper;
	SQLiteDatabase mydb;
		static String name="null";
		
		static String firstName="null";
		
		static String lastName="null";
		
		public static String email="null";
		
		public static String ids="null";
		
		static String link="null";
		
		static String gender="null";
		
		static String timezone="null";
		
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        StartAnimations();
        btnFbLogin = (ImageView) findViewById(R.id.btn_fblogin);
        
        //Check if Network is Available
        if(isNetworkAvailable()){
        	progressBar= (ProgressBar)findViewById(R.id.progress_bar);
        	
            mAsyncRunner = new AsyncFacebookRunner(facebook);
            
            context = this;
            
            if(doesDatabaseExist((ContextWrapper)getApplicationContext(),"DogLoversDB")){
            	
            	// Initialize QuickBlox application with credentials.
                QBSettings.getInstance().fastConfigInit(String.valueOf(Constants.APP_ID), Constants.AUTH_KEY, Constants.AUTH_SECRET);

                // Create QuickBlox session
            	QBAuth.createSession(new QBEntityCallbackImpl<QBSession>() {
                    @Override
                    public void onSuccess(QBSession qbSession, Bundle bundle) {
                    	
                        progressBar.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "This might take some Time", Toast.LENGTH_LONG).show();
                        createSession();
                        }
                    
                    @Override
                    public void onError(List<String> errors) {
                        // print errors that came from server
                    }
                });
            	
            }
            else{
            	btnFbLogin.setVisibility(View.VISIBLE);
            	/**
        		 * Login button Click event
        		 * */
        		btnFbLogin.setOnClickListener(new View.OnClickListener() {

        			@Override
        			public void onClick(View v) {
        				loginToFacebook();
        			}
        		});
            }
            
        	
        }
    }
    
    public void alertBox(){
    	
    	//Disabling Login Button
    	btnFbLogin.setVisibility(View.INVISIBLE);
    	
         AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
   	     
   		 alertDialogBuilder.setTitle(this.getTitle());
   		 alertDialogBuilder.setIcon(R.drawable.ic_launcher);
   		 alertDialogBuilder.setMessage("Please Enable your Network Connection and Retry");
   		 // set positive button: Yes message
   		 alertDialogBuilder.setPositiveButton("Retry",new DialogInterface.OnClickListener() {
   				public void onClick(DialogInterface dialog,int id) {
   					finish();
   					Intent positveActivity = new Intent(getApplicationContext(),
                               SplashActivity.class);
   		            startActivity(positveActivity);	
   				}
   			  });
   		 // set neutral button
   		 alertDialogBuilder.setNeutralButton("Exit the App",new DialogInterface.OnClickListener() {
   				public void onClick(DialogInterface dialog,int id) {
   					finish();
   				}
   			});
   		 
   		 AlertDialog alertDialog = alertDialogBuilder.create();
   		 // show alert
   		 alertDialog.show();
    	
    }
    
    
    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(isNetworkAvailable()){}
		else{
			alertBox();
		}
	}



	private boolean doesDatabaseExist(ContextWrapper applicationContext,
			String string) {
		
		File dbFile = applicationContext.getDatabasePath(string);
		return dbFile.exists();
	}
    
    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        RelativeLayout l=(RelativeLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);
        
        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.logo);
        iv.clearAnimation();
        iv.startAnimation(anim);
    }

    	//SignIn if user already exist
		private void createSession() {
			
			dbHelper = new SQLiteOpenHelper(context, "DogLoversDB", null, 1) {
				
				@Override
				public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
					
				}
				
				@Override
				public void onCreate(SQLiteDatabase db) {
					
				}
			};
			String col[]={"Login","Password"};
			Cursor cursor= dbHelper.getReadableDatabase().query("DogLoversTable",col , null, null, null, null,null);
			cursor.moveToFirst();
			
			while (!cursor.isAfterLast()) {
				 
				userName = cursor.getString(0);
				 
				userPassword = cursor.getString(1);
				 
				cursor.moveToNext();
				 
			}
			
	        QBUser qbUser = new QBUser(userName, userPassword);
	       
	        // Create QuickBlox session
	        QBAuth.createSession(qbUser, new QBEntityCallbackImpl<QBSession>() {
	            @Override
	            public void onSuccess(QBSession qbSession, Bundle bundle) {
	            	DataHolder.getDataHolder().setSignInUserId(qbSession.getUserId());
	            	
	            	getFileList();
	            	
	            }

	            @Override
	            public void onError(List<String> strings) {
	            	 Toast.makeText(getApplicationContext(), strings+"", Toast.LENGTH_LONG).show();
	            }
	        });
	        dbHelper.close();
	    }
		
		
		//Get files of images and text
		private void getFileList() {
			
			
			/* Error Error Error Error //
			 * 
			 * 
			 * ****
			 * when setPage(2)
			 *   no content , no image loaded
			 *  
			 *  
			 * ****
			 * requestBuilder.setPerPage(100);
			 *   no more than 100
			 * 
			 * requestBuilder.setPage(1);
			 * requestBuilder.setPerPage(100);
			 * 
			 * */
			
			  QBPagedRequestBuilder requestBuilder = new QBPagedRequestBuilder();
			  requestBuilder.setPage(1);
			  requestBuilder.setPerPage(100);
		        QBContent.getTaggedList(requestBuilder, new QBEntityCallbackImpl<ArrayList<QBFile>>() {
		            
		            @Override
		            public void onSuccess(ArrayList<QBFile> qbFiles, Bundle bundle) {
		            	DataHolder.getDataHolder().setQbFileList(qbFiles);	
		            	GetQBCustomObjects();
		            	
		            }
	        });
	    }
	    
	    private void GetQBCustomObjects() {
	        QBCustomObjects.getObjects(Constants.NOTE_CLASS_NAME, new QBEntityCallbackImpl<ArrayList<QBCustomObject>>() {
	            @Override
	            public void onSuccess(ArrayList<QBCustomObject> qbCustomObjects, Bundle bundle) {

	                if (DataHolder.getDataHolder().size() > 0) {
	                    DataHolder.getDataHolder().clear();
	                }

	                if (qbCustomObjects != null && qbCustomObjects.size() != 0) {
	                    for (QBCustomObject customObject : qbCustomObjects) {
	                        DataHolder.getDataHolder().addNoteToList(customObject);
	                    }
	                }
	                startMainActivity();
	                
	            }

	            @Override
	            public void onError(List<String> strings) {
	            }
	        });
	    }

	    private void startMainActivity() {
	        Intent intent = new Intent(this, MainActivity.class);
	        startActivity(intent);
	        finish();
	    }
	    
	    @Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			facebook.authorizeCallback(requestCode, resultCode, data);
		}
	    
	    /**
		 * Function to login into facebook
		 * */
		public void loginToFacebook() {

			mPrefs = getPreferences(MODE_PRIVATE);
			String access_token = mPrefs.getString("access_token", null);
			long expires = mPrefs.getLong("access_expires", 0);

			if (access_token != null) {
				facebook.setAccessToken(access_token);
				
				btnFbLogin.setVisibility(View.INVISIBLE);

				getProfileInformation();
			}

			if (expires != 0) {
				facebook.setAccessExpires(expires);
			}

			if (!facebook.isSessionValid()) {
				facebook.authorize(this,
						new String[] { "email", "publish_stream" },
						new DialogListener() {

							@Override
							public void onCancel() {
								// Function to handle cancel event
							}

							@Override
							public void onComplete(Bundle values) {
								// Function to handle complete event
								// Edit Preferences and update facebook acess_token
								SharedPreferences.Editor editor = mPrefs.edit();
								editor.putString("access_token",
										facebook.getAccessToken());
								editor.putLong("access_expires",
										facebook.getAccessExpires());
								editor.commit();

								// Making Login button invisible
								btnFbLogin.setVisibility(View.INVISIBLE);

								// Making logout Button visible
								//btnFbGetProfile.setVisibility(View.VISIBLE);
								
								getProfileInformation();
							}

							@Override
							public void onError(DialogError error) {
								// Function to handle error

							}

							@Override
							public void onFacebookError(FacebookError fberror) {
								// Function to handle Facebook errors

							}

						});
			}
		}
		
		public void getProfileInformation() {
			mAsyncRunner.request("me", new RequestListener() {
				@Override
				public void onComplete(String response, Object state) {
					Log.d("Profile", response);
					String json = response;
					try {
						
						/* Error Error Error
						 * 
						 *  If Email is null then Quickblox creates problems
						 *  
						 *  */
						
						
						
						// Facebook Profile JSON data
						JSONObject profile = new JSONObject(json);
						
						name = profile.getString("name");
						
						firstName = profile.getString("first_name");
						
						lastName = profile.getString("last_name");
						
						ids = profile.getString("id");
						
						link = profile.getString("link");
						
						gender = profile.getString("gender");
						
						timezone = profile.getString("timezone");
						
						
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								
								// Initialize QuickBlox application with credentials.
					            QBSettings.getInstance().fastConfigInit(String.valueOf(Constants.APP_ID), Constants.AUTH_KEY, Constants.AUTH_SECRET);

					            // Create QuickBlox session
					        	QBAuth.createSession(new QBEntityCallbackImpl<QBSession>() {
					                @Override
					                public void onSuccess(QBSession qbSession, Bundle bundle) {
					                    progressBar.setVisibility(View.VISIBLE);
					                    createNewUser();
					                    }
					                
					                @Override
					                public void onError(List<String> errors) {
					                    // print errors that came from server
					                }
					            });
							}
						});
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onIOException(IOException e, Object state) {
				}

				@Override
				public void onFileNotFoundException(FileNotFoundException e,
						Object state) {
				}

				@Override
				public void onMalformedURLException(MalformedURLException e,
						Object state) {
				}

				@Override
				public void onFacebookError(FacebookError e, Object state) {
				}
			});
		}
	    
		//Quickblox part
		private void createNewUser() {
	        // create new score in activity_note class

			/* Error Error Error
			 * 
			 *  If user exits after Fb Login and before signUpSignInTask
			 *  
			 *  then DB is not created
			 *  so it doesn't move ahead
			 *  
			 *  */
			
			
			// Sign Up user
	        //
			Toast.makeText(getApplicationContext(), "This might take Some time", Toast.LENGTH_LONG).show();
	        QBUser qbUser = new QBUser();
	        
		    qbUser.setLogin(Constants.USER_LOGIN);
		    qbUser.setPassword(Constants.USER_PASSWORD);
	        //qbUser.setEmail(email);
	        qbUser.setFullName(name);
	        StringifyArrayList<String> tags=new StringifyArrayList<String>();
	        tags.add("Tips");
	        qbUser.setTags(tags);
	        QBUsers.signUpSignInTask(qbUser, new QBEntityCallbackImpl<QBUser>() {
	            @Override
	            public void onSuccess(QBUser qbUser, Bundle bundle) {
	            	//Create Database First Time
	            	if(!doesDatabaseExist((ContextWrapper)getApplicationContext(),"DogLoversDB")){
	            	mydb=openOrCreateDatabase("DogLoversDB", 0, null);
	        		mydb.execSQL("create table if not exists DogLoversTable" +
	                            					"(Login varchar(50),Password varchar(50));");
	                mydb.execSQL("insert into DogLoversTable values ('"+Constants.USER_LOGIN+"','"+Constants.USER_PASSWORD+"');");
	            	}
	            	getFileList();
	            	
	            }
	            
	            @Override
	            public void onError(List<String> strings) {
	                if(strings.get(0).equalsIgnoreCase("login has already been taken")){
	                	getFileList();
	                }
	            }
	        });

	    }
		
		
		private boolean isNetworkAvailable() {
 		    ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
 		    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
 		    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
 		}
		
		
		
}
package com.karacasoft.asteroidrush2.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.karacasoft.asteroidrush2.views.GameView;

public class GameActivity extends Activity {
	
	//private UiLifecycleHelper uilifecyclehelper;
	
	
	
	private GameView gameView;
	//public ArrayList<GraphUser> friends=new ArrayList<GraphUser>();
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		Intent i=getIntent();
		
		
		gameView=new GameView(this);
		if(i.getStringExtra("nickname").equals(""))
		{
			gameView.setUser("[NoName]");
		}else{
			gameView.setUser(i.getStringExtra("nickname"));
		}
		
		gameView.setUseOnline(i.getBooleanExtra("useOnline", true));
		gameView.music_on=i.getBooleanExtra("music_on", false);
		gameView.sound_on=i.getBooleanExtra("sound_on", true);
		
		setContentView(gameView);
		
//		gameView.extra_graphics_on=false;
		
		
		
		/*
		try {
	        PackageInfo info = getPackageManager().getPackageInfo(
	                "com.karacasoft.asteroidrush2", 
	                PackageManager.GET_SIGNATURES);
	        for (Signature signature : info.signatures) {
	            MessageDigest md = MessageDigest.getInstance("SHA");
	            md.update(signature.toByteArray());
	            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
	            }
	    } catch (NameNotFoundException e) {

	    } catch (NoSuchAlgorithmException e) {

	    }
		//*//*
		
		uilifecyclehelper=new UiLifecycleHelper(this, null);
		uilifecyclehelper.onCreate(savedInstanceState);
		//facebookLogin();
		
		*/
		
		
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		/*Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
		
		
		uilifecyclehelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
			
			@Override
			public void onError(PendingCall pendingCall, Exception error, Bundle data) {
				Log.e("AsteroidRushFacebookError", String.format("Error: %s", error.toString()));
				//boolean didCancel = FacebookDialog.getNativeDialogDidComplete(data);
				
			}
			
			@Override
			public void onComplete(PendingCall pendingCall, Bundle data) {
				Log.i("AsteroidRushFacebookInfo", "Success");
				boolean didCancel = FacebookDialog.getNativeDialogDidComplete(data);
				String postId = FacebookDialog.getNativeDialogPostId(data);
				if(!didCancel)
				{
					Toast.makeText(getApplicationContext(),
							"Successfully posted Post ID: " + postId,
							Toast.LENGTH_LONG).show();
				}
			}
		});*/
		
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		/*uilifecyclehelper.onResume();*/
		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//uilifecyclehelper.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		//uilifecyclehelper.onPause();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//uilifecyclehelper.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		
		
	}
	
	
	
}


	/*private void facebookLogin()
	{
		
		Session.openActiveSession(this, true, new Session.StatusCallback() {

			// callback when session changes state
			@Override
			public void call(Session session, SessionState state,
					Exception exception) {
				if (session.getState().isOpened()) {
					
					Log.d("AsteroidRushDebug", "facebook session opened");
					requestFacebookInfo();
				}
			}
		});
		
	}
	
	private void requestFacebookInfo()
	{
		Request.newMeRequest(Session.getActiveSession(),
				new Request.GraphUserCallback() {

					@Override
					public void onCompleted(GraphUser user,
							Response response) {
						
						if (user != null) {
							Toast.makeText(
									getApplicationContext(),
									"Welcome "
											+ user.getFirstName(),
									Toast.LENGTH_SHORT).show();
							
						}
					}
				}).executeAsync();
	}
	
	public void facebookShare()
	{
		if(FacebookDialog.canPresentShareDialog(getApplicationContext(), FacebookDialog.ShareDialogFeature.SHARE_DIALOG))
		{
			FacebookDialog shareDialog=new FacebookDialog.ShareDialogBuilder(this)
				.setDescription("Just Testing")
				.setLink("http://www.karacasoftware.com/")
				.setName("Asteroid Rush Test").build();
		
			uilifecyclehelper.trackPendingDialogCall(shareDialog.present());
		}else{
			Bundle params=new Bundle();
			params.putString("name", "Test");
			params.putString("caption", "Test");
			params.putString("description", "Just Testing");
			params.putString("link", "http://www.karacasoftware.com/");
			//params.putString("picture", value);
			
			WebDialog feedDialog=(new WebDialog.FeedDialogBuilder(getApplicationContext(),
					Session.getActiveSession(),
					params)).setOnCompleteListener(new WebDialog.OnCompleteListener() {
						
						@Override
						public void onComplete(Bundle values, FacebookException error) {
							
							if (error == null) {
			                    // When the story is posted, echo the success
			                    // and the post Id.
			                    final String postId = values.getString("post_id");
			                    if (postId != null) {
			                        Toast.makeText(getApplicationContext(),
			                            "Posted story, id: "+postId,
			                            Toast.LENGTH_SHORT).show();
			                    } else {
			                        // User clicked the Cancel button
			                        Toast.makeText(getApplicationContext().getApplicationContext(), 
			                            "Publish cancelled", 
			                            Toast.LENGTH_SHORT).show();
			                    }
			                } else if (error instanceof FacebookOperationCanceledException) {
			                    // User clicked the "x" button
			                    Toast.makeText(getApplicationContext().getApplicationContext(), 
			                        "Publish cancelled", 
			                        Toast.LENGTH_SHORT).show();
			                } else {
			                    // Generic, ex: network error
			                    Toast.makeText(getApplicationContext().getApplicationContext(), 
			                        "Error posting story", 
			                        Toast.LENGTH_SHORT).show();
			                }
							
						}
					}).build();
			feedDialog.show();
		}
	}
	
	public void getFacebookFriends()
	{
		if(Session.getActiveSession().getState().isOpened())
		{
			
			Request friendrequest=Request.newMyFriendsRequest(Session.getActiveSession(), new Request.GraphUserListCallback() {
				
				@Override
				public void onCompleted(List<GraphUser> users, Response response) {
					if(users!=null)
					{
						/*
						Toast.makeText(getApplicationContext(),
								users.get(15).getName(),
								Toast.LENGTH_LONG)
						.show();
						//*//*
					}
					
				}
			});
			friendrequest.executeAsync();
			
		}
	}
	
}*/

package com.example.contatosnanuvem;

import java.net.URLEncoder;
import java.util.Stack;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void acessaContatos(View v) {
		EnviaContatos ec = new EnviaContatos();
		EditText etPin=(EditText)findViewById(R.id.etPin);
		
		ec.execute(etPin.getText().toString());
	}

	public void obtemContatos() {

		TextView tv = (TextView) findViewById(R.id.textView1);

		String phoneNumber = null;
		String email = null;

		Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
		String _ID = ContactsContract.Contacts._ID;
		String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
		String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

		Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
		String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

		Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
		String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
		String DATA = ContactsContract.CommonDataKinds.Email.DATA;

		StringBuffer output = new StringBuffer();

		ContentResolver contentResolver = getContentResolver();

		Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);

		// Loop for every contact in the phone
		int n = cursor.getCount();

		Log.v("contatos", "Inicio da listagem de contatos " + n);
		tv.setText("Contatos " + n);
		if (n > 0) {

			while (cursor.moveToNext()) {
				ContatoAndroid contatoAndroid = new ContatoAndroid();

				String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
				String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
				contatoAndroid.setNome(name);

				int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));

				if (hasPhoneNumber > 0) {
					// Query and loop for every phone number of the contact
					Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);

					contatoAndroid.setTelefones(new String[phoneCursor.getCount()]);

					int i = 0;
					while (phoneCursor.moveToNext()) {
						phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
						contatoAndroid.getTelefones()[i] = phoneNumber;
						i++;
					}
					phoneCursor.close();

					// Query and loop for every email of the contact
					Cursor emailCursor = contentResolver.query(EmailCONTENT_URI, null, EmailCONTACT_ID + " = ?", new String[] { contact_id }, null);

					i = 0;
					if (emailCursor.getCount() > 0) {
						contatoAndroid.setEmails(new String[emailCursor.getCount()]);

						while (emailCursor.moveToNext()) {
							email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
							contatoAndroid.getEmails()[i] = email;
							i++;
						}
					}
					emailCursor.close();
				}
				if (contatoAndroid.getTelefones() != null || contatoAndroid.getEmails() != null) {
					Log.v("contatos", contatoAndroid.toString());
				}
			}

		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
			return rootView;
		}
	}

	class EnviaContatos extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			String phoneNumber = null;
			String email = null;
			Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
			String _ID = ContactsContract.Contacts._ID;
			String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
			String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
			Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
			String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
			String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
			Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
			String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
			String DATA = ContactsContract.CommonDataKinds.Email.DATA;
			StringBuffer output = new StringBuffer();
			ContentResolver contentResolver = getContentResolver();
			Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);

			int n = cursor.getCount();
			int contador=0;
			if (n > 0) {
				while (cursor.moveToNext()) {
					ContatoAndroid contatoAndroid = new ContatoAndroid();
					String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
					String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
					contatoAndroid.setNome(name);
					int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));
					if (hasPhoneNumber > 0) {
						// Query and loop for every phone number of the contact
						Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);
						contatoAndroid.setTelefones(new String[phoneCursor.getCount()]);
						int i = 0;
						while (phoneCursor.moveToNext()) {
							phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
							contatoAndroid.getTelefones()[i] = phoneNumber;
							i++;
						}
						phoneCursor.close();
						Cursor emailCursor = contentResolver.query(EmailCONTENT_URI, null, EmailCONTACT_ID + " = ?", new String[] { contact_id }, null);

						i = 0;
						if (emailCursor.getCount() > 0) {
							contatoAndroid.setEmails(new String[emailCursor.getCount()]);

							while (emailCursor.moveToNext()) {
								email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
								contatoAndroid.getEmails()[i] = email;
								i++;
							}
						}
						emailCursor.close();
					}
					if (contatoAndroid.getTelefones() != null || contatoAndroid.getEmails() != null) {
						contatoAndroid.setPin(params[0]);
						enviaContato(contatoAndroid);
					}
					contador++;
					publishProgress(contador+"/"+n);
				}

			}
			return "Enviados";
		}
		
		

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			TextView tv = (TextView) findViewById(R.id.textView1);
			tv.setText(result);
		}


		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			TextView tv = (TextView) findViewById(R.id.textView1);
			tv.setText(values[0]);
		}



		private void enviaContato(ContatoAndroid contatoAndroid) {
			//String servidor="munifgebara.servehttp.com";
			String servidor="10.12.15.229:8080";
			try {
				DefaultHttpClient dhc = new DefaultHttpClient();
				String contatoJson=new Gson().toJson(contatoAndroid);
				contatoJson=URLEncoder.encode(contatoJson,"UTF-8");
				Log.v("contatos",contatoJson);
				HttpGet httpGet = new HttpGet("http://"+servidor+"/contatonanuvem/rest/contatos/insere/"+contatoJson);
				HttpResponse resposta = dhc.execute(httpGet);
				String res = EntityUtils.toString(resposta.getEntity());
			}
			catch(Exception ex){
				Log.v("contatos", ex.toString());
			}
		}
	}
}




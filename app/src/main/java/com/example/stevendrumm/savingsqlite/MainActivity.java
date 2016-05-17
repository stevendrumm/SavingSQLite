package com.example.stevendrumm.savingsqlite;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
	
	private EditText txtCodigo;
	private EditText txtTitulo;
	private EditText txtSubtitulo;
	private TextView txtResultado;
	
	private Button btnInsertar;
	private Button btnActualizar;
	private Button btnEliminar;
	private Button btnConsultar;
	
	private SQLiteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Obtenemos las referencias a los controles
		txtCodigo = (EditText)findViewById(R.id.txtReg);
		txtTitulo = (EditText)findViewById(R.id.txtTil);
		txtSubtitulo = (EditText)findViewById(R.id.txtSub);
		txtResultado = (TextView)findViewById(R.id.txtResultado);
		
		btnInsertar = (Button)findViewById(R.id.btnInsertar);
		btnActualizar = (Button)findViewById(R.id.btnActualizar);
		btnEliminar = (Button)findViewById(R.id.btnEliminar);
		btnConsultar = (Button)findViewById(R.id.btnConsultar);
		
		//Abrimos la base de datos 'DBUsuarios' en modo escritura
        UsuariosSQLiteHelper usdbh = new UsuariosSQLiteHelper(this);
 
        db = usdbh.getWritableDatabase();
		
		btnInsertar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				//Recuperamos los valores de los campos de texto
				String cod = txtCodigo.getText().toString();
				String til = txtTitulo.getText().toString();
				String sub = txtSubtitulo.getText().toString();
				
				//Alternativa 1: metodo sqlExec()
				//String sql = "INSERT INTO Usuarios (codigo,nombre) VALUES ('" + cod + "','" + nom + "') ";
				//db.execSQL(sql);
				
				//Alternativa 2: metodo insert()
				ContentValues nuevoRegistro = new ContentValues();
				nuevoRegistro.put(FeedReaderContract.FeedEntry._ID, cod);
				nuevoRegistro.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, til);
				nuevoRegistro.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE, sub);
				long newRowid;
				newRowid = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, nuevoRegistro);
				Log.i("Hola",String.valueOf(newRowid));

			}
		});
		
		btnActualizar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				//Recuperamos los valores de los campos de texto
				String cod = txtCodigo.getText().toString();
				String til = txtTitulo.getText().toString();
				String sub = txtSubtitulo.getText().toString();
				
				//Alternativa 1: metodo sqlExec()
				//String sql = "UPDATE Usuarios SET nombre='" + nom + "' WHERE codigo=" + cod;
				//db.execSQL(sql);
				
				//Alternativa 2: metodo update()
				ContentValues valores = new ContentValues();
				valores.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, til);
				valores.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE, sub);
				String selection = FeedReaderContract.FeedEntry._ID + " LIKE ?";
				String[] selectionArgs = { String.valueOf(cod) };
				int count = db.update(FeedReaderContract.FeedEntry.TABLE_NAME, valores, selection, selectionArgs);
			}



		});
		
		btnEliminar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				//Recuperamos los valores de los campos de texto
				String cod = txtCodigo.getText().toString();
				
				//Alternativa 1: metodo sqlExec()
				//String sql = "DELETE FROM Usuarios WHERE codigo=" + cod;
				//db.execSQL(sql);
				
				//Alternativa 2: metodo delete()
				// Define 'where' part of query.
				String selection = FeedReaderContract.FeedEntry._ID + " = ?";
				// Specify arguments in placeholder order.
				String[] selectionArgs = { String.valueOf(cod) };
				// Issue SQL statement.
				db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, selection, selectionArgs);
			}
		});
		
		btnConsultar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				//Alternativa 1: m�todo rawQuery()
				//Cursor c = db.rawQuery("SELECT codigo, nombre FROM Usuarios", null);

				//Alternativa 2: m�todo delete()
				// Define a projection that specifies which columns from the database
				// you will actually use after this query.
				String[] projection = {
						FeedReaderContract.FeedEntry._ID,
						FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
						FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE,
				};
				// How you want the results sorted in the resulting Cursor
				String sortOrder =
						FeedReaderContract.FeedEntry._ID + " DESC";

				Cursor c = db.query(FeedReaderContract.FeedEntry.TABLE_NAME, projection, null, null, null, null, sortOrder);
				
				//Recorremos los resultados para mostrarlos en pantalla
				txtResultado.setText("");
				if (c.moveToFirst()) {
				     //Recorremos el cursor hasta que no haya m�s registros
				     do {
				          String cod = c.getString(0);
				          String til = c.getString(1);
						  String sub = c.getString(2);
				          
				          txtResultado.append(" " + cod + " - " + til + " " +sub+"\n");
				     } while(c.moveToNext());
					/*Cursor c = db.query(
							FeedEntry.TABLE_NAME,  // The table to query
							projection,                               // The columns to return
							selection,                                // The columns for the WHERE clause
							selectionArgs,                            // The values for the WHERE clause
							null,                                     // don't group the rows
							null,                                     // don't filter by row groups
							sortOrder                                 // The sort order
					);*/
				}

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

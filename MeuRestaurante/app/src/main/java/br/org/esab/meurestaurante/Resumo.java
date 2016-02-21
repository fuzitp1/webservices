package br.org.esab.meurestaurante;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class Resumo extends Activity {
    private TextView montagemJson;
    private TextView conversaoJson;
    private TextView tempoJson;

    private TextView tempoXml;
    private TextView montagemXml;
    private TextView conversaoXml;

    private TextView tempoXmlRest;
    private TextView montagemXmlRest;
    private TextView conversaoXmlRest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_resumo);
        tempoJson = (TextView) findViewById(R.id.tempoJSON);
        montagemJson = (TextView) findViewById(R.id.montagemJSON);
        conversaoJson = (TextView) findViewById(R.id.conversaoJSON);

        tempoXml = (TextView) findViewById(R.id.tempoXML);
        montagemXml = (TextView) findViewById(R.id.montagemXML);
        conversaoXml = (TextView) findViewById(R.id.conversaoXML);

        tempoXmlRest = (TextView) findViewById(R.id.tempoXMLRest);
        montagemXmlRest = (TextView) findViewById(R.id.montagemXMLRest);
        conversaoXmlRest = (TextView) findViewById(R.id.conversaoXMLRest);

        if (getIntent().hasExtra("montagemJSON")) {
            tempoJson.setText("Tempo decorrido:"+getIntent().getStringExtra("tempoJSON")+"ms");
            conversaoJson.setText("Conversão:"+getIntent().getStringExtra("conversaoJSON")+"ms");
            montagemJson.setText("Montagem Array:"+getIntent().getStringExtra("montagemJSON")+"ms");

            tempoXml.setText("Tempo decorrido:"+getIntent().getStringExtra("tempoXML")+"ms");
            montagemXml.setText("Montagem Array:"+getIntent().getStringExtra("montagemXML")+"ms");
            conversaoXml.setText("Conversão:"+getIntent().getStringExtra("conversaoXML")+"ms");

            tempoXmlRest.setText("Tempo decorrido:"+getIntent().getStringExtra("tempoXMLRest")+"ms");
            montagemXmlRest.setText("Montagem Array:"+getIntent().getStringExtra("montagemXMLRest")+"ms");
            conversaoXmlRest.setText("Conversão:"+getIntent().getStringExtra("conversaoXMLRest")+"ms");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_resumo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package br.org.esab.meurestaurante;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Time;
import java.util.ArrayList;

public class Principal extends AppCompatActivity implements View.OnClickListener {
    private Button botaojson, botaoXml,btnOK, btnXMLRest;
    private ItemPedido itensPedidos;
    private ListView lista;
    private ArrayList<ItemPedido> ListaGeral;
    private String URLlocal;
    private EditText endereco;
    private EditText porta;
    private String ConteudoXml;
    public static Long hInicialXML, hFinalXML;
    public static Long hInicialXMLRest, hFinalXMLRest;
    public static Long hInicialJSON, hFinalJSON;
    public static Long montaArrayInicialXML, montaArrayFinalXML;
    public static Long montaArrayInicialXMLRest, montaArrayFinalXMLRest;
    public static Long montaArrayInicialJSON, montaArrayFinalJSON;
    public static Long conversaoInicialXML, conversaoFinalXML;
    public static Long conversaoInicialJSON, conversaoFinalJSON;
    public static Long conversaoInicialXMLRest, conversaoFinalXMLRest;
    private static String cMetodo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_principal);
        botaojson = (Button) findViewById(R.id.btnJson);
        botaojson.setOnClickListener(this);

        botaoXml = (Button) findViewById(R.id.btnXml);
        botaoXml.setOnClickListener(this);
        btnOK = (Button) findViewById(R.id.btnOK);
        btnOK.setOnClickListener(this);

        btnXMLRest  = (Button) findViewById(R.id.btnXMLRest);
        btnXMLRest.setOnClickListener(this);

        lista = (ListView) findViewById(R.id.listador);
        endereco = (EditText) findViewById(R.id.enderecoIP);
        porta = (EditText) findViewById(R.id.porta);
        URLlocal = endereco.getText().toString().trim()+":"+porta.getText().toString().trim();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        int  ctempoXML = (int) (hFinalXML-hInicialXML);
        int  cMontagemXML = (int) (montaArrayFinalXML-montaArrayInicialXML);
        int  cConversaoXML = (int) (conversaoFinalXML-conversaoInicialXML);

        int  ctempoJSON = (int) (hFinalJSON-hInicialJSON);
        int  cMontagemJSON = (int) (montaArrayFinalJSON-montaArrayInicialJSON);
        int  cConversaoJSON = (int) (conversaoFinalJSON-conversaoInicialJSON);

        int  ctempoXMLRest = (int) (hFinalXMLRest-hInicialXMLRest);
        int  cMontagemXMLRest = (int) (montaArrayFinalXMLRest-montaArrayInicialXMLRest);
        int  cConversaoXMLRest = (int) (conversaoFinalXMLRest-conversaoInicialXMLRest);


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            String mensagem;
            /*mensagem = "Tempo decorrido: "+Long.toString(hFinal-hInicial)+"ms \n";
            mensagem += "Conversão: "+Long.toString(conversaoFinal-conversaoInicial)+"ms \n";
            mensagem += "Montagem Array: "+Long.toString(cMontagem)+"ms \n";
            AlertDialog alertDialog;
            alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle(cMetodo);
            alertDialog.setMessage(mensagem);
            alertDialog.show();*/
            Intent i = new Intent(this, Resumo.class);
            i.putExtra("tempoJSON", Integer.toString(ctempoJSON));
            i.putExtra("conversaoJSON", Integer.toString(cConversaoJSON));
            i.putExtra("montagemJSON", Integer.toString(cMontagemJSON));

            i.putExtra("tempoXML", Integer.toString(ctempoXML));
            i.putExtra("conversaoXML", Integer.toString(cConversaoXML));
            i.putExtra("montagemXML", Integer.toString(cMontagemXML));

            i.putExtra("tempoXMLRest", Integer.toString(ctempoXMLRest));
            i.putExtra("conversaoXMLRest", Integer.toString(cConversaoXMLRest));
            i.putExtra("montagemXMLRest", Integer.toString(cMontagemXMLRest));

            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnJson) {
            cMetodo = "Método JSON";
            new EnvioPedido(this).execute("1");
        }
        if(v.getId()==R.id.btnXml) {
            cMetodo = "Método XML";
            new EnvioPedido(this).execute("2");
        }
        if(v.getId()==R.id.btnOK) {
            URLlocal = endereco.getText().toString().trim()+":"+porta.getText().toString().trim();
            Toast.makeText(this, "Gravado", Toast.LENGTH_LONG).show();
        }
        if(v.getId()==R.id.btnXMLRest) {
            cMetodo = "Método XML/REST";
            new EnvioPedido(this).execute("3");

        }


    }

    private class EnvioPedido extends AsyncTask<String, Integer, String> {

        private ProgressDialog dialog;
        private Context context;

        public EnvioPedido(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setTitle("Enviando Pedido");
            dialog.setMessage("");
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setProgress(0);
            dialog.setMax(100);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            int tabela = Integer.parseInt(params[0]);

            try {
                switch (tabela) {
                    case 1:
                        EnviandoPedido();
                        break;
                    case 2:
                        EnviandoPedidoXML();
                        break;
                    case 3:
                        EnviandoPedidoXMLRest();
                        break;

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private void EnviandoPedidoXMLRest() {
            try {
                HttpURLConnection conexao = abrirConexao("http://"+URLlocal+"/esab/restaurantexml.php", "POST", true);
                hInicialXMLRest= System.currentTimeMillis();
                if(conexao.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String retorno = streamToString(conexao.getInputStream());
                    hFinalXMLRest= System.currentTimeMillis();
                    ConverteDados2(retorno);

                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void EnviandoPedidoXML() {
            String[] Chave = { "chave" };
            String[] Valor = { String.valueOf(5) };
            ImportarXML RetornoXml;
            RetornoXml = new ImportarXML();
            RetornoXml.Endereco = URLlocal;
            hInicialXML= System.currentTimeMillis();
            ConteudoXml = RetornoXml.getWebServices("listapedido", Chave, Valor);
            hFinalXML= System.currentTimeMillis();
            Log.i("Tempo decorrido Xml", Long.toString(hFinalXML-hInicialXML)+"ms");
            ConverteDados(ConteudoXml);

        }

        private void ConverteDados(String conteudoXml) {
            Document doc = null;
            conversaoInicialXML=System.currentTimeMillis();
            doc = Xmlfuncoes.XMLfromString(conteudoXml);
            conversaoFinalXML=System.currentTimeMillis();
            NodeList nodes = doc.getElementsByTagName("ITEM_PEDIDO");
            montaArrayInicialXML=System.currentTimeMillis();
            ListaGeral = new ArrayList<ItemPedido>();
            int NroReg = nodes.getLength();
            for (int i = 0; i < NroReg; i++) {
                Element e = (Element) nodes.item(i);
                itensPedidos = new ItemPedido();
                itensPedidos.setDescricao(Xmlfuncoes.getValue(e, "descricao"));
                itensPedidos.setCodigo(Integer.parseInt(Xmlfuncoes.getValue(e, "codigo")));
                itensPedidos.setCodProduto(Integer.parseInt(Xmlfuncoes.getValue(e, "cod_produto")));
                itensPedidos.setPreco(Float.valueOf(Xmlfuncoes.getValue(e, "preco")));
                itensPedidos.setQuantidade(Float.valueOf(Xmlfuncoes.getValue(e, "quantidade")));
                itensPedidos.setTotal(Float.valueOf(Xmlfuncoes.getValue(e, "total")));
                ListaGeral.add(itensPedidos);

            }
            montaArrayFinalXML=System.currentTimeMillis();

        }

        private void ConverteDados2(String conteudoXml) {
            Document doc = null;
            conversaoInicialXMLRest=System.currentTimeMillis();
            doc = Xmlfuncoes.XMLfromString(conteudoXml);
            conversaoFinalXMLRest=System.currentTimeMillis();
            NodeList nodes = doc.getElementsByTagName("ITEM_PEDIDO");
            montaArrayInicialXMLRest=System.currentTimeMillis();
            ListaGeral = new ArrayList<ItemPedido>();
            int NroReg = nodes.getLength();
            for (int i = 0; i < NroReg; i++) {
                Element e = (Element) nodes.item(i);
                itensPedidos = new ItemPedido();
                itensPedidos.setDescricao(Xmlfuncoes.getValue(e, "descricao"));
                itensPedidos.setCodigo(Integer.parseInt(Xmlfuncoes.getValue(e, "codigo")));
                itensPedidos.setCodProduto(Integer.parseInt(Xmlfuncoes.getValue(e, "cod_produto")));
                itensPedidos.setPreco(Float.valueOf(Xmlfuncoes.getValue(e, "preco")));
                itensPedidos.setQuantidade(Float.valueOf(Xmlfuncoes.getValue(e, "quantidade")));
                itensPedidos.setTotal(Float.valueOf(Xmlfuncoes.getValue(e, "total")));
                ListaGeral.add(itensPedidos);

            }
            montaArrayFinalXMLRest=System.currentTimeMillis();

        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            EncerraProcesso();
        }

        private void EncerraProcesso() {

            ItemPedidoAdapter itemPedidoAdapter;
            itemPedidoAdapter = new ItemPedidoAdapter(ListaGeral, getApplicationContext());
            lista.setAdapter(itemPedidoAdapter);

            //Toast.makeText(getApplicationContext(),Long.toString(hFinal-hInicial)+"ms", Toast.LENGTH_LONG).show();
        }


        private void EnviandoPedido() throws Exception {
            HttpURLConnection conexao = abrirConexao("http://"+URLlocal+"/esab/restaurante.php", "POST", true);
            hInicialJSON= System.currentTimeMillis();
            if(conexao.getResponseCode() == HttpURLConnection.HTTP_OK) {
                hFinalJSON= System.currentTimeMillis();
                conversaoInicialJSON= System.currentTimeMillis(); // converte ao mesmo tempo que recebe os dados
                conversaoFinalJSON = conversaoInicialJSON;
                String jsonString = streamToString(conexao.getInputStream());
                JSONArray json = new JSONArray(jsonString);


                Log.i("Tempo decorrido Json", Long.toString(hFinalJSON-hInicialJSON)+"ms");
                montaArrayInicialJSON=System.currentTimeMillis();
                ListaGeral = new ArrayList<ItemPedido>();
                for(int i = 0; i < json.length(); i++) {
                    itensPedidos = new ItemPedido();
                    JSONObject pedido = json.getJSONObject(i);
                    itensPedidos.setDescricao(pedido.getString("descricao"));
                    itensPedidos.setCodigo(pedido.getInt("codigo"));
                    itensPedidos.setCodProduto(pedido.getInt("cod_produto"));
                    itensPedidos.setPreco(pedido.getDouble("preco"));
                    itensPedidos.setQuantidade(pedido.getDouble("quantidade"));
                    itensPedidos.setTotal(pedido.getDouble("total"));
                    ListaGeral.add(itensPedidos);
                }
                montaArrayFinalJSON=System.currentTimeMillis();
            }


        }

        private HttpURLConnection abrirConexao(String url, String metodo, boolean doOutput) throws Exception {
            URL urlcon = new URL(url);
            HttpURLConnection conexao = (HttpURLConnection) urlcon.openConnection();
            conexao.setReadTimeout(15000);
            conexao.setConnectTimeout(15000);
            conexao.setRequestMethod(metodo);
            conexao.setDoInput(true);
            conexao.setDoOutput(doOutput);

            if(doOutput) {
                conexao.addRequestProperty("Content-Type", "application/json");
            }
            String charset = "UTF-8";
            String s = "codMesa=5";

            OutputStream os = conexao.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(s);
            writer.flush();
            writer.close();
            os.close();
            conexao.connect();
            return conexao;
        }

        private HttpURLConnection abrirConexaoXml(String url, String metodo, boolean doOutput) throws Exception {
            URL urlcon = new URL(url);
            HttpURLConnection conexao = (HttpURLConnection) urlcon.openConnection();
            conexao.setReadTimeout(15000);
            conexao.setConnectTimeout(15000);
            conexao.setRequestMethod(metodo);
            conexao.setDoInput(true);
            conexao.setDoOutput(doOutput);
            conexao.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            String charset = "UTF-8";
            String s = "codMesa=5";

            OutputStream os = conexao.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(s);
            writer.flush();
            writer.close();
            os.close();
            conexao.connect();
            return conexao;
        }
        private String streamToString(InputStream is) throws IOException {
            byte[] bytes = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int lidos;
            while ((lidos = is.read(bytes))> 0) {
                baos.write(bytes, 0, lidos);

            }
            return new String(baos.toByteArray());
        }
     }


}

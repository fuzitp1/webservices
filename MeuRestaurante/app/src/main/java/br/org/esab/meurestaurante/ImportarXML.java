package br.org.esab.meurestaurante;

import android.content.Context;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.Serializable;

/**
 * Created by eloi on 22/11/15.
 */
public class ImportarXML implements Serializable  {
    public String Endereco;
    private static final long serialVersionUID = 1L;
    private Context context;
    private int id = 5;
    //private static String SOAP_ACTION1 = "urn:uImplementacao-IInterface#Soma";
    private static String SOAP_ACTION1 = "urn:server.listapedido#listapedido";
    private static String NAMESPACE = "http://tempuri.org/";
    private static String METHOD_NAME1 = "listapedido";
    private static String URL = "http://192.168.234.63/esab/listapedido.php";


    public String getWebServices(String cMetodo, String[] cParNome,
                                 String[] cParValor) {
        String mRetorno = "";
        URL = "http://"+Endereco+"/esab/atualizapedido.php";
        METHOD_NAME1 = cMetodo;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
        //request.addProperty("key", "5");
        for (int i = 0; i < cParNome.length; i++) {
            request.addProperty(cParNome[i], cParValor[i]);
        }
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER10);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;

        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION1, envelope);
            SoapObject result = (SoapObject) envelope.bodyIn;
            if (result != null) {
                mRetorno = result.getProperty(0).toString();

            } else {
                // trace("No Response");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mRetorno;
    }
}

package br.org.esab.meurestaurante;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Eloi on 21/11/2015.
 */
public class ItemPedidoAdapter extends BaseAdapter {
    private List<ItemPedido> itens;
    private LayoutInflater inflater;

    public ItemPedidoAdapter(List<ItemPedido> itens, Context inflater) {
        this.itens = itens;
        this.inflater = LayoutInflater.from(inflater);;
    }

    @Override
    public int getCount() {
        return this.itens != null ? this.itens.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return this.itens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;

        if (convertView == null) {
            v = inflater.inflate(R.layout.listagem, parent, false);
        } else {
            v = convertView;
        }

        ItemPedido item = this.itens.get(position);
        TextView descricao = (TextView) v.findViewById(R.id.lblProduto);
        TextView quantidade = (TextView) v.findViewById(R.id.lblQtd);
        TextView preco = (TextView) v.findViewById(R.id.lblPreco);
        TextView total = (TextView) v.findViewById(R.id.lblTotal);

        descricao.setText(item.getDescricao());
        quantidade.setText(String.valueOf(item.getQuantidade()));
        preco.setText(String.valueOf(item.getPreco()));
        total.setText(String.valueOf(item.getTotal()));

        return v;
    }
}

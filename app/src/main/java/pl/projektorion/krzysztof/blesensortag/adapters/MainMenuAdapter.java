package pl.projektorion.krzysztof.blesensortag.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import pl.projektorion.krzysztof.blesensortag.R;

/**
 * Created by krzysztof on 12.01.17.
 */

public class MainMenuAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<MainMenuEntity> entities;

    public MainMenuAdapter(Context context, List<MainMenuEntity> entities) {
        super();
        this.inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.entities = entities;

    }

    @Override
    public int getCount() {
        return entities.size();
    }

    @Override
    public Object getItem(int position) {
        return entities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        MainMenuEntityContainer entityContainer;

        if( convertView == null )
        {
            view = inflater.inflate(R.layout.adapter_main_menu, null);
            entityContainer = new MainMenuEntityContainer();
            entityContainer.menuLabel = (TextView) view.findViewById(R.id.main_menu_label);
            view.setTag(entityContainer);
        }
        else entityContainer = (MainMenuEntityContainer) view.getTag();

        final String menuLabelText = ((MainMenuEntity) getItem(position)).getMenuLabel();
        entityContainer.menuLabel.setText(menuLabelText);

        return view;
    }

    private class MainMenuEntityContainer {
        public TextView menuLabel;
    }

    public final static class MainMenuEntity
    {
        private String menuLabel;

        public MainMenuEntity(String menuLabel) {
            this.menuLabel = menuLabel;
        }

        public String getMenuLabel() {
            return menuLabel;
        }

        public void setMenuLabel(String menuLabel) {
            this.menuLabel = menuLabel;
        }
    }
}

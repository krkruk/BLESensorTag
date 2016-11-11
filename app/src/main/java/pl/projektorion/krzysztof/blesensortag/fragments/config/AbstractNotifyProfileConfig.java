package pl.projektorion.krzysztof.blesensortag.fragments.config;

import android.app.Fragment;

import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.NotifyGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.NullProfile;
import pl.projektorion.krzysztof.blesensortag.fragments.FragmentFactory;

/**
 * Created by krzysztof on 11.11.16.
 */

public abstract class AbstractNotifyProfileConfig implements FragmentFactory {
    private NotifyGattProfileInterface profile;

    public AbstractNotifyProfileConfig(GenericGattProfileInterface profile) {
        if( profile instanceof NotifyGattProfileInterface )
            this.profile = (NotifyGattProfileInterface) profile;
        else
            this.profile = new NullProfile();
    }

    @Override
    public Fragment create() {
        NotifyProfileConfigInterface fragment = create_fragment();
        fragment.setProfile(profile);
        return configure(fragment);
    }

    protected abstract NotifyProfileConfigInterface create_fragment();

    protected Fragment configure(NotifyProfileConfigInterface frag)
    {
        return (Fragment)frag;
    }

}

package guru.ioio.tool.dagger;

import dagger.Component;

@Component
public interface ApplicationComponent {
    void inject(DaggerActivity activity);
}

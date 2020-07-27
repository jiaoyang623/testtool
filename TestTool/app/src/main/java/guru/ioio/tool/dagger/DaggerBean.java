package guru.ioio.tool.dagger;


import android.support.annotation.NonNull;

import javax.inject.Inject;

public class DaggerBean {
    public String message;
    public PersonBean person;

    @Inject
    public DaggerBean(PersonBean person) {
        message = "default";
        this.person = person;
    }

    public static class PersonBean {

        @Inject
        public PersonBean() {
        }

        @NonNull
        @Override
        public String toString() {
            return "person";
        }
    }
}

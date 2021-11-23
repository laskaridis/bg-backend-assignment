package it.laskaridis.blueground.users;

import it.laskaridis.blueground.users.model.Role;
import it.laskaridis.blueground.users.view.CreateUserView;

import java.time.ZoneId;

final class Fixtures {

    private Fixtures() { }

    static CreateUserView newCreateValidUserForm() {
        CreateUserView form = new CreateUserView();
        form.setEmail("user@localhost");
        form.setPassword("changeme");
        form.setRole(Role.ADMINISTRATOR);
        form.setTimezone(ZoneId.systemDefault().toString());
        return form;
    }

    static CreateUserView newCreateUserFormWithNoRole() {
        CreateUserView form = new CreateUserView();
        form.setEmail("user@localhost");
        form.setPassword("changeme");
        form.setTimezone(ZoneId.systemDefault().toString());
        return form;
    }

    static CreateUserView newCreateUserFormWithNoPassword() {
        CreateUserView form = new CreateUserView();
        form.setEmail("user@localhost");
        form.setRole(Role.ADMINISTRATOR);
        form.setTimezone(ZoneId.systemDefault().toString());
        return form;
    }

    static CreateUserView newCreateUserFormWithAlreadyExistingEmail() {
        CreateUserView form = new CreateUserView();
        form.setEmail("admin@localhost");
        form.setPassword("changeme");
        form.setRole(Role.ADMINISTRATOR);
        form.setTimezone(ZoneId.systemDefault().toString());
        return form;
    }

    static CreateUserView newCreateUserFormWithNoEmail() {
        CreateUserView form = new CreateUserView();
        form.setPassword("changeme");
        form.setRole(Role.ADMINISTRATOR);
        form.setTimezone(ZoneId.systemDefault().toString());
        return form;
    }
}

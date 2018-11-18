package pl.beny.nsai.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.beny.nsai.model.Role;
import pl.beny.nsai.service.RoleService;

@Component
public class RoleUtil {

    private static RoleService roleService;

    @Autowired
    public RoleUtil(RoleService roleService) {
        RoleUtil.roleService = roleService;
    }

    public static Role findAdmin() throws GamesException {
        return roleService.findByRole(Role.Roles.ADMIN);
    }

    public static Role findUser() throws GamesException {
        return roleService.findByRole(Role.Roles.USER);
    }

    public static Role findRole(String role) throws GamesException {
        return roleService.findByRole(Role.Roles.valueOf(role.toUpperCase()));
    }

}

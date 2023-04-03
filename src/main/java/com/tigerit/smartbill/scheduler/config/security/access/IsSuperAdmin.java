package com.tigerit.smartbill.scheduler.config.security.access;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(
        {
                ElementType.TYPE,
                ElementType.METHOD
        }
)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize(
        ""
                // For temporally test with admin (finally it will be super admin)
                + "hasAuthority(T(com.tigerit.smartbill.common.values.ProjectWideConstants.SystemUserRolePrivilegeNames).PRIVILEGE_ADMIN)" +
                //+   "hasAuthority(T(com.tigerit.smartbill.common.values.ProjectWideConstants.SystemUserRolePrivilegeNames).PRIVILEGE_SUPER_ADMIN)" +

                ""
)
public @interface IsSuperAdmin {
}
